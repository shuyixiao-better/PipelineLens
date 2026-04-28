package com.poeticcoder.nginx.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import com.poeticcoder.nginx.token.NginxTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class NginxLexer extends LexerBase {

    private static final Set<String> KEYWORDS = Set.of(
            "http", "server", "location", "upstream", "events", "stream", "mail", "types", "map", "geo",
            "include", "listen", "server_name", "root", "index", "try_files", "proxy_pass", "return",
            "rewrite", "if", "set", "add_header", "proxy_set_header", "client_max_body_size", "keepalive_timeout",
            "ssl_certificate", "ssl_certificate_key", "error_page", "access_log", "error_log", "gzip", "resolver",
            "limit_req", "limit_conn", "fastcgi_pass", "uwsgi_pass", "scgi_pass", "alias", "deny", "allow"
    );

    private CharSequence buffer;
    private int startOffset;
    private int endOffset;
    private int position;
    private int tokenStart;
    private int tokenEnd;
    private IElementType tokenType;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.position = startOffset;
        advance();
    }

    @Override
    public int getState() {
        return 0;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        return tokenType;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return tokenEnd;
    }

    @Override
    public void advance() {
        if (position >= endOffset) {
            tokenType = null;
            tokenStart = tokenEnd = endOffset;
            return;
        }

        tokenStart = position;
        char c = buffer.charAt(position);

        if (Character.isWhitespace(c)) {
            while (position < endOffset && Character.isWhitespace(buffer.charAt(position))) {
                position++;
            }
            tokenEnd = position;
            tokenType = NginxTokenTypes.WHITE_SPACE;
            return;
        }

        if (c == '#') {
            while (position < endOffset && buffer.charAt(position) != '\n') {
                position++;
            }
            tokenEnd = position;
            tokenType = NginxTokenTypes.COMMENT;
            return;
        }

        if (c == '"' || c == '\'') {
            char quote = c;
            position++;
            while (position < endOffset) {
                char ch = buffer.charAt(position);
                if (ch == '\\' && position + 1 < endOffset) {
                    position += 2;
                    continue;
                }
                position++;
                if (ch == quote) {
                    break;
                }
            }
            tokenEnd = position;
            tokenType = NginxTokenTypes.STRING;
            return;
        }

        if (c == '{') {
            position++;
            tokenEnd = position;
            tokenType = NginxTokenTypes.LBRACE;
            return;
        }

        if (c == '}') {
            position++;
            tokenEnd = position;
            tokenType = NginxTokenTypes.RBRACE;
            return;
        }

        if (c == ';') {
            position++;
            tokenEnd = position;
            tokenType = NginxTokenTypes.SEMICOLON;
            return;
        }

        if (c == '$') {
            position++;
            while (position < endOffset && isIdentifierPart(buffer.charAt(position))) {
                position++;
            }
            tokenEnd = position;
            tokenType = NginxTokenTypes.VARIABLE;
            return;
        }

        if (Character.isDigit(c)) {
            position++;
            while (position < endOffset) {
                char ch = buffer.charAt(position);
                if (Character.isDigit(ch) || ch == '.' || Character.isLetter(ch)) {
                    position++;
                } else {
                    break;
                }
            }
            tokenEnd = position;
            tokenType = NginxTokenTypes.NUMBER;
            return;
        }

        if (isIdentifierStart(c)) {
            position++;
            while (position < endOffset && isIdentifierPart(buffer.charAt(position))) {
                position++;
            }
            tokenEnd = position;
            String word = buffer.subSequence(tokenStart, tokenEnd).toString();
            tokenType = KEYWORDS.contains(word) ? NginxTokenTypes.KEYWORD : NginxTokenTypes.IDENTIFIER;
            return;
        }

        position++;
        tokenEnd = position;
        tokenType = NginxTokenTypes.BAD_CHARACTER;
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }

    private boolean isIdentifierStart(char c) {
        return Character.isLetter(c) || c == '_' || c == '/';
    }

    private boolean isIdentifierPart(char c) {
        return Character.isLetterOrDigit(c) || c == '_' || c == '-' || c == '.' || c == ':' || c == '/';
    }
}

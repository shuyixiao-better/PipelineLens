package com.poeticcoder.nginx.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import com.poeticcoder.nginx.lexer.NginxLexer;
import com.poeticcoder.nginx.token.NginxTokenTypes;
import org.jetbrains.annotations.NotNull;

public class NginxSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final TextAttributesKey NGINX_KEYWORD = DefaultLanguageHighlighterColors.KEYWORD;
    public static final TextAttributesKey NGINX_IDENTIFIER = DefaultLanguageHighlighterColors.IDENTIFIER;
    public static final TextAttributesKey NGINX_STRING = DefaultLanguageHighlighterColors.STRING;
    public static final TextAttributesKey NGINX_NUMBER = DefaultLanguageHighlighterColors.NUMBER;
    public static final TextAttributesKey NGINX_COMMENT = DefaultLanguageHighlighterColors.LINE_COMMENT;
    public static final TextAttributesKey NGINX_VARIABLE = DefaultLanguageHighlighterColors.INSTANCE_FIELD;
    public static final TextAttributesKey NGINX_BRACE = DefaultLanguageHighlighterColors.BRACES;
    public static final TextAttributesKey NGINX_SEMICOLON = DefaultLanguageHighlighterColors.SEMICOLON;
    public static final TextAttributesKey NGINX_BAD = HighlighterColors.BAD_CHARACTER;

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new NginxLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (tokenType == NginxTokenTypes.KEYWORD) {
            return pack(NGINX_KEYWORD);
        }
        if (tokenType == NginxTokenTypes.STRING) {
            return pack(NGINX_STRING);
        }
        if (tokenType == NginxTokenTypes.NUMBER) {
            return pack(NGINX_NUMBER);
        }
        if (tokenType == NginxTokenTypes.COMMENT) {
            return pack(NGINX_COMMENT);
        }
        if (tokenType == NginxTokenTypes.VARIABLE) {
            return pack(NGINX_VARIABLE);
        }
        if (tokenType == NginxTokenTypes.LBRACE || tokenType == NginxTokenTypes.RBRACE) {
            return pack(NGINX_BRACE);
        }
        if (tokenType == NginxTokenTypes.SEMICOLON) {
            return pack(NGINX_SEMICOLON);
        }
        if (tokenType == NginxTokenTypes.BAD_CHARACTER) {
            return pack(NGINX_BAD);
        }
        if (tokenType == NginxTokenTypes.IDENTIFIER) {
            return pack(NGINX_IDENTIFIER);
        }
        return TextAttributesKey.EMPTY_ARRAY;
    }
}

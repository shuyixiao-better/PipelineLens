package com.poeticcoder.nginx.token;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.poeticcoder.nginx.lang.NginxLanguage;

public final class NginxTokenTypes {

    public static final IElementType KEYWORD = new IElementType("NGINX_KEYWORD", NginxLanguage.INSTANCE);
    public static final IElementType IDENTIFIER = new IElementType("NGINX_IDENTIFIER", NginxLanguage.INSTANCE);
    public static final IElementType STRING = new IElementType("NGINX_STRING", NginxLanguage.INSTANCE);
    public static final IElementType NUMBER = new IElementType("NGINX_NUMBER", NginxLanguage.INSTANCE);
    public static final IElementType COMMENT = new IElementType("NGINX_COMMENT", NginxLanguage.INSTANCE);
    public static final IElementType VARIABLE = new IElementType("NGINX_VARIABLE", NginxLanguage.INSTANCE);
    public static final IElementType LBRACE = new IElementType("NGINX_LBRACE", NginxLanguage.INSTANCE);
    public static final IElementType RBRACE = new IElementType("NGINX_RBRACE", NginxLanguage.INSTANCE);
    public static final IElementType SEMICOLON = new IElementType("NGINX_SEMICOLON", NginxLanguage.INSTANCE);

    public static final IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    public static final IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    private NginxTokenTypes() {
    }
}

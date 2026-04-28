package com.poeticcoder.nginx.lang;

import com.intellij.lang.Language;

/**
 * Nginx configuration language marker.
 */
public class NginxLanguage extends Language {

    public static final NginxLanguage INSTANCE = new NginxLanguage();

    private NginxLanguage() {
        super("Nginx");
    }
}

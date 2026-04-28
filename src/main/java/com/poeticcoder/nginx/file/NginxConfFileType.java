package com.poeticcoder.nginx.file;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import com.poeticcoder.nginx.lang.NginxLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class NginxConfFileType extends LanguageFileType {

    public static final NginxConfFileType INSTANCE = new NginxConfFileType();
    public static final String DEFAULT_EXTENSION = "conf";

    private NginxConfFileType() {
        super(NginxLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Nginx Config";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Nginx configuration file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Nullable
    @Override
    public Icon getIcon() {
        try {
            return IconLoader.getIcon("/icons/nginx.svg", NginxConfFileType.class);
        } catch (Exception e) {
            return null;
        }
    }
}

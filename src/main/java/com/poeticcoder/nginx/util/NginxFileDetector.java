package com.poeticcoder.nginx.util;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public final class NginxFileDetector {

    private NginxFileDetector() {
    }

    public static boolean isNginxConfFile(@NotNull VirtualFile file) {
        String name = file.getName().toLowerCase();
        return isNginxConfFileName(name);
    }

    public static boolean isNginxConfFileName(@NotNull String fileNameLowerCase) {
        return fileNameLowerCase.equals("nginx.conf")
                || fileNameLowerCase.endsWith(".nginx.conf")
                || fileNameLowerCase.endsWith(".conf");
    }
}

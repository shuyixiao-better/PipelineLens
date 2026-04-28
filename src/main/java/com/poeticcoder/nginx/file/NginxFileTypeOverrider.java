package com.poeticcoder.nginx.file;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.impl.FileTypeOverrider;
import com.intellij.openapi.vfs.VirtualFile;
import com.poeticcoder.nginx.util.NginxFileDetector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NginxFileTypeOverrider implements FileTypeOverrider {

    @Nullable
    @Override
    public FileType getOverriddenFileType(@NotNull VirtualFile file) {
        if (NginxFileDetector.isNginxConfFileName(file.getName().toLowerCase())) {
            return NginxConfFileType.INSTANCE;
        }
        return null;
    }
}

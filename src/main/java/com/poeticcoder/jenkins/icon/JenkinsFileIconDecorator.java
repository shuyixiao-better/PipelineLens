package com.poeticcoder.jenkins.icon;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.poeticcoder.nginx.util.NginxFileDetector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Jenkins文件图标装饰器
 */
public class JenkinsFileIconDecorator implements FileIconProvider {

    private static final Icon JENKINS_ICON = IconLoader.getIcon("/icons/jenkinsfile.svg", JenkinsFileIconDecorator.class);
    private static final Icon NGINX_ICON = IconLoader.getIcon("/icons/nginx.svg", JenkinsFileIconDecorator.class);

    @Nullable
    @Override
    public Icon getIcon(@NotNull VirtualFile file, int flags, @Nullable Project project) {
        if (isJenkinsFile(file)) {
            return JENKINS_ICON;
        }
        if (isNginxFile(file)) {
            return NGINX_ICON;
        }
        return null;
    }

    private boolean isJenkinsFile(@NotNull VirtualFile file) {
        String fileName = file.getName();
        
        if ("Jenkinsfile".equals(fileName)) {
            return true;
        }
        
        if (fileName.startsWith("Jenkinsfile.")) {
            return true;
        }
        
        String lowerFileName = fileName.toLowerCase();
        if (lowerFileName.endsWith(".jenkinsfile")) {
            return true;
        }
        
        if (lowerFileName.equals("jenkins") || lowerFileName.equals("pipeline")) {
            return true;
        }
        
        return false;
    }

    private boolean isNginxFile(@NotNull VirtualFile file) {
        return NginxFileDetector.isNginxConfFile(file);
    }
}

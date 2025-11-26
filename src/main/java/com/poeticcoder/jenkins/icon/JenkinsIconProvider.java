package com.poeticcoder.jenkins.icon;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.poeticcoder.jenkins.file.JenkinsFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Jenkins图标提供器
 */
public class JenkinsIconProvider extends IconProvider implements DumbAware {

    private static final Logger LOG = Logger.getInstance(JenkinsIconProvider.class);
    private static final Icon JENKINS_ICON;
    
    static {
        Icon icon = null;
        try {
            icon = IconLoader.getIcon("/icons/jenkinsfile.svg", JenkinsIconProvider.class);
            LOG.info("Successfully loaded Jenkins icon");
        } catch (Exception e) {
            LOG.warn("Failed to load Jenkins icon", e);
            try {
                icon = IconLoader.getIcon("/icons/jenkinsfile@2x.svg", JenkinsIconProvider.class);
                LOG.info("Successfully loaded Jenkins @2x icon as fallback");
            } catch (Exception ex) {
                LOG.error("Failed to load any Jenkins icon", ex);
            }
        }
        JENKINS_ICON = icon;
    }

    @Nullable
    @Override
    public Icon getIcon(@NotNull PsiElement element, int flags) {
        if (element instanceof PsiFile) {
            PsiFile psiFile = (PsiFile) element;
            VirtualFile virtualFile = psiFile.getVirtualFile();
            
            if (virtualFile != null && isJenkinsFile(virtualFile)) {
                LOG.debug("Providing Jenkins icon for file: " + virtualFile.getName());
                return JENKINS_ICON;
            }
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
        
        if (file.getFileType() instanceof JenkinsFileType) {
            return true;
        }
        
        return false;
    }
}

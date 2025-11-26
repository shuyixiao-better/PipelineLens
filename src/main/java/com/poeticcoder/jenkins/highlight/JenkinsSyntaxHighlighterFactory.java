package com.poeticcoder.jenkins.highlight;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.poeticcoder.jenkins.file.JenkinsFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.highlighter.GroovySyntaxHighlighter;

/**
 * Jenkins语法高亮器工厂
 */
public class JenkinsSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
    
    @NotNull
    @Override
    public SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile) {
        if (virtualFile != null) {
            if (virtualFile.getFileType() instanceof JenkinsFileType) {
                return new JenkinsSyntaxHighlighter();
            }
            
            String fileName = virtualFile.getName();
            if (isJenkinsFile(fileName)) {
                return new JenkinsSyntaxHighlighter();
            }
            
            if (project != null) {
                try {
                    String content = new String(virtualFile.contentsToByteArray());
                    if (content.contains("pipeline {") || content.contains("node {") || content.contains("@Library")) {
                        return new JenkinsSyntaxHighlighter();
                    }
                } catch (Exception e) {
                    // 忽略异常
                }
            }
        }
        
        return new GroovySyntaxHighlighter();
    }
    
    private boolean isJenkinsFile(@NotNull String fileName) {
        return "Jenkinsfile".equals(fileName) ||
               fileName.startsWith("Jenkinsfile.") ||
               fileName.toLowerCase().endsWith(".jenkinsfile");
    }
}

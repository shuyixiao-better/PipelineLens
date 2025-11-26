package com.poeticcoder.jenkins.util;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * Jenkins文件识别工具类
 * 用于识别Jenkinsfile和Jenkins Pipeline脚本
 */
public class JenkinsFileDetector {
    
    /**
     * 判断是否为Jenkins Pipeline文件
     */
    public static boolean isJenkinsFile(@NotNull PsiFile file) {
        String fileName = file.getName();
        String fileContent = file.getText();
        
        if (isJenkinsFileName(fileName)) {
            return true;
        }
        
        return containsJenkinsPipelineContent(fileContent);
    }
    
    /**
     * 检查文件名是否为Jenkins文件名模式
     */
    private static boolean isJenkinsFileName(@NotNull String fileName) {
        return "Jenkinsfile".equals(fileName) || 
               fileName.startsWith("Jenkinsfile.") ||
               fileName.toLowerCase().contains("jenkins");
    }
    
    /**
     * 检查文件内容是否包含Jenkins Pipeline语法
     */
    private static boolean containsJenkinsPipelineContent(@NotNull String content) {
        if (content.contains("pipeline {") || content.contains("pipeline{")) {
            return true;
        }
        
        if (content.contains("node {") || content.contains("node(") || content.contains("node{")) {
            return true;
        }
        
        return content.contains("@Library") ||
               content.contains("agent ") ||
               content.contains("stages {") ||
               content.contains("stage(") ||
               content.contains("steps {") ||
               content.contains("post {") ||
               content.contains("environment {") ||
               content.contains("parameters {") ||
               content.contains("tools {") ||
               content.contains("options {");
    }
    
    /**
     * 检查是否为声明式Pipeline
     */
    public static boolean isDeclarativePipeline(@NotNull PsiFile file) {
        String content = file.getText();
        return content.contains("pipeline {") || content.contains("pipeline{");
    }
    
    /**
     * 检查是否为脚本式Pipeline
     */
    public static boolean isScriptedPipeline(@NotNull PsiFile file) {
        String content = file.getText();
        return (content.contains("node {") || content.contains("node(") || content.contains("node{")) &&
               !isDeclarativePipeline(file);
    }
}

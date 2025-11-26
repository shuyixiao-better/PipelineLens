package com.poeticcoder.jenkins.gdsl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.poeticcoder.jenkins.model.Descriptor;
import com.poeticcoder.jenkins.util.JenkinsFileDetector;
import com.poeticcoder.jenkins.util.PsiUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Jenkins GDSL成员提供器
 */
public class JenkinsGdslMembersProvider {

    public void processDynamicElements(@NotNull PsiElement context) {
        PsiFile file = context.getContainingFile();
        if (file == null || !JenkinsFileDetector.isJenkinsFile(file)) {
            return;
        }

        JenkinsGdslService service = JenkinsGdslService.getInstance(file.getProject());
        if (service == null) {
            return;
        }

        Map<String, Descriptor> descriptors = service.getAllDescriptors("jenkins");
        if (descriptors.isEmpty()) {
            return;
        }
    }

    public List<String> getJenkinsPipelineCompletions(@NotNull PsiElement context) {
        PsiFile file = context.getContainingFile();
        if (file == null || !JenkinsFileDetector.isJenkinsFile(file)) {
            return List.of();
        }

        JenkinsGdslService service = JenkinsGdslService.getInstance(file.getProject());
        return service.getAllDefinitionNames();
    }

    public List<String> getAvailableDirectives(@NotNull PsiElement context) {
        if (isInPipelineBlock(context)) {
            return List.of("agent", "stages", "environment", "options", "parameters", "tools", "post", "triggers");
        } else if (isInStagesBlock(context)) {
            return List.of("stage");
        } else if (isInStageBlock(context)) {
            return List.of("steps", "agent", "when", "post", "environment", "tools");
        } else if (isInStepsBlock(context)) {
            return List.of("sh", "bat", "powershell", "echo", "checkout", "git", "script", 
                          "parallel", "build", "archiveArtifacts", "publishTestResults", 
                          "emailext", "input", "timeout", "retry", "catchError", "stash", 
                          "unstash", "dir", "deleteDir", "pwd", "readFile", "writeFile", 
                          "fileExists", "isUnix", "tool", "withEnv", "withCredentials");
        } else if (isInPostBlock(context)) {
            return List.of("always", "success", "failure", "unstable", "changed");
        } else if (isInParametersBlock(context)) {
            return List.of("string", "booleanParam", "choice", "password", "text", "file");
        } else if (PsiUtils.isTopLevel(context)) {
            return List.of("pipeline", "node");
        }
        return List.of();
    }

    public boolean isPropertyOf(@NotNull String name, @NotNull PsiElement context) {
        return false;
    }

    private boolean isInPipelineBlock(@NotNull PsiElement context) {
        return PsiUtils.isInClosureBlock(context, "pipeline");
    }

    private boolean isInStagesBlock(@NotNull PsiElement context) {
        return PsiUtils.isInClosureBlock(context, "stages");
    }

    private boolean isInStageBlock(@NotNull PsiElement context) {
        return PsiUtils.isInClosureBlock(context, "stage");
    }

    private boolean isInStepsBlock(@NotNull PsiElement context) {
        return PsiUtils.isInClosureBlock(context, "steps");
    }

    private boolean isInPostBlock(@NotNull PsiElement context) {
        return PsiUtils.isInClosureBlock(context, "post");
    }

    private boolean isInParametersBlock(@NotNull PsiElement context) {
        return PsiUtils.isInClosureBlock(context, "parameters");
    }
}

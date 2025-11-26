package com.poeticcoder.jenkins.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrMethod;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.blocks.GrClosableBlock;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

/**
 * PSI工具类
 * 提供PSI元素操作的便利方法
 */
public class PsiUtils {
    
    @Nullable
    public static GrMethod getContainingMethod(@NotNull PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, GrMethod.class);
    }
    
    @Nullable
    public static GrClosableBlock getContainingClosure(@NotNull PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, GrClosableBlock.class);
    }
    
    @Nullable
    public static String getMethodName(@NotNull GrMethodCall methodCall) {
        PsiElement invokedExpression = methodCall.getInvokedExpression();
        if (invokedExpression instanceof GrReferenceExpression) {
            return ((GrReferenceExpression) invokedExpression).getReferenceName();
        }
        return invokedExpression != null ? invokedExpression.getText() : null;
    }
    
    public static boolean isJenkinsBuiltinMethod(@Nullable PsiElement method) {
        if (method == null) {
            return false;
        }
        
        String methodName = method.getText();
        return isJenkinsBuiltinMethodName(methodName);
    }
    
    public static boolean isJenkinsBuiltinMethodName(@Nullable String methodName) {
        if (methodName == null) {
            return false;
        }
        
        return methodName.equals("sh") ||
               methodName.equals("bat") ||
               methodName.equals("powershell") ||
               methodName.equals("echo") ||
               methodName.equals("checkout") ||
               methodName.equals("git") ||
               methodName.equals("stage") ||
               methodName.equals("steps") ||
               methodName.equals("script") ||
               methodName.equals("parallel") ||
               methodName.equals("build") ||
               methodName.equals("archiveArtifacts") ||
               methodName.equals("publishTestResults") ||
               methodName.equals("emailext") ||
               methodName.equals("input") ||
               methodName.equals("timeout") ||
               methodName.equals("retry") ||
               methodName.equals("catchError") ||
               methodName.equals("unstash") ||
               methodName.equals("stash") ||
               methodName.equals("dir") ||
               methodName.equals("deleteDir") ||
               methodName.equals("pwd") ||
               methodName.equals("readFile") ||
               methodName.equals("writeFile") ||
               methodName.equals("fileExists") ||
               methodName.equals("isUnix") ||
               methodName.equals("tool") ||
               methodName.equals("withEnv") ||
               methodName.equals("withCredentials") ||
               methodName.equals("node") ||
               methodName.equals("pipeline") ||
               methodName.equals("agent") ||
               methodName.equals("stages") ||
               methodName.equals("post") ||
               methodName.equals("environment") ||
               methodName.equals("parameters") ||
               methodName.equals("options") ||
               methodName.equals("tools") ||
               methodName.equals("when") ||
               methodName.equals("matrix") ||
               methodName.equals("triggers");
    }
    
    public static boolean isTopLevel(@NotNull PsiElement element) {
        return getContainingMethod(element) == null && getContainingClosure(element) == null;
    }
    
    @NotNull
    public static String getTextSafe(@Nullable PsiElement element) {
        return element != null ? element.getText() : "";
    }
    
    public static boolean isReferenceExpression(@NotNull PsiElement element, @NotNull String referenceName) {
        if (element instanceof GrReferenceExpression) {
            GrReferenceExpression ref = (GrReferenceExpression) element;
            return referenceName.equals(ref.getReferenceName());
        }
        return false;
    }
    
    public static boolean isInClosureBlock(@NotNull PsiElement element, @NotNull String blockName) {
        GrClosableBlock closureBlock = getContainingClosure(element);
        if (closureBlock == null) {
            return false;
        }
        
        PsiElement parent = closureBlock.getParent();
        if (parent instanceof GrMethodCall) {
            String methodName = getMethodName((GrMethodCall) parent);
            return blockName.equals(methodName);
        }
        
        return false;
    }
}

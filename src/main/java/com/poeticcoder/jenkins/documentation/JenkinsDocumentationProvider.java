package com.poeticcoder.jenkins.documentation;

import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.poeticcoder.jenkins.gdsl.JenkinsGdslService;
import com.poeticcoder.jenkins.model.Descriptor;
import com.poeticcoder.jenkins.util.JenkinsFileDetector;
import com.poeticcoder.jenkins.util.PsiUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.groovy.lang.documentation.GroovyDocumentationProvider;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

import java.util.List;

/**
 * Jenkins文档提供器
 */
public class JenkinsDocumentationProvider implements DocumentationProvider {
    
    private final GroovyDocumentationProvider delegated = new GroovyDocumentationProvider();

    @Override
    public String generateDoc(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        PsiFile file = element.getContainingFile();
        if (file == null || !JenkinsFileDetector.isJenkinsFile(file)) {
            return delegated.generateDoc(element, originalElement);
        }

        String jenkinsDoc = generateJenkinsDocumentation(element, originalElement);
        if (jenkinsDoc != null) {
            return jenkinsDoc;
        }

        return delegated.generateDoc(element, originalElement);
    }

    @Override
    public PsiElement getDocumentationElementForLookupItem(@NotNull PsiManager psiManager, 
                                                          @NotNull Object object, 
                                                          @NotNull PsiElement element) {
        return delegated.getDocumentationElementForLookupItem(psiManager, object, element);
    }

    @Override
    public PsiElement getDocumentationElementForLink(@NotNull PsiManager psiManager, 
                                                    @NotNull String link, 
                                                    @NotNull PsiElement context) {
        return delegated.getDocumentationElementForLink(psiManager, link, context);
    }

    @Override
    public List<String> getUrlFor(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        List<String> jenkinsUrls = getJenkinsDocumentationUrls(element, originalElement);
        if (jenkinsUrls != null && !jenkinsUrls.isEmpty()) {
            return jenkinsUrls;
        }

        return null;
    }

    @Nullable
    private String generateJenkinsDocumentation(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        if (element instanceof GrReferenceExpression) {
            GrReferenceExpression ref = (GrReferenceExpression) element;
            String methodName = ref.getReferenceName();
            
            if (methodName != null && PsiUtils.isJenkinsBuiltinMethodName(methodName)) {
                return buildJenkinsMethodDocumentation(methodName, element);
            }
        }

        PsiElement parent = element.getParent();
        if (parent instanceof GrMethodCall) {
            GrMethodCall methodCall = (GrMethodCall) parent;
            String methodName = PsiUtils.getMethodName(methodCall);
            
            if (methodName != null && PsiUtils.isJenkinsBuiltinMethodName(methodName)) {
                return buildJenkinsMethodDocumentation(methodName, element);
            }
        }

        return null;
    }

    @NotNull
    private String buildJenkinsMethodDocumentation(@NotNull String methodName, @NotNull PsiElement context) {
        Project project = context.getProject();
        JenkinsGdslService service = JenkinsGdslService.getInstance(project);
        
        Descriptor descriptor = service.getDescriptor("jenkins", methodName);
        if (descriptor != null) {
            return buildGdslDocumentation(descriptor);
        }

        return buildBasicMethodDocumentation(methodName);
    }

    @NotNull
    private String buildGdslDocumentation(@NotNull Descriptor descriptor) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class='definition'>");
        html.append("<pre>");
        
        html.append("<b>").append(descriptor.getName()).append("</b>");
        
        List<Descriptor.Parameter> parameters = descriptor.getParameters();
        if (!parameters.isEmpty()) {
            html.append("(");
            for (int i = 0; i < parameters.size(); i++) {
                if (i > 0) html.append(", ");
                
                Descriptor.Parameter param = parameters.get(i);
                html.append("<i>").append(param.getType()).append("</i> ");
                html.append(param.getName());
                
                if (!param.isRequired()) {
                    html.append(" <span style='color: #888;'>[可选]</span>");
                }
            }
            html.append(")");
        } else {
            html.append("()");
        }
        
        html.append("</pre>");
        html.append("</div>");
        
        if (!descriptor.getDocumentation().isEmpty()) {
            html.append("<div class='content'>");
            html.append("<p>").append(descriptor.getDocumentation()).append("</p>");
            html.append("</div>");
        }
        
        if (!parameters.isEmpty()) {
            html.append("<div class='sections'>");
            html.append("<p><b>参数:</b></p>");
            html.append("<table>");
            
            for (Descriptor.Parameter param : parameters) {
                html.append("<tr>");
                html.append("<td valign='top'><code>").append(param.getName()).append("</code></td>");
                html.append("<td valign='top'>").append(param.getType());
                if (param.isRequired()) {
                    html.append(" <span style='color: #d73a49;'>(必需)</span>");
                }
                html.append("</td>");
                html.append("<td>").append(param.getDocumentation()).append("</td>");
                html.append("</tr>");
            }
            
            html.append("</table>");
            html.append("</div>");
        }
        
        return html.toString();
    }

    @NotNull
    private String buildBasicMethodDocumentation(@NotNull String methodName) {
        StringBuilder html = new StringBuilder();
        
        html.append("<div class='definition'>");
        html.append("<pre><b>").append(methodName).append("</b></pre>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<p>Jenkins Pipeline内置步骤: <code>").append(methodName).append("</code></p>");
        html.append("<p>请参考Jenkins官方文档了解详细用法。</p>");
        html.append("</div>");
        
        return html.toString();
    }

    @Nullable
    private List<String> getJenkinsDocumentationUrls(@NotNull PsiElement element, @Nullable PsiElement originalElement) {
        String methodName = null;
        
        if (element instanceof GrReferenceExpression) {
            methodName = ((GrReferenceExpression) element).getReferenceName();
        } else if (element.getParent() instanceof GrMethodCall) {
            methodName = PsiUtils.getMethodName((GrMethodCall) element.getParent());
        }
        
        if (methodName != null && PsiUtils.isJenkinsBuiltinMethodName(methodName)) {
            String baseUrl = "https://www.jenkins.io/doc/pipeline/steps/";
            return List.of(baseUrl + "workflow-basic-steps/");
        }
        
        return null;
    }
}

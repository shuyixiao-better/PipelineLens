package com.poeticcoder.nginx.fold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.poeticcoder.nginx.util.NginxFileDetector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Fallback folding for TEXT language, only active for .conf files.
 */
public class NginxTextFallbackFoldingBuilder extends FoldingBuilderEx {

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        PsiFile file = root.getContainingFile();
        if (file == null || file.getVirtualFile() == null) {
            return FoldingDescriptor.EMPTY_ARRAY;
        }

        String fileName = file.getVirtualFile().getName().toLowerCase();
        if (!NginxFileDetector.isNginxConfFileName(fileName)) {
            return FoldingDescriptor.EMPTY_ARRAY;
        }

        String text = root.getText();
        ASTNode node = root.getNode();
        if (text == null || node == null || text.isEmpty()) {
            return FoldingDescriptor.EMPTY_ARRAY;
        }

        List<FoldingDescriptor> descriptors = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '{') {
                stack.push(i);
            } else if (ch == '}' && !stack.isEmpty()) {
                int start = stack.pop();
                int end = i + 1;
                if (document.getLineNumber(start) < document.getLineNumber(Math.max(start, end - 1))) {
                    descriptors.add(new FoldingDescriptor(node, new TextRange(start, end)));
                }
            }
        }

        return descriptors.toArray(FoldingDescriptor.EMPTY_ARRAY);
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        return "{...}";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false;
    }
}

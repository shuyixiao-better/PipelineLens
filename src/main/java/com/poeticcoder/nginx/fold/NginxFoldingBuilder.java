package com.poeticcoder.nginx.fold;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Folding for Nginx config blocks, e.g. http/server/location/upstream.
 */
public class NginxFoldingBuilder extends FoldingBuilderEx {

    @NotNull
    @Override
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root,
                                                @NotNull Document document,
                                                boolean quick) {
        String text = root.getText();
        ASTNode node = root.getNode();
        if (text == null || node == null || text.isEmpty()) {
            return FoldingDescriptor.EMPTY_ARRAY;
        }

        List<FoldingDescriptor> descriptors = new ArrayList<>();
        Deque<Integer> braceStack = new ArrayDeque<>();

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '{') {
                braceStack.push(i);
                continue;
            }

            if (ch != '}' || braceStack.isEmpty()) {
                continue;
            }

            int start = braceStack.pop();
            int end = i + 1;
            int startLine = document.getLineNumber(start);
            int endLine = document.getLineNumber(Math.max(start, end - 1));

            if (end > start + 2 && endLine > startLine) {
                descriptors.add(new FoldingDescriptor(node, new TextRange(start, end)));
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

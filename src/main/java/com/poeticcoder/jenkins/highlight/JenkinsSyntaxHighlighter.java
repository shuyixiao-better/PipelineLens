package com.poeticcoder.jenkins.highlight;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.highlighter.GroovySyntaxHighlighter;

/**
 * Jenkins Pipeline 语法高亮器
 */
public class JenkinsSyntaxHighlighter extends SyntaxHighlighterBase {
    
    public static final TextAttributesKey JENKINS_KEYWORD = DefaultLanguageHighlighterColors.KEYWORD;
    public static final TextAttributesKey JENKINS_PIPELINE_BLOCK = DefaultLanguageHighlighterColors.KEYWORD;
    public static final TextAttributesKey JENKINS_STAGE_BLOCK = DefaultLanguageHighlighterColors.KEYWORD;
    public static final TextAttributesKey JENKINS_STEP_METHOD = DefaultLanguageHighlighterColors.FUNCTION_CALL;
    public static final TextAttributesKey JENKINS_VARIABLE = DefaultLanguageHighlighterColors.IDENTIFIER;
    public static final TextAttributesKey JENKINS_STRING = DefaultLanguageHighlighterColors.STRING;
    public static final TextAttributesKey JENKINS_COMMENT = DefaultLanguageHighlighterColors.LINE_COMMENT;
    public static final TextAttributesKey JENKINS_NUMBER = DefaultLanguageHighlighterColors.NUMBER;
    public static final TextAttributesKey JENKINS_BRACKET = DefaultLanguageHighlighterColors.BRACKETS;
    public static final TextAttributesKey JENKINS_BRACE = DefaultLanguageHighlighterColors.BRACES;
    public static final TextAttributesKey JENKINS_OPERATOR = DefaultLanguageHighlighterColors.OPERATION_SIGN;
    
    private final GroovySyntaxHighlighter groovyHighlighter = new GroovySyntaxHighlighter();

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return groovyHighlighter.getHighlightingLexer();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey[] groovyKeys = groovyHighlighter.getTokenHighlights(tokenType);
        
        if (groovyKeys.length > 0) {
            TextAttributesKey groovyKey = groovyKeys[0];
            
            if (groovyKey != null) {
                if (groovyKey.equals(DefaultLanguageHighlighterColors.KEYWORD)) {
                    return new TextAttributesKey[]{JENKINS_KEYWORD};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.STRING)) {
                    return new TextAttributesKey[]{JENKINS_STRING};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.LINE_COMMENT) || 
                           groovyKey.equals(DefaultLanguageHighlighterColors.BLOCK_COMMENT)) {
                    return new TextAttributesKey[]{JENKINS_COMMENT};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.NUMBER)) {
                    return new TextAttributesKey[]{JENKINS_NUMBER};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.BRACKETS)) {
                    return new TextAttributesKey[]{JENKINS_BRACKET};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.BRACES)) {
                    return new TextAttributesKey[]{JENKINS_BRACE};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.OPERATION_SIGN)) {
                    return new TextAttributesKey[]{JENKINS_OPERATOR};
                } else if (groovyKey.equals(DefaultLanguageHighlighterColors.IDENTIFIER)) {
                    return new TextAttributesKey[]{JENKINS_VARIABLE};
                }
            }
        }
        
        if (groovyKeys.length > 0) {
            return new TextAttributesKey[]{JENKINS_KEYWORD};
        }
        
        return groovyKeys;
    }
}

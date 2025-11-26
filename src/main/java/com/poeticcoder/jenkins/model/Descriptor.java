package com.poeticcoder.jenkins.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Jenkins Pipeline语法描述符
 */
public class Descriptor {
    private final String id;
    private final String name;
    private final boolean hasGetter;
    private final String documentation;
    private final List<Parameter> parameters;

    public Descriptor(@NotNull String id, @NotNull String name, boolean hasGetter, 
                     @Nullable String documentation, @NotNull List<Parameter> parameters) {
        this.id = id;
        this.name = name;
        this.hasGetter = hasGetter;
        this.documentation = documentation != null ? documentation : "";
        this.parameters = new ArrayList<>(parameters);
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean hasGetter() {
        return hasGetter;
    }

    @NotNull
    public String getDocumentation() {
        return documentation;
    }

    @NotNull
    public List<Parameter> getParameters() {
        return new ArrayList<>(parameters);
    }

    @Nullable
    public Parameter getParameter(@NotNull String name) {
        return parameters.stream()
                .filter(param -> name.equals(param.getName()))
                .findFirst()
                .orElse(null);
    }

    public boolean hasRequiredParameters() {
        return parameters.stream().anyMatch(Parameter::isRequired);
    }

    @NotNull
    public List<Parameter> getRequiredParameters() {
        return parameters.stream()
                .filter(Parameter::isRequired)
                .toList();
    }

    @Override
    public String toString() {
        return "Descriptor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hasGetter=" + hasGetter +
                ", parametersCount=" + parameters.size() +
                '}';
    }

    public static class Parameter {
        private final String name;
        private final String type;
        private final boolean required;
        private final String documentation;

        public Parameter(@NotNull String name, @NotNull String type, boolean required, 
                        @Nullable String documentation) {
            this.name = name;
            this.type = type;
            this.required = required;
            this.documentation = documentation != null ? documentation : "";
        }

        @NotNull
        public String getName() {
            return name;
        }

        @NotNull
        public String getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }

        @NotNull
        public String getDocumentation() {
            return documentation;
        }

        public boolean isClosure() {
            return "closure".equals(type);
        }

        public boolean isPrimitiveType() {
            return "string".equals(type) || 
                   "boolean".equals(type) || 
                   "int".equals(type) || 
                   "long".equals(type) || 
                   "double".equals(type);
        }

        public boolean isCollectionType() {
            return "list".equals(type) || 
                   "map".equals(type) || 
                   "array".equals(type);
        }

        @Override
        public String toString() {
            return "Parameter{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", required=" + required +
                    '}';
        }
    }
}

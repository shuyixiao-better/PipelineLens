package com.poeticcoder.jenkins.gdsl;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.poeticcoder.jenkins.model.Descriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(Service.Level.PROJECT)
public final class JenkinsGdslService {
    private static final Logger LOG = Logger.getInstance(JenkinsGdslService.class);
    private static final String DESCRIPTORS_FILE = "/descriptors/jenkinsPipeline.xml";
    
    private final Project project;
    private final ConcurrentHashMap<String, Map<String, Descriptor>> descriptorsCache = new ConcurrentHashMap<>();
    private volatile boolean initialized = false;

    public JenkinsGdslService(@NotNull Project project) {
        this.project = project;
    }

    @Nullable
    public Descriptor getDescriptor(@NotNull String gdslId, @NotNull String definitionId) {
        ensureInitialized();
        
        Map<String, Descriptor> descriptors = descriptorsCache.get(gdslId);
        if (descriptors == null) {
            descriptors = descriptorsCache.get("jenkins");
        }
        
        return descriptors != null ? descriptors.get(definitionId) : null;
    }

    @NotNull
    public Map<String, Descriptor> getAllDescriptors(@NotNull String gdslId) {
        ensureInitialized();
        
        Map<String, Descriptor> descriptors = descriptorsCache.get(gdslId);
        if (descriptors == null) {
            descriptors = descriptorsCache.get("jenkins");
        }
        
        return descriptors != null ? Map.copyOf(descriptors) : Map.of();
    }

    public boolean hasDefinition(@NotNull String gdslId, @NotNull String definitionId) {
        return getDescriptor(gdslId, definitionId) != null;
    }

    @NotNull
    public List<String> getAllDefinitionNames() {
        ensureInitialized();
        
        return descriptorsCache.values().stream()
                .flatMap(map -> map.keySet().stream())
                .distinct()
                .sorted()
                .toList();
    }

    private void ensureInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    loadDescriptors();
                    initialized = true;
                }
            }
        }
    }

    private void loadDescriptors() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(DESCRIPTORS_FILE);
            if (inputStream == null) {
                LOG.warn("Jenkins Pipeline descriptors file not found: " + DESCRIPTORS_FILE);
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            
            Map<String, Descriptor> jenkinsDescriptors = parseDescriptors(document);
            
            descriptorsCache.put("jenkins", jenkinsDescriptors);
            descriptorsCache.put("jenkinsfile", jenkinsDescriptors);
            
            LOG.info("Loaded " + jenkinsDescriptors.size() + " Jenkins Pipeline descriptors");
            
        } catch (Exception e) {
            LOG.error("Failed to load Jenkins Pipeline descriptors", e);
        }
    }

    @NotNull
    private Map<String, Descriptor> parseDescriptors(@NotNull Document document) {
        Map<String, Descriptor> descriptors = new ConcurrentHashMap<>();
        
        NodeList definitionNodes = document.getElementsByTagName("definition");
        for (int i = 0; i < definitionNodes.getLength(); i++) {
            Node definitionNode = definitionNodes.item(i);
            if (definitionNode.getNodeType() == Node.ELEMENT_NODE) {
                Descriptor descriptor = parseDefinition((Element) definitionNode);
                if (descriptor != null) {
                    descriptors.put(descriptor.getId(), descriptor);
                }
            }
        }
        
        return descriptors;
    }

    @Nullable
    private Descriptor parseDefinition(@NotNull Element definitionElement) {
        try {
            String id = definitionElement.getAttribute("id");
            String name = definitionElement.getAttribute("name");
            boolean hasGetter = "true".equals(definitionElement.getAttribute("hasGetter"));
            
            if (id.isEmpty() || name.isEmpty()) {
                LOG.warn("Invalid definition element: missing id or name");
                return null;
            }
            
            String documentation = parseDocumentation(definitionElement);
            List<Descriptor.Parameter> parameters = parseParameters(definitionElement);
            
            return new Descriptor(id, name, hasGetter, documentation, parameters);
            
        } catch (Exception e) {
            LOG.warn("Failed to parse definition element", e);
            return null;
        }
    }

    @Nullable
    private String parseDocumentation(@NotNull Element definitionElement) {
        NodeList docNodes = definitionElement.getElementsByTagName("doc");
        if (docNodes.getLength() > 0) {
            Node docNode = docNodes.item(0);
            return docNode.getTextContent().trim();
        }
        return null;
    }

    @NotNull
    private List<Descriptor.Parameter> parseParameters(@NotNull Element definitionElement) {
        List<Descriptor.Parameter> parameters = new ArrayList<>();
        
        NodeList parametersNodes = definitionElement.getElementsByTagName("parameters");
        if (parametersNodes.getLength() > 0) {
            Element parametersElement = (Element) parametersNodes.item(0);
            NodeList parameterNodes = parametersElement.getElementsByTagName("parameter");
            
            for (int i = 0; i < parameterNodes.getLength(); i++) {
                Node parameterNode = parameterNodes.item(i);
                if (parameterNode.getNodeType() == Node.ELEMENT_NODE) {
                    Descriptor.Parameter parameter = parseParameter((Element) parameterNode);
                    if (parameter != null) {
                        parameters.add(parameter);
                    }
                }
            }
        }
        
        return parameters;
    }

    @Nullable
    private Descriptor.Parameter parseParameter(@NotNull Element parameterElement) {
        try {
            String name = parameterElement.getAttribute("name");
            String type = parameterElement.getAttribute("type");
            boolean required = "true".equals(parameterElement.getAttribute("required"));
            
            if (name.isEmpty() || type.isEmpty()) {
                LOG.warn("Invalid parameter element: missing name or type");
                return null;
            }
            
            String documentation = null;
            NodeList docNodes = parameterElement.getElementsByTagName("doc");
            if (docNodes.getLength() > 0) {
                documentation = docNodes.item(0).getTextContent().trim();
            }
            
            return new Descriptor.Parameter(name, type, required, documentation);
            
        } catch (Exception e) {
            LOG.warn("Failed to parse parameter element", e);
            return null;
        }
    }

    public void reload() {
        synchronized (this) {
            descriptorsCache.clear();
            initialized = false;
            ensureInitialized();
        }
    }

    @NotNull
    public static JenkinsGdslService getInstance(@NotNull Project project) {
        return project.getService(JenkinsGdslService.class);
    }
}

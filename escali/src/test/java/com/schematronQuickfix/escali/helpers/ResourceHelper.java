package com.schematronQuickfix.escali.helpers;
import org.xmlunit.builder.Input;

import com.github.oxygenPlugins.common.text.TextSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import static org.xmlunit.builder.Input.fromURI;



public class ResourceHelper {

    private final Class<?> testClass;
    private final String baseResourcePath;

    public ResourceHelper(Class<?> testClass) {
        this(testClass, ".");
    }


    public ResourceHelper(Class<?> testClass, String baseResourcePath) {
        this.testClass = testClass;
        String sep = (!"".equals(baseResourcePath) && !baseResourcePath.endsWith("/")) ? "/" : "";
        this.baseResourcePath = baseResourcePath + sep;
    }


    public Input.Builder xml(String relativePath) throws IOException {

        try {
            URL resource = url(relativePath);
            return fromURI(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }


    public InputStream stream(String relativePath) throws IOException {

        InputStream resource = testClass.getResourceAsStream(baseResourcePath + relativePath);

        if (resource == null) {
            throw new IOException(
                    "resource not found: " + (baseResourcePath + relativePath) +
                            " relative to " + testClass.getPackage().getName());
        }

        return resource;

    }
    
    public TextSource textSource(String relativePath) throws IOException{

        URL resource = url(relativePath);
        return TextSource.readTextFile(resource);
    }

    public TextSource[] textSource(String[] relativePath) throws IOException{
        TextSource[] textSources = new TextSource[relativePath.length];
        for (int i = 0; i < relativePath.length; i++) {
            textSources[i] = textSource(relativePath[i]);
        }
        return textSources;
    }


    public URL url(String relativePath) throws IOException {

        URL resource = testClass.getResource(baseResourcePath + relativePath);

        if (resource == null) {
            throw new IOException(
                    "resource not found: " + (baseResourcePath + relativePath) +
                            " relative to " + testClass.getPackage().getName());
        }

        return resource;
    }

    public ResourceHelper resolve(String relativePath) {
        return new ResourceHelper(this.testClass, this.baseResourcePath + "/" + relativePath);
    }
}
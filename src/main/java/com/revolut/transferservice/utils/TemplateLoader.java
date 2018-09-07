package com.revolut.transferservice.utils;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public final class TemplateLoader {

    public static Template getTemplate(String templateName) {
        VelocityEngine velocityEngine = getVelocityEngine();
        return velocityEngine.getTemplate(templateName);
    }

    private static VelocityEngine getVelocityEngine() {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        return ve;
    }
}

package com.revolut.transferservice.utils.resolver;

public interface TemplateResolver<T> {
    String resolveTemplate(String templatePath, long id);
    String resolveTemplate(String templatePath, T model);
}

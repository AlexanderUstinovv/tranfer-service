package com.revolut.transferservice.utils.resolver;

import com.revolut.transferservice.model.Account;
import com.revolut.transferservice.utils.TemplateLoader;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;

public class AccountTemplateResolver implements TemplateResolver<Account> {
    @Override
    public String resolveTemplate(String templatePath, long id) {
        Template template = TemplateLoader.getTemplate(templatePath);
        VelocityContext context = new VelocityContext();
        context.put("idNumber", id);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    @Override
    public String resolveTemplate(String templatePath, Account model) {
        Template template = TemplateLoader.getTemplate(templatePath);
        VelocityContext context = new VelocityContext();
        if (model.getId() != 0) {
            context.put("idNumber", model.getId());
        }
        context.put("balanceValue", model.getBalance());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }
}

package com.revolut.transferservice.dao;

import com.revolut.transferservice.data.QueryExecutor;
import com.revolut.transferservice.model.Account;
import com.revolut.transferservice.utils.TemplateLoader;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TransferAccountDao extends QueryExecutor implements AccountDao {

    public Optional<Account> findById(long id) {
        Template template = TemplateLoader.getTemplate(readAccountTemplate);
        Optional<Account> accountResult = Optional.empty();
        try {
            List<HashMap<String, Object>> results = executeQuery(prepareTemplate(template, id));
            if (results.size() > 0) {
                long resultId = ((Number) results.get(0).get("ID")).longValue();
                double amount = ((Number) results.get(0).get("BALANCE")).doubleValue();
                Account account = new Account(resultId, amount);
                accountResult = Optional.of(account);
            }
        } catch (SQLException ex) {
            logger.error("Error in reading from database", ex);
        }
        return accountResult;
    }

    public void update(Account account) {
        try {
            String query = prepareTemplate(TemplateLoader.getTemplate(updateAccountTemplate), account);
            executeUpdate(query);
        } catch (SQLException ex) {
            logger.error("Error in updating account", ex);
        }
    }

    private String prepareTemplate(Template template, long id) {
        VelocityContext context = new VelocityContext();
        context.put("idNumber", id);
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    private String prepareTemplate(Template template, Account account) {
        VelocityContext context = new VelocityContext();
        if (account.getId() != 0) {
            context.put("idNumber", account.getId());
        }
        context.put("balanceValue", account.getBalance());
        StringWriter stringWriter = new StringWriter();
        template.merge(context, stringWriter);
        return stringWriter.toString();
    }

    private final Logger logger = LoggerFactory.getLogger(TransferAccountDao.class);
    private final String readAccountTemplate = "queries/read_account.vm";
    private final String updateAccountTemplate = "queries/update_account.vm";
}

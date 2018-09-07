package com.revolut.transferservice.data;

import com.revolut.transferservice.config.ApplicationProperties;
import com.revolut.transferservice.utils.TemplateLoader;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum DerbyDataSource implements DbDataSource {
    INSTANCE;

    DerbyDataSource() {
        EmbeddedConnectionPoolDataSource connectionPool = getDatasource();
        try {
            pooledConnection = connectionPool.getPooledConnection();
        } catch (SQLException ex) {
            logger.error("Database connection not initialized", ex);
        }
        initDatabase();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pooledConnection.getConnection();
    }

    public void closePooledConnection() throws SQLException {
        pooledConnection.close();
    }

    private EmbeddedConnectionPoolDataSource getDatasource() {
        EmbeddedConnectionPoolDataSource dataSource = new EmbeddedConnectionPoolDataSource();
        dataSource.setDatabaseName(databaseName);
        dataSource.setConnectionAttributes(databaseAttributes);
        dataSource.setCreateDatabase("create");
        return dataSource;
    }

    private void initDatabase() {
        try (Connection connection = getConnection()) {
            try (StringWriter stringWriter = new StringWriter()){
                Template template = TemplateLoader.getTemplate(initialScript);
                template.merge(new VelocityContext(), stringWriter);
                try (PreparedStatement preparedStatement = connection.prepareStatement(stringWriter.toString())) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            logger.error("Database not created", ex);
        } catch (IOException ex) {
            logger.error("Initial script not found", ex);
        }
    }

    private PooledConnection pooledConnection;
    private final Logger logger = LoggerFactory.getLogger(DerbyDataSource.class);
    private final String databaseName = ApplicationProperties.INSTANCE.getValue("database_name");
    private final String databaseAttributes = ApplicationProperties.INSTANCE.getValue("database_attributes");
    private final String initialScript = "queries/create_account_table.vm";
}

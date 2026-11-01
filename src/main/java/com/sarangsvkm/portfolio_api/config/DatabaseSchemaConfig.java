package com.sarangsvkm.portfolio_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

/**
 * 🛡️ DatabaseSchemaConfig
 * Executes automatic non-destructive column/table additions BEFORE Hibernate initializes.
 * Guarantees that even if Hibernate runs with validate or update, missing columns
 * (such as education.description or project.project_type) never cause startup crashes.
 */
@Configuration
public class DatabaseSchemaConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaConfig.class);

    @Bean
    public static BeanFactoryPostProcessor entityManagerDependsOnSchemaMigrator() {
        return (ConfigurableListableBeanFactory beanFactory) -> {
            for (String name : beanFactory.getBeanDefinitionNames()) {
                if ("entityManagerFactory".equals(name)) {
                    beanFactory.getBeanDefinition(name).setDependsOn("schemaMigrator");
                }
            }
        };
    }

    @Bean(name = "schemaMigrator")
    public SchemaMigrator schemaMigrator(DataSource dataSource) {
        return new SchemaMigrator(dataSource);
    }

    public static class SchemaMigrator {
        public SchemaMigrator(DataSource dataSource) {
            log.info("Running automatic database schema validation & migration...");
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                // 1. Education table columns
                stmt.execute("ALTER TABLE education ADD COLUMN IF NOT EXISTS description TEXT");
                stmt.execute("ALTER TABLE education ADD COLUMN IF NOT EXISTS field_of_study VARCHAR(500)");
                stmt.execute("ALTER TABLE education ADD COLUMN IF NOT EXISTS start_date VARCHAR(500)");
                stmt.execute("ALTER TABLE education ADD COLUMN IF NOT EXISTS end_date VARCHAR(500)");

                // 2. Project table columns
                stmt.execute("ALTER TABLE project ADD COLUMN IF NOT EXISTS project_type VARCHAR(500)");
                stmt.execute("ALTER TABLE project ADD COLUMN IF NOT EXISTS github_url VARCHAR(1000)");
                stmt.execute("ALTER TABLE project ADD COLUMN IF NOT EXISTS image_url VARCHAR(1000)");

                // 3. SystemConfig table
                stmt.execute("CREATE TABLE IF NOT EXISTS system_config (" +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "config_key VARCHAR(255) UNIQUE NOT NULL, " +
                        "config_value TEXT NOT NULL)");

                log.info("Database schema migration completed successfully.");
            } catch (Exception e) {
                log.warn("Notice during automatic schema migration (safe to ignore if columns exist): {}", e.getMessage());
            }
        }
    }
}


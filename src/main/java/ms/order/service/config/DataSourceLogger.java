package ms.order.service.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Configuration
public class DataSourceLogger {

    @Bean
    public ApplicationRunner printDbUrl(DataSource dataSource) {

        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                System.out.println("🔍 JDBC URL: " + conn.getMetaData().getURL());
                System.out.println("👤 User: " + conn.getMetaData().getUserName());
                System.out.println("🧩 Driver: " + conn.getMetaData().getDriverName());

            }
        };
    }

    @Bean
    public ApplicationRunner printTablesAfterMigration(Flyway flyway, DataSource dataSource) {
        return args -> {
            // Forzar que flyway migraciones estén aplicadas
            flyway.migrate();

            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'")) {

                System.out.println("🚀 Tablas en DB:");
                while (rs.next()) {
                    System.out.println(" - " + rs.getString("TABLE_NAME"));
                }
            }
        };
    }


}

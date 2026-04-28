
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FlywayMigrationIntegrationTest {

    @Autowired
    DataSource dataSource;

    @Test
    void debeHaberTablasMigradas() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            String[] types = {"TABLE"};

            System.out.println("🚀 Tablas encontradas en el schema 'public':");
            try (ResultSet rs = meta.getTables(null, "PUBLIC", "%", types)) {
                boolean foundProduct = false;
                boolean foundFlyway = false;
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    System.out.println(" - " + table);
                    if (table.equalsIgnoreCase("product")) foundProduct = true;
                    if (table.equalsIgnoreCase("flyway_schema_history")) foundFlyway = true;
                }

                assertThat(foundProduct).as("Tabla PRODUCT debería existir").isTrue();
                assertThat(foundFlyway).as("Tabla FLYWAY_SCHEMA_HISTORY debería existir").isTrue();
            }
        }
    }
}

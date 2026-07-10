package com.celal.roadrunner.migration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class MySqlFlywayMigrationTest {

    @Container
    static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.4")
            .withDatabaseName("roadrunner")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.flyway.baseline-on-migrate", () -> false);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void migratesFreshMySqlSchemaToLatestVersion() {
        List<String> appliedVersions = jdbcTemplate.queryForList(
                "SELECT version FROM flyway_schema_history WHERE success = 1 ORDER BY installed_rank",
                String.class);
        Integer rentalColumnCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = DATABASE()
                  AND table_name = 'cars'
                  AND column_name IN (
                      'supplier',
                      'vehicle_type',
                      'transmission_type',
                      'fuel_type',
                      'seat_count',
                      'unlimited_mileage',
                      'flexible_cancellation',
                      'carla_cash_eligible',
                      'collision_damage_waiver_included',
                      'taxes_and_fees_included'
                  )
                """, Integer.class);
        Integer bookingTableCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = DATABASE()
                  AND table_name = 'bookings'
                """, Integer.class);

        assertThat(appliedVersions).containsExactly("1", "2", "3", "4");
        assertThat(rentalColumnCount).isEqualTo(10);
        assertThat(bookingTableCount).isZero();
    }
}

package co.com.pragma.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class PostgreSQLConnectionPoolTest {

    @InjectMocks
    private PostgreSQLConnectionPool connectionPool;

    @Mock
    private PostgresqlConnectionProperties properties;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(properties.getUrl()).thenReturn("r2dbc:postgresql://localhost:5432/authentication_db");
        when(properties.getUsername()).thenReturn("username");
        when(properties.getPassword()).thenReturn("password");
    }

    @Test
    void getConnectionConfigBeanCreation() {
        // Solo verificamos que el pool se cree, sin conectar a la DB real
        ConnectionPool pool = connectionPool.getConnectionConfig(properties);
        assertNotNull(pool);
    }
}

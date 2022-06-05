package hello.jdbc.connection;


import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection connection1 = DriverManager.getConnection(ConnectionConst.URL,ConnectionConst.USERNAME,
                ConnectionConst.PASSWORD);
        Connection connection2 = DriverManager.getConnection(ConnectionConst.URL,ConnectionConst.USERNAME,
                ConnectionConst.PASSWORD);

        log.info("connection={}, class={}",connection1, connection1.getClass());
        log.info("connection={}, class={}",connection2, connection2.getClass());

    }

    @Test
    void dataSourceDriverManager() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(ConnectionConst.URL
                ,ConnectionConst.USERNAME, ConnectionConst.PASSWORD);

        dataSource(dataSource);
    }

    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(ConnectionConst.URL);
        dataSource.setUsername(ConnectionConst.USERNAME);
        dataSource.setPassword(ConnectionConst.PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("DB ConnectionPool");

        useDataSource(dataSource);
        Thread.sleep(1000);
    }


    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        log.info("connection={}, class={}",connection1, connection1.getClass());
        log.info("connection={}, class={}",connection2, connection2.getClass());

    }


      private void dataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();

        log.info("connection={}, class={}",con1, con1.getClass());
        log.info("connection={}, class={}",con2, con2.getClass());
    }
}

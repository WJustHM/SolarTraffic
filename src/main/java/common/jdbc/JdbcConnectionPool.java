package common.jdbc;


import common.ConnectionPool;
import common.PoolBase;
import common.PoolConfig;
import common.jdbc.JdbcConfig;

import java.sql.Connection;
import java.util.Properties;

/**
 * Created by cloud computing on 2016/9/21 0021.
 */
public class JdbcConnectionPool extends PoolBase<Connection> implements ConnectionPool<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2743612676107943708L;

    /**
     * <p>Title: JdbcConnectionPool</p>
     * <p>Description: </p>
     */
    public JdbcConnectionPool() {

        this(JdbcConfig.DEFAULT_DRIVER_CLASS, JdbcConfig.DEFAULT_JDBC_URL, JdbcConfig.DEFAULT_JDBC_USERNAME, JdbcConfig.DEFAULT_JDBC_PASSWORD);
    }

    /**
     * <p>Title: JdbcConnectionPool</p>
     * <p>Description: </p>
     *
     * @param properties JDBC
     */
    public JdbcConnectionPool(final Properties properties) {

        this(new PoolConfig(), properties);
    }

    /**
     * <p>Title: JdbcConnectionPool</p>
     * <p>Description: </p>
     *
     * @param driverClass
     * @param jdbcUrl     URL
     * @param username
     * @param password
     */
    public JdbcConnectionPool(final String driverClass, final String jdbcUrl, final String username, final String password) {

        this(new PoolConfig(), driverClass, jdbcUrl, username, password);
    }

    /**
     * <p>Title: JdbcConnectionPool</p>
     * <p>Description: </p>
     *
     * @param poolConfig
     * @param properties JDBC
     */
    public JdbcConnectionPool(final PoolConfig poolConfig, final Properties properties) {

        super(poolConfig, new JdbcConnectionFactory(properties));
    }

    /**
     * <p>Title: JdbcConnectionPool</p>
     * <p>Description: </p>
     *
     * @param poolConfig
     * @param driverClass
     * @param jdbcUrl     URL
     * @param username
     * @param password
     */
    public JdbcConnectionPool(final PoolConfig poolConfig, final String driverClass, final String jdbcUrl, final String username, final String password) {

        super(poolConfig, new JdbcConnectionFactory(driverClass, jdbcUrl, username, password));
    }

    @Override
    public Connection getConnection() {

        return super.getResource();
    }

    @Override
    public void returnConnection(Connection conn) {

        super.returnResource(conn);
    }

    @Override
    public void invalidateConnection(Connection conn) {

        super.invalidateResource(conn);
    }

}

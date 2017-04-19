
package common.jdbc;



import common.ConnectionException;
import common.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by cloud computing on 2016/9/21 0021.
 */
class JdbcConnectionFactory implements ConnectionFactory<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4941500146671191616L;

    /**
     * driverClass
     */
    private final String driverClass;

    /**
     * jdbcUrl
     */
    private final String jdbcUrl;

    /**
     * username
     */
    private final String username;

    /**
     * password
     */
    private final String password;

    /**
     * <p>Title: JdbcConnectionFactory</p>
     * <p>Description: </p>
     *
     * @param properties JDBC
     */
    public JdbcConnectionFactory(final Properties properties) {

        this.driverClass = properties.getProperty(JdbcConfig.DRIVER_CLASS_PROPERTY);
        if (driverClass == null)
            throw new ConnectionException("[" + JdbcConfig.DRIVER_CLASS_PROPERTY + "] is required !");

        this.jdbcUrl = properties.getProperty(JdbcConfig.JDBC_URL_PROPERTY);
        if (jdbcUrl == null)
            throw new ConnectionException("[" + JdbcConfig.JDBC_URL_PROPERTY + "] is required !");

        this.username = properties.getProperty(JdbcConfig.JDBC_USERNAME_PROPERTY);
        if (username == null)
            throw new ConnectionException("[" + JdbcConfig.JDBC_USERNAME_PROPERTY + "] is required !");

        this.password = properties.getProperty(JdbcConfig.JDBC_PASSWORD_PROPERTY);
        if (password == null)
            throw new ConnectionException("[" + JdbcConfig.JDBC_PASSWORD_PROPERTY + "] is required !");

        this.loadDriver();
    }

    /**
     * <p>Title: JdbcConnectionFactory</p>
     * <p>Description: </p>
     *
     * @param driverClass
     * @param jdbcUrl     URL
     * @param username
     * @param password
     */
    public JdbcConnectionFactory(final String driverClass, final String jdbcUrl, final String username, final String password) {

        this.driverClass = driverClass;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.loadDriver();
    }

    /**
     * <p>Title: loadDriver</p>
     * <p>Description: </p>
     */
    private void loadDriver() {

        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public PooledObject<Connection> makeObject() throws Exception {

        Connection connection = this.createConnection();

        return new DefaultPooledObject<Connection>(connection);
    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {

        Connection connection = p.getObject();

        if (connection != null)

            connection.close();
    }

    @Override
    public boolean validateObject(PooledObject<Connection> p) {

        Connection connection = p.getObject();

        if (connection != null)
            try {
                return ((!connection.isClosed()) && (connection.isValid(1)));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return false;
    }

    @Override
    public void activateObject(PooledObject<Connection> p) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void passivateObject(PooledObject<Connection> p) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public Connection createConnection() throws Exception {

        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

        return connection;
    }

}

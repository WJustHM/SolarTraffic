
package common.jdbc;

/**
 * Created by cloud computing on 2016/9/21 0021.
 */
public interface JdbcConfig {

    /**
     * MYSQL DEFAULT_DRIVER_CLASS
     */
    public static final String DEFAULT_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    /**
     * MYSQL DEFAULT_JDBC_URL
     */
    public static final String DEFAULT_JDBC_URL = "jdbc:mysql://localhost:3306/test";
    /**
     * MYSQL DEFAULT_JDBC_USERNAME
     */
    public static final String DEFAULT_JDBC_USERNAME = "root";
    /**
     * MYSQL DEFAULT_JDBC_PASSWORD
     */
    public static final String DEFAULT_JDBC_PASSWORD = "root";

    /**
     * DRIVER_CLASS_PROPERTY
     */
    public static final String DRIVER_CLASS_PROPERTY = "driverClass";
    /**
     * JDBC_URL_PROPERTY
     */
    public static final String JDBC_URL_PROPERTY = "jdbcUrl";
    /**
     * JDBC_USERNAME_PROPERTY
     */
    public static final String JDBC_USERNAME_PROPERTY = "username";
    /**
     * JDBC_PASSWORD_PROPERTY
     */
    public static final String JDBC_PASSWORD_PROPERTY = "password";
}


package common.hbase;


import common.ConnectionPool;
import common.PoolBase;
import common.PoolConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

import java.util.Properties;

/*
 * Created by cloud computing on 2016/9/21 0021.
 */
public class HbaseConnectionPool extends PoolBase<Connection> implements ConnectionPool<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -9126420905798370243L;

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     */
    public HbaseConnectionPool() {

        this(HbaseConfig.DEFAULT_HOST, HbaseConfig.DEFAULT_PORT);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     *
     * @param host
     * @param port
     */
    public HbaseConnectionPool(final String host, final String port) {

        this(new PoolConfig(), host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     *
     * @param host
     * @param port
     * @param master  hbase
     * @param rootdir hdfs
     */
    public HbaseConnectionPool(final String host, final String port, final String master, final String rootdir) {

        this(new PoolConfig(), host, port, master, rootdir);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     *
     * @param hadoopConfiguration hbase
     */
    public HbaseConnectionPool(final Configuration hadoopConfiguration) {

        this(new PoolConfig(), hadoopConfiguration);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     *
     * @param poolConfig
     * @param host
     * @param port
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final String host, final String port) {

        this(poolConfig, host, port, HbaseConfig.DEFAULT_MASTER, HbaseConfig.DEFAULT_ROOTDIR);
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     *
     * @param poolConfig
     * @param hadoopConfiguration hbase
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final Configuration hadoopConfiguration) {

        super(poolConfig, new HbaseConnectionFactory(hadoopConfiguration));
    }

    /**
     * <p>Title: HbaseConnectionPool</p>
     * <p>Description: </p>
     *
     * @param poolConfig
     * @param host
     * @param port
     * @param master     hbase
     * @param rootdir    hdfs
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final String host, final String port, final String master, final String rootdir) {

        super(poolConfig, new HbaseConnectionFactory(host, port, master, rootdir));
    }

    /**
     * @param poolConfig
     * @param properties
     * @since 1.2.1
     */
    public HbaseConnectionPool(final PoolConfig poolConfig, final Properties properties) {

        super(poolConfig, new HbaseConnectionFactory(properties));
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

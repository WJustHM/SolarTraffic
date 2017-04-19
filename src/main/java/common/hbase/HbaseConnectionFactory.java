
package common.hbase;


import common.ConnectionException;
import common.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Connection;

import java.util.Map.Entry;
import java.util.Properties;

/*
 * Created by cloud computing on 2016/9/21 0021.
 */
class HbaseConnectionFactory implements ConnectionFactory<Connection> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4024923894283696465L;

    /**
     * hadoopConfiguration
     */
    private final Configuration hadoopConfiguration;

    /**
     * <p>Title: HbaseConnectionFactory</p>
     * <p>Description: </p>
     *
     * @param hadoopConfiguration hbase
     */
    public HbaseConnectionFactory(final Configuration hadoopConfiguration) {

        this.hadoopConfiguration = hadoopConfiguration;
    }

    /**
     * <p>Title: HbaseConnectionFactory</p>
     * <p>Description:</p>
     *
     * @param host    zookeeper
     * @param port    zookeeper
     * @param master  hbase
     * @param rootdir hdfs
     */
    public HbaseConnectionFactory(final String host, final String port, final String master, final String rootdir) {

        this.hadoopConfiguration = new Configuration();

        if (host == null)
            throw new ConnectionException("[" + HbaseConfig.ZOOKEEPER_QUORUM_PROPERTY + "] is required !");
        this.hadoopConfiguration.set(HbaseConfig.ZOOKEEPER_QUORUM_PROPERTY, host);

        if (port == null)
            throw new ConnectionException("[" + HbaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY + "] is required !");
        this.hadoopConfiguration.set(HbaseConfig.ZOOKEEPER_CLIENTPORT_PROPERTY, port);

        if (master != null)
            this.hadoopConfiguration.set(HbaseConfig.MASTER_PROPERTY, master);

        if (rootdir != null)
            this.hadoopConfiguration.set(HbaseConfig.ROOTDIR_PROPERTY, rootdir);
    }

    /**
     * @param properties
     * @since 1.2.1
     */
    public HbaseConnectionFactory(final Properties properties) {

        this.hadoopConfiguration = new Configuration();

        for (Entry<Object, Object> entry : properties.entrySet()) {

            this.hadoopConfiguration.set((String) entry.getKey(), (String) entry.getValue());
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

            return ((!connection.isAborted()) && (!connection.isClosed()));

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

        Connection connection = org.apache.hadoop.hbase.client.ConnectionFactory
                .createConnection(hadoopConfiguration);

        return connection;
    }

}

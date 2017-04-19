package common.es;


import common.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.util.Collection;

/**
 * Created by cloud computing on 2016/9/21 0021.
 */
 class EsConnectionFactory implements ConnectionFactory<TransportClient> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4024923894283696465L;

    private  final Collection<InetSocketTransportAddress> address ;

    private  final Class<? extends Plugin>[] plugins;

    private  final Settings settings;


    public EsConnectionFactory(final Settings settings , final Collection<InetSocketTransportAddress> address, final Class<? extends Plugin>[] plugins) {

        this.address = address;
        this.settings = settings;
        this.plugins = plugins;
    }


    @Override
    public PooledObject<TransportClient> makeObject() throws Exception {
        TransportClient clent = this.createConnection();
        return new DefaultPooledObject<TransportClient>(clent);
    }

    @Override
    public void destroyObject(PooledObject<TransportClient> p) throws Exception {

        TransportClient client = p.getObject();

        if (client != null)

            client.close();
    }

    @Override
    public boolean validateObject(PooledObject<TransportClient> p) {

        TransportClient client = p.getObject();
        if (client == null)
            return false;
        return true;
    }

    @Override
    public void activateObject(PooledObject<TransportClient> p) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void passivateObject(PooledObject<TransportClient> p) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public TransportClient createConnection() throws Exception {

        TransportClient client  = new PreBuiltTransportClient(settings,plugins);
        for (InetSocketTransportAddress ta : address){
            client.addTransportAddress(ta);
        }
        return client;
    }
}

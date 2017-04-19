
package common;


import org.apache.commons.pool2.PooledObjectFactory;

import java.io.Serializable;

/*
 * Created by cloud computing on 2016/9/21 0021.
 */
public interface ConnectionFactory<T> extends PooledObjectFactory<T>, Serializable {

    public abstract T createConnection() throws Exception;
}

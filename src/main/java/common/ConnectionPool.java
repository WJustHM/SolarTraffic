
package common;

import java.io.Serializable;

/**
 * Created by cloud computing on 2016/9/21 0021.
 */
public interface ConnectionPool<T> extends Serializable {


    public abstract T getConnection();

    public void returnConnection(T conn);


    public void invalidateConnection(T conn);
}

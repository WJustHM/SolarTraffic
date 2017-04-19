package common;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Closeable;
import java.io.Serializable;

/**
 * Created by cloud computing on 2016/9/21 0021.
 */
public abstract class PoolBase<T> implements Closeable, Serializable {


    private static final long serialVersionUID = 536428799879058482L;


    protected GenericObjectPool<T> internalPool;


    public PoolBase() {
    }


    public PoolBase(final GenericObjectPoolConfig poolConfig,
                    PooledObjectFactory<T> factory) {
        this.initPool(poolConfig, factory);
    }


    protected void initPool(final GenericObjectPoolConfig poolConfig,
                            PooledObjectFactory<T> factory) {
        if (this.internalPool != null)
            this.destroy();

        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }


    protected void destroy() {
        this.close();
    }


    protected T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new ConnectionException(
                    "Could not get a resource from the pool", e);
        }
    }


    protected void returnResource(final T resource) {
        if (null != resource)
            try {
                internalPool.returnObject(resource);
            } catch (Exception e) {
                throw new ConnectionException(
                        "Could not return the resource to the pool", e);
            }
    }


    protected void invalidateResource(final T resource) {
        if (null != resource)
            try {
                internalPool.invalidateObject(resource);
            } catch (Exception e) {
                throw new ConnectionException(
                        "Could not invalidate the resource to the pool", e);
            }
    }


    public int getNumActive() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getNumActive();
    }


    public int getNumIdle() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getNumIdle();
    }


    public int getNumWaiters() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getNumWaiters();
    }


    public long getMeanBorrowWaitTimeMillis() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }


    public long getMaxBorrowWaitTimeMillis() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getMaxBorrowWaitTimeMillis();
    }


    public boolean isClosed() {
        try {
            return this.internalPool.isClosed();
        } catch (Exception e) {
            throw new ConnectionException(
                    "Could not check closed from the pool", e);
        }
    }


    private boolean isInactived() {
        try {
            return this.internalPool == null || this.internalPool.isClosed();
        } catch (Exception e) {
            throw new ConnectionException(
                    "Could not check inactived from the pool", e);
        }
    }


    protected void addObjects(final int count) {
        try {
            for (int i = 0; i < count; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new ConnectionException("Error trying to add idle objects", e);
        }
    }


    public void clear() {
        try {
            this.internalPool.clear();
        } catch (Exception e) {
            throw new ConnectionException("Could not clear the pool", e);
        }
    }


    public void close() {
        try {
            this.internalPool.close();
        } catch (Exception e) {
            throw new ConnectionException("Could not destroy the pool", e);
        }
    }
}

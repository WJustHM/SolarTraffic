package com.common

import java.net.InetAddress
import java.sql.{Connection => MysqlConnection}
import java.util

import common.PoolConfig
import common.es.EsConnectionPool
import common.jdbc.JdbcConnectionPool
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import redis.clients.jedis.{Jedis, JedisPool}

/**
  * Created by linux on 17-4-17.
  */
trait Pools extends Logging with Serializable {
  private var redisPool: JedisPool = _
  private var jdbcConnectionPool : JdbcConnectionPool = _
  private var esConnectionPool: EsConnectionPool = _

  private def getPoolConfig: PoolConfig = {
    val poolConfig: PoolConfig = new PoolConfig
    poolConfig.setMaxTotal(100)
    poolConfig.setMaxIdle(100)
    poolConfig.setMaxWaitMillis(1000000)
    poolConfig.setTestOnBorrow(true)
    poolConfig.setTestOnReturn(true)
    poolConfig.setTestOnCreate(true)
    poolConfig
  }

  private def initRedisPool: JedisPool = {
    val pools = new JedisPool(getPoolConfig, Constants.REDIS_HOST, Constants.REDIS_PORT)
    pools
  }
  def getRedisConn: Jedis = {
    synchronized {
      if (redisPool == null || redisPool.isClosed) {
        redisPool = initRedisPool
      }
    }
    redisPool.getResource
  }
  def returnRedisConn(redis: Jedis): Unit = {
    synchronized {
      redisPool.returnResource(redis)
    }
  }


  private def initJDBC : JdbcConnectionPool = {
    val mysqlPool = new JdbcConnectionPool(getPoolConfig,
      Constants.MYSQL_DRIVER,
      Constants.MYSQL_JDBC_URL,
      Constants.MYSQL_USER_NAME,
      Constants.MYSQL_USER_PASSWORD)
    mysqlPool
  }
  def getMysqlConn : MysqlConnection = {
    synchronized{
      if (jdbcConnectionPool == null || jdbcConnectionPool.isClosed){
        jdbcConnectionPool = initJDBC
      }
    }
    jdbcConnectionPool.getConnection
  }
  def returnMysqlConn(conn : MysqlConnection): Unit ={
    synchronized{
      jdbcConnectionPool.returnConnection(conn)
    }
  }

  private def initEs : EsConnectionPool = {
    println("===================> init ES ")
    val  settings = Settings.builder()
    settings.put("cluster.name",Constants.ES_CLUSTER_NAME)
    settings.put("es.mapping.date.rich",false)

    val address: util.Collection[InetSocketTransportAddress] = new util.LinkedList[InetSocketTransportAddress]()
    Constants.ES_URL.split(",").foreach( s =>{
      val hp = s.split(":")
      logInfo("  ES " + hp(0) + "   " +hp(1))
      address.add(new InetSocketTransportAddress(InetAddress.getByName(hp(0)), hp(1).toInt))
    })

    val esPool = new EsConnectionPool(getPoolConfig,settings.build(),address)
    esPool
  }
  def getEsClient : TransportClient = {
    synchronized{
      if (esConnectionPool == null || esConnectionPool.isClosed){
        esConnectionPool = initEs
      }
    }
    esConnectionPool.getConnection
  }
  def returnEsConn(client : TransportClient) : Unit={
    synchronized{
      esConnectionPool.returnConnection(client)
    }
  }


}

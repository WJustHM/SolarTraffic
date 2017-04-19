package com.common

/**
  * Created by linux on 17-4-17.
  */
object Constants extends Serializable{

  val ES_CLUSTER_NAME="handge-cloud"
  val ES_URL = "datanode1:9300,datanode2:9300,datanode3:9300"

  val MYSQL_DRIVER="com.mysql.jdbc.Driver"
  val MYSQL_USER_NAME="root"
  val MYSQL_USER_PASSWORD="mysql"
  val MYSQL_JDBC_URL="jdbc:mysql://172.20.31.127:3306/solar?characterEncoding=UTF8"


  val REDIS_HOST="DataStore"
  val REDIS_PORT=6379
}

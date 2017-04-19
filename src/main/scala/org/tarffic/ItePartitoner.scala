package org.tarffic

import java.util.logging.Logger

import org.apache.spark.Partitioner

/**
  * Created by linux on 17-4-17.
  */
class ItePartitoner(numpart: Int) extends Partitioner with Serializable{
  override def numPartitions: Int = numpart

  override def getPartition(key: Any): Int = {
    val code = key.toString.split("")(0).hashCode() % numPartitions
    if (code < 0) {
      code + numPartitions
    } else {
      code
    }
  }

  override def equals(obj: scala.Any): Boolean = obj match{
    case ite:ItePartitoner =>
      ite.numPartitions==numPartitions
    case _ =>
      false
  }

  override def hashCode(): Int = numPartitions
}

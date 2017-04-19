package test

import java.lang.Double
import java.sql.{Connection => MysqlConnection}
import java.text.SimpleDateFormat
import java.util.{ArrayList => JArrayList, Date => JDate, HashMap => JHashMap, List => JavaList, Map => JMap}

import com.common.Pools
import dijkstra.ConnectionDis
import org.apache.spark.{SparkConf, SparkContext}
import org.elasticsearch.spark._
import org.tarffic.ItePartitoner

import scala.util.Random


/**
  * Created by linux on 17-4-17.
  */
class SparkES extends Pools with Serializable {
  var map: JHashMap[Integer, JHashMap[Integer, Double]] = _
  val conndis = new ConnectionDis
  val bkxw = Array("01", "02", "03", "04", "05", "06", "07", "08", "09", "99")
  val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  val speed = 180

  def connmysql(): Unit = {
    map = conndis.build()
  }


  def insertMysql(HPHM: String, JGSK: String, HPYS: String, CLPP: String, CSYS: String, CLLX: String, HPZL: String): Unit = {
    val conn: MysqlConnection = getMysqlConn
    val sql = "REPLACE INTO PreyInfo(BKXXBH,HPHM,HPYS,CLPP1,CLPP2,CSYS,CLLX,HPZL,CLZP,BKDWDM," +
      "BKDWMC,BKDWZBDH,BKR,BKRZJHM,BKLXRSJ,BKSZ,BKXW,BKJB,BKLX,BKSJ" +
      ",BKFKSJ,BKJZRQ,AJMS,BKZT,CKDW,CKDWMC,CKR,CKRZJHM,CKSJ,CKYY," +
      "BKCZR,CKCZR) VALUES(" +
      "?,?,?,?,?,?,?,?,?,?," +
      "?,?,?,?,?,?,?,?,?,?," +
      "?,?,?,?,?,?,?,?,?,?," +
      "?,?)"

    val res = conn.prepareStatement(sql)
    res.setString(1, HPHM + JGSK)
    res.setString(2, HPHM)
    res.setString(3, HPYS)
    res.setString(4, CLPP)
    res.setString(5, CLPP)
    res.setString(6, CSYS)
    res.setString(7, CLLX)
    res.setString(8, HPZL)
    res.setString(9, "")
    res.setString(10, "")
    res.setString(11, "")
    res.setString(12, "")
    res.setString(13, "")
    res.setString(14, "")
    res.setString(15, "")
    res.setString(16, "")
    res.setString(17, Random.nextInt(10) + "")
    res.setString(18, Random.nextInt(3) + "")
    res.setString(19, "")
    res.setString(20, "")
    res.setString(21, "")
    res.setString(22, "")
    res.setString(23, "")
    res.setString(24, "")
    res.setString(25, "")
    res.setString(26, "")
    res.setString(27, "")
    res.setString(28, "")
    res.setString(29, "")
    res.setString(30, "")
    res.setString(31, "")
    res.setString(32, "")
    res.execute()
    println("-------插入成功！！")
    returnMysqlConn(conn)
  }


  def Search(start: String, end: String): Unit = {
    val conf = new SparkConf()
      .set("es.nodes", "datanode1,datanode2,datanode3")
      .set("es.port", "9200")
      .set("es.mapping.date.rich", "false")
      .setMaster("local[4]")
      .setAppName("SparktoES")
    val sc = new SparkContext(conf)
    val queryg = "{\n" +
      "  \"query\": {\n" +
      "    \"range\": {\n" +
      "      \"JGSK\": {\n" +
      "        \"gte\": \"2017-03-23 10:02:55\",\n" +
      "        \"lte\": \"2017-03-25 10:02:55\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    val queryt = "{\n" +
      "  \"query\": {\n" +
      "    \"range\": {\n" +
      "      \"Time.keyword\": {\n" +
      "        \"gte\": \"" + start + "\",\n" +
      "        \"lte\": \"" + end + "\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}"
    val se = sc.esRDD("traffic/traffic", queryt)
    val sort = se.map(x => (x._2.getOrElse("Plate_License", 0) + " " + x._2.getOrElse("Time", 0)
      , (x._2.getOrElse("Plate_License",0), x._2.getOrElse("Time",0), x._2.getOrElse("Plate_Color",0)
      , x._2.getOrElse("CLPP",0), x._2.getOrElse("Vehicle_Color",0), x._2.getOrElse("Vehicle_Type",0)
      , x._2.getOrElse("HPZL",0), x._2.getOrElse("SBBH",0))))
    val par = sort.partitionBy(new ItePartitoner(34)).sortByKey()
    val plate = par.map(x => (x._1.split(" ")(0), x._2))
    plate.reduceByKey((x, y) => {
      if (x._4 + "" + x._5 + x._6 != y._4 + "" + y._5 + y._6) {
        insertMysql(x._1.toString, x._2.toString, x._3.toString, x._4.toString, x._5.toString, x._6.toString, x._7.toString)
      } else {
        val stime = formatter.parse(x._2.toString).getTime
        val etime = formatter.parse(y._2.toString).getTime
        val skkbh = Integer.parseInt(x._8.toString)
        val ekkbh = Integer.parseInt(y._8.toString)
        val actual = etime - stime
        val step = conndis.backMinStep(skkbh, ekkbh, map)
        val theory: Double = (step.getMinStep / 180) * 3600 * 1000
        if (actual < theory) {
          insertMysql(x._1.toString, x._2.toString, x._3.toString, x._4.toString, x._5.toString, x._6.toString, x._7.toString)
          println("HPHM--:" + x._1)
          println("----------actual" + actual)
          println("```````````theory" + theory)
          println("~~~~~~~~~~minstep" + step.getMinStep)
          println("step---" + step.getStep)
        }
      }
      y
    }).take(1)
  }
}

object SparkES {
  def main(args: Array[String]): Unit = {
    val s = new SparkES
    s.connmysql()
    s.Search("2017-03-28 12:09:11", "2017-03-31 12:09:11")
  }
}

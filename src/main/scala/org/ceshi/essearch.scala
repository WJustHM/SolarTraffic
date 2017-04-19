package org.ceshi

import com.common.Pools
import org.elasticsearch.index.query.QueryBuilders


/**
  * Created by linux on 17-4-19.
  */
class essearch extends Pools {
  val conn = getEsClient

  def se(): Unit = {
    val request = conn.prepareSearch().setIndices("gxy").setTypes("gxy")
    val qu = "{\n" +
      "  \"query\": {\n" +
      "    \"range\": {\n" +
      "      \"JGSK\": {\n" +
      "        \"gte\": \"2017-03-23 10:02:55\",\n" +
      "        \"lte\": \"2017-03-25 10:02:55\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}";
    val res=request.setQuery(QueryBuilders.wrapperQuery(qu)).execute().actionGet()
    for(i <- res.getHits.getHits){
      println(i.getSource.get("HPHM"))
    }
  }

}
object essearch{
  def main(args:Array[String]): Unit ={
    val ses=new essearch
    ses.se()
  }
}

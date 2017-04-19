package dijkstra;

import org.apache.hadoop.hbase.util.JsonMapper;
import org.codehaus.jackson.map.ObjectMapper;
import org.omg.CORBA.Object;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by root on 17-3-6.
 */
public class JsonUtil implements Serializable {
    public static String objectToJsonStr(MinStep minstep) {
        ObjectMapper s = new ObjectMapper();
        String res = "";
        try {
            res = s.writeValueAsString(minstep);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}

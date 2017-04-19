package dijkstra;


import com.common.Pools;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by root on 17-3-1.
 */
public class ConnectionDis implements Serializable {
    private static HashMap<Integer, HashMap<Integer, Double>> open = new HashMap<Integer, HashMap<Integer, Double>>();
    private int num = 0;

    public static void main(String[] args) throws IOException {
//        ConnectionDis conn = new ConnectionDis();
//        System.out.println(conn.backMinStep(1, 34).getMinStep());
    }

    public MinStep backMinStep(int var1, int var2, HashMap<Integer, HashMap<Integer, Double>> map) {
        DistanceDijkstraImpl var3 = new DistanceDijkstraImpl();
        MinStep var4 = var3.getMinStep(var1, var2, map);
        return var4;
    }

    public java.sql.Connection startyuan() {
        java.sql.Connection conn = null;
        String url = "jdbc:mysql://172.20.31.127/solar";
        String name = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "mysql";
        try {
            Class.forName(name);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public HashMap<Integer, HashMap<Integer, Double>> build() {
        String sql = "select id,agate,distance from gate";
        System.out.println("-------------+++++++++++++++++" + num++);
        java.sql.Connection conn = startyuan();
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int device = Integer.valueOf(rs.getString("id"));
                String agate = rs.getString("agate");
                String gateDistance = rs.getString("distance");
                if (device > 0) {
                    String[] gate = agate.split(",");
                    String[] distance = gateDistance.split(",");
                    HashMap<Integer, Double> openchild = new HashMap<Integer, Double>();
                    for (int i = 0; i < gate.length; i++) {
                        double juli = Double.parseDouble(distance[i]);
                        if (Integer.parseInt(gate[i]) >= 17) {
                            juli = juli * 2.4;
                        }
                        openchild.put(Integer.parseInt(gate[i]), juli);
                    }
                    open.put(device, openchild);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return open;
    }
}
package dijkstra;

import java.util.HashMap;

/**
 * Created by root on 17-3-6.
 */
public interface Distances  {
    public static final MinStep UNREACHABLE = new MinStep(false, -1);
    public MinStep getMinStep(int start, int end, final HashMap<Integer, HashMap<Integer, Double>> stepLength);
}

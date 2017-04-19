package dijkstra;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 17-3-6.
 */
public class MinStep implements Serializable {
    private boolean reachable;//是否可达
    private double minStep;//最短步长
    private List<Integer> step;//最短路径

    public MinStep() {
    }

    public MinStep(boolean reachable, int minStep) {
        this.reachable = reachable;
        this.minStep = minStep;
    }

    public boolean isReachable() {
        return reachable;
    }
    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }
    public double getMinStep() {
        return minStep;
    }
    public void setMinStep(double minStep) {
        this.minStep = minStep;
    }
    public List<Integer> getStep() {
        return step;
    }
    public void setStep(List<Integer> step) {
        this.step = step;
    }
}

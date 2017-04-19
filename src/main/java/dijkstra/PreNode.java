package dijkstra;

import java.io.Serializable;

/**
 * Created by root on 17-3-6.
 */
public class PreNode implements Serializable {
    private int preNodeNum;// 最优 前一个节点
    private double nodeStep;// 最小步长

    public PreNode(int preNodeNum, double nodeStep) {
        this.preNodeNum = preNodeNum;
        this.nodeStep = nodeStep;
    }

    public int getPreNodeNum() {
        return preNodeNum;
    }
    public void setPreNodeNum(int preNodeNum) {
        this.preNodeNum = preNodeNum;
    }
    public double getNodeStep() {
        return nodeStep;
    }
    public void setNodeStep(double nodeStep) {
        this.nodeStep = nodeStep;
    }
}

package dijkstra;

import java.io.Serializable;
import java.util.*;

/**
 * Created by root on 17-3-6.
 */
public class DistanceDijkstraImpl implements Distances,Serializable {
    private HashMap<Integer, HashMap<Integer, Double>> stepLength;
    private int nodeNum;
    private HashSet<Integer> outNode;
    private HashMap<Integer, PreNode> nodeStep;
    private LinkedList<Integer> nextNode;
    private int startNode;
    private int endNode;


    public MinStep getMinStep(int start, int end, final HashMap<Integer, HashMap<Integer, Double>> stepLength) {
        this.stepLength = stepLength;
        this.nodeNum = this.stepLength != null ? this.stepLength.size() : 0;
        if (this.stepLength == null || (!this.stepLength.containsKey(start)) || (!this.stepLength.containsKey(end))) {
            return UNREACHABLE;
        }
        initProperty(start, end);
        step();
        if (nodeStep.containsKey(end)) {
            return changeToMinStep();
        }
        return UNREACHABLE;
    }


    private MinStep changeToMinStep() {
        MinStep minStep = new MinStep();
        minStep.setMinStep(nodeStep.get(endNode).getNodeStep());
        minStep.setReachable(true);
        LinkedList<Integer> step = new LinkedList<Integer>();
        minStep.setStep(step);
        int nodeNum = endNode;
        step.addFirst(nodeNum);
        while (nodeStep.containsKey(nodeNum)) {
            int node = nodeStep.get(nodeNum).getPreNodeNum();
            step.addFirst(node);
            nodeNum = node;
        }
        return minStep;
    }


    private void initProperty(int start, int end) {
        outNode = new HashSet<Integer>();
        nodeStep = new HashMap<Integer, PreNode>();
        nextNode = new LinkedList<Integer>();
        nextNode.add(start);
        startNode = start;
        endNode = end;
    }


    private void step() {
        if (nextNode == null || nextNode.size() < 1) {
            return;
        }
        if (outNode.size() == nodeNum) {
            return;
        }
        int start = nextNode.removeFirst();
        double step = 0;
        if (nodeStep.containsKey(start)) {
            step = nodeStep.get(start).getNodeStep();
        }

        HashMap<Integer,Double> nextStep = stepLength.get(start);
        Iterator<Map.Entry<Integer, Double>> iter = nextStep.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Double> entry = iter.next();
            Integer key = entry.getKey();
            if (key == startNode) {
                continue;
            }

            double value = entry.getValue() + step;
            if ((!nextNode.contains(key)) && (!outNode.contains(key))) {
                nextNode.add(key);
            }
            if (nodeStep.containsKey(key)) {
                if (value < nodeStep.get(key).getNodeStep()) {
                    nodeStep.put(key, new PreNode(start, value));
                }
            } else {
                nodeStep.put(key, new PreNode(start, value));
            }
        }
        outNode.add(start);
        step();
    }
}


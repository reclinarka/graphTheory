/**
 * Created by Philipp Polland on 24-Jun-20.
 */
package dijkstra;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class clsDijkstraManager {

    public clsDijkstraManager() {

    }

    public clsDijkstraManager(clsGraph graph) {
        this.graph = graph;
    }

    //Attributes
    private clsVertex[] shortestPath;

    private clsGraph graph;

    public long speed = 1000;

    //Getter
    public clsGraph getGraph() {
        return graph;
    }


    public clsVertex[] getShortestPath() {
        return shortestPath;
    }

    //Setter
    public void setGraph(clsGraph graph) {
        this.graph = graph;
    }

    public void setShortestPath(clsVertex[] shortestPath) {
        this.shortestPath = shortestPath;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void calculatePath() {
        clsVertexList<clsVertex> vertices = new clsVertexList<>(graph.getV());
        for (clsVertex v : graph.getV()) {
            v.setValue(Integer.MAX_VALUE);
            v.setPrevious(null);
            if(!v.equals(graph.getGoal()) && !v.equals(graph.getStart()) )
                v.setMode(clsVertex.MODES.DEFAULT);
        }
        graph.getStart().setValue(0);

        clsVertex closest = vertices.getClosest();
        while (vertices.size() != 0 && !closest.equals(graph.getGoal())) {
            if (closest.getMode() != clsVertex.MODES.GOAL)
                closest.setMode(clsVertex.MODES.ACTIVE);
            closest.resetCache();
            List<clsEdge> availablePath = closest.getUndirectedEdges(graph.getE());
            if (availablePath.size() != 0) {
                for (clsEdge e : availablePath) {
                    clsVertex target = closest.getNeighbour(e);

                    int newDistance = closest.getValue() + e.getLength();
                    if (newDistance < target.getValue()) {
                        target.setValue(newDistance);
                        target.setPrevious(closest);
                    }
                }
            }
            vertices.remove(closest);
            try {
                TimeUnit.MILLISECONDS.sleep(speed);
            } catch (Exception e) {}
            if (closest.getMode() != clsVertex.MODES.GOAL && closest.getMode() != clsVertex.MODES.PATH) {
                closest.setMode(clsVertex.MODES.PATH);
            }
            closest = vertices.getClosest();
        }


        shortestPath = graph.getGoal().getPreviousArray();
        Map<String, clsVertex> pathMap = new HashMap<>(shortestPath.length);
        for (clsVertex v : shortestPath) {
            pathMap.put(v.getIdentifier(), v);
        }
        for (clsVertex v : graph.getV()) {
            if (pathMap.containsKey(v.getIdentifier())) {
                if (v.getMode() != clsVertex.MODES.GOAL) {
                    v.setMode(clsVertex.MODES.PATH);
                }
            } else {
                if (v.getMode() != clsVertex.MODES.GOAL) {
                    v.setMode(clsVertex.MODES.DEFAULT);
                }
            }
        }
    }

}


class clsVertexList<V extends clsVertex> extends ArrayList<V> {
    public clsVertexList(List<V> V) {
        super();
        for (V v : V) {
            add(v);
        }
    }

    public void remove(V vertex) {
        for (int i = 0; i < size(); i++) {
            if (get(i).equals(vertex)) {
                remove(i);
                return;
            }
        }
    }

    public V getClosest() {
        V out = get(0);
        for (V v : this) {
            if (v.getValue() < out.getValue()) {
                out = v;
            }
        }
        return out;
    }
}

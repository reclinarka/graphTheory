/**
 * Created by Philipp Polland on 20-May-20.
 */
package dijkstra;

import java.util.ArrayList;
import java.util.List;


public class clsGraph {
    // Graph G := (V, E)
    // V = List<Vertex>
    // E = List<Edge>

    //Attributes

    private List<clsVertex> V;
    private List<clsEdge> E;
    private clsVertex start;
    private clsVertex goal;


    //Getter


    public clsVertex getGoal() {
        return goal;
    }

    public clsVertex getVertexbyID(String identifyer){
        for(clsVertex v : V)
            if(v.equals(identifyer))
                return v;
        return null;
    }

    public List<clsEdge> getE() {
        return E;
    }

    public List<clsVertex> getV() {
        return V;
    }

    public clsVertex getStart() {
        return start;
    }

    //Setter


    public void setGoal(clsVertex goal) {
        this.goal = goal;
    }

    public void setStart(clsVertex start) {
        this.start = start;
    }

    public void setE(List<clsEdge> e) {
        E = e;
    }

    public void setV(List<clsVertex> v) {
        V = v;
    }

    public void remove(String identifyer){
        for(clsVertex v :V){
            if(v.getIdentifier().contentEquals(identifyer)) {
                for (clsEdge e : v.getUndirectedEdges(E)) {
                    E.remove(e);
                }
                V.remove(v);
            }

        }

    }

    //Contructors
    public clsGraph(){
        this.V = new ArrayList<>();
        this.E = new ArrayList<>();
    }

    public clsGraph(List<clsVertex> vertices, List<clsEdge> edges){
        this.V = vertices;
        this.E = edges;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("V := {\n");
        for (clsVertex v : V){
            out.append(v.toString() + "\n");
        }
        out.append("}\nE := {\n");
        for(clsEdge e : E){
            out.append(e.toString() + "\n");
        }
        out.append("}");
        if(start != null){
            out.append("\n");
            out.append(" S := {");
            out.append(start.toString());
            out.append(" }");
        }
        return out.toString();
    }
}

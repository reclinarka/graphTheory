/**
 * Created by Philipp Polland on 20-May-20.
 */
package dijkstra;

import java.util.ArrayList;
import java.util.List;


public class clsVertex {

    //Attributes

    private String identifier; // Name of Vertex
    private int value = Integer.MAX_VALUE; // distance from Start
    private clsVertex previous; //the Vertex with the shortest path to the Start Node
    private List<clsEdge> directedCache; //Cache for getDirectedEdges
    private List<clsEdge> undirectedCache; //Cache for getUndirectedEdges
    private MODES mode = MODES.DEFAULT; //Mode the Vertex is in
    public enum MODES{ //Modes a Vertex can be in, only used for Drawing the graph
        ACTIVE,
        DEFAULT,
        PATH,
        GOAL
    }




    //Getter

    /**
     * @return next Vertex in the shortest path to the Start
     * **/
    public clsVertex getPrevious() {
        return previous;
    }

    public MODES getMode() {
        return mode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getValue() {
        return value;
    }

    //Setter


    public void setValue(int value) {
        this.value = value;
    }

    public void setIdentifier(String identifier){
        this.identifier = identifier;
        this.resetCache(); // cache needs to be reset since the identity of the current vertex has changed,
    }                      // therefore the old List of connected Edges may be wrong

    public void setMode(MODES mode) {
        this.mode = mode;
    }

    public void setPrevious(clsVertex previous) {
        this.previous = previous;
    }

    //Constructor

    public clsVertex(){}

    public clsVertex(String identifier){
        this.identifier = identifier;
    }



    //Funktionen

    /**
     * Resets cache
     * **/
    public void resetCache(){
        this.directedCache = null;
        this.undirectedCache = null;
    }

    /**
     * @param edge Edge connecting this to another Vertex
     * @return either null if current Vertex isn't in edge or the other Vertex connected by the Edge
     * **/
    public clsVertex getNeighbour(clsEdge edge){
        if(edge.vertices[0].equals(this))
            return edge.vertices[1];
        else if(edge.vertices[1].equals(this))
            return edge.vertices[0];
        return null;
    }

    /**
     * @param graph List of Edges of th Graph containing this Vertex
     * @return List of Directed Edges going from the current Vertex
     * **/
    public List<clsEdge> getDirectedEdges(List<clsEdge> graph){
        if (directedCache != null) // if cache is not null it is returned instead
            return directedCache;  // shared cache with getUndirectedEdges because a graph is either

        List<clsEdge> out = new ArrayList<>();
        for(clsEdge e : graph){
            if ( this.equals(e.vertices[0]) )
                out.add(e);
        }
        this.directedCache = out;
        return out;
    }

    /**
     * @param graph List of Edges of th Graph containing this Vertex
     * @return List of Undirected Edges connected to the current Vertex
     * **/
    public List<clsEdge> getUndirectedEdges(List<clsEdge> graph){
        if (undirectedCache != null)
            return undirectedCache;

        List<clsEdge> out = new ArrayList<>();
        for(clsEdge e : graph){
            if ( this.equals(e.vertices[0]) || this.equals(e.vertices[1]))
                out.add(e);
        }
        this.undirectedCache = out;
        return out;
    }

    /**
     * Generates the shortest Path from the current Vertex to the Start Vertex
     * @return Array of Vertices from Start to Current Vertex with index 0 being the Start
     * **/
    public clsVertex[] getPreviousArray(){
        if (previous == null){
            return new clsVertex[]{this};
        }
        clsVertex[] previous_arr = previous.getPreviousArray();
        clsVertex[] out = new clsVertex[previous_arr.length + 1];
        for (int i = 0; i< previous_arr.length; i++){
            out[i] = previous_arr[i];
        }
        out[previous_arr.length] = this;
        return out;
    }

    /**
     * @param s ID of Vertex that needs to be compared to this
     * @return true if s = this.getIdentifier
     * **/
    public boolean equals(String s){
        return s.contentEquals(this.getIdentifier());
    }

    /**
     * @param v1 Vertex that needs to be compared to this
     * @return true if ID of v1 equals ID of this else false
     * **/
    public boolean equals(clsVertex v1){
        if(v1 == null)
            return false;
        return equals(v1.getIdentifier());
    }

    @Override
    public String toString() {
        return "Vertex( \"" + this.identifier +  "\", " + value + " )";
    }
}

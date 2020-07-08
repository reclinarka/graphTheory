/**
 * Created by Philipp Polland on 20-May-20.
 */
package dijkstra;

public class clsEdge {
    // Edge E := E in V x V
    // Edge E = {v1,v2} | v in V

    //Attributes

    public clsVertex[] vertices = new clsVertex[2];
    public int length;

    //Setter

    public void setLength(int length) {
        this.length = length;
    }

    public void setVertices(clsVertex[] vertices) {
        this.vertices = vertices;
    }

    //Getters

    public int getLength() {
        return length;
    }

    public clsVertex[] getVertices() {
        return vertices;
    }

    //Constructors

    public clsEdge(){
    }

    public clsEdge(int length, clsVertex[] vertices){
        this.length = length;
        this.vertices = vertices;

    }

    public clsEdge(int length, clsVertex v1, clsVertex v2){
        this.length = length;
        this.vertices[0] = v1;
        this.vertices[1] = v2;
    }

    @Override
    public String toString() {

        return "Edge( \"" + vertices[0].getIdentifier() +  "\", \"" + vertices[1].getIdentifier() + "\", \"" + length + "\" )";
    }
}

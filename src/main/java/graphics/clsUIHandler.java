/**
 * Created by Philipp Polland on 19-May-20.
 */
package graphics;

import dijkstra.clsEdge;
import dijkstra.clsGraph;
import dijkstra.clsVertex;
import graphics.frame.*;
import graphics.frame.clsPanel;
import graphics.frame.clsWindow;
import util.clsGraphIO;

import java.util.*;

public class clsUIHandler {

    private float xOffset = 0;
    private float yOffset = 0;
    private float multiplyer =10;
    private ArrayList<Drawable> layer_top = new ArrayList<>();
    private ArrayList<Drawable> layer_bottom = new ArrayList<>();
    private ArrayList<clsLine> L;
    private Map<String, clsNode> N;
    private Map<String,Interactable> interactables = new HashMap<>();
    private clsWindow window;
    private clsPanel panel;
    private clsGraph graph;
    private clsUniversalListener listener = new clsUniversalListener();

    public clsUIHandler(){

    }

    public ArrayList<Drawable> getLayer_top() {
        return layer_top;
    }

    public ArrayList<Drawable> getLayer_bottom() {
        return layer_bottom;
    }

    public clsUniversalListener getListener() {
        return listener;
    }

    public clsGraph getGraph() {
        return graph;
    }

    public float getxOffset() {
        return xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public float getMultiplyer() {
        return multiplyer;
    }

    public Map<String, Interactable> getInteractables() {
        return interactables;
    }

    public clsPanel getPanel() {
        return panel;
    }

    public clsWindow getWindow() {
        return window;
    }

    public void setGraph(clsGraph graph) {
        this.graph = graph;
    }

    public void setListener(clsUniversalListener listener) {
        this.listener = listener;
    }

    public void setPanel(clsPanel panel) {
        this.panel = panel;
    }

    public void setWindow(clsWindow window) {
        this.window = window;
    }

    public void setMultiplyer(float multiplyer) {
        this.multiplyer = multiplyer;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }

    private void spreadGraph(){

    }

    private void loadGraph(){
        L = new ArrayList<>();
        N = new HashMap<>(graph.getV().size());

        interactables = new HashMap<>(graph.getV().size() + graph.getE().size());
        layer_top = new ArrayList<>();
        layer_top.add(listener);

        for(clsVertex v : graph.getV()){
            v.getDirectedEdges(graph.getE());
            clsNode n = new clsNode(v);
            N.put(v.getIdentifier(),n);
            layer_top.add(n);
            interactables.put(v.getIdentifier(),n);

        }

        for(clsEdge e : graph.getE()){
            clsLine l = new clsLine(e,N);
            L.add(l);
            layer_bottom.add(l);
        }




    }

    public boolean loadGraph(String filename){
        clsGraph in = clsGraphIO.readGraph(filename);
        if(in == null)
            return false;
        layer_top.clear();
        layer_bottom.clear();
        graph = in;
        loadGraph();
        return true;

    }



    public ArrayList[] getLayers(){
        ArrayList[] layers = new ArrayList[2];
        layers[0] = layer_bottom;
        layers[1] = layer_top;
        return layers;
    }



    public int run(){
        clsPanel panel = new clsPanel(this);
        this.panel = panel;
        boolean _continue = true;
        clsWindow window = new clsWindow("testwindow",panel,1920,1080,true);
        this.window = window;
        listener.setParent(this);
        xOffset = window.getWidth()/2;
        yOffset = window.getHeight()/2;
        loadGraph("test");
        listener.applyListeners(window);
        layer_top.add(listener);

        while(_continue){
            window.repaint();
        }
        return 0;
    }


}

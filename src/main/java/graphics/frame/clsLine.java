/**
 * Created by Philipp Polland on 21-May-20.
 */

package graphics.frame;

import dijkstra.clsEdge;
import dijkstra.clsVertex;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import static util.clsVectorMaths.getMidPoint;

public class clsLine implements Drawable, Interactable {

    private clsNode[] nodes = new clsNode[2];
    private int[] lPos = {0,0};
    private int value = 0;
    private String identifyer;
    private clsEdge eRef;

    public clsLine(){

    }

    public clsLine(clsEdge e, Map<String, clsNode> Nodes){
        this.eRef = e;
        clsVertex[] vertices = eRef.getVertices();
        this.setRoot(Nodes.get(vertices[0].getIdentifier()));
        this.setTarget(Nodes.get(vertices[1].getIdentifier()));
        this.setValue(eRef.getLength());
    }

    //Setter
    public void setNodes(clsNode[] nodes) {
        this.nodes = nodes;
    }

    public void setRoot(clsNode node){
        nodes[0] = node;
    }

    public void setTarget(clsNode node){
        nodes[1] = node;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String getIdentifyer() {
        return identifyer;
    }

    //Getter
    public clsNode[] getNodes() {
        return nodes;
    }

    public clsNode getRoot(){
        return nodes[0];
    }

    public clsNode getTarget(){
        return nodes[1];
    }

    public int getValue() {
        return value;
    }

    public void setIdentifyer(String identifyer) {
        this.identifyer = identifyer;
    }

    //Funktionen
    public boolean contains(String identifyer){
        if(nodes[0].equals(identifyer) || nodes[1].equals(identifyer)){
            return true;
        }
        return false;
    }

    @Override
    public Object[] react(String[] arguments, Object[] objects) {
        if("mouseDragged".contentEquals(arguments[0])){
            MouseEvent e = (MouseEvent) objects[0];
            lPos[0] = e.getX();
            lPos[1] = e.getY();
        }
        return null;
    }



    @Override
    public void paintElement(Graphics g, float xOffset, float yOffset, float multiplyer) {
        if(getRoot()!= null && getTarget() != null) {


            float x1 = nodes[0].getXOrigin(multiplyer, xOffset) + nodes[0].getR() * multiplyer;
            float y1 = nodes[0].getYOrigin(multiplyer, yOffset) + nodes[0].getR() * multiplyer;

            float x2 = nodes[1].getXOrigin(multiplyer, xOffset) + nodes[1].getR() * multiplyer;
            float y2 = nodes[1].getYOrigin(multiplyer, yOffset) + nodes[1].getR() * multiplyer;

            float[] mid = getMidPoint(x1,y1,x2,y2);

            if((clsVertex.MODES.PATH == nodes[0].getvRef().getMode() || clsVertex.MODES.GOAL == nodes[0].getvRef().getMode())
              && (clsVertex.MODES.PATH == nodes[1].getvRef().getMode() || clsVertex.MODES.GOAL == nodes[1].getvRef().getMode())){
                g.setColor(ColorStorage.path_line);
            } else if(nodes[0].getvRef().getMode() == clsVertex.MODES.ACTIVE || clsVertex.MODES.ACTIVE == nodes[1].getvRef().getMode()){
                g.setColor(ColorStorage.active_line);
            } else {
                g.setColor(ColorStorage.lineColor);
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g.drawLine( (int) x1,(int) y1,(int) x2,(int) y2);
            g.setColor(ColorStorage.mask);
            g.fillRect((int) x1 + (int) mid[0],(int) y1 + (int) mid[1] - g.getFontMetrics().getHeight(),g.getFontMetrics().charsWidth(("v: " + getValue()).toCharArray(),0,("v: " + getValue()).length()),g.getFontMetrics().getHeight());
            g.setColor(Color.WHITE);
            g.drawString("v: " + getValue(),(int) x1 + (int) mid[0],(int) y1 + (int) mid[1]);
        } else if(getRoot() != null){

            float x1 = nodes[0].getXOrigin(multiplyer, xOffset) + nodes[0].getR() * multiplyer;
            float y1 = nodes[0].getYOrigin(multiplyer, yOffset) + nodes[0].getR() * multiplyer;

            float x2 = lPos[0] - 9;
            float y2 = lPos[1] - 31;

            float[] mid = getMidPoint(x1,y1,x2,y2);

            g.setColor(ColorStorage.lineColor);
            g.drawString(getRoot().getIdentifyer() + " -> ",(int) x1 + (int) mid[0],(int) y1 + (int) mid[1]);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            g.drawLine( (int) x1,(int) y1,(int) x2,(int) y2);
        }
    }
}

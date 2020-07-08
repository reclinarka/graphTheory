/**
 * Created by Philipp Polland on 21-May-20.
 */
package graphics.frame;

import dijkstra.clsVertex;
import graphics.clsUIHandler;

import java.awt.*;
import java.awt.event.MouseEvent;

public class clsNode implements Drawable, Interactable {
    public clsNode(){}

    private clsVertex vRef;

    public clsNode(float x, float y, float r, clsVertex vRef){
        this.x = x;
        this.y = y;
        this.r = r;
        this.vRef = vRef;
        this.identifyer = vRef.getIdentifier();
    }

    public clsNode(clsVertex vertex){
        this.x = 0;
        this.y = 0;
        this.r = 1;
        this. vRef = vertex;
        this.identifyer = vRef.getIdentifier();
    }

    public void setIdentifyer(String identifyer) {
        this.identifyer = identifyer;
    }

    private String identifyer;

    private boolean isSelected = false;
    float[] lastMousePos = new float[]{0f,0f};
    private float x;

    private float y;
    private float r;
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public float getR() {
        return r;
    }

    public float getXOrigin(float multiplyer, float xOffset){
        return (( x-r ) * multiplyer + xOffset);
    }

    public float getYOrigin(float multiplyer, float yOffset){
        return (( y-r ) * multiplyer + yOffset);
    }

    public clsVertex getvRef() {
        return vRef;
    }

    public boolean equals(String identifyer){
        return this.identifyer.contentEquals(identifyer);
    }

    @Override
    public String getIdentifyer() {
        return identifyer;
    }

    @Override
    public void paintElement(Graphics g, float xOffset, float yOffset, float multiplyer) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        int xOrigin = (int) getXOrigin(multiplyer,xOffset);
        int yOrigin = (int) getYOrigin(multiplyer,yOffset);



        Stroke old = ((Graphics2D) g).getStroke();

        //g.drawString("x", (int) lastMousePos[0], (int) lastMousePos[1] );

        if(isSelected){
            switch(vRef.getMode()){
                case ACTIVE:
                    g.setColor(ColorStorage.active_node);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.highlighted);
                    break;
                case DEFAULT:
                    g.setColor(ColorStorage.nodeColor);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.highlighted);
                    break;
                case PATH:
                    g.setColor(ColorStorage.path_node);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.highlighted);
                    break;
                case GOAL:
                    g.setColor(ColorStorage.goal_node);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.highlighted);
            }

            drawBody(g, multiplyer, xOrigin, yOrigin);

        }else {
            switch(vRef.getMode()){
                case ACTIVE:
                    g.setColor(ColorStorage.active_node);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.active_line);
                    break;
                case DEFAULT:
                    g.setColor(ColorStorage.nodeColor);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.boarderColor);
                    break;
                case PATH:
                    g.setColor(ColorStorage.path_node);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.path_line);
                    break;
                case GOAL:
                    g.setColor(ColorStorage.goal_node);
                    g.fillOval( xOrigin, yOrigin,
                            (int) (r*2*multiplyer), (int) (r*2*multiplyer));
                    g.setColor(ColorStorage.goal_line);
            }

            drawBody(g, multiplyer, xOrigin, yOrigin);
        }


    }

    private void drawBody(Graphics g, float multiplyer, int xOrigin, int yOrigin) {
        g.drawOval( xOrigin, yOrigin,
                (int) (r*2*multiplyer), (int) (r*2*multiplyer));
        g.drawString(identifyer,xOrigin, (int) (yOrigin-r  * multiplyer) + 10);
        if(vRef.getValue() < Integer.MAX_VALUE)
            g.drawString(String.valueOf(vRef.getValue()),xOrigin + (int) (r * 0.5 * multiplyer),yOrigin +  (int) (r * 0.5  * multiplyer)+ (int) multiplyer );
    }


    @Override
    public Object[] react(String[] arguments, Object[] objects) {
        if("mouseClicked".contentEquals(arguments[0])){
            MouseEvent e = (MouseEvent) objects[0];
            clsUIHandler parent = (clsUIHandler) objects[1];

            float[] clickPos = clsUniversalListener.mouseCorr(e.getX(),e.getY());

            float yOrigin =  ((y-r) * parent.getMultiplyer() + parent.getyOffset());
            float xOrigin =  ((x-r) * parent.getMultiplyer() + parent.getxOffset());



            lastMousePos = clickPos;
            if( xOrigin <= clickPos[0] && clickPos[0] < xOrigin + r * 2 * parent.getMultiplyer()){
                if(yOrigin <= clickPos[1] && clickPos[1] < yOrigin + r * 2 * parent.getMultiplyer()){
                    isSelected = true;
                    return new Object[]{this};
                }
            }
            isSelected = false;

        } else if("moveByVector".contentEquals(arguments[0])) {
            clsUIHandler parent = (clsUIHandler) objects[1];
            MouseEvent e = (MouseEvent) objects[0];
            int[] oldPos = (int[]) objects[2];
            float[] clickPos = new float[]{e.getX(),e.getY()};

            float xMovement = (clickPos[0]-oldPos[0]) / parent.getMultiplyer();
            float yMovement = (clickPos[1]-oldPos[1]) / parent.getMultiplyer();

            x += xMovement;
            y += yMovement;

            return new Object[]{this};
        }

        return null;
    }


}

/**
 * Created by Philipp Polland on 21-May-20.
 */
package graphics.frame;

import graphics.clsUIHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class clsPanel extends JPanel {

    public clsPanel(){}

    public clsPanel(clsUIHandler parent){
        this.parent = parent;
    }

    private clsUIHandler parent;


    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(0,0,parent.getWindow().getWidth(),parent.getWindow().getHeight());

        float mp = parent.getMultiplyer();
        float xOffset = parent.getxOffset();
        float yOffset = parent.getyOffset();
        g.setFont(g.getFont().deriveFont(mp));

        for(ArrayList<Drawable> layer : parent.getLayers()){
            for(Drawable d :layer){
                d.paintElement(g,xOffset,yOffset,mp);
            }
        }
    }
}

/**
 * Created by Philipp Polland on 21-May-20.
 */
package graphics.frame;

import javax.swing.*;
import java.awt.*;

public class clsWindow extends JFrame {
    public clsWindow(String windowTitle, clsPanel content, int width, int height, boolean resizable){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        this.setLocation(( (gd.getDisplayMode().getWidth() / 2) - (width / 2) ),
                ( (gd.getDisplayMode().getHeight() / 2) - (height / 2) ));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setPreferredSize(new Dimension(width,height));
        setResizable(resizable);
        setTitle(windowTitle);
        init(content);
    }
    private void init(clsPanel content){
        setLayout(new GridLayout(1,1,0,0));
        getContentPane().add(content);
        pack();
        setVisible(true);
    }
}

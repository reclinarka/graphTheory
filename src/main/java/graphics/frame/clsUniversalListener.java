/**
 * Created by Philipp Polland on 21-May-20.
 */
package graphics.frame;

import dijkstra.clsDijkstraManager;
import dijkstra.clsEdge;
import dijkstra.clsGraph;
import dijkstra.clsVertex;
import graphics.clsUIHandler;
import util.clsGraphIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class clsUniversalListener implements java.awt.event.MouseListener, MouseMotionListener, MouseWheelListener, KeyListener, Drawable {

    private clsUIHandler parent;
    private Interactable selected;
    private int[] lastPos = new int[2];
    private boolean textefieldOpen;
    private AtomicBoolean messageActive = new AtomicBoolean(false);
    private TEXT_MODE textfieldMode = TEXT_MODE.VERTEX;
    private String textfield, message = "";
    private Color messageColor = Color.WHITE;
    private int[] clickPos;
    private clsLine creator;
    private int tries = 0;
    private clsDijkstraManager manager;
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    public void setParent(clsUIHandler parent) {
        this.parent = parent;
    }

    public clsUIHandler getParent() {
        return parent;
    }

    public static float[] mouseCorr(int xPos, int yPos) {
        return new float[]{xPos - 8, yPos - 31};
    }

    public void applyListeners(JFrame frame) {
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);
        frame.addMouseWheelListener(this);
        frame.addKeyListener(this);
    }

    private TimerTask resetMessageTask(){
        return new TimerTask() {
            @Override
            public void run() {
                messageActive.set(false);
            }
        };
    }

    private void displayMessage(String message, Color color, long duration){
        this.message = message;
        this.messageColor = color;
        this.messageActive.set(true);
        Timer timer = new Timer();
        timer.schedule(resetMessageTask(), duration );

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //System.out.println(e.getButton());
        if (e.getButton() == 3 && !textefieldOpen) {
            clickPos = new int[]{e.getX(), e.getY()};
            textfield = "";
            textefieldOpen = true;
        } else if (e.getButton() == 1 && !textefieldOpen) {
            clickPos = new int[]{e.getX(), e.getY()};
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (textefieldOpen) {
            if (Character.isLetterOrDigit(e.getKeyChar()) || e.getKeyChar() == ' ') { // Handle Letters and Digits
                if (textfieldMode == TEXT_MODE.VERTEX || textfieldMode == TEXT_MODE.SAVE || textfieldMode == TEXT_MODE.OPEN)
                    textfield += e.getKeyChar();
            }
            if (Character.isDigit(e.getKeyChar())) {     //Handle Digits only
                if (textfieldMode == TEXT_MODE.EDGE)
                    textfield += e.getKeyChar();
            } else if (e.getKeyCode() == 27) { //Handle ESC
                textefieldOpen = false;
                textfield = "";
                textfieldMode = TEXT_MODE.VERTEX;
            } else if (e.getKeyCode() == 8) { //Handle Backspace
                if (!textfield.isEmpty())
                    textfield = textfield.substring(0, textfield.length() - 1);
            } else if (e.getKeyCode() == 10) {  //Handle Enter Press

                if (textfieldMode == TEXT_MODE.VERTEX) {

                    clsVertex nV = new clsVertex(textfield);
                    clsNode node = new clsNode((clickPos[0] - parent.getxOffset()) / parent.getMultiplyer(),
                            (clickPos[1] - parent.getyOffset()) / parent.getMultiplyer(), 1, nV);
                    parent.getGraph().getV().add(nV);
                    parent.getLayer_top().add(node);
                    parent.getInteractables().put(textfield, node);
                    textefieldOpen = false;

                } else if (textfieldMode == TEXT_MODE.EDGE) {

                    clsGraph graph = parent.getGraph();
                    clsVertex[] vs = new clsVertex[]{graph.getVertexbyID(creator.getRoot().getIdentifyer()),
                            graph.getVertexbyID(creator.getTarget().getIdentifyer())};
                    clsEdge edge = new clsEdge(Integer.parseInt(textfield), vs);
                    creator.setValue(Integer.parseInt(textfield));
                    graph.getE().add(edge);
                    parent.getLayer_bottom().add(creator);
                    parent.getInteractables().remove("edge_root_created");
                    creator = null;
                    textefieldOpen = false;
                    textfieldMode = TEXT_MODE.VERTEX;

                } else if (textfieldMode == TEXT_MODE.OPEN) {
                    if(parent.loadGraph(textfield))
                        parent.getLayer_top().add(this);
                    else
                        displayMessage("ERROR LOADING FILE, IS THE FILENAME CORRECT?", Color.RED, 5000);
                    textefieldOpen = false;
                    textfieldMode = TEXT_MODE.VERTEX;
                } else if (textfieldMode == TEXT_MODE.SAVE) {
                    clsGraphIO.saveGraph(parent.getGraph(), textfield);
                    textefieldOpen = false;
                    textfieldMode = TEXT_MODE.VERTEX;
                }

            }

        } else {
            //System.out.println(e.getKeyCode());
            if (e.isControlDown()) {
                if (e.getKeyCode() == 83) { //Handles ctrl + s
                    textfield = "";
                    textefieldOpen = true;
                    textfieldMode = TEXT_MODE.SAVE;
                } else if (e.getKeyCode() == 79) { //Handles ctrl + o
                    textfield = "";
                    textefieldOpen = true;
                    textfieldMode = TEXT_MODE.OPEN;
                } else if (e.getKeyCode() == 84) { //Handles ctrl + t
                    ((clsNode) selected).getvRef().setMode(clsVertex.MODES.PATH);
                    parent.getGraph().setStart(((clsNode) selected).getvRef());
                } else if (e.getKeyCode() == 90) { //Handles ctrl + z
                    ((clsNode) selected).getvRef().setMode(clsVertex.MODES.GOAL);
                    parent.getGraph().setGoal(((clsNode) selected).getvRef());
                } else if (e.getKeyCode() == 68) { //Handles ctrl + d
                    manager = new clsDijkstraManager(parent.getGraph());
                    executorService.execute(() -> manager.calculatePath());


                }
            } else if (e.getKeyCode() == 127) {
                if (selected != null) {
                    clsNode toRemove = (clsNode) selected;
                    parent.getGraph().remove(toRemove.getIdentifyer());
                    ArrayList<Drawable> vertex_list = parent.getLayer_top();
                    for (int i = 0; i < vertex_list.size(); i++) {
                        if (vertex_list.get(i).getIdentifyer().contentEquals(toRemove.getIdentifyer())) {
                            vertex_list.remove(i);
                            i--;
                        }
                    }
                    ArrayList<Drawable> line_list = parent.getLayer_bottom();
                    for (int i = 0; i < line_list.size(); i++) {
                        if (((clsLine) line_list.get(i)).contains(toRemove.getIdentifyer())) {
                            line_list.remove(i);
                            i--;
                        }
                    }
                    selected = null;
                    parent.getInteractables().remove(toRemove.getIdentifyer());
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastPos[0] = e.getX();
        lastPos[1] = e.getY();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (creator != null && !textefieldOpen) {
            clickPos = new int[]{e.getX(), e.getY()};
            for (Interactable i : parent.getInteractables().values()) {
                if (i.react(new String[]{"mouseClicked"}, new Object[]{e, parent}) != null) {
                    selected = i;
                    break;
                }
            }
            creator.setTarget((clsNode) selected);
            textfield = "";
            textefieldOpen = true;
            textfieldMode = TEXT_MODE.EDGE;

        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selected != null && !textefieldOpen) {

            if (e.isShiftDown()) {
                if (creator == null) {
                    textfieldMode = TEXT_MODE.EDGE;
                    creator = new clsLine();
                    creator.setRoot((clsNode) selected);
                    parent.getInteractables().put("edge_root_created", creator);
                }
                creator.react(new String[]{"mouseDragged"}, new Object[]{e});


            } else {
                if (creator != null) {
                    creator = null;
                    parent.getInteractables().remove("edge_root_created");
                }

                int[] lPosCopy = new int[]{lastPos[0], lastPos[1]};

                selected.react(new String[]{"moveByVector"}, new Object[]{e, parent, lPosCopy});

                lastPos[0] = e.getX();
                lastPos[1] = e.getY();

            }
        } else if (textefieldOpen) {

        } else {
            int[] lPosCopy = new int[]{lastPos[0], lastPos[1]};


            parent.setxOffset(parent.getxOffset() + (e.getX() - lPosCopy[0]));
            parent.setyOffset(parent.getyOffset() + (e.getY() - lPosCopy[1]));

            lastPos[0] = e.getX();
            lastPos[1] = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        //System.out.println("x: " + e.getX() + ", y: " + e.getY());

        clickPos = new int[]{e.getX(), e.getY()};
        for (Interactable i : parent.getInteractables().values()) {
            if (i.react(new String[]{"mouseClicked"}, new Object[]{e, parent}) != null) {

                selected = i;

                return;
            }
        }
        selected = null;

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        float mp = parent.getMultiplyer() + e.getUnitsToScroll() * -1;
        if (mp < 0)
            mp = 1;
        parent.setMultiplyer(mp);

    }

    @Override
    public void paintElement(Graphics g, float xOffset, float yOffset, float multiplyer) {


        if (textefieldOpen) {
            g.setColor(ColorStorage.mask);
            g.fillRect(0, 0, parent.getWindow().getWidth(), parent.getWindow().getHeight());
            switch (textfieldMode) {
                case VERTEX:
                    g.setColor(Color.WHITE);
                    g.drawString("Please enter a VertexID or press ESC to exit: " + textfield, 10,  g.getFontMetrics().getHeight());
                    break;
                case EDGE:
                    g.setColor(Color.WHITE);
                    g.drawString("Please enter a Value for the Edge or press ESC to Exit: " + textfield, 10, g.getFontMetrics().getHeight());
                    break;
                case OPEN:
                    g.setColor(Color.WHITE);
                    g.drawString("Please enter a filename to load or press ESC to Exit: " + textfield, 10, g.getFontMetrics().getHeight());
                    break;
                case SAVE:
                    g.setColor(Color.WHITE);
                    g.drawString("Please enter a filename for the Graph or press ESC to Exit: " + textfield, 10, g.getFontMetrics().getHeight());
                    break;
            }
        }

        if (creator != null) {
            creator.paintElement(g, xOffset, yOffset, multiplyer);
        }


        if(messageActive.get()){
            Color saved = g.getColor();
            g.setColor(ColorStorage.mask);
            int screenWidth = g.getClipBounds().width;
            int screenHeight = g.getClipBounds().height;
            int stringWidth = g.getFontMetrics().charsWidth(message.toCharArray(),0,message.length());
            int stringHeight = g.getFontMetrics().getHeight();
            g.fillRect(screenWidth/ 2 - stringWidth / 2,
                    screenHeight / 2 - stringHeight ,stringWidth,stringHeight);
            g.setColor(messageColor);
            g.drawString(message, screenWidth / 2 - stringWidth / 2,
                    screenHeight / 2);
            g.setColor(saved);
        }
    }

    @Override
    public String getIdentifyer() {
        return "UnicersalListener" + toString();
    }

    private enum TEXT_MODE {
        VERTEX,
        EDGE,
        SAVE,
        OPEN
    }
}

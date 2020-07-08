/**
 * Created by Philipp Polland on 19-May-20.
 */

import dijkstra.clsEdge;
import dijkstra.clsGraph;
import dijkstra.clsVertex;
import graphics.clsUIHandler;
import util.clsGraphIO;

import java.util.Scanner;

/**
 *
 * PSEUDOCODE:
 * Funktion DiJkstra(Graph,Startknoten):
 *  intitialisiere(Graph,Startknoten,abstand[],vorgänger[],Q)
 *  solange Q nicht leer:
 *      u:= Knoten in Q mit kleinstem Wert in abstand[]
 *      entferne u aus Q
 *      für jeden Nachbarn v von u:
 *          falls v in Q:
 *              distanz_update(u,v,abstand[],vorgänger[])
 *  return vorgänger[]
 *
 *
 *
 *  Methode initialisiere(Graph,Startknoten,abstand[],vorgänger[],Q):
 *      für jeden Knoten v in Graph:
 *          abstand[v]:= unendlich
 *          vorgänger[v]:= null
 *      abstand[Startknoten]:= 0
 *      Q:= Die Menge aller Knoten in Graph
 *
 *
 *
 *  Methode distanz_update(u,v,abstand[],vorgänger[]):
 *      alternativ:= abstand[u] + abstand_zwischen(u, v)   // Weglänge vom Startknoten nach v über u
 *      falls alternativ < abstand[v]:
 *          abstand[v]:= alternativ
 *          vorgänger[v]:= u
 *
 *  Funktion erstelleKürzestenPfad(Zielknoten,vorgänger[])
 *   Weg[]:= [Zielknoten]
 *   u:= Zielknoten
 *   solange vorgänger[u] nicht null:   // Der Vorgänger des Startknotens ist null
 *       u:= vorgänger[u]
 *       füge u am Anfang von Weg[] ein
 *   return Weg[]
 *
 *
 *
 *
 * **/

public class Main {
    public static void main(String[] args) {
        if(args.length > 0)
            if(args[0].contentEquals("-gui"))
                graphicsTest();
        cmdLineTest1();
    }


    public static void graphicsTest(){
        clsUIHandler handler = new clsUIHandler();
        handler.run();
    }

    public static void cmdLineTest1(){
        clsGraph graph = new clsGraph();
        Scanner s = new Scanner(System.in);
        String buffer;


        String input = s.nextLine();
        while(!input.contentEquals("quit")){
            clrScreen();
            System.out.println("Test Program 1:");
            if(input.contains("display graph")){
                System.out.println("displaying graph:\n");
                System.out.println(graph.toString());

            } else if(input.contains("add vertex")){
                System.out.println("Adding Vertex:\n");
                System.out.println("    please Enter a unique Identifyer:\n    ");
                input = s.nextLine();
                graph.getV().add(new clsVertex(input));

            } else if(input.contains("add edge")){
                System.out.println("Adding Edge:\n");
                clsEdge e = new clsEdge();
                System.out.println("    please Enter V1 ID:\n    ");
                input = s.nextLine();
                e.vertices[0] = graph.getVertexbyID(input);
                System.out.println("    please Enter V2 ID:\n    ");
                input = s.nextLine();
                e.vertices[1] = graph.getVertexbyID(input);
                System.out.println("    please Enter a length:\n    ");
                input = s.nextLine();
                e.setLength(Integer.valueOf(input));

                graph.getE().add(e);
            } else if(input.contains("save graph")){
                System.out.println("Saving graph:");
                System.out.println("    please Enter a filename:\n    ");
                clsGraphIO.saveGraph(graph,s.nextLine());
                System.out.println("graph loaded!\nPress Enter to continue...");
            } else if(input.contains("load graph")){
                System.out.println("Loading graph:");
                System.out.println("    please Enter a filename:\n    ");
                buffer = s.nextLine();
                System.out.println("    starting to read: " + buffer + ".graph");
                graph = clsGraphIO.readGraph(buffer);
                System.out.println("graph loaded!\nPress Enter to continue...");
            } else {

            }
            input = s.nextLine();
        }
    }

    private static void clrScreen(){
        for(int i = 0; i < 20; i++)
            System.out.println("\n");
    }
}

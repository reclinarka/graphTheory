/**
 * Created by Philipp Polland on 20-May-20.
 */

package util;

import dijkstra.clsEdge;
import dijkstra.clsGraph;
import dijkstra.clsVertex;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Graphs will be stored as filename + ".graph" files
 * The File is formatted as Follows:
 *
 * #GraphFile#
 * V := { "vertexID", ... }
 * E := { {"vertexID1", "vertexID2", "value" }, ... }
 * //optional:
 * S := "vertexID"
 * G := "vertexID"
 */
public class clsGraphIO {

    public static String readFromFile(String filename){
        File file = new File(filename + ".graph");
        StringBuilder content = new StringBuilder();
        if(file.exists()){
            try {
                BufferedReader in = new BufferedReader(new FileReader(file));
                content.append(in.readLine());
                String inputLine = in.readLine();
                while(inputLine != null){
                    content.append("\n");
                    content.append(inputLine);
                    inputLine = in.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        return content.toString();
    }

    public static void writeToFile(String payload, String filename){

        File file = new File(filename + ".json");
        try {
            if(file.exists()){

            } else {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            writer.write(payload);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String vertexListToString(List<clsVertex> in){
        StringBuilder vertexListBuilder = new StringBuilder();
        vertexListBuilder.append("V := { ");
        int i = 0;
        for(clsVertex v : in){
            i++;
            if( i < 7){
                i = 0;
                vertexListBuilder.append("\n");
            }
            vertexListBuilder.append("\"" + v.getIdentifier() + "\"");
            vertexListBuilder.append(", ");
        }
        vertexListBuilder.deleteCharAt(vertexListBuilder.lastIndexOf(","));
        vertexListBuilder.append(" }\n");
        return vertexListBuilder.toString();
    }

    private static String edgeListToString(List<clsEdge> in){
        StringBuilder edgeListBuilder = new StringBuilder();
        edgeListBuilder.append("E := { ");
        for(clsEdge e : in){
            edgeListBuilder.append("    { \"" + e.vertices[0].getIdentifier() + "\"");
            edgeListBuilder.append(", ");
            edgeListBuilder.append("\"" + e.vertices[1].getIdentifier() + "\"");
            edgeListBuilder.append(", ");
            edgeListBuilder.append("\"" + e.length + "\"");
            edgeListBuilder.append(" }");
            edgeListBuilder.append(", \n");
        }
        edgeListBuilder.deleteCharAt(edgeListBuilder.lastIndexOf(","));
        edgeListBuilder.append(" }\n");
        return edgeListBuilder.toString();
    }

    public static clsGraph readGraph(String filename) {

        File file = new File(filename + ".graph");
        Map<String, clsVertex> vertices = new HashMap<>();
        List<clsEdge> edges = new ArrayList<>();

        if (file.exists()) {
            try {

                BufferedReader in = new BufferedReader(new FileReader(file));
                reading_mode mode = reading_mode.NEUTRAL;
                String inputLine = in.readLine();
                clsVertex start;


                int value = 0;
                clsVertex[] vEdge = new clsVertex[2];

                char[] inputChars;
                String buffer = "";

                if (inputLine.contentEquals("#GraphFile#")) {
                    while ((inputLine = in.readLine()) != null) {
                        inputChars = inputLine.toCharArray();
                        for (char c : inputChars) {
                            switch (mode) {
                                case NEUTRAL: //NETRAL MODE looks for either declaration of V or E
                                    if (c == 'V')
                                        mode = reading_mode.SEARCHING_V; //IF V is declared switch to Searching for Vertices bracket
                                    else if (c == 'E')
                                        mode = reading_mode.SEARCHING_E;
                                    else if(c == 'S')
                                        mode = reading_mode.START;
                                    else if(c == 'G')
                                        mode = reading_mode.GOAL;
                                    break;

                                case START:
                                    if (c == '"') {
                                        buffer = "";
                                        mode = reading_mode.START_VERTEX;
                                    }else if (c == ';')
                                        mode = reading_mode.NEUTRAL;
                                    break;

                                case START_VERTEX:
                                    if (c == '"'){
                                        start = new clsVertex(buffer);
                                        mode = reading_mode.NEUTRAL;
                                    } else {
                                        buffer = buffer + c;
                                    }
                                    break;

                                case SEARCHING_E:
                                    if (c == '{')
                                        mode = reading_mode.EDGES;
                                    break;


                                case EDGES:
                                    if (c == '{')
                                        mode = reading_mode.EDGE;
                                    else if (c == '}')
                                        mode = reading_mode.NEUTRAL;
                                    break;


                                case EDGE:
                                    if (c == '"') {
                                        vEdge = new clsVertex[2];
                                        mode = reading_mode.EDGE_V1;
                                        buffer = "";
                                    } else if (c == '}') {
                                        mode = reading_mode.EDGES;
                                        edges.add(new clsEdge(value, vEdge[0], vEdge[1]));
                                    }
                                    break;


                                case EDGE_V1:
                                    if (c == '"') {
                                        vEdge[0] = vertices.get(buffer);
                                        mode = reading_mode.SEARCHING_V2;
                                    } else {
                                        buffer = buffer + c;
                                    }
                                    break;

                                case SEARCHING_V2:
                                    if (c == '"') {
                                        mode = reading_mode.EDGE_V2;
                                        buffer = "";
                                    } else if (c == '}')
                                        mode = reading_mode.EDGES;
                                    break;


                                case EDGE_V2:
                                    if (c == '"') {
                                        vEdge[1] = vertices.get(buffer);
                                        mode = reading_mode.SEARCHING_LENGTH;
                                    } else {
                                        buffer = buffer + c;
                                    }
                                    break;


                                case SEARCHING_LENGTH:
                                    if (c == '"') {
                                        mode = reading_mode.EDGE_LENGTH;
                                        buffer = "";
                                    } else if( c == '}') {
                                        mode = reading_mode.EDGES;
                                    }
                                    break;
                                case EDGE_LENGTH:
                                    if (c == '"') {
                                        mode = reading_mode.EDGE;
                                        value = Integer.valueOf(buffer);
                                    } else
                                        buffer = buffer + c;
                                    break;


                                case SEARCHING_V:
                                    if (c == '{')
                                        mode = reading_mode.VERTICES;
                                    break;

                                case VERTICES:
                                    if (c == '"') {
                                        buffer = "";
                                        mode = reading_mode.VERTEX_ID;
                                    } else if (c == '}')
                                        mode = reading_mode.NEUTRAL;
                                    break;

                                case VERTEX_ID:
                                    if (c == '"') {
                                        mode = reading_mode.VERTEX_NEXT;
                                        vertices.put(buffer, new clsVertex(buffer));
                                    } else {
                                        buffer = buffer + c;
                                    }
                                    break;

                                case VERTEX_NEXT:
                                    if (c == ',')
                                        mode = reading_mode.VERTICES;
                                    if (c == '}')
                                        mode = reading_mode.NEUTRAL;
                                    break;
                            }
                        }

                    }
                    in.close();
                    return new clsGraph(new ArrayList<>(vertices.values()),edges);
                } else {
                    System.out.println(filename + ".graph is not a valid Graph File!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void saveGraph(clsGraph graph, String filename){

        File file = new File(filename + ".graph");
        try {
            if(file.exists()){

            } else {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            String data = vertexListToString(graph.getV()) + "\n" + edgeListToString(graph.getE());
            writer.write("#GraphFile#\n" + data);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private enum reading_mode {
        NEUTRAL,          // Looks for either V, S, G or E
        SEARCHING_V,      // Looks for {
        VERTICES,         // Looks for " or }
        VERTEX_ID,        // Looks for " and adds everything to Buffer; Also creates Vertex once it reached "
        VERTEX_NEXT,      // Looks for either , or }
        SEARCHING_E,      // Looks for {
        EDGES,            // Looks for either { or }
        EDGE,             // Looks for either " or }
        EDGE_V1,          // Looks for " and adds everything to Buffer; Also looks up vertices.get(buffer) to get vEdges[0]
        SEARCHING_V2,     // Looks for either " or }
        EDGE_V2,          // Looks for " and adds everything to Buffer; Also looks up vertices.get(buffer) to get vEdges[1]
        SEARCHING_LENGTH, // Looks for either " or }
        EDGE_LENGTH,      // Looks for " and adds everything to Buffer;
        START,            // Looks for " or ;
        START_VERTEX,     // Looks for " and adds everything to Buffer; Also adds Vertex as starting Point; used for Djikstra, therefore optional
        GOAL,             // Looks for " or ;
        GOAL_VERTEX       // Looks for " and adds everything to Buffer; Also adds Vertex as Goal; used for Djikstra, therefore optional
    }


}

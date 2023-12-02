import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class GraphExperiment {
    private static final Random random = new Random();
    private static int vertices;
    private static int edges;
    private static int v1;

    public static void main (String[] args) throws IOException {
        int[] V = {10, 20, 30, 40, 50};
        int[] E = {20, 35, 50, 65, 80};

        ArrayList<Edges> edgeList = new ArrayList<>();

        // create datasets
        for (int v : V) {
            File graphData = new File("GraphDataset" + v + ".txt");
            graphData.createNewFile();
            FileWriter writer = new FileWriter("GraphDataset" + v + ".txt");
            for (int e : E) {
                vertices = v;
                edges = e;
               
                for (int i = 0; i < edges; i++) {
                    // Get random vertices and edges
                    v1 = random.nextInt(vertices) + 1;
                    int v2 = random.nextInt(vertices - v1 + 1) + v1 + 1;

                    String source = "Node" + Integer.toString(v1);
                    String dest = "Node" + Integer.toString(v2);
                    int cost = random.nextInt(11);
                    Edges edge = new Edges(v1, v2);

                    // Check for repeating edges
                    if ((!(edgeList.stream().anyMatch(ed -> ed.getV2() == v2))) || !(edgeList.stream().anyMatch(ed -> ed.getV1() == v1))) {
                        edgeList.add(edge); 
                        writer.write(source + " " + dest + " " + Integer.toString(cost) + "\n");
                    }
                }
            }
            writer.close();
        }
        
        // Create graphs and output information
        for (int v : V) {
            // Read in datasets
            Graph graph = new Graph();
            FileReader file = new FileReader("GraphDataset" + v + ".txt");
            Scanner scanner = new Scanner(file);
            String line;
            for (int e : E) {
                // Find source node
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine( );
                    StringTokenizer st = new StringTokenizer(line);

                    try {
                        if( st.countTokens() != 3)
                        {
                            System.err.println( "Skipping ill-formatted line " + line );
                            continue;
                        }
                        String source = st.nextToken();
                        String dest = st.nextToken();
                        int cost = Integer.parseInt(st.nextToken());
                        graph.addEdge(source, dest, cost);
                    }
                    catch( NumberFormatException ex )
                    { System.err.println( "Skipping ill-formatted line " + line ); }
                }
                
                // Print out results
                file = new FileReader("GraphDataset" + v + ".txt");
                scanner = new Scanner(file);
                line = scanner.nextLine();
                StringTokenizer st = new StringTokenizer(line);

                graph.dijkstra(st.nextToken());
                System.out.println(v + " " + e + " " + graph.getVcount() + " " + graph.getEcount() + " " + graph.getPQcount() + " " + e*Math.log(v) + " " + (graph.getVcount() + graph.getEcount() + graph.getPQcount()));
                System.out.println();
            }
        }
    }

    // Edges class to create Edge objects for list of Edges, which is used to make sure no edges are repeated
    private static class Edges {
        int v1;
        int v2;

        public Edges(int v1, int v2) {
            this.v1 = v1;
            this.v2 = v2;
        }

        public int getV1() {
            return v1;
        }

        public int getV2() {
            return v2;
        }

        public String toString() {
            String s1 = Integer.toString(v1);
            String s2 = Integer.toString(v2);
            String s = s1 + " " + s2;
            return s;
        }
    }
}

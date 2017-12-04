import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

// Kruskal's algorithm to find Minimum Spanning Tree of a given connected, 
// undirected and weighted graph. It will choose the cheapest weight 
// in the collection of edges which will not form a inside cycle.

public class Kruskal {
    
    public static Graph kruskal(Graph g)
    {   
       
        int numVertices = g.getNumVertices();
        Kruskal k = new Kruskal();
        DisjSets ds = k.new DisjSets(numVertices);            // Initialize a forest of trees
        Graph mst = k.new Graph(numVertices);                 // Store the resultant MST
        PriorityQueue<Edge> edges = k.new PriorityQueue<>();  // MinHeap data structure of edges       
        for (Edge e : g.getAllEdges()) {
            edges.insert(e);
        }
        
        while(!edges.isEmpty())
        {    
            Edge e = edges.deleteMin();    // Pick edges in a ascending order by weights
                int uset = ds.find(e.getv1());
                int vset = ds.find(e.getv2());    
                if( uset != vset )         // Avoid form a cycle
                {
                    mst.addEdge(e);        // Accept the edge and add to MST
                    ds.union(uset, vset);       
                } 
        }          
        return mst;
    }           
    
    /** Edge-weighted graph representation
     * 
     */
    public class Graph {    
        int V;  // number of vertices
        int E;  // number of edges
        private ArrayList<Edge>[] adj;            // Adjacent-lists representation
        List<Edge> edges = new ArrayList<Edge>(); // All edges
        
        // Constructor
        Graph(int V) {
            if(V < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative.");
            this.V = V;
            this.E = 0;
            adj = new ArrayList[V];
            for(int v = 0; v < V; v++) {
                adj[v] = new ArrayList<Edge>();
            }
        }
        
        public void addEdge(int v1, int v2, int weight) {
            adj[v1].add(new Edge(v1, v2, weight));
            E++;
        }
        
        public void addEdge(Edge e) {
            adj[e.getv1()].add(e);
            E++;
        }
        
        public int getNumVertices() {
            return V;
        }
        
        public int getNumEdges() {
            return E;
        }
  
        public List<Edge> getAllEdges() {
            for(int v = 0; v < V; v++) {
                for(Edge e : adj[v]) {
                    edges.add(e);
                }
            }
            return edges;   
        }
    }
    
    /** Create a object of type Edge of undirected graph
     * 
     */
    public class Edge implements Comparable<Edge> {
        private int v1;   // an edge has 2 vertices (v1,v2) and a weight
        private int v2;
        private int weight;
        
        public Edge(int v1, int v2, int weight) {
            this.v1 =v1;
            this.v2 = v2;
            this.weight = weight;
        }
        
        public int getv1() {
            return v1;
        }
        
        public int getv2() {
            return v2;
        }
        
        public int getWeight(){
            return weight;
        }
        
        @Override
        public int compareTo(Edge nextEdge) {
            if(this.getWeight() > nextEdge.getWeight()) 
                return 1;
            else if(this.getWeight() < nextEdge.getWeight()) 
                return -1;
            else 
                return 0;
        }
    }
    
    // Disjoint set structure implementations
    //
    // ******************PUBLIC OPERATIONS***********************
    // void union( root1, root2 ) --> Union two disjoint sets
    // int find( x )              --> Return the set containing x
    
    public class DisjSets {
        private int[ ] s;
        /**
         * Construct the disjoint sets object.
         * @param numElements the initial number of disjoint sets.
         */
        public DisjSets(int numElements) {
            s = new int[numElements];
            for(int i = 0;i < s.length; i++) {
                s[i] = -1;
            }
        }
        /**
         * Union two disjoint sets using the height heuristic.
         * For simplicity, we assume root1 and root2 are distinct
         * and represent set names.
         * @param root1 the root of set 1.
         * @param root2 the root of set 2.
         */
        public void union(int root1, int root2) {       
            if( s[root2] < s[root1] )   // root2 is deeper
                s[root1] = root2;       // Make root2 new root
            else {
                if( s[root1] == s[root2]) 
                    s[root1]--;         // Update height if same 
                    s[root2] = root1;   // Make root1 new root
                }
            }
        /**
         * Perform a find with path compression.
         * Error checks omitted again for simplicity.
         * @param x the element being searched for.
         * @return the set containing x.
         */
        public int find(int x) {
            if( s[x] < 0 )
                return x;
            else
                return s[x] = find(s[x]);
        }   
    }
    
    
    // Priority Queue Class Implementation
    // CONSTRUCTION: with optional capacity (that defaults to 100)
    //               or an array containing initial items
    //
    // ******************PUBLIC OPERATIONS*********************
    // void insert( x )       --> Insert x
    // Comparable deleteMin( )--> Return and remove smallest item
    // Comparable findMin( )  --> Return smallest item
    // boolean isEmpty( )     --> Return true if empty; else false
    // void makeEmpty( )      --> Remove all items
    // ******************ERRORS********************************
    // Throws UnderflowException as appropriate

    public class PriorityQueue<AnyType extends Comparable<? super AnyType>>
    {
        /**
         * Construct the binary heap.
         */
        public PriorityQueue( )
        {
            this( DEFAULT_CAPACITY );
        }

        /**
         * Construct the binary heap.
         * @param capacity the capacity of the binary heap.
         */
        public PriorityQueue( int capacity )
        {
            currentSize = 0;
            array = (AnyType[]) new Comparable[ capacity + 1 ];
        }
        
        /**
         * Construct the binary heap given an array of items.
         */
        public PriorityQueue( AnyType [ ] items )
        {
                currentSize = items.length;
                array = (AnyType[]) new Comparable[ ( currentSize + 2 ) * 11 / 10 ];

                int i = 1;
                for( AnyType item : items )
                    array[ i++ ] = item;
                buildHeap( );
        }

        /**
         * Insert into the priority queue, maintaining heap order.
         * Duplicates are allowed.
         * @param x the item to insert.
         */
        public void insert( AnyType x )
        {
            if( currentSize == array.length - 1 )
                enlargeArray( array.length * 2 + 1 );

                // Percolate up
            int hole = ++currentSize;
            for( array[ 0 ] = x; x.compareTo( array[ hole / 2 ] ) < 0; hole /= 2 )
                array[ hole ] = array[ hole / 2 ];
            array[ hole ] = x;
        }

        private void enlargeArray( int newSize )
        {
                AnyType [] old = array;
                array = (AnyType []) new Comparable[ newSize ];
                for( int i = 0; i < old.length; i++ )
                    array[ i ] = old[ i ];        
        }
        
        /**
         * Find the smallest item in the priority queue.
         * @return the smallest item, or throw an UnderflowException if empty.
         */
        public AnyType findMin( )
        {
            if( isEmpty( ) )
                throw new NoSuchElementException("Priority queue underflow");
            return array[ 1 ];
        }

        /**
         * Remove the smallest item from the priority queue.
         * @return the smallest item, or throw an UnderflowException if empty.
         */
        public AnyType deleteMin( )
        {
            if( isEmpty( ) )
                throw new NoSuchElementException("Priority queue underflow");

            AnyType minItem = findMin( );
            array[ 1 ] = array[ currentSize-- ];
            percolateDown( 1 );

            return minItem;
        }

        /**
         * Establish heap order property from an arbitrary
         * arrangement of items. Runs in linear time.
         */
        private void buildHeap( )
        {
            for( int i = currentSize / 2; i > 0; i-- )
                percolateDown( i );
        }

        /**
         * Test if the priority queue is logically empty.
         * @return true if empty, false otherwise.
         */
        public boolean isEmpty( )
        {
            return currentSize == 0;
        }

        /**
         * Make the priority queue logically empty.
         */
        public void makeEmpty( )
        {
            currentSize = 0;
        }

        private static final int DEFAULT_CAPACITY = 10;

        private int currentSize;      // Number of elements in heap
        private AnyType [ ] array;    // The heap array

        /**
         * Internal method to percolate down in the heap.
         * @param hole the index at which the percolate begins.
         */
        private void percolateDown( int hole )
        {
            int child;
            AnyType tmp = array[ hole ];

            for( ; hole * 2 <= currentSize; hole = child )
            {
                child = hole * 2;
                if( child != currentSize &&
                        array[ child + 1 ].compareTo( array[ child ] ) < 0 )
                    child++;
                if( array[ child ].compareTo( tmp ) < 0 )
                    array[ hole ] = array[ child ];
                else
                    break;
            }
            array[ hole ] = tmp;
        }
    }
    
    /**
     *  main() test
     **/
        public static void main(String[] args) {
            // Loading the input txt. file
            String fileName = "src/assn9_data.txt";
            String line = null;
            List<String> vertices = new ArrayList<>(); 
            int mstTotalWeight = 0;

            try {
                FileReader fr = new FileReader(fileName);
                BufferedReader br = new BufferedReader(fr);
                
                
                int numVertices =Integer.parseInt( br.readLine());
                System.out.println("Read no. of vertices in file: " + numVertices);
                System.out.println();
                
                int numEdges =Integer.parseInt( br.readLine());
                System.out.println("Read no. of vertices in file: " + numEdges);
                System.out.println();
                
                // Store all vertices
                for(int i = 0; i < numVertices; i++) {    
                       line = br.readLine();
                        vertices.add(line);                        
                }    
                System.out.println(vertices); 
                System.out.println();

                Kruskal k = new Kruskal();
                Graph graph = k.new Graph(numVertices);
              
                // Add all edges to initialized graph
                for(int j = 0; j < numEdges; j++ ) {
                graph.addEdge(vertices.indexOf(br.readLine()), vertices.indexOf(br.readLine()), 
                        Integer.parseInt(br.readLine()));
                }
                
                System.out.println("Implementation of Kruskal's algorithm...");
                List<Edge> edges = new ArrayList<Edge>();
                edges = kruskal(graph).getAllEdges();
                System.out.println("The edges of Minimum Cost Spanning Tree are:");
                for(Edge e : edges) {
                    System.out.println(vertices.get(e.getv1()) + "-" + 
                            vertices.get(e.getv2()) + ": " + e.getWeight() );
                    mstTotalWeight += e.getWeight();
                }
                System.out.println();
                System.out.println("The Minimum Cost = " + mstTotalWeight);
                
                
                br.close();         
            }
            catch(FileNotFoundException ex) {
                System.out.println("Unable to open file '" +  fileName + "'");                
            }
            catch(IOException ex) {
                System.out.println( "Error reading file '" + fileName + "'");                  
            }
        }
}
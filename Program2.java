import java.util.*;

/**
 * Vinay Shah
 * vss452
 */

public class Program2 {

    private int max_connects = 4;

    public int constructIntensityGraph(int[][] image){
        int sum = 0;
        for (int i = 0; i < image.length; i++){
            for (int j = 0; j < image[i].length; j++){
                if (!(j + 1 >= image[i].length)){
                    sum += Math.abs((image[i][j]) - (image[i][j+1]));
                }
                if (!(i + 1 >= image.length)){
                    sum += Math.abs((image[i][j]) - (image[i+1][j]));
                }
            }
        }
        return sum;
    }

    public int constructPrunedGraph(int[][] image){
        //create adjacency list
        ArrayList<ArrayList<Node>> adjList = new ArrayList<>(image.length*image[0].length);
        for (int i = 0; i < image.length*image[0].length; i++){
            adjList.add(new ArrayList<Node>(max_connects));
        }

        //arraylist of all nodes
        ArrayList<Node> allNodes = new ArrayList<>(image.length*image[0].length);
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                allNodes.add(new Node(image[i][j], i*image[0].length + j));
            }
        }

        //populate adjacency list
        for (int i = 0; i < image.length; i++){
            for (int j = 0; j < image[i].length; j++){
                if (!(i - 1 < 0)){
                    adjList.get(i*image[0].length + j).add(allNodes.get((i-1)*image[0].length + j));
                }
                if (!(i + 1 >= image.length)){
                    adjList.get(i*image[0].length + j).add(allNodes.get((i+1)*image[0].length + j));
                }
                if (!(j - 1 < 0)){
                    adjList.get(i*image[0].length + j).add(allNodes.get(i*image[0].length + (j-1)));
                }
                if (!(j + 1 >= image[i].length)){
                    adjList.get(i*image[0].length + j).add(allNodes.get(i*image[0].length + (j+1)));
                }

            }
        }


        //find MST using Prim's Algorithm
        int sum = 0;
        HashSet<Node> mstSet = new HashSet<>();
        allNodes.get(0).key = 0;
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(allNodes.get(0));
        Node smallestKeyNode;
        int size = image.length*image[0].length; //the number all the vertices/nodes in the image

        while (mstSet.size() < size){
            smallestKeyNode = priorityQueue.poll();
            if (!mstSet.contains(smallestKeyNode)){
                mstSet.add(smallestKeyNode);
                sum += smallestKeyNode.key;
                for (Node neighbour : adjList.get(smallestKeyNode.index)){
                    if (!mstSet.contains(neighbour)){
                        int difference = Math.abs(neighbour.intensity - smallestKeyNode.intensity);
                        if (difference < neighbour.key){
                            //try removing from pq first
                            priorityQueue.remove(neighbour);
                            neighbour.key = difference;
                            priorityQueue.add(neighbour);
                        }
                    }
                }
            }
        }
        return sum;
    }


    /**
     * Node class to store date about each node in the graph
     */
    class Node implements Comparable {
        int index;
        int intensity;
        int key;

        public Node(int intensity, int index){
            this.intensity = intensity;
            this.index = index;
            this.key = Integer.MAX_VALUE;
        }

        @Override
        public int compareTo(Object o) {
            return Integer.compare(this.key, ((Node) o).key);
        }

        @Override
        public String toString() {
            return Integer.toString(index);
        }
    }

}

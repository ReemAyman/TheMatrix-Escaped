package code;

import java.util.ArrayList;
import java.util.Collections;

public abstract class GenericSearchProblem {

    /**
     * checks if a node passes the goal test
     * @param currNode the current node
     * @return a boolean value indicating if it passes the goal test
     */
    public abstract boolean goalTest(Node currNode);

    /**
     * expanding the node according to the operators of the problem
     * @param currNode a node of expansion
     * @return the expanded nodes
     */
//    public abstract ArrayList<code.Node> expandNode(code.Node currNode);

    /**
     * performs the general procedure of a search problem
     * @param initialState the initial state of a search problem
     * @param searchStrategy the search strategy used (or a queuing function)
     * @return the node in the search tree that passes the goal test
     */
    public Node genericSearch(Node initialState, SearchStrategies searchStrategy){
        // Dalia's part -> IDS algorithm calling
        if(searchStrategy == SearchStrategies.ID) return ids(initialState);
        //---
        // Initializing an empty queue of nodes to be expanded
        ArrayList<Node> queue = new ArrayList<>();
        // Adding the root node to the queue
        queue.add(initialState);
        // Iterating till returning a solution or a failure
        while(true){
            // Returning null (failure) if the queue is empty (no further nodes to be expanded)
            if(queue.isEmpty()) return null;
            // Dequeueing the first node
            Node firstNode = queue.remove(0);
            // Returning the node (a solution to the problem) if it passes the goal test
            if(goalTest(firstNode)) return firstNode;
            // Expanding the node and adding them to the queue according to the search strategy
            else queue = queuingFunction(searchStrategy, queue, firstNode.expand());
        }     
    }

    /**
     * sorts the nodes in the queue after expansion according to the search strategy
     * @param searchStrategy the search strategy used for sorting the nodes inside the queue
     * @param currQueue the current queue of the search problem
     * @param expandedNodes the expanded nodes
     * @return the queue after sorting the nodes
     */
    private ArrayList<Node> queuingFunction(SearchStrategies searchStrategy, ArrayList<Node> currQueue, ArrayList<Node> expandedNodes){
        // checking for the search strategy that should be used for sorting
        switch(searchStrategy) {
            case BF: return breadthFirstSearchStrategy(currQueue, expandedNodes);
            case DF: return depthFirstSearchStrategy(currQueue, expandedNodes);
//            case ID:
            case UC: return uniformCostSearchStrategy(currQueue,expandedNodes);
            case GR: return greedySearchStrategy(currQueue, expandedNodes);
            case AS: return aStarSearchStrategy(currQueue,expandedNodes);
        }
        return currQueue;
    }

    /**
     * sorts a queue based on Last in First Out strategy
     * @param currQueue the current queue of the search problem
     * @param expandedNodes the expanded nodes
     * @return the current queue
     */
    private ArrayList<Node> depthFirstSearchStrategy(ArrayList<Node> currQueue, ArrayList<Node> expandedNodes){
        // Adding the current Queue to the expanded nodes and returning it
        expandedNodes.addAll(currQueue);
        return expandedNodes;
    }

    /**
     * sorts a queue based on First In First Out strategy
     * @param currQueue the current queue of the search problem
     * @param expandedNodes the expanded nodes
     * @return the current queue
     */
    private ArrayList<Node> breadthFirstSearchStrategy(ArrayList<Node> currQueue, ArrayList<Node> expandedNodes){
        // Adding the expanded nodes to the current queue
        currQueue.addAll(expandedNodes);
        return currQueue;
    }

    /**
     * sorts a queue with uniform cost search strategy
     * @param currQueue the current queue of the search problem
     * @param expandedNodes the expanded nodes
     * @return the queue after sorting the nodes with uniform cost search
     */
    private ArrayList<Node> uniformCostSearchStrategy(ArrayList<Node> currQueue, ArrayList<Node> expandedNodes){
        // Adding the expanded nodes to the current queue
        currQueue.addAll(expandedNodes);
        // Sorting the queue with the UC compare method in code.Node class
        Collections.sort(currQueue,Node.NodeCosts.UCSEARCH);
        return currQueue;
    }

    /**
     * sorts a queue with greedy search strategy
     * @param currQueue the current queue of the search problem
     * @param expandedNodes the expanded nodes
     * @return the queue after sorting the nodes with greedy search
     */
    private ArrayList<Node> greedySearchStrategy(ArrayList<Node> currQueue, ArrayList<Node> expandedNodes){
        // Adding the expanded nodes to the current queue
        currQueue.addAll(expandedNodes);
        // Sorting the queue with the Greedy compare method in code.Node class
        Collections.sort(currQueue,Node.NodeCosts.GRSEARCH);
        return currQueue;
    }

    /**
     * sorts a queue with A* search strategy
     * @param currQueue the current queue of the search problem
     * @param expandedNodes the expanded nodes
     * @return the queue after sorting the nodes with A* search
     */
    private ArrayList<Node> aStarSearchStrategy(ArrayList<Node> currQueue, ArrayList<Node> expandedNodes){
        // Adding the expanded nodes to the current queue
        currQueue.addAll(expandedNodes);
        // Sorting the queue with the A* compare method in code.Node class
        Collections.sort(currQueue,Node.NodeCosts.ASTARSEARCH);
        return currQueue;
    }

    /**
     * performs the Depth Limited Search algorithm
     * @param current the current code.Node of the search problem
     * @param depth the depth of the search problem
     * @return a solution if found or a failure
     */
    private Node dls(Node current, int depth){
        // Checking if the current node passes the goal test
        if(goalTest(current)) return current;
        // Checking if the depth is greater than 0
        if(depth > 0){
            // Expanding the current node
//            ArrayList<code.Node> expandedNodes = expandNode(current);
            ArrayList<Node> expandedNodes = current.expand();
            // Iterating over the expanded nodes
            for(Node node : expandedNodes){
                // Checking if the node passes the goal test
                if(goalTest(node)) return node;
                // Recursively calling the DLS algorithm
                Node result = dls(node, depth - 1);
                // Checking if the result is not null
                if(result != null) return result;
            }
        }
        // Returning null if the depth is 0 or the current node does not pass the goal test
        return null;
    }

    /**
     * performs the Iterative Deepening Search which calls the DLS algorithm.
     * @param root the root node of the search problem
     * @return null however, this will loop on forever
     */
    private Node ids(Node root){
        // Initializing the depth to 0 then if no solution increment depth by 1 until a solution is found
        int depth = 0;
        // Iterating till returning a solution or a failure
        while(true){
            // Calling the DLS algorithm
            Node result = dls(root, depth);
            // If the result is not null then returning the result
            if(result != null) return result;
            // else incrementing the depth
            depth++;
        }
    }
}

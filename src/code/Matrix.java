package code;

public class Matrix extends GenericSearchProblem{
    @Override
    public boolean goalTest(Node currNode) {

        int notKilledMutatedAgents = notKilledMutatedAgentsCount(currNode);
        // Checking that all the hostages are either saved or died and the mutated agents are killed
        // and Neo is at the telephone booth and does not carry any hostages
        System.out.println("" + currNode.grid.getNumberOfHostages());
        System.out.println("" + currNode.grid.saved);
        System.out.println("" + currNode.grid.deaths);
        System.out.println("" + currNode.grid.atTB());
        System.out.println("" + currNode.grid.carriedHostages.size());
        System.out.println("" + notKilledMutatedAgents);
        System.out.println();

        return (currNode.grid.getNumberOfHostages() - (currNode.grid.saved + currNode.grid.deaths) == 0)
                && currNode.grid.atTB() && currNode.grid.carriedHostages.size() == 0 && notKilledMutatedAgents == 0;
    }

//    @Override
//    public ArrayList<code.Node> expandNode(code.Node currNode) {
//        return null;
//    }

    /**
     * calculates a heuristic of the matrix search problem (the maximum manhattan distance between Neo and the hostages)
     * @param n a node in the search problem to get the state
     * @return the maximum distance
     */
    static int heuristicOne(Node n){
        byte[][] gridPos = n.grid.grid;
        // Setting the maximum distance to zero
        int maxDistance = 0;
        // Iterating over the grid
        for(int i = 0; i < gridPos.length; i++){
            for (int j = 0; j < gridPos[i].length; j++) {
                // Checking if the current position has a hostage (either living or mutated)
                    if(gridPos[i][j] > 0 && gridPos[i][j] < 102){
                        // Calculating the manhattan distance between Neo and the hostage
                        int currDistance = Math.abs(i - n.grid.NeoX) + Math.abs(j - n.grid.NeoY);
                        // Getting the maximum of the calculated distance and the maximum distance so far
                        maxDistance = Math.max(maxDistance, currDistance);
                    }
            }
        }
        // returning the maximum distance
        return maxDistance;
    }

    /**
     * calculates a heuristic of the matrix search problem
     * (calculates the number of hostages that are not rescued yet or not killed yet)
     * @param n a node in the search problem to get the state
     * @return the un-rescued hostages and the un-killed mutated agents
     */
    static int heuristicTwo(Node n){
        return  n.grid.getNumberOfHostages() - (n.grid.saved + n.grid.deaths) + notKilledMutatedAgentsCount(n);
    }

    /**
     * calculates the number of un-killed mutated agents
     * @param n a node in the search problem to get the state
     * @return the number of un-killed mutated agents
     */
    static int notKilledMutatedAgentsCount(Node n){
        // Initializing the number of un-killed mutated agents
        int notKilledMutatedAgents = 0;
        // Iterating over the grid
//        System.out.println(n.grid);
        for(int i = 0;i < n.grid.grid.length; i++){
            for(int j = 0;j < n.grid.grid[i].length;j++){
                // Incrementing the number of un-killed mutated agents if there is one in the grid
                notKilledMutatedAgents =  n.grid.grid[i][j] == 101?notKilledMutatedAgents+1:notKilledMutatedAgents;
            }
        }
        // Returning the value
        return notKilledMutatedAgents;
    }

    /**
     * generates a random grid of the search problem
     * @return the random grid inside a string
     */
    public static String genGrid(){
        Grid2 grid = new Grid2();
        return grid.toString();
    }

    /**
     * solves the matrix search problem
     * @param grid represents the state of the problem
     * @param searchStrategy the search strategy that should be used
     * @param visualize a boolean value to choose to visualize the grid in the console or not
     * @return a solution of the problem as a string
     */
    public static String solve(String grid, String searchStrategy, boolean visualize){
        // PROBLEMS !!!!!
        //1. Handling the initialization if the parent and the action are NULL !!!!
        //2. Handing the heuristic choice inside the node class
        SearchStrategies searchStrategyEnum;
        // checking the search strategy is used to solve the matrix problem
        switch (searchStrategy){
            case "BF": searchStrategyEnum = SearchStrategies.BF;break;
            case "DF": searchStrategyEnum = SearchStrategies.DF;break;
            case "UC": searchStrategyEnum = SearchStrategies.UC;break;
            case "GR1":
            case "GR2":
                searchStrategyEnum = SearchStrategies.GR;break;
            case "AS1":
            case "AS2":
                searchStrategyEnum = SearchStrategies.AS;break;
            default: searchStrategyEnum = null;
        }
        // Initializing the grid object
        Grid2 problem = new Grid2(grid);
        // Initializing a node representing the initial state of the problem
        Node rootNode;
        // Checking for the search heuristic to change the node values according to it
        if(searchStrategy.equals("GR1") || searchStrategy.equals("AS1"))
            rootNode = new Node(problem, null, null, (byte) 1);
        else if(searchStrategy.equals("GR2") || searchStrategy.equals("AS2"))
            rootNode = new Node(problem, null, null, (byte) 2);
        else
            rootNode = new Node(problem, null, null, (byte) 0);
        // visualize the grid if the boolean is true
        if(visualize) problem.visualize();

        // Calling the method generic Search to find a solution of the problem
        Matrix m = new Matrix();
        Node solution = m.genericSearch(rootNode, searchStrategyEnum);
        // Printing the solution of the problem needed
        String solutionString = solution == null?"No Solution":printGoalStateInfo(solution);
        // Returning the solution of the problem
        System.out.println(""+ solutionString);
        return solutionString;
    }

    /**
     * prints the information of the goal state (actions, deaths, number of kills, number of nodes expanded)
     * @param goalState the node that passed the goal state of the problem
     * @return the information inside a string
     */
    private static String printGoalStateInfo(Node goalState){
        String results = printActionsToGoalState(goalState, true) + ";" +
                goalState.grid.deaths + ";" + goalState.grid.kills + ";" +numberOfExpansionNodes(goalState);
        return results;
    }

    /**
     * prints recursively the actions that led to the goal state
     * @param n the goal state
     * @param first if it is the first time to call the method (to either print a comma ',' or not)
     * @return the actions inside a string
     */
    private static String printActionsToGoalState(Node n,boolean first){
        // Returning an empty string if the node is null (the base case)
        if(n == null) return "";
        // Initializing a string for the action to map it from the enum to the string
        String op = "";
        // Checking for the operator of the node to map it to the proper string
        switch (n.operator){
            case FLY: op = "fly";break;
            case LEFT:op = "left";break;
            case UP: op = "up";break;
            case DOWN: op = "down";break;
            case CARRY: op = "carry";break;
            case DROP: op = "drop";break;
            case KILL: op = "kill";break;
            case RIGHT: op = "right";break;
            case TAKEPILL: op = "takePill";break;
        }
        // Returning the operator of the node added with the actions of their parents and ancestors
        return printActionsToGoalState(n.parent,false) + op + (first?"":",");
    }

    /**
     * counts the number of the chosen expanded nodes recursively
     * @param goalState the goal state
     * @return the number of the expanded nodes
     */
    private static int numberOfExpansionNodes(Node goalState){
        // Returning 1 if the node is null (the base case)
        if(goalState == null) return 1;
        // Incrementing one to the total number of the expanded nodes of its parent
        return 1 + numberOfExpansionNodes(goalState.parent);
    }
}

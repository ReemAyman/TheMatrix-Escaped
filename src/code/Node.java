package code;

import java.util.ArrayList;
import java.util.Comparator;

public class Node {
	public Grid2 grid; // state info
	public Node parent = null;
	public Actions operator = null;
	public int depth = 0;	//depth in the search tree
	public int pathCost;	//cost of getting to this node from the root
	public byte heuristicFn =0; //this should be passed as a constructor parameter
	public int hr; // heuristic value for this node

	
	
	//Reem's part (enum for overriding the compare method to sort the nodes in Uniform Cost, Greedy, and A* Search)
     //just a dummy value for the path cost till finalized
    enum NodeCosts implements Comparator<Node> {
        UCSEARCH{
            @Override
            public int compare(Node o1, Node o2) {
                return o1.pathCost < o2.pathCost?-1:1;
            }
        },
        GRSEARCH{
            @Override
            public int compare(Node o1, Node o2) {
                return o1.hr < o2.hr?-1:1;
            }
        },
        ASTARSEARCH{
            @Override
            public int compare(Node o1, Node o2) {
                return o1.pathCost + o1.hr < o2.pathCost + o2.hr?-1:1;
            }
        },
    }

	public Node(Grid2 g, Node p, Actions act, byte heuristicFn) {
		this.grid = g;	//the grid representing the current state (precept) 
		this.parent = p;  
		this.operator = act;	//the operator that when applied to the parent generated this node
		this.heuristicFn=heuristicFn;
		if (parent!=null) {
//			Initializing  the node depth and path cost incrementally based on the parent 
//			where the cost of the node is that of the parent + action required to get from parent to this node
		this.depth = p.depth + 1;
		this.pathCost=p.pathCost+act.getCost();}
		
		else {
			this.depth = 0;
			this.pathCost=0;
		}
		
		switch(this.heuristicFn) {
		case 1: this.hr=this.heuristicOne();break;
		case 2: this.hr=this.heuristicTwo();break;
		default: this.hr=1;
		}
	}
    
	public Node(Grid2 g, Node p, Actions act) {
		this.grid = g;	//the grid representing the current state (precept) 
		this.parent = p;  
		this.operator = act;	//the operator that when applied to the parent generated this node
		
		if (parent!=null) {
//			Initializing  the node depth and path cost incrementally based on the parent 
//			where the cost of the node is that of the parent + action required to get from parent to this node
		this.depth = p.depth + 1;
		this.pathCost=p.pathCost+act.getCost();
		this.heuristicFn=p.heuristicFn;}
		
		else {
			this.depth = 0;
			this.pathCost=0;
		}
		
		switch(this.heuristicFn) {
		case 1: this.hr=this.heuristicOne();break;
		case 2: this.hr=this.heuristicTwo();break;
		default: this.hr=1;
		}
	}

	
    /**
     * generates the children of the node for all possible legal actions
     * @param
     * @return an ArrayList of children of this node
     */
	public ArrayList<Node> expand() {
		ArrayList<Actions> validActions = new ArrayList<Actions>();

		ArrayList<Node> children = new ArrayList<Node>();
//		based on the current location and the state of the grid, this section of the code determines the valid actions
		if (grid.atHostage()) {
			validActions.add(Actions.CARRY);
		}
		if (grid.atTB() & grid.carriedHostages.size() > 0) { // can only drop if there are carried hostages to avoid
			// repeated states
			validActions.add(Actions.DROP);
		}
		if (grid.atPill()) {
			validActions.add(Actions.TAKEPILL);
		}
		// making sure Neo does not go to an adjacent cell where an agent
		// exists.
		System.out.println("at Edge"+ grid.atEdge());

		switch (grid.atEdge()) {
			case 1:
				validActions.add(Actions.DOWN);
				validActions.add(Actions.LEFT);
				validActions.add(Actions.RIGHT); // north edge
				break;
			case 2:
				validActions.add(Actions.DOWN);
				validActions.add(Actions.LEFT); // northeast corner
				break;
			case 3:
				validActions.add(Actions.UP);
				validActions.add(Actions.DOWN);
				validActions.add(Actions.LEFT); // eastern edge
				break;
			case 4:
				validActions.add(Actions.UP);
				validActions.add(Actions.LEFT); // southeast corner
				break;
			case 5:
				validActions.add(Actions.UP);
				validActions.add(Actions.LEFT);
				validActions.add(Actions.RIGHT); // south edge
				break;
			case 6:
				validActions.add(Actions.UP);
				validActions.add(Actions.RIGHT); // southwest corner
				break;
			case 7:
				validActions.add(Actions.UP);
				validActions.add(Actions.DOWN);
				validActions.add(Actions.RIGHT);
				break;
			// west edge
			case 8:
				validActions.add(Actions.DOWN);
				validActions.add(Actions.RIGHT); // northwest corner
				break;
			case 0:
				validActions.add(Actions.UP);
				validActions.add(Actions.DOWN);
				validActions.add(Actions.LEFT);
				validActions.add(Actions.RIGHT); // not edge
				break;
		}

		// CHECK PAD TO ADD FLYING
		if (grid.atPad()) {
			validActions.add(Actions.FLY);
		}

		if (grid.atSmith().contains(true)) { // adjacent not exactly at
			if (!grid.atHostage() & grid.grid[grid.NeoY][grid.NeoX] > 97) { 
				//handling the case where the hostage is about to die. 
				//In this case Neo has to move and cannot stay and kill an adjacent agent.
				validActions.add(Actions.KILL);
			}
			ArrayList<Boolean> directions = grid.atSmith();
			if (directions.get(0)) {
				validActions.remove(Actions.UP); // agent @ north
			}
			if (directions.get(1)) {
				validActions.remove(Actions.RIGHT); // agent @ east
			}
			if (directions.get(2)) {
				validActions.remove(Actions.DOWN); // agent @ south
			}
			if (directions.get(3)) {
				validActions.remove(Actions.LEFT); // agent @ west
			}
		}

		System.out.println(""+ validActions);
//		this section creates the children of this nodes with updated versions of the grid
		// for each action:
		// 1. update grid
		// 2. create child node with new grid after performing the action
		for (Actions act : validActions) {
			// check hash values of grids of parents
			grid.update(act);
			Node child = new Node(grid, this, act);
			children.add(child);
		}
		return children;
	}
	
	public void setHeuristicFn(byte choice) {
		this.heuristicFn=choice;
		switch(this.heuristicFn) {
		case 1: this.hr=this.heuristicOne();break;
		case 2: this.hr=this.heuristicTwo();break;
		default: this.hr=1;
		}
	}
	
	/**
     * calculates a heuristic of the matrix search problem (the maximum manhattan distance between Neo and the hostages)
     * @return the maximum distance
     */
    int heuristicOne(){
        byte[][] gridPos = this.grid.grid;
        // Setting the maximum distance to zero
        int maxDistance = 0;
        // Iterating over the grid
        for(int i = 0; i < gridPos.length; i++){
            for (int j = 0; j < gridPos[i].length; j++) {
                // Checking if the current position has a hostage (either living or mutated)
                    if(gridPos[i][j] > 0 && gridPos[i][j] < 102){
                        // Calculating the manhattan distance between Neo and the hostage
                        int currDistance = Math.abs(i - this.grid.NeoX) + Math.abs(j - this.grid.NeoY);
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
     * @return the un-rescued hostages and the un-killed mutated agents
     */
    int heuristicTwo(){
        return  this.grid.getNumberOfHostages() - (this.grid.saved + this.grid.deaths) + this.notKilledMutatedAgentsCount();
    }
    
    /**
     * calculates the number of un-killed mutated agents
     * @return the number of un-killed mutated agents
     */
    int notKilledMutatedAgentsCount(){
        // Initializing the number of un-killed mutated agents
        int notKilledMutatedAgents = 0;
        // Iterating over the grid
        for(int i = 0;i < this.grid.grid.length; i++){
            for(int j = 0;j < this.grid.grid[i].length;j++){
                // Incrementing the number of un-killed mutated agents if there is one in the grid
                notKilledMutatedAgents =  this.grid.grid[i][j] == 101?notKilledMutatedAgents+1:notKilledMutatedAgents;
            }
        }
        // Returning the value
        return notKilledMutatedAgents;
    }
    

}

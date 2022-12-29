package code;

import java.util.Comparator;

public enum Actions {
    UP(2), DOWN(2), LEFT(2), RIGHT(2), CARRY(0), DROP(0), TAKEPILL(1), KILL(5), FLY(1);

	private int cost;

	Actions(int cost) {
		// TODO Auto-generated constructor stub
		this.cost=cost;
	}

	public int getCost() {
		return cost;
	}
}

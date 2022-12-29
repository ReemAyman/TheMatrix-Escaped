package code;

public abstract class Tests
{
	public static void mainTest(boolean printMetadata)
	{
		Grid2 g = new Grid2("5,5;2;3,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.UP);
		g.visualize();
		g.metadata(printMetadata);
	}
	
	public static void testMoveUp(boolean printMetadata)
	{
		Grid2 g = new Grid2("5,5;2;3,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.UP);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should move up one cell
		// Damages should be increased
	}

	public static void testMoveDown(boolean printMetadata)
	{
		Grid2 g = new Grid2("5,5;2;3,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.DOWN);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should move down one cell
		// Damages should be increased
	}

	public static void testMoveRight(boolean printMetadata)
	{
		Grid2 g = new Grid2("5,5;2;3,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.RIGHT);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should move right one cell
		// Damages should be increased
	}

	public static void testMoveLeft(boolean printMetadata)
	{
		Grid2 g = new Grid2("5,5;2;3,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.LEFT);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should move left one cell
		// Damages should be increased
	}

	public static void testFly(boolean printMetadata)
	{
		Grid2 g = new Grid2("5,5;2;4,3;2,1;2,0,0,4,0,3,0,1;3,1,3,2;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.RIGHT);
		g.update(Actions.FLY);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should move to the correct targetPad
		// Damages should be increased
	}

	public static void testKill(boolean printMetadata)
	{
		Grid2 g = new Grid2(
				"5,5;2;3,3;2,1;2,0,0,4,0,3,0,1,3,2,2,2,2,3,2,4,3,4,4,3;3,1;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.KILL);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should kill all agents in adj cells not diagonal ones
		// Neo's Damage should increase by 20
		// Hostages damages should increase by 2
		// Kills should increase
	}

	public static void testCarry(boolean printMetadata)
	{
		Grid2 g = new Grid2(
				"5,5;2;3,3;2,1;2,0,0,4,0,3,0,1,3,2,2,2,2,3,2,4,3,4,4,3;3,1;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.DOWN);
		g.update(Actions.LEFT);
		g.update(Actions.LEFT);
		g.update(Actions.CARRY);
		g.update(Actions.LEFT);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should be in the same cell as the hostage
		// Neo's should carry the hostage
		// The hostage should be removed from the grid
		// Damages should be increased
	}

	public static void testTakePill(boolean printMetadata)
	{
		Grid2 g = new Grid2(
				"5,5;2;3,3;2,1;2,0,0,4,0,3,0,1,3,2,2,2,2,3,2,4,3,4,4,3;3,1;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.DOWN);
		g.update(Actions.LEFT);
		g.update(Actions.LEFT);
		g.update(Actions.CARRY);
		g.update(Actions.UP);
		g.update(Actions.TAKEPILL);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should be in the same cell as the pill
		// Neo's damage should decrease
		// CarriedHostages' damages should decrease
		// Hostages' damages should decrease
		// Pill should be removed
	}

	public static void testDrop(boolean printMetadata)
	{
		Grid2 g = new Grid2(
				"5,5;2;3,3;2,1;2,0,0,4,0,3,0,1,3,2,2,2,2,3,2,4,3,4,4,3;3,1;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,46,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.DOWN);
		g.update(Actions.LEFT);
		g.update(Actions.LEFT);
		g.update(Actions.CARRY);
		g.update(Actions.UP);
		g.update(Actions.TAKEPILL);
		g.update(Actions.UP);
		g.update(Actions.DROP);
		g.visualize();
		g.metadata(printMetadata);
		// Neo should be in the same cell as the TB
		// Neo's damage should increase
		// Living CarriedHostages' should be removed as saved
		// Hostages' damages should decrease
	}
	
	public static void testDropWhileOtherHostageTurnsIntoAgent(boolean printMetadata)
	{
		Grid2 g = new Grid2(
				"5,5;2;3,3;2,1;2,0,0,4,0,3,0,1,3,2,2,2,2,3,2,4,3,4,4,3;3,1;4,4,1,3,1,3,4,4,4,0,1,2,1,2,4,0;0,0,86,4,1,22");
		g.visualize();
		g.metadata(printMetadata);
		g.update(Actions.DOWN);
		g.update(Actions.LEFT);
		g.update(Actions.LEFT);
		g.update(Actions.CARRY);
		g.update(Actions.UP);
		g.update(Actions.UP);
		g.update(Actions.DROP);
		g.visualize();
		g.metadata(printMetadata);
		// Same as testDrop but the deaths should increase
		// The died one should be an agent
	}
}

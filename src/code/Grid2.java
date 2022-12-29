package code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;

public class Grid2 {
	int height, width, availableCells;
	byte[][] grid; // default values are 255
	int c; // Neo's maximum carrying capacity
	ArrayList<Byte> carriedHostages; // contains damage levels of hostages
	byte deaths;
	byte kills;
	byte saved;
	byte orgHostages;
	int NeoX;
	int NeoY;
	byte neoDamage;


	HashMap<String, String> generatedPositions;
	
	public Grid2(String grid)
	{
		String[] arrGrid = grid.split(";");
		this.deaths = 0;
		this.kills = 0;
		this.saved = 0;
		this.orgHostages = 0;
		
		// M,N;
		this.width = Integer.parseInt(arrGrid[0].charAt(0) + "");
		this.height = Integer.parseInt(arrGrid[0].charAt(2) + "");
		this.grid = new byte[height][width];
				
		// C;
		this.c = Integer.parseInt(arrGrid[1]);
		
		// NeoX,NeoY;
		this.NeoX = Integer.parseInt(arrGrid[2].charAt(0) + "");
		this.NeoY = Integer.parseInt(arrGrid[2].charAt(2) + "");
		this.neoDamage = 1;
		
		// TelephoneX,TelehoneY;
		int tbX = Integer.parseInt(arrGrid[3].charAt(0) + "");
		int tbY = Integer.parseInt(arrGrid[3].charAt(2) + "");
		this.grid[tbX][tbY] = 103;
		
		// AgentX1,AgentY1, ...,AgentXk,AgentYk;
		String temp[] = arrGrid[4].split(",");
		for(int i = 0; i < temp.length; i += 2)
		{
			int x = Integer.parseInt(temp[i]);
			int y = Integer.parseInt(temp[i + 1]);
			this.grid[x][y] = 102;
		}
		
		// PillX1,PillY1, ...,PillXg,PillYg;
		temp = arrGrid[5].split(",");
		for(int i = 0; i < temp.length; i += 2)
		{
			int x = Integer.parseInt(temp[i]);
			int y = Integer.parseInt(temp[i + 1]);
			this.grid[x][y] = 104;
		}
		
		// StartPadX1,StartPadY1,FinishPadX1,FinishPadY1,...,
		// StartPadXl,StartPadYl,FinishPadXl,FinishPadYl;
		byte currentPad = -1;
		temp = arrGrid[6].split(",");
		for(int i = 0; i < temp.length; i += 8)
		{
			int startPadX = Integer.parseInt(temp[i]);
			int startPadY = Integer.parseInt(temp[i + 1]);
			int finishPadX = Integer.parseInt(temp[i + 2]);
			int finishPadY = Integer.parseInt(temp[i + 3]);
			this.grid[startPadX][startPadY] = currentPad;
			this.grid[finishPadX][finishPadY] = currentPad;
			currentPad--;
		}
		
		// HostageX1,HostageY1,HostageDamage1, ...,HostageXw,HostageYw,HostageDamagew
		temp = arrGrid[7].split(",");
		for(int i = 0; i < temp.length; i += 3)
		{
			int x = Integer.parseInt(temp[i]);
			int y = Integer.parseInt(temp[i + 1]);
			int damage = Integer.parseInt(temp[i + 2]);
			this.grid[x][y] = (byte) damage;
			this.orgHostages++;
		}
		
		this.carriedHostages = new ArrayList<Byte>();
	}
	
	public Grid2() {
		int minWidth = 5, minHeight = 5, maxWidth = 15, maxHeight = 15;
		this.height = getRandomNumber(minHeight, maxHeight);
		this.width = getRandomNumber(minWidth, maxWidth);
		this.height = 5; // For testing only, to be removed
		this.width = 5; // For testing only, to be removed
		this.grid = new byte[height][width];
		this.generatedPositions = new HashMap<>();
		this.carriedHostages = new ArrayList<Byte>();
		this.availableCells = (width * height) - 2; // The 2 cells are Neo & TB
		
		this.deaths = 0;
		this.kills = 0;
		this.saved = 0;
		this.orgHostages = 0;
		
		// generateNeo
		byte neo[] = getRandomPosition();
		this.NeoX = neo[0];
		this.NeoY = neo[1];
		neoDamage = 1;
		grid[NeoX][NeoY] = neoDamage; 
	}

	private byte getRandomNumber(int min, int max) {
		return (byte) (Math.floor(Math.random() * (max - min + 1) + min));
	}

	private byte[] getRandomPosition() {
		byte x, y;
		String tempPos;
		do {
			x = getRandomNumber(0, height - 1);
			y = getRandomNumber(0, width - 1);
			tempPos = x + "," + y;
		} while (generatedPositions.containsKey(tempPos));
		generatedPositions.put(tempPos, "");
		byte position[] = { x, y };
		return position;
	}

	public void genGrid() {
		byte tempPosition[];
		c = getRandomNumber(1, 4);

		// generate telephoneBooth
		tempPosition = getRandomPosition();
		grid[tempPosition[0]][tempPosition[1]] = 103;
		availableCells -= 1;
		
		byte numOfHostages = getRandomNumber(5, 10);
		this.orgHostages = numOfHostages;
		for(int i = 0; i < numOfHostages; i++)
		{
			// generateHostages
			tempPosition = getRandomPosition();
			grid[tempPosition[0]][tempPosition[1]] = getRandomNumber(1, 100);
			
			// generatePills
			tempPosition = getRandomPosition();
			grid[tempPosition[0]][tempPosition[1]] = 104;
			
			availableCells -= 2;
		}

		// generateAgents
		byte numOfAgents = getRandomNumber(1, availableCells - 2); // To make sure there is at least 1 startPad & 1 finishPad
		for(int i = 0; i < numOfAgents; i++)
		{
			tempPosition = getRandomPosition();
			grid[tempPosition[0]][tempPosition[1]] = 102;
			availableCells -= 1;
		}

		// generatePads
		byte numOfPads = getRandomNumber(1, availableCells / 2);
		byte currentPad = -1;
		for(int i = 0; i < numOfPads; i++, currentPad--)
		{
			byte[] padFly = getRandomPosition();
			byte[] padDrop = getRandomPosition();
			grid[padFly[0]][padFly[1]] = currentPad;
			grid[padDrop[0]][padDrop[1]] = currentPad;
			availableCells -= 2;
		}
	}

	private void printSeparator() {
		for (int i = 0; i <= width * 13; i++)
			System.out.print("-");
		System.out.println();
	}

	public void visualize() {
		printSeparator();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				String content = this.translate(i, j);
				System.out.printf("| %1$-10s ", content != null ? content : ".....");
			}
			System.out.println("|");
			printSeparator();
		}

	}
	
	

	public String translate(int row, int col) {
		byte cell = grid[row][col];
		String content = "";
		if (NeoX == row && NeoY == col) {
			content += "N, ";
		}

		switch (cell) {
		case 101:
			return content + "A";
		case 102:
			return content + "A";
		case 103:
			return content + "TB";
		case 104:
			return content + "P";
		}
		if (cell == 0) {
			return content + ".....";
		}
		
		if (cell > 0 && cell < 101) {
			return content + "H" + "(" + cell + ")";
		}
		if (cell >= 250) {
			if (content.length() < 1) {
				return null;
			} else {
				return content;
			}
		}
		if(cell < 0)
		{
			for(int i = 0; i < grid.length; i++)
			{
				for(int j = 0; j < grid[i].length; j++)
				{
					if(grid[i][j] == cell && i != row && j != col)
						return content + "F(" + i + "," + j + ")";
				}
			}
			
			
		}
		return content; 
	}
	
	private ArrayList<String> getLocation(String name)
	{
		ArrayList<String> result = new ArrayList<String>();
		if(name.equals("tb"))
		{
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
					if(grid[i][j] == 103)
						result.add(i + "," + j);
		}
		else if(name.equals("agent"))
		{
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
					if(grid[i][j] == 102)
						result.add(i + "," + j);
		}
		else if(name.equals("pill"))
		{
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
					if(grid[i][j] == 104)
						result.add(i + "," + j);
		}
		else if(name.equals("pad"))
		{
			Hashtable<Byte, String> visitedPads = new Hashtable<Byte, String>();
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
				{
					byte padId = 0;
					String startPad = "" ;
					String finishPad = "" ;
					if(grid[i][j] < 0 && !visitedPads.containsKey(grid[i][j]))
					{
						padId = grid[i][j];
						startPad = i + "," + j;
						for(int k = 0; k < height; k++)
							for(int l = 0; l < width; l++)
								if(grid[k][l] == padId && k != i && l != j)
									finishPad = k + "," + l;
						visitedPads.put(padId, "");
						result.add(startPad + "," + finishPad + "," + finishPad + "," + startPad);
					}
				}
		}
		else if(name.equals("hostage"))
		{
			for(int i = 0; i < height; i++)
				for(int j = 0; j < width; j++)
					if(grid[i][j] >= 1 && grid[i][j] <= 100)
						result.add(i + "," + j + "," + grid[i][j]);
		}
		return result;
	}
	

	public String toString() {
		String s = "";
		s += width + "," + height + ";";
		s += c + ";";
		s += NeoX + "," + NeoY + ";";
		s += getLocation("tb") + ";";
		s += getLocation("agent") + ";";
		s += getLocation("pill") + ";";
		s += getLocation("pad") + ";";
		s += getLocation("hostage");
		return s.replace("[", "").replace("]", "").replace(" ", "");
	}

	// ===> Salma's methods: <===
	public boolean atPill() {
		if (grid[NeoY][NeoX] == 104) {
			return true;
		}
		return false;
	}

	public byte atEdge() {
		int maxY = grid.length - 1;
		int maxX = grid[0].length - 1;

		if (NeoY == 0) {
			if (NeoX == 0) {
				return 8;// northwest corner
			}
			if (NeoX == maxX) {
				return 2;// northeast corner
			}
			return 1;// north edge
		}
		if (NeoY == maxY) {
			if (NeoX == 0) {
				return 6; // southwest corner
			}
			if (NeoX == maxX) {
				return 4; // southeast corner
			}
			return 5; // south edge
		}
		if (NeoX == 0) {
			return 7; // west edge
		}
		if (NeoX == maxX) {
			return 3; // eastern edge
		}
		return 0;
//            case 1:
//                return 1; //north edge
//            case 2:
//                return 2; //northeast corner
//            case 3:
//                return 3; //eastern edge
//            case 4:
//                return 4; //southeast corner
//            case 5:
//                return 5; //south edge
//            case 6:
//                return 6; //southwest corner
//            case 7:
//                return 7; //west edge
//            case 8:
//                return 8; //northwest corner
//            default:
//                return 0; //not edge
	}

	public boolean atPad() {
//		if (neo is at a pad coordinate) {
//			return coordinates of the other pad in the pair;
//		}
//		else {
//			return (-1,-1) or null;
//		}
		if (grid[NeoY][NeoX] > 104 & grid[NeoY][NeoX] < 255) {
			return true;
		}
		return false;
	}

	public boolean atTB() {
		System.out.println("neoX"+NeoX);
		System.out.println("neoY"+NeoY);

		if (grid[NeoY][NeoX] == 103) {
			return true;
		}
		return false;
	}

	public boolean atHostage() {
		if ((grid[NeoY][NeoX] >= 0 & grid[NeoY][NeoX] <= 100) && carriedHostages.size() < c) {
			return true;
		}
		return false;
	}

	public ArrayList<Boolean> atSmith() { // adjacent not exactly at
		int maxY = grid.length - 1;
		int maxX = grid[0].length - 1;
//		System.out.println("here" + maxX);

		Boolean[] directions = new Boolean[4];
		if (NeoY > 0 && (grid[NeoY - 1][NeoX] == 101 || grid[NeoY - 1][NeoX] == 102)) {
			directions[0] = true;// agent @ north
//			System.out.println("cond1" + (grid[NeoY - 1][NeoX] == 101 || grid[NeoY - 1][NeoX] == 102));
		}
		if (NeoX < maxX && (grid[NeoY][NeoX + 1] == 101 || grid[NeoY][NeoX + 1] == 102)) {
			directions[1] = true;// agent @ east
//			System.out.println("cond2" + (grid[NeoY][NeoX + 1] == 101 || grid[NeoY][NeoX + 1] == 102));

		}
		if (NeoY < maxY && (grid[NeoY + 1][NeoX] == 101 || grid[NeoY + 1][NeoX] == 102)) {
			directions[2] = true;// agent @ south
//			System.out.println("cond3" + (grid[NeoY + 1][NeoX] == 101 || grid[NeoY + 1][NeoX] == 102));

		}
		if (NeoX > 0 && (grid[NeoY][NeoX - 1] == 101 || grid[NeoY][NeoX - 1] == 102)) {
			directions[3] = true;// agent @ west
//			System.out.println("cond4" + (grid[NeoY][NeoX - 1] == 101 || grid[NeoY][NeoX - 1] == 102));
		}
		ArrayList<Boolean> dirs = new ArrayList<Boolean>();
//		Collections.fill(dirs, Boolean.FALSE);
		for (Boolean d : directions) {
			dirs.add(d);
		}

		return dirs;
	}

	public ArrayList<int[]> getAdjCells(int x, int y)
	{
		ArrayList<int[]> cells = new ArrayList<int[]>();

		if(y - 1 >= 0)
			cells.add(new int[] {x, y - 1});

		if(y + 1 < height)
			cells.add(new int[] {x, y + 1});
			
		if(x - 1 >= 0)
			cells.add(new int[] {x - 1, y});
			
		if(x + 1 < width)
			cells.add(new int[] {x + 1, y});
		return cells;
	}
	
	private boolean isTelephoneBooth(int x, int y)
	{
		return grid[x][y] == 103;
	}
	
	private boolean isAgent(int x, int y)
	{
		return grid[x][y] == 102;
	}
	
	private boolean isHostageAgent(int x, int y)
	{
		return grid[x][y] == 101;
	}
	
	private boolean isHostage(int x, int y)
	{
		return (grid[x][y] >= 1 && grid[x][y] <= 100);
	}
	
	private boolean isPill(int x, int y)
	{
		return grid[x][y] == 104;
	}
	
	private boolean isPad(int x, int y)
	{
		return grid[x][y] < 0;
	}
	
	private int[] getLandingCell(int x, int y)
	{
		byte padId = grid[NeoX][NeoY];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				if(grid[i][j] == padId && i != x && j != y)
					return new int[] {i, j};
		return new int[] {0, 0};
	}
	
	private void modifyHostageDamage(int i, int j, int amount)
	{
		if(isHostage(i, j))
		{
			int newDamage = this.grid[i][j] + amount;
			this.grid[i][j] = (byte) 
					(newDamage >= 100 ? 101 : // Turns into an agent
						newDamage <= 0 ? 1 :
							newDamage); 
			deaths += newDamage >= 100 ? 1 : 0; // Dies
		}
	}
	
	private void modifyCarriedHostageDamage(int index, int amount)
	{
		int newDamage = this.carriedHostages.get(index) + amount;
		this.carriedHostages.set(index, (byte) 
				(newDamage > 100 ? 100 :
				newDamage < 0 ? 0 :
				newDamage));
		
		deaths += newDamage == 100 ? 1 : 0; 
	}
	
	private void modifyNeoDamage(int amount)
	{
		int newDamage = neoDamage + amount;
		neoDamage = (byte) 
				(newDamage > 100 ? 100 :
				newDamage < 0 ? 0 :
				newDamage);
		// ? What happens if new dies ? 
	}
	
	public void update(Actions action) {
		switch (action)
		{
			case UP:
				NeoY++;
//				NeoX--;
				break;
			
			case DOWN:
				NeoY--;
//				NeoX++;
				break;
			
			case LEFT:
				NeoX--;
				break;
			
			case RIGHT:
				NeoX++;
				break;
				
			case KILL:
				ArrayList<int[]> adjCells = getAdjCells(NeoX, NeoY);
				for(int[] cell : adjCells)
				{
					int x = cell[0];
					int y = cell[1];
					// kill and increment
					if(isAgent(x, y) || isHostageAgent(x, y))
					{
						grid[y][x] = -1;
						kills++;
					}
						
				}
				modifyNeoDamage(20);
				break;

			case TAKEPILL:
				for(int i = 0; i < grid.length; i++)
					for(int j = 0; j < grid[i].length; j++)
						if(isHostage(i, j)) modifyHostageDamage(i, j, -20);
				for(int i = 0; i < carriedHostages.size(); i++)
					modifyCarriedHostageDamage(i, -20);
				modifyNeoDamage(-20);
				this.grid[NeoX][NeoY] = 0; // Remove the pill because it is only taken once
				break;
			
			case FLY:
				int[] newPad = getLandingCell(NeoX, NeoY);
				NeoX = newPad[0];
				NeoY = newPad[1];
				break;
				
			case CARRY:
				this.carriedHostages.add(grid[NeoX][NeoY]); // Carry the hostage
				this.grid[NeoX][NeoY] = 0; // Remove the hostage from the grid
				break;
				
			case DROP:
				for(int damage : carriedHostages)
					if(damage < 100)
						saved++;
				carriedHostages.clear();
				break;
				
			default:
				break;
		}
		
		// ==> General Updates <== //
		if(action != Actions.TAKEPILL)
		{
			int damagePerStep = 2;
			// Increase carriedHostage's damage
			for(int i = 0; i < carriedHostages.size(); i++)
				modifyCarriedHostageDamage(i, damagePerStep);
	
			// Increase the hostages's damage
			for(int i = 0; i < grid.length; i++)
				for(int j = 0; j < grid[i].length; j++)
					modifyHostageDamage(i, j, damagePerStep);
			
			// Increase Neo's damage if it is not kill
			modifyNeoDamage(action == Actions.KILL ? 0 : damagePerStep);
		}		
//		return null;
	}

	private String strNeo()
	{
		return "<" + NeoX + ", " + NeoY + ", " + neoDamage + ", " + carriedHostages + ">"; 
	}
	
	private String strTb()
	{
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				if(isTelephoneBooth(i, j))
					return "(" + i + ", " + j + ")";
		return "whaaaaat";
	}
	
	private String strAgents()
	{
		ArrayList<String> agents = new ArrayList<String>();
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				if(isAgent(i, j))
					agents.add("(" + i + ", " + j + ")");
		return agents.toString();
	}
	
	private String strHostages()
	{
		ArrayList<String> hostages = new ArrayList<String>();
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				if(isHostage(i, j))
					hostages.add("<" + i + ", " + j + ", " + grid[i][j] + ">");
		return hostages.toString();
	}
	
	private String strPills()
	{
		ArrayList<String> pills = new ArrayList<String>();
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				if(isPill(i, j))
					pills.add("(" + i + ", " + j + ")");
		return pills.toString();
	}
	
	private String strPads()
	{
		ArrayList<String> pads = new ArrayList<String>();
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[i].length; j++)
				if(isPad(i, j))
					pads.add("(" + i + ", " + j + ")");
		return pads.toString();
	}
	
	public void metadata(boolean print)
	{
		if(print)
		{
			System.out.println("=========== Metadata ===========");
			System.out.println("Neo: " + strNeo());
			System.out.println("TelephoneBooth: " + strTb());
			System.out.println("Agents: " + strAgents());
			System.out.println("Hostages: " + strHostages());
			System.out.println("Pills: " + strPills());
			System.out.println("Pads: " + strPads());
			System.out.println("Kills: " + kills);
			System.out.println("Deaths: " + deaths);
			System.out.println("Saved: " + saved);
			System.out.println("================================");
		}
	}
	
	// A Method for getting the number of hostages
	public byte getNumberOfHostages(){
		return this.orgHostages;
	}

	// -----------------
	public static void main(String[] args) {
		Tests.mainTest(true);
//	    code.Tests.testMoveUp(true);
//		code.Tests.testMoveDown(true);
//		code.Tests.testMoveLeft(true);
//		code.Tests.testMoveRight(true);
//		code.Tests.testFly(true);
//		code.Tests.testKill(true);
//		code.Tests.testCarry(true);
//		code.Tests.testTakePill(true);
//		code.Tests.testDrop(true);
//		code.Tests.testDropWhileOtherHostageTurnsIntoAgent(true);
	}
	

}
package code;

import java.util.HashMap;

public class Grid { 
    int height, width, availableCells;
    String[][] grid;
    HashMap<String, String> generatedPositions;

    int c;
    byte [] carriedHostages;
    byte NeoX;
    byte NeoY;
    Position neo;
    Position telephoneBooth;
    Position[] agents;
    Hostage[] hostages;
    Position[] pills;
    Position[][] pads;
    

    public Grid() {
        int minWidth = 5, minHeight = 5, maxWidth = 15, maxHeight = 15;
        this.height = getRandomNumber(minHeight, maxHeight);
        this.width = getRandomNumber(minWidth, maxWidth);
        this.height = 5;
        this.width = 5;
        this.grid = new String[height][width];
        this.generatedPositions = new HashMap<>();
        this.availableCells = (width * height) - 2; // The 2 cells are Neo & TB
    }

    private int getRandomNumber(int min, int max) {
        return (int) (Math.floor(Math.random() * (max - min + 1) + min));
    }

    private Position getRandomPosition() {
        int x, y;
        String tempPos;
        do {
            x = getRandomNumber(0, height - 1);
            y = getRandomNumber(0, width - 1);
            tempPos = x + "," + y;
        } while (generatedPositions.containsKey(tempPos));
        generatedPositions.put(tempPos, "");
        return new Position(x, y);
    }

    private void generate(int number, Position[] arr, String name) {
        for (int i = 0; i < number; i++) {
            arr[i] = name.equals("H") ? new Hostage(getRandomPosition(), getRandomNumber(1, 99)) : getRandomPosition();
            grid[arr[i].x][arr[i].y] = name.equals("H") ? name + "(" + ((Hostage) arr[i]).damage + ")" : name;
        }
        availableCells -= number;
    }

    private void generatePads(int number, Position[][] arr, String name) {
        for (int i = 0; i < number; i++) {
            Position startPad = getRandomPosition();
            Position finishPad = getRandomPosition();
            arr[i][0] = startPad;
            arr[i][1] = finishPad;
            grid[startPad.x][startPad.y] = name + "(" + finishPad + ")";
            grid[finishPad.x][finishPad.y] = name + "(" + startPad + ")";
        }
        availableCells -= (number * 2);
    }

    public void genGrid() {
        // generateNeo
        neo = getRandomPosition();
        grid[neo.x][neo.y] = "Neo";
        c = getRandomNumber(1, 4);

        // generate telephoneBooth
        telephoneBooth = getRandomPosition();
        grid[telephoneBooth.x][telephoneBooth.y] = "TB";

        // generateHostages
        int numOfHostages = getRandomNumber(5, 10);
        hostages = new Hostage[numOfHostages];
        generate(numOfHostages, hostages, "H");

        // generatePills
        pills = new Position[numOfHostages];
        generate(numOfHostages, pills, "P");

        // generateAgents
        int numOfAgents = getRandomNumber(1, availableCells - 2); // To make sure there is at least 1 startPad & 1 finishPad
        agents = new Position[numOfAgents];
        generate(numOfAgents, agents, "A");

        // generatePads
        int numOfPads = getRandomNumber(1, availableCells / 2);
        pads = new Position[numOfPads][2];
        generatePads(numOfPads, pads, "Pad");
    }

    private void printSeparator() {
        for (int i = 0; i <= width * 13; i++) System.out.print("-");
        System.out.println();
    }

    public void visualize() {
        printSeparator();
        for (String[] row : grid) {
            for (String column : row) System.out.printf("| %1$-10s ", column != null ? column : ".....");
            System.out.println("|");
            printSeparator();
        }

//		This is only for testing to see if there is any duplicate cells
//		System.out.println("=========== Metadata ===========");
//		System.out.println("Neo: " + neo);
//		System.out.println("TelephoneBooth: " + telephoneBooth);
//		System.out.println("Agents: " + Arrays.deepToString(agents));
//		System.out.println("Hostages: " + Arrays.deepToString(hostages));
//		System.out.println("Pills: " + Arrays.deepToString(pills));
//		System.out.println("StartPads: " + Arrays.deepToString(startPads));
//		System.out.println("FinishPads: " + Arrays.deepToString(finishPads));
//		System.out.println("================================");
    }

    public String toString() {
        String s = "";
        s += width + "," + height + ";";
        s += c + ";";
        s += neo + ";";
        s += telephoneBooth + ";";
        for (int i = 0; i < agents.length; i++)
            s += agents[i] + (i == agents.length - 1 ? ";" : ",");

        for (int i = 0; i < pills.length; i++)
            s += pills[i] + (i == pills.length - 1 ? ";" : ",");

        for (int i = 0; i < pads.length; i++)
            s += pads[i][0] + "," + pads[i][1] + "," + pads[i][1] + "," + pads[i][0] + (i == pads.length - 1 ? ";" : ",");

        for (int i = 0; i < hostages.length; i++)
            s += hostages[i] + (i == hostages.length - 1 ? "" : ",");
        return s;
    }

    // ===> Salma's methods: <===
    public boolean atPill() {
        return false;
    }

    public byte atEdge() {
        switch (3) { //switch on neo's coordinates
            case 1:
                return 1; //north edge
            case 2:
                return 2; //northeast edge
            case 3:
                return 3; //eastern edge
            case 4:
                return 4; //southeast edge
            case 5:
                return 5; //south edge
            case 6:
                return 6; //southwest edge
            case 7:
                return 7; //west edge
            case 8:
                return 8; //northwest edge
            default:
                return 0; //not edge
        }
    }

    public boolean atPad() {
//		if (neo is at a pad coordinate) {
//			return coordinates of the other pad in the pair;
//		}
//		else {
//			return (-1,-1) or null;
//		}
    	return false;
    }

    public boolean atTB() {
        return false;
    }

    public boolean atHostage() {
        return false;
    }

    public int atSmith() { //adjacent not exactly at
    	switch (3) { //switch on neo's coordinates
        case 1:
            return 1; //north edge
        case 2:
            return 2; //northeast edge
        case 3:
            return 3; //eastern edge
        case 4:
            return 4; //southeast edge
        case 5:
            return 5; //south edge
        case 6:
            return 6; //southwest edge
        case 7:
            return 7; //west edge
        case 8:
            return 8; //northwest edge
        default:
            return 0; //not edge
    }
    	}

    public Grid update(Actions act) {
        // TODO Auto-generated method stub
        return null;
    }

    //-----------------
    public static void main(String[] args) {
        Grid g = new Grid();
        g.genGrid();
        g.visualize();
        System.out.println(g);
      
    }


}
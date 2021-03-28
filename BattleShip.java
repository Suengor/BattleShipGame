/*
Project: BattleShip Game
Name: Pang Ho Suen
*/
import java.util.Scanner;
public class BattleShip
{
	private static Scanner input = new Scanner(System.in); //it is the Scanner for user to input
	private static String [][] board = new String [10][10]; //the array to store the numbers
	private static int [] size = {5,4,3,3,2}; //the array to store to store ships size
	private static String [] shipname = {"Carrier", "Battleship", "Destroyer", "Submarine", "Patrol Boat"}; //the array to store ships name
	private static boolean DEBUG = true; //to hide or show the ships location
	private static int x,y; //for user to input numbers
	
	public static void main (final String[] args) {
		createBoard(board); // call the createBoard method
		userinput(); // ask user choice Human , CPU or Exit
	}

	// create the Board
	public static void createBoard(final String[][] board) {
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[0].length; j++)
				board[i][j] = " ."; // print 10x10 " ."
	}

	// show the Board
	public static void showBoard(final String[][] board, final boolean DEBUG) {

		System.out.println("     0 1 2 3 4 5 6 7 8 9  <--x");
		System.out.println("--+--------------------- ");
		for (int i = 0; i < board.length; i++) {
			if (DEBUG == true) // for showing the map
			{
				System.out.print(i + " | ");
				for (int j = 0; j < board[0].length; j++) {

					System.out.print(board[i][j]);
				}

			} else {
				System.out.print(i + " | ");
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j].equals(" S")) // S means there is a ship
					{
						System.out.print(" .");
					} else
						System.out.print(board[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println("^");
		System.out.println("y");
	}

	// this method ask the user to choice being a human to set ships , let cpu
	// randomly set the ships or exit
	// if the user doesn't input the valid input it will ask him input again
	public static void userinput() {
		System.out.print("Battleship Player1 (0 - Human, 1 - CPU, x - Exit): ");
		String player1 = input.nextLine(); // let user input 0,1 or x
		String player2;

		while ((!("0").equals(player1)) && (!("1").equals(player1)) && (!("x").equals(player1))) {
			System.out.print("Please input 0 or 1, x - Exit : "); // ask user input again
			player1 = input.nextLine();
		}
		xExit(player1); // call the exit method
		switch (player1) {
			case "0":
				human(); // Call human method to let player1 to input ships
				showBoard(board, DEBUG);
				do // let player 2 to start the game
				{
					System.out.println("Press Enter for Player2 to start ...");
					player2 = input.nextLine(); // player 2 input
				} while (!(player2.length() == 0)); // check is player 2 input valid
				washboard(); // clear the screen
				DEBUG = false; // to hide ships location
				usershoot(DEBUG); // let player 2 hit the ships
				break;
			default: // cpu randomly set the ships
				CPU(); // call cpu method
				washboard(); // clear the screen
				DEBUG = false; // to hide ships location
				usershoot(DEBUG); // let player 2 hit the ships
		}
	}

	// Create cpuship
	public static void CPU() {
		for (int s = 0; s < size.length; s++) // s is a count of shipsize no.
		{
			int loc;
			String orien;

			if (Math.random() < 0.5) // to define vertical or horizontal
			{
				orien = "0"; // if it is horizontal
				x = (int) (Math.random() * (board.length - size[s])); // give x a random no.
				y = (int) (Math.random() * 10); // give y a random no.
				loc = x * 10 + y;
				while (overlap(board, loc, s, orien)) // check overlap
				{
					x = (int) (Math.random() * (board.length - size[s]));
					y = (int) (Math.random() * 10);
					loc = x * 10 + y;
				}
				for (int i = 0; i < size[s]; i++) {
					board[y][x + i] = " S"; // print S means ship
				}
			} else {
				orien = "1"; // if it is vertical
				x = (int) (Math.random() * 10);
				y = (int) (Math.random() * (board.length - size[s]));
				loc = x * 10 + y;
				while (overlap(board, loc, s, orien)) {
					x = (int) (Math.random() * 10);
					y = (int) (Math.random() * (board.length - size[s]));
					loc = x * 10 + y;
				}
				for (int i = 0; i < size[s]; i++) {
					board[y + i][x] = " S";
				}
			}
			showBoard(board, DEBUG); // show the board
		}
	}

	// check is there any ship are overlapped
	public static boolean overlap(final String[][] baord, final int loc, final int s, final String orien) {
		y = loc % 10;
		x = (loc - y) / 10;
		switch (orien) {
			case "0":
				for (int i = 0; i < size[s]; i++)
					if (board[y][x + i].equals(" S"))
						return true;
				break;
			default:
				for (int i = 0; i < size[s]; i++)
					if (board[y + i][x].equals(" S"))
						return true;
		}
		return false;
	}
	// method for player 2 to hit the ships

	public static void usershoot(final boolean DEBUG) {
		String sloc;
		int loc;
		int hit = 0, lau = 0; // to count hit times and launched times

		while (hit < 17) // cheack game finished
		{
			showBoard(board, DEBUG); // show the board with the ships hidden
			System.out.print("Player2 : Set your missile [XY] , x - Exit: "); // let player 2 to shot or exit
			sloc = input.nextLine(); // get the shooting location from player 2
			xExit(sloc);

			while (isInt(sloc)) // check the sloc
			{
				System.out.print("Error in [XY]! Please input again, x - Exit: "); // let player 2 to input again when
																					// he input invalid
				sloc = input.nextLine(); // get new sloc
			}
			loc = Integer.parseInt(sloc); // transfer sloc to int
			x = loc % 10;
			y = (loc - x) / 10;

			if (board[x][y].equals(" S")) // if that block is a S that means player hit it
			{
				hit++; // add one to the hit
				lau++; // add one to the lanuched
				System.out.println("It's a hit!!");
				board[x][y] = " #"; // change the block to # when player 2 hits the ships
			} else if (board[x][y].equals(" #") || board[x][y].equals(" o")) // if there is a"o" or "#"
			{
				System.out.println("You have already fire this area. "); // tell player 2 that rhe block you have
																			// already shooted
			} else {
				lau++;
				System.out.println("Missed."); // tell player it is a miss
				board[x][y] = " o"; // mark a o there represent player fire but miss
			}
			System.out.println("Lauched:" + lau + " , Hit: " + hit); // print the record of lanched and hit
		}
		showBoard(board, DEBUG); // show board
		System.out.println("You have hit all battleships! "); // tell the user the game is finished
	}

	// check did user input valid
	public static boolean isInt(final String sloc) {
		if (!(sloc.length() == 2)) {
			return true;
		} else if (Character.isDigit(sloc.charAt(0)) && Character.isDigit(sloc.charAt(1))) {
			return false;
		} else {
			return true;
		}
	}

	public static void human() {
		int loc;
		String orien, sloc;

		for (int i = 0; i < size.length; i++) // the big loop for each ship
		{
			showBoard(board, DEBUG); // show the board to user
			System.out.println("Player1: Set the ship: " + shipname[i] + ", ship size: " + size[i]); // tell the ship
																										// name and size
			System.out.print("Orientation (0 - horizontal, 1 - vertical), x - Exit: "); // ask the user ship goes
																						// vertical or horizontal
			orien = input.nextLine(); // input of orienentation
			while ((!("0").equals(orien)) && (!("1").equals(orien)) && (!("x").equals(orien))) // check the orien input
																								// valid or not
			{
				System.out.print("Please input 0 or 1, x - Exit : ");
				orien = input.nextLine();
			}
			xExit(orien); // exit mehod
			switch (orien) {
				case "0":
				case "1":
					System.out.print("Position of " + shipname[i] + " [XY] : "); // ask user xy location
					sloc = input.nextLine(); // save it to String version
					while (isInt(sloc)) // call the method to check sloc
					{
						System.out.print("Error in [XY]! Please input again,x - Exit : ");
						sloc = input.nextLine();
					}
					loc = Integer.parseInt(sloc); // change string to int
					y = loc % 10;
					x = (loc - y) / 10;
					while ((x + size[i]) > 10 || (y + size[i]) > 10) // when the x or y and the size is more than 10 it
																		// is a out boundary mistake!
					{
						System.out.print("The ships cannot be over the boundary!\nPosition of " + shipname[i]
								+ " [XY], x - Exit : ");
						sloc = input.nextLine();
						xExit(sloc);
						loc = Integer.parseInt(sloc);
						y = loc % 10;
						x = (loc - y) / 10;
					}
					while (overlap(board, loc, i, orien)) // check overlap
					{
						System.out
								.print("The ships cannot overlap!\nPosition of " + shipname[i] + " [XY], x - Exit : ");
						sloc = input.nextLine();
						loc = Integer.parseInt(sloc);
						y = loc % 10;
						x = (loc - y) / 10;
					}
					setship(orien, i, x, y); // finally set the ship call the set ship method
			}
		}
	}

	// it is the method to set up ships
	public static void setship(final String orien, final int s, final int x, final int y) {
		switch (orien) {
			case "0":
				for (int i = 0; i < size[s]; i++)
					board[y][x + i] = " S";
				break;
			case "1":
				for (int i = 0; i < size[s]; i++)
					board[y + i][x] = " S";
				break;
		}

	}

	// clear the board for player two to play the game
	private static void washboard() {
		for (int i = 0; i <= 100; i++) {
			System.out.println();
		}
	}

	// Exit the program
	private static void xExit(final String x)
	{
        if (x.length() == 1 && x.charAt(0)=='x')
		{
            System.exit(0);
        }
    }
}
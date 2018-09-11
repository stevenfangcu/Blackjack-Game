package core;

import java.util.ArrayList;
import java.util.*;
import java.util.List;
import javax.naming.InitialContext;

public class BlackjackGame {
	
	//card counter for each player (if they go over 21)
	int playerTotal = 0;
	int dealerTotal = 0;
	
	//player turn
	boolean playersTurn = true;
	boolean playersInstantBlackjack = false;
	//ace counter for each player
	int playerAces = 0;
	int dealerAces = 0;
	
	// the specific suites and values for the cards
	public static final String[] suites = {"H", "S", "C", "D"};
	public static final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	
	//state of the game, if it should be still going or has it finished
	boolean gameStatus = true;
	
	// this is for the initialized deck
	List<Card> initDeck = new ArrayList<Card>(); 
	
	//the initialize method.
	public int play(String txtFile) { 
		if(txtFile.equals("") || txtFile.equals(null)) {// we don't have a textfile
			
			return Init();
		}else { // when we do have a textfile
			return finishedGame(txtFile);
		}
	}
	
	// game logic for when textfile is provided, no need for game status since we know when it finishes
	private int finishedGame(String txtFile) {
		//splitting the array by spaces like in the given example on the BJassignment.pdf
		String[] contentOfGame = txtFile.split(" ");
		int counter = 0;
		
		System.out.println(Arrays.toString(contentOfGame) + " content length: " + contentOfGame.length);
		for(int i = 0; i < contentOfGame.length; i++) {
			if(i < 2) {
				if(contentOfGame[i].length() == 2){
					System.out.println(contentOfGame[i]);
					// the second char of the string in the array
					String secondCharString = Character.toString(contentOfGame[i].charAt(1));  
					while(counter < values.length) {
						if(secondCharString.equals("J") || secondCharString.equals("Q") || secondCharString.equals("K")) {
							playerTotal += 10;
							break;
						}else if(secondCharString.equals("A")) {
							playerAces++;
							playerTotal += 11;
							break;
						}
						counter++;
					}
					counter = 0; // reset the counter
				}
			}else if(i > 1 && i < 4) {
				System.out.println(contentOfGame[i]);
				// the second char of the string in the array
				String secondCharString = Character.toString(contentOfGame[i].charAt(1));  
				while(counter < values.length) {
					if(secondCharString.equals("J") || secondCharString.equals("Q") || secondCharString.equals("K")) {
						dealerTotal += 10;
						break;
					}else if(secondCharString.equals("A")) {
						dealerAces++;
						dealerTotal += 11;
						break;
					}
					counter++;
				}
				if(dealerTotal == 21) return 4;
				counter = 0; // reset the counter
			}	
		}
		System.out.println("Player Total: " + playerTotal);
		System.out.println("Dealer Total: " + dealerTotal);
		return 1;
	}

	// game logic for when textfile isn't provided
	private int Init() {
		
		return 2;
	}
	
}

package core;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;
import javax.naming.InitialContext;

public class BlackjackGame {
	//dealer has to hit below 17
	//id for player = 1; id for dealer = 0;
	//card counter for each player (if they go over 21)
	//dealer wins automatically when they get blackjack starting hand
	int playerTotal = 0;
	int dealerTotal = 0;
	int playerSplitT = 0;
	int dealerSplitT = 0;
	
	//player turn
	boolean playersTurn = true;
	boolean playerSplit = false;
	boolean dealerSplit = false;
	//ace counter for each player
	int playerAces = 0;
	int dealerAces = 0;
	
	// the specific suites and values for the cards
	public static final String[] suites = {"H", "S", "C", "D"};
	public static final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	
	//init the deck 
	List<Card> initDeck = new ArrayList<Card>();
	
	
	//state of the game, if it should be still going or has it finished
	boolean gameStatus = true;
	 
	
	//the initialize method.
	public int play(String txtFile) {
		initDeck = buildDeck(suites, values);
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
		
		System.out.println(Arrays.toString(contentOfGame) + " content length: " + contentOfGame.length);
		for(int i = 0; i < contentOfGame.length; i++) {
			if(i < 2) {
				if(contentOfGame[i].length() == 2){
					// the second char of the string in the array
					String secondCharString = Character.toString(contentOfGame[i].charAt(1));  
					hitResult(1, secondCharString);
				}else if(contentOfGame[i].length() == 3){
					hitResult(1,"10");
				}
			}else if(i > 1 && i < 4) {
				// the second char of the string in the array
				String secondCharString = Character.toString(contentOfGame[i].charAt(1));  
				hitResult(0, secondCharString);
				if(dealerTotal == 21) {
					System.out.println("Dealer automatically wins with blackjack");
					showResults();
					return 4;
				}
			}else{
				if(playersTurn) { // players turn to Hit/Stand
					if(contentOfGame[i].length() == 1) {
						if(contentOfGame[i].equals("S")) {
							//stops the players turn
							playersTurn = false;
						}else if( (contentOfGame[i].equals("H")) && (contentOfGame[i].length() == 1)) {
							System.out.print(i + " ");
							i++;
							String secondCharString = Character.toString(contentOfGame[i].charAt(1)); 
							hitResult(1, secondCharString);
							System.out.println("Hit and i has been incremented by 1: " + i);
						}
					}
				}else if(!playersTurn) { // dealers turn
					if(dealerTotal > 16) {
						showResults();
						return getResults(dealerTotal,playerTotal); // the dealer HAS to stand here
					}else{
						if(dealerTotal < 17) {
							String secondCharString = Character.toString(contentOfGame[i].charAt(1));  
							hitResult(0, secondCharString);
							if(dealerTotal > 21) {
								System.out.println("Dealer bust, Player wins");
								showResults();
								return 2;
							}
						}
					}
				}
			}
		}
		System.out.println("Player Total: " + playerTotal);
		System.out.println("Dealer Total: " + dealerTotal);
		return getResults(dealerTotal, playerTotal);
	}

	private int getResults(int dealers, int players) {
		if(dealers > players) {
			System.out.println("Player has a higher hand, player wins");
			return 2;
		}else if(dealers < players) {
			System.out.println("Dealer has a higher hand, player loses");
			return 3;
		}else {
			return 10; // tie
		}
	}

	public int hitResult(int id, String content){
		int counter = 0;
		if(id == 0) { // id for dealer = 0;
			System.out.println(content);
			while(counter < values.length) {
				if(content.equals("J") || content.equals("Q") || content.equals("K")) {
					dealerTotal += 10;
					break;
				}else if(content.equals("A")){
					dealerAces++;
					if(dealerTotal+11 <= 21) {
						dealerTotal += 11;
						break;
					}else if(dealerTotal+11 > 21) {
						dealerTotal++;
						break;
					}
				}else if(content.equals(values[counter])){
					dealerTotal += counter+1;
					break;
				}
				counter++;
			}
		}else if(id == 1) {
			System.out.println(content);
			while(counter < values.length) {
				if(content.equals("J") || content.equals("Q") || content.equals("K")) {
					playerTotal += 10;
					break;
				}else if(content.equals("A")) {
					playerAces++;
					if(playerTotal+11 <= 21) {
						playerTotal += 11;
						break;
					}else if(playerTotal+11 > 21) {
						playerTotal++;
						break;
					}
					break;
				}else if(content.length() == 1){
					if(content.equals(values[counter])){
							playerTotal += counter+1;
							break;
					}
				}else if(content.length() == 2) {
					playerTotal += 10;
					break;
				}
				counter++;
			}
		}
		return 1;
	}
	
	private void showResults() {
		System.out.println("Player Total: " + playerTotal);
		System.out.println("Dealer Total: " + dealerTotal);
	}

	// game logic for when textfile isn't provided
	private int Init() {
		
		return 2;
	}
	/*making the deck that is a list of cards and returning that to set to the global deck that we will be using
	 * still have to somehow to randomize the list, will look for some specific ways through the array methods that 
	 * are already implemented in java
	*/
	public static List<Card> buildDeck(String[] suites, String[] values){
		List<Card> dummyDeck = new ArrayList<Card>();
		for (int i = 0; i < suites.length; i++){ // for each suite 
			for (int j = 0; j < values.length; j++){ // for each value 
				Card k = new Card(values[j], suites[i]); // making a card with the values
				dummyDeck.add(k); // adding it into the dummy deck 
			}
		}
		System.out.println();
		return dummyDeck;
	}
	
	
}

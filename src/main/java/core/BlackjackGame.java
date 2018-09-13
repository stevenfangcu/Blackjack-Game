package core;

import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;
import javax.naming.InitialContext;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.w3c.dom.css.Counter;

public class BlackjackGame {
	//dealer has to hit below 17
	//id for player = 1; id for dealer = 0;
	//card counter for each player (if they go over 21)
	//dealer wins automatically when they get blackjack starting hand
	/*
	 * implement function to account for aces in the hit function and add aces into the implementation
	 */
	int playerTotal = 0;
	int dealerTotal = 0;
	int playerSplitT = 0;
	int dealerSplitT = 0;
	
	int gameCounter = 0;
	//a global counter for the card count in the arrayList
	int globalCounter = 0;
	//player turn
	boolean playersTurn = true;
	boolean playerSplit = false;
	boolean dealerSplit = false;
	//boolean to keep track which splited hand should recieve the card
	boolean firstHandDealer = true;
	boolean firstHandPlayer = true;
	//ace counter for each player
	int playerAces = 0;
	int dealerAces = 0;
	int dealerAcesSplit = 0;
	int playerAcesSplit = 0;
	
	// the specific suites and values for the cards
	public static final String[] suites = {"H", "S", "C", "D"};
	public static final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	
	//init the deck 
	List<Card> initDeck = new ArrayList<Card>();
	
	//keeping the string value of the first character and comparing it to the second to see if user can split
	String valueFirstPlayer = "";
	String valueSecondPlayer = "";
	String valueFirstDealer = "";
	String valueSecondDealer = "";
	
	//state of the game, if it should be still going or has it finished
	boolean gameStatus = true;
	 
	
	//the initialize method.
	public int play(String txtFile) {
		initDeck = buildDeck(suites, values);
		//this shuffles the deck might be reuseable?
		Collections.shuffle(initDeck);
		for(int i = 0; i < initDeck.size(); i++) {
			//System.out.print(initDeck.get(i).getValue() + initDeck.get(i).getSuit() + " ");
		}
		//System.out.println("Size of Deck: " + initDeck.size());
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
					System.out.println(secondCharString);
					if(i == 0) {
						valueFirstPlayer = secondCharString;
					}else {
						valueSecondPlayer = secondCharString;
					}
					hitResult(1, secondCharString);
				}else if(contentOfGame[i].length() == 3){
					hitResult(1,"10");
				}
			}else if(i > 1 && i < 4) {
				// the second char of the string in the array
				String secondCharString = Character.toString(contentOfGame[i].charAt(1));
				if(i == 2) {
					valueFirstDealer = secondCharString;
				}else{
					valueSecondDealer = secondCharString;
				}
				hitResult(0, secondCharString);
				if(dealerTotal == 21) {
					System.out.println("Dealer automatically wins with blackjack");
					showResults();
					return 4;
				}
			}else{
				if(playersTurn){ // players turn to Hit/Stand
					if(contentOfGame[i].length() == 1) {
						if(contentOfGame[i].equals("S")) { // stand
							//stops the players turn
							gameCounter++;
							if(!playerSplit) {
								playersTurn = false;
							}else if(gameCounter == 3){ // if there is two stands and !playerSplit is true
								playersTurn = false;
							}
						}else if( (contentOfGame[i].equals("H")) && (contentOfGame[i].length() == 1)) { // hit
							i++;
							String secondCharString = Character.toString(contentOfGame[i].charAt(1)); 
							hitResult(1, secondCharString);
							System.out.println("Hit: " + contentOfGame[i]);
							if(playerTotal > 21 && !playerSplit) {
								System.out.println("User loses by busting!");
								return 6;
							}
							if(playerSplit && playerSplitT > 21 && playerTotal > 21){
								System.out.println("User loses by busting!");
								return 6;
							}
						}else if(contentOfGame[i].equals("D") && (contentOfGame[i].length() == 1)) { // split
							if(valueFirstPlayer.equals(valueSecondPlayer)) {
								splitHand(1);
							}
						}else {
						}
					}
				}else if(!playersTurn) { // dealers turn
					if(contentOfGame[i].equals("D")) {
						dealerSplit = true;
						splitHand(0);
					}else if(dealerTotal > 16) {
						showResults();
						return getResults(); // the dealer HAS to stand here
					}else{
						if(dealerTotal < 17) {
							String secondCharString = Character.toString(contentOfGame[i].charAt(1)); 
							hitResult(0, secondCharString);
							if(dealerTotal > 21) {
								if(dealerSplit) {
									hitResult(0, secondCharString);
								}else {
									System.out.println("Dealer bust, Player wins");
									showResults();
									return 2;
								}
							}
						}
					}
				}
			}
		}
		showResults();
		return getResults();
	}

	private void splitHand(int i) {
		//int x = 0;
		if(i == 1) {
			//x = (playerTotal / 2);
			//playerTotal = x*2;
			playerSplit = true;
			playerSplitT = playerTotal;
			System.out.println("Player's hand has been split!");
		}else if(i == 0) {
			dealerTotal = (dealerTotal / 2);
			dealerSplit = true;
			dealerSplitT = dealerTotal;
			System.out.println("Dealer's hand has been split!");
		}
	}

	private int getResults() {
		if(playerSplitT > dealerSplitT && playerSplitT > dealerTotal) {
			System.out.println("User wins by having a bigger hand");
			return 3;
		}else if(playerTotal > dealerSplitT && playerTotal > dealerTotal) {
			System.out.println("User wins by having a bigger hand");
			return 3;
		}else if(dealerSplitT > playerTotal && dealerSplitT > playerSplitT) {
			System.out.println("User loses by having a lower hand");
			return 6;
		}else if(dealerSplitT > playerTotal && dealerSplitT > playerSplitT) {
			System.out.println("User loses by having a lower hand");
			return 6;
		}else if(dealerTotal > playerTotal){
			System.out.println("User loses by having a lower hand");
			return 6;
		}else if(playerTotal > dealerTotal){
			System.out.println("User wins by having a bigger hand");
			return 3;
		}
		else{
			System.out.println("User ties!");
			return 10; // tie
		}
	}

	public int hitResult(int id, String content){
		int counter = 0;
		if(id == 0) { // id for dealer = 0;
			System.out.println(content + " " + firstHandDealer);
			while(counter < values.length) {
				if(content.equals("J") || content.equals("Q") || content.equals("K")) {
					if(firstHandDealer) {
						if((dealerTotal+10) < 21){
							dealerTotal += 10;
							break;
						}else if((dealerTotal+10) > 21 && dealerAces > 0) {
							dealerAces--;
							break;
						}
					}else {
						if((dealerTotal+10) < 21) {
							dealerSplitT += 10;
							break;
						}else if((dealerTotal+10) > 21 && dealerAces > 0) {
							dealerAces--;
							break;
						}
					}
				}else if(content.equals("A")){
					if(firstHandDealer){
						dealerAces++;
						if(dealerTotal+11 <= 21) {
							dealerTotal += 11;
							break;
						}else if(dealerTotal+11 > 21) {
							dealerTotal++;
							break;
						}else if((dealerTotal+1 > 21) && dealerAces > 0) {
							break;
						}
					}else{
						dealerAcesSplit++;
						if(dealerSplitT+11 <= 21) {
							dealerSplitT += 11;
							break;
						}else if(dealerSplitT+11 > 21) {
							dealerSplitT++;
							break;
						}
					}
				}else if(content.equals(values[counter])){
					if(firstHandDealer) {
						dealerTotal += counter+1;
						break;
					}else {
						dealerSplitT += counter+1;
					}
				}
				if(dealerSplit) {
					firstHandDealer = (!firstHandDealer);
				}
				counter++;
			}
			if(dealerSplit) {
				firstHandDealer = !firstHandDealer;
			}
		}else if(id == 1) {
			while(counter < values.length) {
				if(content.equals("J") || content.equals("Q") || content.equals("K")) {
					if(firstHandPlayer){
						playerTotal += 10;
						break;
					}else {
						playerSplitT += 10;
						break;
					}
				}else if(content.equals("A")) {
					playerAces++;
					if(firstHandPlayer) {
						if(playerTotal+11 <= 21) {
							playerTotal += 11;
							break;
						}else if(playerTotal+11 > 21) {
							playerTotal++;
							break;
						}
					}else {
						if(playerSplitT+11 <= 21) {
							playerSplitT += 11;
							break;
						}else if(playerSplitT+11 > 21) {
							playerSplitT++;
							break;
						}
					}
					break;
				}else if(content.length() == 1){
					if(firstHandPlayer) {
						if(content.equals(values[counter])){
								playerTotal += (counter+1);
								break;
						}
					}else{
						if(content.equals(values[counter])){
							playerSplitT += (counter+1);
							break;
						}
					}
				}else if(content.length() == 2) {
					if(firstHandPlayer) {
						playerTotal += 10;
						break;
					}else {
						playerSplitT += 10;
						break;
					}
				}
				counter++;
			}
			if(playerSplit) {
				firstHandPlayer = !firstHandPlayer;
			}
		}
		return 1;
	}
	
	private void showResults() {
		System.out.println("Player Total: " + playerTotal);
		if(playerSplit) System.out.println("Player Split Total: " + playerSplitT);
		System.out.println("Dealer Total: " + dealerTotal);
		if(dealerSplit) System.out.println("Dealer Split Total: " + dealerSplitT);
	}

	// game logic for when textfile isn't provided not implemented.
	private int Init() {
		
		return 2;
	}
	/*making the deck that is a list of cards and returning that to set to the global deck that we will be using
	 * still have to somehow to randomize the list, will look for some specific ways through the array methods that 
	 * are already implemented in java
	*/
	public static List<Card> buildDeck(String[] suites, String[] values){
		List<Card> dummyDeck = new ArrayList<Card>();
		int inputValue = 0;
		for (int i = 0; i < suites.length; i++){ // for each suite 
			for (int j = 0; j < values.length; j++){ // for each value 
				if(j > 8){
					inputValue = 10;
				}else {
					inputValue = j;
				}
				Card k = new Card(values[j], suites[i],inputValue); // making a card with the values
				dummyDeck.add(k); // adding it into the dummy deck 
			}
		}
		return dummyDeck;
	}
	
}

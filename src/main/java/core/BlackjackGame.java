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
	boolean playerStand = false;
	//boolean to keep track which splited hand should recieve the card
	boolean firstHandDealer = true;
	boolean firstHandPlayer = true;
	//ace counter for each player
	int playerAces = 0;
	int dealerAces = 0;
	int dealerAcesSplit = 0;
	int playerAcesSplit = 0;
	int ArrayCounter = 0;
	
	
	// the specific suites and values for the cards
	public static final String[] suites = {"H", "S", "C", "D"};
	public static final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	
	public String[] cardsStrings = new String[52];
	
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
		if(txtFile.equals("") || txtFile.equals(null)) {// we don't have a textfile
			
			Init();
			return 11;
		}else { // when we do have a textfile
			return finishedGame(txtFile);
		}
	}
	
	// game logic for when textfile is provided, no need for game status since we know when it finishes
	private int finishedGame(String txtFile) {
		//splitting the array by spaces like in the given example on the BJassignment.pdf
		String[] contentOfGame = txtFile.split(" ");
		String checkString = "";
		String checkString2 = "";
		System.out.println(Arrays.toString(contentOfGame) + " content length: " + contentOfGame.length);
		for(int i = 0; i < contentOfGame.length; i++) {
			checkString = Character.toString(contentOfGame[i].charAt(0));
			checkString2 = contentOfGame[i].substring(1);
			//error checking
			if(contentOfGame[i].equals("H") && i == contentOfGame.length){
				System.out.println("missing input card");
				break;
			}else if(Arrays.asList(cardsStrings).contains(contentOfGame[i]) && contentOfGame[i].length() != 1 && contentOfGame[i].length() > 1) {
				System.out.println("duplicate card");
				break;
			}else if(!Arrays.asList(suites).contains(checkString)) {
				System.out.println("suit error");
				break;
			}else if(!Arrays.asList(values).contains(checkString2) && checkString2.length() > 0) {
				System.out.println(contentOfGame[i]);
				break;
			}else{
				cardsStrings[ArrayCounter++] = contentOfGame[i];
			}
			if(i < 2) { // first two cards for user
				if(contentOfGame[i].length() == 2){
					// the second char of the string in the array
					String secondCharString = Character.toString(contentOfGame[i].charAt(1));
					if(i == 0) {
						valueFirstPlayer = secondCharString;
					}else {
						valueSecondPlayer = secondCharString;
					}
					hitResult(1, secondCharString);
				}else if(contentOfGame[i].length() == 3){
					hitResult(1,"10");
				}
			}else if(i > 1 && i < 4) { // first two cards for dealer
				// the second char of the string in the array
				if(contentOfGame[i].length() == 2){
					String secondCharString = Character.toString(contentOfGame[i].charAt(1));
					if(i == 2) {
						valueFirstDealer = secondCharString;
					}else{
						valueSecondDealer = secondCharString;
					}
					hitResult(0, secondCharString);
				}else if(contentOfGame[i].length() == 3) {
					hitResult(0, "10");
				}
				if(dealerTotal == 21) {
					System.out.println("Dealer automatically wins with blackjack");
					showResults();
					return 4;
				}
				if(Integer.parseInt(getPlayTotal())== 21 && dealerTotal != 21 && i == 3){
					System.out.println("Player wins automatically");
					return 2;
				}
			}else{
				if(playersTurn){ // players turn to Hit/Stand
					if(contentOfGame[i].length() == 1) {
						if(contentOfGame[i].equals("S")) { // user stand
							//stops the players turn
							gameCounter++;
							if(!playerSplit) {
								playersTurn = false;
								System.out.println("Player's first hand stands. ");
							}else if(gameCounter == 2){ // if there is two stands and !playerSplit is true
								playersTurn = false;
								System.out.println("Player's second hand stands. ");
							}
						}else if( (contentOfGame[i].equals("H")) && (contentOfGame[i].length() == 1)) { // user hit
							i++;
							String secondCharString = "";
							if(i == contentOfGame.length) {
								System.out.println("null error");
								break;
							}else {
								secondCharString = Character.toString(contentOfGame[i].charAt(1)); 
							}
							hitResult(1, secondCharString);
							System.out.println("Hit: " + contentOfGame[i] + " ");
							if(playerTotal > 21 && !playerSplit) {
								System.out.println("User loses by busting!");
								showResults();
								return 6;
							}
							if(playerSplit && playerSplitT > 21 && playerTotal > 21){
								System.out.println("User loses by busting!");
								showResults();
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
					}else if(dealerTotal > 16 && dealerTotal <= 21) {
						showResults();
						return getResults(); // the dealer HAS to stand here
					}else{
						if(dealerTotal <= 17) {
							if(contentOfGame[i].length() == 3) {
								hitResult(0, "10");
							}else if(contentOfGame[i].length() == 2) {
								System.out.println(contentOfGame[i] + " ,");
								String secondCharString = Character.toString(contentOfGame[i].charAt(1));
								hitResult(0, secondCharString);
							}
						}else if(dealerSplitT <= 17) {
							if(contentOfGame[i].length() == 3) {
								hitResult(0, "10");
							}else {
								System.out.println(contentOfGame[i] + " .");
								String secondCharString = Character.toString(contentOfGame[i].charAt(1)); 
								hitResult(2, secondCharString);
							}
						}
					}
				}
			}
		}
		//showResults();
		return getResults();
	}

	private void splitHand(int i) {
		if(i == 1) {
			if(playerAces == 0) {
				playerTotal = (playerTotal / 2);
				playerSplit = true;
				playerSplitT = playerTotal;
				System.out.println("Player's hand has been split!");
			}else{
				playerTotal = 11;
				playerAces = 1;
				playerSplit = true;
				playerSplitT = 11;
				playerAcesSplit = 1;
			}
		}else if(i == 0) {
			if(dealerAces == 0) {
				dealerTotal = (dealerTotal / 2);
				dealerSplit = true;
				dealerSplitT = dealerTotal;
				System.out.println("Dealer's hand has been split! ");
			}else {
				dealerTotal = 11;
				dealerAces = 1;
				dealerSplit = true;
				dealerSplitT = 11;
				dealerAcesSplit = 1;
				System.out.println("Dealer's hand has been split! ");
			}
		}
	}

	public int getResults() {
		if(dealerTotal == 21) {
			System.out.println("dealer wins with BJ");
			return 4;
		}
		if(dealerTotal > 21 && dealerSplit == false){
			System.out.println("User wins by dealer bust");
			return 2;
		}else if(dealerSplitT > 21 ) {
			if(dealerTotal > 21) {
				dealerTotal = 0;
			}
			dealerSplitT = 0;
		}else if(playerTotal > 21 && playerSplit == false) {
			System.out.println("User loses by busting");
			return 5;
		}else if(playerSplitT > 21) {
			if(playerTotal > 21) {
				playerTotal = 0;
			}
			playerSplitT = 0;
		}else if(playerTotal > 21) {
			playerTotal = 0;
		}else if(dealerTotal > 21) {
			dealerTotal = 0;
		}
		if((playerSplitT > dealerSplitT) && (playerSplitT > dealerTotal)) {
			System.out.println("User wins by having a bigger hand (Split)");
			return 3;
		}else if((playerTotal > dealerSplitT) && (playerTotal > dealerTotal)) {
			System.out.println("User wins by having a bigger hand");
			return 3;
		}else if((dealerSplitT > playerTotal) && (dealerSplitT > playerSplitT)) {
			System.out.println("User loses by having a lower hand (Split hand)");
			return 6;
		}else if((dealerTotal > playerTotal) && (dealerTotal > playerSplitT)) {
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
			while(counter < values.length) {
				if(content.equals("J") || content.equals("Q") || content.equals("K") || content.equals("10")) {
					if(firstHandDealer){
						if((dealerTotal+10) < 21){
							dealerTotal += 10;
							break;
						}else if((dealerTotal+10) > 21 && dealerAces > 0) {
							dealerAces--;
							break;
						}else{
							dealerTotal += 10;
							break;
						}
					}else {
						if((dealerSplitT+10) < 21) {
							dealerSplitT += 10;
							break;
						}else if((dealerSplitT+10) > 21 && dealerAcesSplit > 0) {
							dealerAcesSplit--;
							break;
						}else{
							dealerSplitT += 10;
							break;
						}
					}
				}else if(content.equals("A")){
					if(firstHandDealer){
						dealerAces++;
						if(dealerTotal+11 <= 21) {
							dealerTotal += 11;
							break;
						}else if( ( (dealerTotal+1) > 21) && dealerAces > 1) {
							dealerAces -= 2;
							dealerTotal -= 9;
							break;
						}else if( ( (dealerTotal+11) > 21) && dealerAces > 0){
							dealerAces--;
							dealerTotal++;
							break;
						}else{
							dealerTotal++;
							break;
						}
					}else{
						dealerAcesSplit++;
						if(dealerSplitT+11 <= 21) {
							dealerSplitT += 11;
							break;
						}else if( ( (dealerSplitT+11) > 21) && dealerAcesSplit > 1) {
							dealerAcesSplit -= 2;
							dealerSplitT -= 9;
							break;
						}else if(( (dealerSplitT+11) > 21) && dealerAcesSplit > 0){
							dealerAcesSplit--;
							dealerSplitT++;
							break;
						}else{
							dealerSplitT++;
							break;
						}
					}
				}else if(content.equals(values[counter])){
					if(firstHandDealer) {
						if( (dealerTotal+(counter+1))  < 21) {
							dealerTotal += counter+1;
							break;
						}else if( ( (dealerTotal+(counter+1) ) > 21) && dealerAces > 0){
							dealerTotal -= 10;
							dealerAces--;
							dealerTotal += counter+1;
							break;
						}else {
							dealerTotal += counter+1;
							break;
						}
					}else {
						if( (dealerSplitT+(counter+1) ) < 21) {
							dealerSplitT += counter+1;
							break;
						}else if( ( (dealerSplitT+(counter+1) ) > 21) && dealerAcesSplit > 0){
							dealerSplitT -= 10;
							dealerAcesSplit--;
							dealerSplitT += counter+1;
							break;
						}else {
							dealerSplitT += counter+1;
							break;
						}
					}
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
						if((playerTotal+10) < 21) {
							playerTotal += 10;
							break;
						}else if( ( (playerTotal+10) > 21)  && playerAces > 0){
							playerAces--;
							break;
						}else {
							playerTotal += 10;
							break;
						}
					}else{ // finish this part for aces
						if((playerSplitT+10) < 21){
						playerSplitT += 10;
						break;
						}else if( ( (playerSplitT+10)>21) && playerAcesSplit > 0) {
							playerAcesSplit--;
							break;
						}else {
							playerSplitT += 10;
							break;
						}
					}
				}else if(content.equals("A")) {
					if(firstHandPlayer) {
						playerAces++;
						if(playerTotal+11 <= 21) {
							playerTotal += 11;
							break;
						}else if((playerTotal+1 > 21) && playerAces > 1){
							playerAces -= 2;
							playerTotal -= 9;
							break;
						}else if( ( (playerTotal+11) > 21) && playerAces > 0){
							playerAces--;
							playerTotal++;
							break;
						}else {
							playerTotal += 11;
							break;
						}
					}else {
						playerAcesSplit++;
						if(playerSplitT+11 <= 21) {
							playerSplitT += 11;
							break;
						}else if( ( (playerSplitT+1) > 21) && playerAcesSplit > 1) {
							playerAcesSplit -= 2;
							playerSplitT -= 9;
							break;
						}else if( ( (playerSplitT+11) > 21) && playerAcesSplit > 0) {
							playerAcesSplit--;
							playerSplitT++;
						}else{
							playerSplitT += 11;
							break;
						}
					}
				}else if(content.length() == 1){ // this part for aces
					if(firstHandPlayer) {
						if(content.equals(values[counter])){
								if( (playerTotal+(counter+1) ) < 21) {
									playerTotal += (counter+1);
									break;
								}else if( ( (playerTotal+(counter+1) ) > 21) && playerAces > 0){
									playerAces--;
									playerTotal -= 10;
									playerTotal += (counter+1);
									break;
								}else {
									playerTotal += (counter+1);
									break;
								}
						}
					}else{
						if(content.equals(values[counter])){
							if( (playerSplitT+(counter+1) ) < 21) {
								playerSplitT += (counter+1);
								break;
							}else if( ( (playerSplitT+(counter+1) ) > 21) && playerAcesSplit > 0){
								playerAcesSplit--;
								playerSplitT -= 10;
								playerSplitT += (counter+1);
								break;
							}else {
								playerSplitT += (counter+1);
								break;
							}
						}
					}
				}else if(content.length() == 2) { // this part for aces 
					if(firstHandPlayer) {
						if((playerTotal+10) < 21) {
							playerTotal += 10;
							break;
						}else if (((playerTotal+10) > 21 ) && playerAces > 0) {
							playerAces--;
							break;
						}else {
							playerTotal += 10;
							break;
						}
					}else {
						if((playerSplitT+10) < 21) {
							playerSplitT += 10;
							break;
						}else if (((playerSplitT+10) > 21 ) && playerAcesSplit > 0) {
							playerAcesSplit--;
							break;
						}else {
							playerSplitT += 10;
							break;
						}
					}
				}
				counter++;
			}
			if(playerSplit) {
				firstHandPlayer = !firstHandPlayer;
			}
		}else if(id == 2) {// for split dealer
			while(counter < values.length) {
				if(content.equals(values[counter])) {
					if( (dealerSplitT + (counter+1)) > 21 && (dealerAcesSplit > 0)){
						dealerSplitT -= 10;
						dealerSplitT += (counter+1);
						break;
					}
				}
				counter++;
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
	public void Init() {
		initDeck = buildDeck(suites, values);
		Collections.shuffle(initDeck);
		for(int i = 0; i < initDeck.size(); i++) {
			System.out.print(initDeck.get(i).getSuit() + initDeck.get(i).getValue() + " ");
		}
		System.out.println();
		System.out.println(initDeck.size());
	}
	public String firstHands(int id, int split, int counter){
		String returnV = "";
		Card cardHand = initDeck.remove(0);
		if(cardHand.getValue().equals("10") || cardHand.getValue().equals("A") || cardHand.getValue().equals("K") || 
		cardHand.getValue().equals("Q") || cardHand.getValue().equals("J")) {
			if(cardHand.getValue().equals("A") && id == 1) {
				playerAces++;
			}else if(cardHand.getValue().equals("A") && id == 0){
				dealerAces++;
			}
			returnV = cardHand.getSuit() + cardHand.getValue();
		}else {
			returnV = cardHand.getSuit() + "0" + cardHand.getValue();
		}
		if(split == 1 && counter == 0 && id == 1){ // save first card
			valueFirstPlayer = cardHand.getValue();
		}else if(split == 1 && counter == 1 && id == 1) {
			valueSecondPlayer = cardHand.getValue();
		}
		if(id == 1) {
			playerTotal += cardHand.getCount();
			if(playerTotal > 21) {
				playerTotal = 12;
				playerAces--;
			}
		}else {
			if(dealerTotal > 21) {
				dealerTotal = 12;
				dealerAces--;
			}
			dealerTotal += cardHand.getCount();
		}
		if(valueFirstPlayer.equals(cardHand.getValue()) && counter != 0) {
			playerSplit = true;
		}
		return returnV;
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

	public String getPlayTotal() {
		// TODO Auto-generated method stub
		return Integer.toString(playerTotal);
	}

	public String getDealTotal() {
		// TODO Auto-generated method stub
		return Integer.toString(dealerTotal);
	}
	public int getPlayerSplitTotal() {
		// TODO Auto-generated method stub
		return playerSplitT;
	}
	public boolean getPlayerSplit() {
		return valueFirstPlayer.equals(valueSecondPlayer);
	}

	public String hit(int id) {
		if(id == 1) {
			Card cardHand = initDeck.remove(0);
			String xString = "";
			String yString = "";
			if(cardHand.getValue().equals("10") || cardHand.getValue().equals("A") || cardHand.getValue().equals("K") || 
			cardHand.getValue().equals("Q") || cardHand.getValue().equals("J")) {
				xString = cardHand.getSuit() + cardHand.getValue();
			}else {
				xString = cardHand.getSuit() + "0" + cardHand.getValue();
				yString = cardHand.getSuit() + cardHand.getValue();
			}
			System.out.println(yString);
			if((gameCounter == 1 && playerStand == true) || playerTotal > 21){
				firstHandPlayer = false;
			}else {
				firstHandPlayer = true;
			}
			hitResult(1, cardHand.getValue());
			if(	Integer.parseInt(getPlayTotal()) > 21 && !playerSplit && ( (playerSplitT==0) || (playerSplitT > 21) ) ){
				gameStatus = false;
			}
			System.out.println(yString);
			return xString;
		}else if(id == 0){
			Card cardHand = initDeck.remove(0);
			String xString = "";
			String yString = "";
			if(cardHand.getValue().equals("10") || cardHand.getValue().equals("A") || cardHand.getValue().equals("K") || 
			cardHand.getValue().equals("Q") || cardHand.getValue().equals("J")) {
				xString = cardHand.getSuit() + cardHand.getValue();
				System.out.println(xString + " 4.");
			}else {
				xString = cardHand.getSuit() + "0" + cardHand.getValue();
				yString = cardHand.getSuit() + cardHand.getValue();
			}
			hitResult(0, cardHand.getValue());
			dealerSplit = false;
			return xString;
		}else {
			return "";
		}
	}

	public boolean getGameState() {
		// TODO Auto-generated method stub
		System.out.println(gameStatus);
		return gameStatus;
	}


	public void splitHandGUI() {
		if(valueFirstPlayer.equals(valueSecondPlayer)) {
			splitHand(1);
		}
	}

	public void stand() {
		gameCounter++;
		if(!playerSplit) {
			playersTurn = false;
			System.out.println("Player's first hand stands. ");
		}else if(gameCounter == 2){ // if there is two stands and !playerSplit is true
			playersTurn = false;
			System.out.println("Player's second hand stands. ");
		}else {
			playerStand = true;
		}
	}

	public boolean getPlayersTurn() {
		return playersTurn;
	}

	public int getCounter() {
		// TODO Auto-generated method stub
		return gameCounter;
	}
	
}

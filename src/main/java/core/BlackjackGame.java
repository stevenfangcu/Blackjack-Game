package core;

import java.util.ArrayList;
import java.util.*;
import java.util.List;

import javax.naming.InitialContext;

public class BlackjackGame {
	// the specific suites and values for the cards
	public static final String[] suites = {"H", "S", "C", "D"};
	public static final String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	//state of the game, if it should be still going or has it finished
	boolean gameStatus = true;
	// this is for the initialized deck
	List<Card> initDeck = new ArrayList<Card>(); 
	
	//the initialize method.
	public int play(String x) { 
		if(x == null) {	
			// when we don't have a text file
			return Init();
		}else {
			// when we do have a text file, split up the string by " " and get the specific characters to go with it
			return 3;
		}
	}

	private int Init() {

		//card counter for each player (if they go over 21)
		int playerTotal = 0;
		int dealerTotal = 0;
		//ace counter for each player
		int playerAces = 0;
		int dealerAces = 0;
		
		return 2;
	}
}

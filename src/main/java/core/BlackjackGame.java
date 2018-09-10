package core;

import java.util.ArrayList;
import java.util.*;
import java.util.List;

import javax.naming.InitialContext;

public class BlackjackGame {
	public static final String[] suites = {"H", "S", "C", "D"};
	public static final String[] cards = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	boolean gameStatus = true;
	List<Card> initDeck = new ArrayList<Card>(); // this is for the initialized deck
	
	public int play(String x) { // to start the game basically 
		if(x == null) {	
			return Init();
		}
		return 1;
	}

	private int Init() {
		// TODO Auto-generated method stub

		//card counter for each player (if they go over 21)
		int playerTotal = 0;
		int dealerTotal = 0;
		//ace counter for each player
		int playerAces = 0;
		int dealerAces = 0;
		
		return 2;
	}
}

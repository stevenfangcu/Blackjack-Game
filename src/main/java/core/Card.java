package core;

public class Card {
	private int value;
	private String suit;
	public Card(String suit, int value) {
		this.value = value;
		this.suit = suit;
	}
	public int getValue() {
		return this.value;
	}
	public String getSuit() {
		return this.suit;
	}
}

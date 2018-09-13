package core;

public class Card {
	//these values should NEVER change
	private String value;
	private String suit;
	private int count;
	public Card(String suit, String value, int count) {
		this.value = value;
		this.suit = suit;
		this.count = count;
	}
	public String getValue() {
		return this.value;
	}
	public String getSuit() {
		return this.suit;
	}
	public int getCount() {
		return this.count;
	}
}

package core;

public class Card {
	private String value;
	private String suit;
	public Card(String suit, String value) {
		this.value = value;
		this.suit = suit;
	}
	public String getValue() {
		return this.value;
	}
	public String getSuit() {
		return this.suit;
	}
}

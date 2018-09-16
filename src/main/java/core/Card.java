package core;

import org.w3c.dom.css.Counter;

public class Card {
	//these values should NEVER change
	private String value;
	private String suit;
	private int count;
	public Card(String value, String suit, int count) {
		this.value = value;
		this.suit = suit;
		if(count >= 1 && count < 10) {
			this.count = (count+1);
		}else if(value.equals("A")) {
			this.count += 11;
		}else {
			this.count = count;
		}
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

package core;

import junit.framework.TestCase;
import java.io.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class BlackjackTest extends TestCase{
	/* 	1 for BJ win for the player
	2 for Bust win for the player
	3 for higher hand for the player
	4 for BJ win for the dealer
	5 for Bust win for the dealer
	6 for higher hand for the dealer
	 */
	String fileName = "";
	public void testGame1() {
		fileName = "src/test/resources/a1.txt";
		
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

			stream.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(1, result.play("game1"));
	}
	public void testGame2() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(2, result.play("game2"));
	}
	public void testGame3() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(3, result.play("game3"));
	}
	public void testGame4() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(3, result.play("game4"));
	}
	public void testGame5() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(3, result.play("game5"));
	}
}

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
	7 for error in the textfile
	 */
	String fileName = "";
	String contentString = "";
	public void testGame1() {
		fileName = "src/test/resources/a1.txt";
		
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			//getting the content of the textfile that is pushed through
			String content = new String(Files.readAllBytes(Paths.get(fileName)));
			contentString = content;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(contentString);
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(4, result.play(contentString));
	}
	/*
	public void testGame2() {
		fileName = "src/test/resources/a2.txt";
		contentString = "";
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			String content = new String(Files.readAllBytes(Paths.get(fileName)));
			contentString = content;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(2, result.play(contentString));
	}
	public void testGame3() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(3, result.play(""));
	}
	public void testGame4() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(3, result.play(""));
	}
	public void testGame5() {
		BlackjackGame result = new BlackjackGame();
		// using game1 as a dummy for now.
		assertEquals(3, result.play(""));
	}
	*/
}

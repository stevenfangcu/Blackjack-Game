package core;


import java.awt.Panel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Random;
import org.w3c.dom.css.Counter;
import java.util.Scanner;
import java.io.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class blackjackGUI extends Application{
	Button hitButton,SplitButton,Standbutton;
	BlackjackGame blackjack = new BlackjackGame();
	boolean split = false;
	int Counter = 2;
	int CounterD = 2;
	
	Label label = new Label("Player Total: " + blackjack.getPlayTotal());
	Label label2 = new Label("Player Total: " + blackjack.getDealTotal());
	Label labelSplit = new Label(""); 
	Label label3 = new Label("");
	
	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in); 
		System.out.println("Enter a file name or start: ");
		String n = reader.next();
		reader.close();
		if(n.equals("start")) {
			launch(args);
		}else {
			String fileName = "src/test/resources/" + n;
			String contentString = "";
			try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
				String content = new String(Files.readAllBytes(Paths.get(fileName)));
				contentString = content;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			BlackjackGame result = new BlackjackGame();
			result.play(contentString);
			result.getResults();
		}
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		blackjack.Init();
		initGui(primaryStage);
	}
	private void initGui(Stage primaryStage) {
		Pane canvas = new Pane();
		
		setupCardsAnimation(canvas);
		addControlsToCanvas(canvas);
		
		Scene scene = new Scene(canvas, 320, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Simple Blackjack Game");
		primaryStage.show();
	}

	private void setupCardsAnimation(Pane canvas) { // makes the first hand for both dealer and player
		for(int i = 0; i < 2; i++) { // for the player
			String fileString = "src/test/resources/cards/" + blackjack.firstHands(1,1,i) + ".bmp";
			Image img = null;
			try {
				img = new Image(new FileInputStream(fileString));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageView imgView = new ImageView();
			imgView.setImage(img);
			imgView.relocate(5+(12*i), 85);
			
			label.setText("Player Total: " + blackjack.getPlayTotal());
			label.setFont(Font.font("Serif", FontWeight.NORMAL, 20));
			label.relocate(5, 10);
			labelSplit.relocate(5, 40);		
			canvas.getChildren().addAll(imgView);
		}
		for(int i = 0; i < 2; i++) {
			String fileString = "src/test/resources/cards/" + blackjack.firstHands(0,0,123) + ".bmp";
			Image img = null;
			try {
				img = new Image(new FileInputStream(fileString));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageView imgView = new ImageView();
			imgView.setImage(img);
			imgView.relocate(310+(12*i), 85);
			
			label2.setText("Dealer Total: " + blackjack.getDealTotal());
			label2.setFont(Font.font("Serif", FontWeight.NORMAL, 20));
			label2.relocate(300, 10);
			canvas.getChildren().addAll(imgView);
		}
		label3.setFont(Font.font("Serif", FontWeight.NORMAL, 20));
		labelSplit.setFont(Font.font("Serif", FontWeight.NORMAL, 20));
		label3.relocate(225, 210);
		if(blackjack.getDealTotal().equals("21")) {
			label3.setText("User loses by dealer blackjack");
		}
		canvas.getChildren().addAll(label, label2, label3, labelSplit);
	}
	
	private void addControlsToCanvas(Pane canvas) {
		hitButton = new Button("Hit");
		hitButton.relocate(5, 210);
		Standbutton = new Button("Stand");
		Standbutton.relocate(55, 210);
		hitButtonEventHandler(canvas);
		if(blackjack.getPlayerSplit() == true) {
			SplitButton = new Button("Split");
			SplitButton.relocate(125, 210);
			SplitButtonEventHandler(canvas);
			canvas.getChildren().addAll(hitButton,SplitButton,Standbutton);
		}else {
			canvas.getChildren().addAll(hitButton,Standbutton);
		}
		
	}
	public void SplitButtonEventHandler(Pane canvas) {
		SplitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				blackjack.splitHandGUI();
				updatePlayTotal(canvas);
				updatePlaySplitTotal(canvas);
				SplitButton.setDisable(true);
			}
		});
	}
	private void hitButtonEventHandler(Pane canvas) {
		hitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(blackjack.getGameState()){
					System.out.println("Player Hit!");
					String cardIdString = blackjack.hit(1);
					makeNewImg(cardIdString, canvas);
				}else if(blackjack.getCounter() == 1 && blackjack.getPlayerSplitTotal() > 21) {
					hitButton.setDisable(true);
					updatePlayTotal(canvas);
					updatePlaySplitTotal(canvas);
				}else if(blackjack.getCounter() == 1 && Integer.parseInt(blackjack.getPlayTotal()) > 21) {
					hitButton.setDisable(true);
					updatePlayTotal(canvas);
					updatePlaySplitTotal(canvas);
				}else{
					updatePlaySplitTotal(canvas);
					hitButton.setDisable(true);
					updatePlayTotal(canvas);
					label.setText("Player busted with : " + blackjack.getPlayTotal());
					
				}
			}
			
		});
		Standbutton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(!blackjack.getPlayersTurn()){
					hitButton.setDisable(true);
					while(Integer.parseInt(blackjack.getDealTotal()) <= 16) {
						String cardIdstring = blackjack.hit(0);
						makeNewImg1(cardIdstring, canvas);
						updateDealTotal();
						
					}
					if(blackjack.getResults() == 1) {
						label3.setText("User wins with blackjack");
					}else if(blackjack.getResults() == 2) {
						label3.setText("Users win by dealer bust");
					}else if(blackjack.getResults() == 3){
						label3.setText("User wins by having a higher hand");
					}else if(blackjack.getResults() == 4) {
						label3.setText("Blackjack win for dealer");
					}else if((blackjack.getResults() == 6)) {
						label3.setText("user loses by having a lower hand");
					}else if(blackjack.getResults() == 10){
						label3.setText("Tie!");
					}
				}else {
					blackjack.stand();
				}
			}
		});
	}
	private void makeNewImg1(String cardIdstring, Pane canvas) {
		String fileString = "src/test/resources/cards/" + cardIdstring + ".bmp";
		Image img = null;
		try {
			img = new Image(new FileInputStream(fileString));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView imgView = new ImageView();
		imgView.setImage(img);
		imgView.relocate(310+(12*CounterD++), 85);
		
		canvas.getChildren().addAll(imgView);
	}

	private void makeNewImg(String cardIdString,Pane canvas){
		String fileString = "src/test/resources/cards/" + cardIdString + ".bmp";
		
		updatePlayTotal(canvas);
		updatePlaySplitTotal(canvas);
		Image img = null;
		try {
			img = new Image(new FileInputStream(fileString));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		ImageView imgView1 = new ImageView();
		imgView1.setImage(img);
		imgView1.relocate(5+(12*(Counter++)), 85);

		canvas.getChildren().addAll(imgView1);
	}
	private void updatePlayTotal(Pane canvas) {
		label.setText("Player Total: " + blackjack.getPlayTotal());
	}
	private void updatePlaySplitTotal(Pane canvas) {
		labelSplit.setText("Player Split Total: " + blackjack.getPlayerSplitTotal());
	}
	private void updateDealTotal() {
		label2.setText("Dealer Total: " + blackjack.getDealTotal());
	}
}

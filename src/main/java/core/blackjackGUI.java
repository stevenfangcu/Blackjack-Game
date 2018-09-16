package core;

import java.awt.Panel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Random;
import org.w3c.dom.css.Counter;

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
	
	Label label = new Label("Player Total: " + blackjack.getPlayTotal());
	Label label2 = new Label("Player Total: " + blackjack.getDealTotal());
	
	public static void main(String[] args) {
		launch(args);
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
		canvas.getChildren().addAll(label, label2);
	}
	
	private void addControlsToCanvas(Pane canvas) {
		hitButton = new Button("Hit");
		hitButton.relocate(5 , 210);
		Standbutton = new Button("Stand");
		Standbutton.relocate(75, 210);
		hitButtonEventHandler(canvas);
		if(blackjack.getPlayerSplit() == true) {
			SplitButton = new Button("Split");
			SplitButton.relocate(150, 210);
			canvas.getChildren().addAll(hitButton,SplitButton);
		}else {
			canvas.getChildren().addAll(hitButton);
		}
		
	}
	
	private void hitButtonEventHandler(Pane canvas) {
		hitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if(blackjack.getGameState()){
					System.out.println("Player Hit!");
					System.out.println("Update please1 " + blackjack.getPlayTotal());
					String cardIdString = blackjack.hit();
					System.out.println("Update please " + blackjack.getPlayTotal());
					makeNewImg(cardIdString, canvas);
				}else{
					hitButton.setDisable(true);
					updatePlayTotal(canvas);
					label.setText("Player busted with : " + blackjack.getPlayTotal());
					System.out.println("?");
				}
			}
			
		});
	}
	
	private void makeNewImg(String cardIdString,Pane canvas){
		String fileString = "src/test/resources/cards/" + cardIdString + ".bmp";
		System.out.print(fileString);
		updatePlayTotal(canvas);
		Image img = null;
		try {
			img = new Image(new FileInputStream(fileString));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
	
}

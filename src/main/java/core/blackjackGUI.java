package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Random;
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
	
	BlackjackGame blackjack = new BlackjackGame();
	
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
		
		Scene scene = new Scene(canvas, 320, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Simple Blackjack Game");
		primaryStage.show();
	}
	private void setupCardsAnimation(Pane canvas) { // makes the first hand for both dealer and player
		blackjack.firstHands();
		Image img = null;
		try {
			img = new Image(new FileInputStream("src/test/resources/core/cards/C2.png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageView imgView = new ImageView();
		imgView.setImage(img);
		imgView.relocate(20, 180);
		
		canvas.getChildren().addAll(imgView);
	}
	
}

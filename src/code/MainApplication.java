//import TypingGame.TypingGameGUI;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//public class MainApplication extends Application {
//
//    private Stage primaryStage;
//
//    @Override
//    public void start(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//
//        // Create a main menu layout
//        VBox menuLayout = new VBox(20);
//        menuLayout.setStyle("-fx-font-size: 24px; -fx-alignment: center;");
//
//        // Button to start the Typing Game
//        Button typingGameButton = new Button("Play Typing Game");
//        typingGameButton.setOnAction(e -> startTypingGame());
//
//        // Button to start the Number Game
//        Button numberGameButton = new Button("Play Number Game");
//        numberGameButton.setOnAction(e -> startNumberGame());
//
//        // Add buttons to the menu layout
//        menuLayout.getChildren().addAll(typingGameButton, numberGameButton);
//
//        // Set up the main menu scene
//        Scene menuScene = new Scene(menuLayout, 600, 400);
//        primaryStage.setTitle("Select Game");
//        primaryStage.setScene(menuScene);
//        primaryStage.show();
//    }
//
//    // Start Typing Game
//    private void startTypingGame() {
//        TypingGameGUI typingGameGUI = new TypingGameGUI();
//        typingGameGUI.start(primaryStage);  // Launch Typing Game
//    }
//
//    // Start Number Game
//    private void startNumberGame() {
//        NumberGame.NumberGame numberGame = new NumberGame.NumberGame();
//        numberGame.start(primaryStage);  // Launch Number Game
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package tetrisfx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.stage.WindowEvent;

public class Main extends Application {
    Game game = null;

    @Override
    public void start(Stage stage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        stage.setTitle("Tetris");
        Scene scene = new Scene(root, 360, 444);
        scene.getStylesheets().add(getClass().getResource("resources/stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        game = new Game();
        game.setBoardGraphicsContext(((Canvas)scene.lookup("#gameCanvas")).getGraphicsContext2D());
        game.setNextShapeCanvasGraphicsContext(((Canvas)scene.lookup("#nextShapeCanvas")).getGraphicsContext2D());
        game.setScoreLabel((Label)scene.lookup("#lblScore"));
        game.setNewGameButton((Label)scene.lookup("#btnNewGame"));
        game.setStopGameButton((Label)scene.lookup("#btnStopGame"));
        game.setPauseGameButton((Label)scene.lookup("#btnPauseGame"));
        game.init();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (game != null) {
                    game.destroy();
                }
            }
        });
        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (game != null && !game.isPaused()) {
                    game.pause();
                }
            }
        });
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent arg0) {
                if (game != null) {
                    try {
                        game.handleKeyEvents(arg0);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

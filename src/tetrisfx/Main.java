package tetrisfx;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    Game game = null;

    @Override
    public void start(Stage mainStage) throws Exception{
        Parent mainRoot = FXMLLoader.load(getClass().getResource("window.fxml"));
        mainStage.setTitle("Tetris");
        Scene mainScene = new Scene(mainRoot, 360, 444);
        mainScene.getStylesheets().add(getClass().getResource("resources/stylesheet.css").toExternalForm());
        mainStage.setScene(mainScene);
        mainStage.setResizable(false);
        mainStage.show();

        game = new Game();
        game.setBoardGraphicsContext(((Canvas)mainScene.lookup("#gameCanvas")).getGraphicsContext2D());
        game.setNextShapeCanvasGraphicsContext(((Canvas)mainScene.lookup("#nextShapeCanvas")).getGraphicsContext2D());
        game.setScoreLabel((Label)mainScene.lookup("#lblScore"));
        game.setHiScoreLabel((Label)mainScene.lookup("#lblHiScore"));
        game.setNewGameButton((Label)mainScene.lookup("#btnNewGame"));
        game.setStopGameButton((Label)mainScene.lookup("#btnStopGame"));
        game.setPauseGameButton((Label)mainScene.lookup("#btnPauseGame"));
        game.setBtnSaveScoreButton((Label)mainScene.lookup("#btnSaveScore"));
        game.init();

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (game != null) {
                    game.destroy();
                }
            }
        });
        mainStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (game != null && !game.isPaused()) {
                    game.pause();
                }
            }
        });
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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

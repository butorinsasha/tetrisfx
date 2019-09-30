package tetrisfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

class ScoresController extends Stage {

    private Scores scores;

    private TableView scoresTableView;
    private TextField nameTextField;
    private Label scoreLabel;
    private Button saveButton;

    public void setScoresTableView(TableView scoresTableView) {
        this.scoresTableView = scoresTableView;
    }

    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public void setNameTextField(TextField nameTextField) {
        this.nameTextField = nameTextField;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public void init(int currentScore) {

        // Assign event listeners to buttons

        saveButton.setOnMouseClicked((me) -> {
            scores.addScore(
                nameTextField.getText(),
                scores.currentScore);
            saveButton.setDisable(true);
            fillScoreTableView();
        });

        scoreLabel.setText(String.valueOf(currentScore));
    }

    // Stub for update score table
    private void fillScoreTableView() {
        return;
    }

    public ScoresController(int currentScore) throws IOException {

        Parent scoresRoot = FXMLLoader.load(getClass().getResource("scores.fxml"));
        this.setTitle("Hi-Scores");
        Scene scoresScene = new Scene(scoresRoot, 300, 400);
        this.setScene(scoresScene);
        this.setResizable(false);
        this.show();

        scores = new Scores(currentScore);

        setScoresTableView((TableView) scoresScene.lookup(("#tblScores")));
        setScoreLabel((Label) scoresScene.lookup("#lblScore"));
        setNameTextField((TextField) scoresScene.lookup("#txtfldName"));
        setSaveButton((Button) scoresScene.lookup("#btnSave"));
        init(currentScore);
    }

}

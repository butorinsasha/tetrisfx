package tetrisfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    public void addScoresTableColumn(String header, String property, int size) {
        TableColumn<String, Score> col = new TableColumn<>(header);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setPrefWidth(size);
        scoresTableView.getColumns().add(col);
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

        // Init Scores table
        // Assign event listeners to buttons

        initScoresTableView();
        refreshScoresTableView();

        scoreLabel.setText(String.valueOf(currentScore));

        saveButton.setOnMouseClicked((me) -> {
            scores.addScore(
                    nameTextField.getText(),
                    scores.currentScore
            );
            saveButton.setDisable(true);
            refreshScoresTableView();
        });
    }

    private void initScoresTableView() {
        addScoresTableColumn("Score", "score", 50);
        addScoresTableColumn("Name", "name", 250);
    }

    private void clearScoresTableView() {
        scoresTableView.getItems().clear();
    }

    private void fillScoresTableView() {
        if (scores != null && scores.getScoresList() != null && scores.getScoresList().size() != 0)
            scoresTableView.getItems().addAll(scores.getScoresList());
    }

    private void refreshScoresTableView() {
        clearScoresTableView();
        fillScoresTableView();
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

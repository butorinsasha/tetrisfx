package tetrisfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.text.TableView;
import java.io.IOException;

public class Scores extends Stage {

    private TableView tableView;
    private TextField enterNameTextField;
    private Label scoreLabel;
    private Button saveButton;
    private Button cancelButton;

    public Scores() throws IOException {
        Parent scoresRoot = FXMLLoader.load(getClass().getResource("scores.fxml"));
        this.setTitle("Hi-Scores");
        Scene scoreScene = new Scene(scoresRoot, 300, 400);
        this.setScene(scoreScene);
        this.setResizable(false);
        this.show();
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    public void setScoreLabel(Label scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public void setEnterNameTextField(TextField enterNameTextField) {
        this.enterNameTextField = enterNameTextField;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }
}

package tetrisfx;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scores {

    int currentScore;

    // An arraylist of the type "score" we will use to work with the scores inside the class
    private ArrayList<Score> scoresList;

    // The name of the file where the highscores will be saved
    // Using a binary file to keep the high-scores in, this will avoid cheating.
    private static final String SCORES_FILE = "src/tetrisfx/resources/scores.dat";

    //Initialising an in and outputStream for working with the file
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    public Scores(int currentScore) {
        this.currentScore = currentScore;
        scoresList = getScoresList();
    }

    public ArrayList<Score> getScoresList() {
        loadScoresFile(SCORES_FILE);
        return scoresList;
    }

    private void sort() {
        ScoresComparator comparator = new ScoresComparator();
        Collections.sort(scoresList, comparator);
    }

    public void addScore(String name, int score) {
        loadScoresFile(SCORES_FILE);
        scoresList.add(new Score(name, score));
        sort();
        updateScoresFile(SCORES_FILE);
    }

    public void loadScoresFile(String filename) {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(filename));
            scoresList = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Load] FileNotFoundException: " + e.getMessage());
            System.out.println("[Load] Zero-score will be set as hi-score");
            currentScore = 0;
            scoresList = new ArrayList<>();
        } catch (IOException e) {
            System.out.println("[Load] IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Load] ClassNotFoundException: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Load] IO Error: " + e.getMessage());
            }
        }
    }

    public void updateScoresFile(String filename) {
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(filename));
            outputStream.writeObject(scoresList);
        } catch (FileNotFoundException e) {
            System.out.println("[Update] FNF Error: " + e.getMessage() + ", the program will make a new file");
        } catch (IOException e) {
            System.out.println("[Update] IO Error: " + e.getMessage());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                System.out.println("[Update] Error: " + e.getMessage());
            }
        }
    }

    public int getHiScore() {
        if (scoresList.size() != 0)
            return scoresList.get(0).getScore();
        else
            return 0;
    }
}
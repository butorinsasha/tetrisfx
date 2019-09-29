package tetrisfx;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Scores {

    // An arraylist of the type "score" we will use to work with the scores inside the class
    private ArrayList<Score> scoresList;

    // The name of the file where the highscores will be saved
    // Using a binary file to keep the high-scores in, this will avoid cheating.
    private static final String SCORES_FILE = "src/tetrisfx/resources/scores.dat";

    //Initialising an in and outputStream for working with the file
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    public Scores() {
        //initialising the scores-arraylist
        scoresList = new ArrayList<Score>();
    }

    public ArrayList<Score> getScoresList() {
        loadScoresFile(SCORES_FILE);
        sort();
        return scoresList;
    }

    private void sort() {
        ScoresComparator comparator = new ScoresComparator();
        Collections.sort(scoresList, comparator);
    }

    public void addScore(String name, int score) {
        loadScoresFile(SCORES_FILE);
        scoresList.add(new Score(name, score));
        updateScoresFile(SCORES_FILE);
    }

    public void loadScoresFile(String filename) {
        try {
            inputStream = new ObjectInputStream(new FileInputStream(filename));
            scoresList = (ArrayList<Score>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("[Load] FNF Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("[Load] IO Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("[Load] CNF Error: " + e.getMessage());
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
            System.out.println("[Update] FNF Error: " + e.getMessage() + ",the program will try and make a new file");
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

    public String getHighscoreString() {
        String highscoreString = "";
        int max = 10;

        ArrayList<Score> scores;
        scores = getScoresList();

        int i = 0;
        int x = scores.size();
        if (x > max) {
            x = max;
        }
        while (i < x) {
            highscoreString += (i + 1) + ".\t" + scores.get(i).getName() + "\t\t" + scores.get(i).getScore() + "\n";
            i++;
        }
        return highscoreString;
    }
}
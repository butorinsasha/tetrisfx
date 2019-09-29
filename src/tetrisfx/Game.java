package tetrisfx;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

// (?) What this timer is for?
public class Game extends TimerTask {

    public static final int BLOCK_WIDTH = 20;
    public static final int BLOCK_MARGIN = 2;
    public static final int BOARD_ROWS = 20;
    public static final int BOARD_COLS = 10;
    public static final int REPAINT_DELAY_MS = 500;

    private GraphicsContext boardCanvas;
    private GraphicsContext nextShapeCanvas;
    private Label scoreLabel;
    private Label hiScoreLabel;
    private Label btnNewGame;
    private Label btnStopGame;
    private Label btnPauseGame;
    private Label btnSaveScore;

    private int score = 0;

    private boolean isPause = false;
    private boolean isGameRun = false;
    private boolean isGameOver = false;
    private int[][] boardArray = new int[BOARD_ROWS][BOARD_COLS];
    private Shape currentShape = null;
    private Shape nextShape = null;

    // (?) What this timer is for?
    private Timer gameTimer = new Timer();
    private int repaintDelayCounter = 0;

    public void setBoardGraphicsContext(GraphicsContext gc) {
        boardCanvas = gc;
    }

    public void setNextShapeCanvasGraphicsContext(GraphicsContext gc) {
        nextShapeCanvas = gc;
    }

    public void setScoreLabel(Label control) {
        scoreLabel = control;
    }

    public void setHiScoreLabel(Label control) {
        hiScoreLabel = control;
    }

    public void setNewGameButton(Label control) {
        btnNewGame = control;
    }

    public void setStopGameButton(Label control) {
        btnStopGame = control;
    }

    public void setPauseGameButton(Label control) {
        btnPauseGame = control;
    }

    public void setBtnSaveScoreButton(Label btnSaveScore) {
        this.btnSaveScore = btnSaveScore;
    }

    /**
     * Init game.
     *
     * @throws Exception
     */
    public void init() throws Exception {

        // Assign events listeners to buttons

        btnNewGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startNewGame();
            }
        });

        btnNewGame.setStyle("visibility: visible;");

        btnStopGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopGame();
            }
        });

        btnPauseGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                pause();
            }
        });

        btnSaveScore.setOnMouseClicked(event -> {
            try {
                new ScoresController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        drawBoardBackground();

        repaintDelayCounter = 0;

        // (?) What this timesr is for?
        gameTimer.schedule(this, 0, 1);
    }

    public void destroy() {

        // (?) What this timesr is for
        gameTimer.cancel();
    }

    public void pause() {
        isPause = !isPause;
        btnPauseGame.setText(isPause ? "RESUME" : "PAUSE");
    }

    public boolean isPaused() {
        return isPause;
    }

    public void handleKeyEvents(KeyEvent e) throws CloneNotSupportedException {
        if (currentShape == null) return;
        switch (e.getCode()) {
            case UP:
                if (canRotate()) currentShape.rotateClockwise();
                break;

            case DOWN:
                if (canMoveDown()) currentShape.moveDown();
                break;

            case LEFT:
                if (canMoveLeft()) currentShape.moveLeft();
                break;

            case RIGHT:
                if (canMoveRight()) currentShape.moveRight();
                break;
        }
    }

    /*-----------------------------------------------------------------------------*/

    private void startNewGame() {
        currentShape = null;
        nextShape = null;
        score = 0;
        isPause = false;
        isGameRun = true;
        isGameOver = false;

        // show/hide buttons & UI
        btnNewGame.setStyle("visibility: hidden;");
        btnStopGame.setStyle("visibility: visible;");
        btnPauseGame.setStyle("visibility: visible;");
        btnSaveScore.setStyle("visibility: hidden");
        btnPauseGame.setText("PAUSE");
        scoreLabel.setText("0");
        hiScoreLabel.setText(String.valueOf(Integer.MAX_VALUE));

        clearBoardArray();
        putNextShapeOnBoard();
        repaint();
    }

    private void stopGame() {
        isGameRun = false;
        isPause = false;

        // show/hide buttons & UI
        btnNewGame.setStyle("visibility: visible;");
        btnStopGame.setStyle("visibility: hidden;");
        btnPauseGame.setStyle("visibility: hidden;");

        // remove currentShape from board
        if (!isGameOver) {
            currentShape = null;
            repaint();
        }

        // clear next shape preview
        nextShapeCanvas.setFill(Color.web("#373737"));
        nextShapeCanvas.fillRect(0, 0, 200, 200);
    }

    /**
     * Game "main loop"
     */
    public void run() {
        Platform.runLater(new Runnable() {
            public void run() {
                if (isPause || !isGameRun) return;
                if (++repaintDelayCounter >= REPAINT_DELAY_MS) {
                    repaintDelayCounter = 0;
                    updateGame();
                }
                repaint();
            }
        });
    }

    private void updateGame() {
        if (canMoveDown()) {
            currentShape.moveDown();
        } else {
            putNextShapeOnBoard();
        }
    }

    private void repaint() {
        drawBoardBackground();
        drawBoardBlocks();
        if (currentShape != null) {
            currentShape.draw(boardCanvas);
        }

        if (isGameOver) {
            drawGameOverMessage();
            btnSaveScore.setStyle("visibility: visible");
        }
    }

    /**
     * Check if possible current shape move down
     *
     * @return boolean
     */
    private boolean canMoveDown() {
        return canShapePlaced(currentShape, 0, 1);
    }

    private boolean canMoveLeft() {
        return currentShape.getX() > 0 && canShapePlaced(currentShape, -1, 0);
    }

    private boolean canMoveRight() {
        return (currentShape.getX() + currentShape.getWidth() < BOARD_COLS) && canShapePlaced(currentShape, 1, 0);
    }

    /**
     * Check if possible to rotate currentShape
     *
     * @return boolean
     */
    private boolean canRotate() throws CloneNotSupportedException {
        Shape rotatedShape;
        rotatedShape = currentShape.clone();
        rotatedShape.rotateClockwise();
        if (currentShape.getY() + rotatedShape.getHeight() >= BOARD_ROWS) return false;
        if (currentShape.getX() + rotatedShape.getWidth() > BOARD_COLS) return false;
        return canShapePlaced(rotatedShape, 0, 0);
    }

    private boolean canShapePlaced(Shape shape, int dx, int dy) {
        int[][] shapeArray = shape.getArray();
        for (int y = 0; y < shape.getHeight(); y++) {
            for (int x = 0; x < shape.getWidth(); x++) {
                if (shapeArray[y][x] == 0) continue;
                if (currentShape.getY() + y < 0) continue;
                if (currentShape.getY() + y + dy >= BOARD_ROWS) return false;
                if (boardArray[currentShape.getY() + y][currentShape.getX() + x] != 0) return false;
                if (dx != 0 && boardArray[currentShape.getY() + y][currentShape.getX() + x + dx] != 0) return false;
                if (dy != 0 && boardArray[currentShape.getY() + y + dy][currentShape.getX() + x] != 0) return false;
            }
        }
        return true;
    }

    private boolean isGameOver() {
        for (int i = 0; i < BOARD_COLS; i++) {
            if (boardArray[0][i] != 0) return true;
        }
        return false;
    }

    private void putNextShapeOnBoard() {
        if (currentShape != null) {
            saveCurrentShapeOnBoard();

            if (!clearLines()) {
                updateScore(1);
            }

            if (isGameOver()) {
                stopGame();
                isGameOver = true;
                return;
            }
        }

        currentShape = nextShape == null ? new Shape() : nextShape;
        nextShape = new Shape();
        nextShape.drawPreview(nextShapeCanvas);
    }

    private void saveCurrentShapeOnBoard() {

        // hack that help to prevent very strange bug with modification of boardArray's value
        int[][] boardArrayCopy = copyBoardArray();

        int[][] shapeArray = currentShape.getArray();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (shapeArray[y][x] != 0 && currentShape.getY() + y >= 0) {
                    //boardArray[currentShape.getY() + y][currentShape.getX() + x] = shapeArray[y][x];
                    boardArrayCopy[currentShape.getY() + y][currentShape.getX() + x] = shapeArray[y][x];
                }
            }
        }

        boardArray = boardArrayCopy;
    }

    private int[][] copyBoardArray() {
        int[][] newBoardArray = new int[BOARD_ROWS][BOARD_COLS];
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                newBoardArray[row][col] = boardArray[row][col];
            }
        }
        return newBoardArray;
    }

    private boolean clearLines() {
        boolean isAnyLinesCleared = false;
        int row = BOARD_ROWS - 1;
        while (row >= 0) {
            boolean isRowFull = true;
            for (int i = 0; i < BOARD_COLS; i++) {
                if (boardArray[row][i] == 0) {
                    isRowFull = false;
                    break;
                }
            }

            if (isRowFull) {
                isAnyLinesCleared = true;
                for (int i = row; i >= 1; i--) {
                    boardArray[i] = boardArray[i - 1];
                }
                if (row > 0) {
                    for (int i = 0; i < BOARD_COLS; i++) {
                        boardArray[0][i] = 0;
                    }
                }
                updateScore(10);
                continue;
            }

            row--;
        }

        return isAnyLinesCleared;
    }

    private void updateScore(int score) {
        this.score += score;
        scoreLabel.setText(String.valueOf(this.score));
    }

    private void clearBoardArray() {
        for(int y=0; y<BOARD_ROWS; y++) {
            for(int x=0; x<BOARD_COLS; x++) {
                boardArray[y][x] = 0;
            }
        }
    }

    private void drawBoardBackground() {
        boardCanvas.setFill(Color.web("#272727"));
        boardCanvas.fillRect(
                0,
                0,
                BOARD_COLS * (BLOCK_WIDTH + BLOCK_MARGIN) + BLOCK_MARGIN,
                BOARD_ROWS * (BLOCK_WIDTH + BLOCK_MARGIN) + BLOCK_MARGIN);

        boardCanvas.setFill(Color.web("#393939"));
        for (int i = 0; i < BOARD_COLS; i++) {
            for (int j = 0; j < BOARD_ROWS; j++) {
                boardCanvas.fillRect(
                        i * (BLOCK_WIDTH + BLOCK_MARGIN) + BLOCK_MARGIN,
                        j * (BLOCK_WIDTH + BLOCK_MARGIN) + BLOCK_MARGIN,
                        BLOCK_WIDTH,
                        BLOCK_WIDTH);
            }
        }
    }

    private void drawBoardBlocks() {
        for (int y = 0; y < BOARD_ROWS; y++) {
            for (int x = 0; x < BOARD_COLS; x++) {
                if (boardArray[y][x] != 0) {
                    Shape.drawBlock(boardCanvas, boardArray[y][x], x, y);
                }
            }
        }
    }

    private void drawGameOverMessage() {
        boardCanvas.setFill(Color.rgb(10, 10, 10, 0.85));
        boardCanvas.fillRect(2, 2, 218, 440);
        boardCanvas.setFont(new Font(32));
        boardCanvas.setFill(Color.WHITE);
        boardCanvas.fillText("GAME OVER", 20, 230);
    }

}

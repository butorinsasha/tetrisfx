package tetrisfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Shape implements Cloneable {

    private int x = 0;
    private int y = 0;
    private int shapeType = 0;
    private int[][] shapeArray = new int[4][4];
    private Color shapeColor = null;
    private static final Color[] shapeColors = new Color[] {
        Color.RED,
        Color.GREEN,
        Color.AZURE,
        Color.YELLOW,
        Color.MAGENTA,
        Color.CYAN,
        Color.ORANGE
    };

    public Shape() {
        shapeType = new Random().nextInt(7)+1;
        switch (shapeType) {
            case 1:
                shapeArray = new int[][]{
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                    {1, 0, 0, 0},
                };
                break;

            case 2:
                shapeArray = new int[][]{
                    {2, 2, 0, 0},
                    {0, 2, 2, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                };
                break;

            case 3:
                shapeArray = new int[][]{
                    {0, 3, 3, 0},
                    {3, 3, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                };
                break;

            case 4:
                shapeArray = new int[][]{
                    {0, 4, 0, 0},
                    {4, 4, 4, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                };
                break;

            case 5:
                shapeArray = new int[][]{
                    {5, 5, 0, 0},
                    {5, 5, 0, 0},
                    {0, 0, 0, 0},
                    {0, 0, 0, 0},
                };
                break;

            case 6:
                shapeArray = new int[][]{
                    {6, 0, 0, 0},
                    {6, 0, 0, 0},
                    {6, 6, 0, 0},
                    {0, 0, 0, 0},
                };
                break;

            case 7:
                shapeArray = new int[][]{
                    {0, 7, 0, 0},
                    {0, 7, 0, 0},
                    {7, 7, 0, 0},
                    {0, 0, 0, 0},
                };
                break;
        }

        shapeColor = shapeColors[shapeType-1];

        int rotateCount = new Random().nextInt(4);
        for (int i = 0; i <= rotateCount; i++) {
            rotateClockwise();
        }

        x = (Game.BOARD_COLS - getWidth())/2;
        y = -getHeight() + 1;
    }

    public int getX() {
        return x;
    }

    public void setX(int newX) {
        x = newX;
    }

    public int getY() {
        return y;
    }

    public void setY(int newY) {
        y = newY;
    }

    public int getType() {
        return shapeType;
    }

    public int[][] getArray() {
        return shapeArray;
    }

    public Color getColor() {
        return shapeColor;
    }

    public static Color getColor(int shapeType) {
        return Shape.shapeColors[shapeType-1];
    }

    public void draw(GraphicsContext gc) {
        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                if (shapeArray[i][j] != 0) {
                    Shape.drawBlock(gc, this.getType(), x + j, y + i);
                }
            }
        }
    }

    public void drawPreview(GraphicsContext gc) {
        gc.setFill(Color.web("#373737"));
        gc.fillRect(0, 0, 200, 200);

        gc.setFill(shapeColor);
        for(int y=0; y<4; y++) {
            for(int x=0; x<4; x++) {
                if (shapeArray[y][x] != 0) {
                    int dx = (6 - getWidth()) * (Game.BLOCK_WIDTH + Game.BLOCK_MARGIN) / 2;
                    int dy = (4 - getHeight()) * (Game.BLOCK_WIDTH + Game.BLOCK_MARGIN) / 2;
                    gc.fillRect(
                        dx + x * (Game.BLOCK_WIDTH + Game.BLOCK_MARGIN) + Game.BLOCK_MARGIN,
                        dy + y * (Game.BLOCK_WIDTH + Game.BLOCK_MARGIN) + Game.BLOCK_MARGIN,
                        Game.BLOCK_WIDTH,
                        Game.BLOCK_WIDTH);
                }
            }
        }
    }

    // Draw a single block
    public static void drawBlock(GraphicsContext gc, int shapeType, int x, int y) {
        gc.setFill(Shape.getColor(shapeType));
        gc.fillRect(
            x * (Game.BLOCK_WIDTH + Game.BLOCK_MARGIN) + Game.BLOCK_MARGIN,
            y * (Game.BLOCK_WIDTH + Game.BLOCK_MARGIN) + Game.BLOCK_MARGIN,
            Game.BLOCK_WIDTH,
            Game.BLOCK_WIDTH);
    }

    public void moveDown() {
        y++;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void rotateClockwise() {
        // check square
        if (shapeType == 5) return;

        int[][] newShapeArray = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        for(int x=0; x<getHeight(); x++){
            for(int y=0; y<getWidth(); y++){
                newShapeArray[y][getHeight()-x-1] = shapeArray[x][y];
            }
        }

        shapeArray = newShapeArray;
    }

    public void rotateCounterClockwise() {
        // check square
        if (shapeType == 5) return;

        int[][] newShapeArray = new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        for(int x=0; x<getHeight(); x++){
            for(int y=0; y<getWidth(); y++){
                newShapeArray[getWidth()-y-1][x] = shapeArray[x][y];
            }
        }

        shapeArray = newShapeArray;
    }

    public int getWidth() {
        int width = 0, tmpWidth = 0;
        for(int y=0; y<4; y++){
            tmpWidth = 0;
            for(int x=3; x>=0; x--){
                if (shapeArray[y][x] != 0) {
                    tmpWidth = x;
                    break;
                }
            }
            if (tmpWidth > width) {
                width = tmpWidth;
            }
        }
        return width + 1;
    }

    public int getHeight() {
        int height = 0, tmpHeight = 0;
        for(int x=0; x<4; x++){
            tmpHeight = 0;
            for(int y=3; y>=0; y--){
                if (shapeArray[y][x] != 0) {
                    tmpHeight = y;
                    break;
                }
            }
            if (tmpHeight > height) {
                height = tmpHeight;
            }
        }
        return height + 1;
    }

    public Shape clone() throws CloneNotSupportedException {
        return (Shape)super.clone();
    }

}

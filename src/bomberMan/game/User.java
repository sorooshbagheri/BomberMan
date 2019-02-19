package bomberMan.game;

import bomberMan.game.gameObjects.GameBoard;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int score;
    private int bombLimit = 6;
    private boolean bombControl = true;
    private int speedLevel = 3;
    private int maxSpeedLevel = 6;
    private int bombRadius = 2;
    private boolean ghostMode = false;

    private LevelManager lvlManager;
    private int currentLvl;
    private int row, column;

    public User(LevelManager lvlManager,int row,int col) {
        this.lvlManager = lvlManager;
        currentLvl = 1;
        this.row = row;
        this.column = col;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setCurrentLvl(int currentLvl) {
        this.currentLvl = currentLvl;
    }

    public boolean isGhostMode() {
        return ghostMode;
    }

    public int getBombLimit() {
        return bombLimit;
    }

    public boolean hasBombControl() {
        return bombControl;
    }

    public int getBombRadius() {
        return bombRadius;
    }

    public int getScore() {
        return score;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setGhostMode(boolean ghostMode) {
        this.ghostMode = ghostMode;
    }

    public void setBombControl(boolean bombControl) {
        this.bombControl = bombControl;
    }

    public void increaseBombsLimit() {
        bombLimit++;
    }

    public void increaseBombRadius(GameBoard gameBoard) {
        if (bombRadius < gameBoard.getMaxBombRadius()){
            bombRadius++;
        }
    }

    public void increaseSpeed() {
        if (speedLevel < maxSpeedLevel) {
            speedLevel++;
        }
    }

    public void increaseScore(int increment) {
        score += increment;
    }

    public void decreaseBombsLimit() {
        bombLimit = Math.max(1,bombLimit-1);
    }

    public void decreaseBombRadius() {
        bombRadius = Math.max(1,bombRadius-1);
    }

    public void decreaseSpeed() {
        speedLevel = Math.max(2,speedLevel-1);
    }

    public void decreaseScore(int decrement) {
        score = Math.max(0,score - decrement);
    }
}

package com.javarush.games.minesweeper;

import com.javarush.engine.cell.*;


import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {

    private static final int SIDE = 9;
    private GameObject [][] gameField = new GameObject [SIDE][SIDE];
    private int countMinesOnField;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private int countFlags;
    private boolean isGameStopped = false;
    private int countClosedTiles = SIDE*SIDE;
    private int score = 0;


    public void initialize(){
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    public void onMouseLeftClick(int x, int y){
        if (isGameStopped){ restart(); }
        else { openTile(x, y); }

    }

    public void onMouseRightClick(int x, int y) { markTile(x, y); }

    private void createGame(){
        int count = 0;
        for (int i=0; i<gameField.length; i++){
            for (int j=0; j<gameField[i].length; j++){
                setCellValue(j, i, "");
                int mine =  getRandomNumber(10);
                if (mine == 5) {
                    gameField[j][i] = new GameObject(i, j, true);
                    count++;
                }
                else {
                    gameField[j][i] = new GameObject(i, j, false);
                }

                setCellColor(j, i, Color.ORANGE);

            }
        }
        countMinesOnField = count;
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private void markTile(int x, int y){
        if (isGameStopped) {return;}
        if (!gameField[y][x].isOpen){
            if (!gameField[y][x].isFlag && countFlags>0){
                gameField[y][x].isFlag = true;
                countFlags--;
                setCellValue(x, y, FLAG);
                setCellColor(x, y, Color.RED);
            }
            else if (gameField[y][x].isFlag && countFlags>=0){
                gameField[y][x].isFlag = false;
                countFlags++;
                setCellValue(x, y, "");
                setCellColor(x, y, Color.ORANGE);
            }
        }

    }

    private void openTile(int x, int y){

        if (!gameField[y][x].isFlag && !gameField[y][x].isOpen && !isGameStopped){

            gameField[y][x].isOpen = true;
            countClosedTiles--;
            setCellColor(x, y, Color.GREEN);

            if (gameField[y][x].isMine){
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            }
            else {
                score = score + 5;
                setScore(score);
                setCellNumber(x, y, gameField[y][x].countMineNeighbors);
                if (gameField[y][x].countMineNeighbors == 0 && !gameField[y][x].isMine){
                    setCellValue(x,y,"");
                    List<GameObject> listOfNeighbors = getNeighbors (gameField[y][x]);
                    for (GameObject neighbor : listOfNeighbors) {
                        if (!neighbor.isMine && !neighbor.isOpen)
                            openTile(neighbor.x, neighbor.y);
                    }
                }
            }
        }
        if (countClosedTiles==countMinesOnField && !gameField[y][x].isMine){
            win();
        }
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "VICTORY!!!", Color.RED, 56);
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "BOOOOOM!!!", Color.RED, 56);
    }

    private void restart(){
        isGameStopped = false;
        countClosedTiles = SIDE*SIDE;
        score = 0;
        countMinesOnField = 0;
        setScore(score);
        createGame();
    }

    private void countMineNeighbors(){
        for (int i = 0; i<SIDE; i++){
            for (int j = 0; j<SIDE; j++){
                if (!gameField[j][i].isMine){
                    List<GameObject> listOfMinedNeighbors = getNeighbors(gameField[j][i]);
                    for (GameObject object: listOfMinedNeighbors) {
                        if (object.isMine) gameField[j][i].countMineNeighbors++;
                    }
                }
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject cell){
        ArrayList<GameObject> listOfNeighbors = new ArrayList<>();
        try{
            listOfNeighbors.add(gameField[cell.y-1][cell.x-1]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y-1][cell.x]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y][cell.x-1]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y+1][cell.x-1]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y-1][cell.x+1]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y+1][cell.x+1]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y][cell.x+1]);
        }
        catch (IndexOutOfBoundsException e){}
        try{
            listOfNeighbors.add(gameField[cell.y+1][cell.x]);
        }
        catch (IndexOutOfBoundsException e){}

        return listOfNeighbors;
    }
}


package ru.webgrozny.fifteenpuzzle.model;

import java.io.Serializable;

public class Game implements Serializable{
    private Board mBoard;
    private int moves;
    private int size;

    public Game(int size){
        this.size = size;
        this.start();
    }

    public void start(){
        mBoard = new Board(size);
        moves = 0;
    }

    public boolean move(Puzzle pzl){
        boolean ret = false;
        if(!isWin()) {
            if (mBoard.move(pzl)) {
                moves++;
                ret = true;
            }
        }
        return ret;
    }

    public void setHardShuffle(boolean hardShuffle){
        mBoard.setHardShuffle(hardShuffle);
    }

    public boolean getHardShuffle(){
        return mBoard.hardShuffle;
    }

    public boolean isWin(){
        Puzzle[] puzzles = new Puzzle[size * size];
        int n = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                puzzles[n++] = mBoard.getPuzzle(new Puzzle(j, i));
            }
        }
        int prevNum = 0;
        boolean good = true;
        for(int i = 0; i < size * size; i++){
            int cNum = puzzles[i].getNumber() == 0 ? 100 : puzzles[i].getNumber();
            if(cNum < prevNum){
                good = false;
                break;
            }
            prevNum = cNum;
        }
        return good;
    }

    public int getNumber(int x, int y){
        return mBoard.getPuzzle(new Puzzle(x, y)).getNumber();
    }

    public int getMoves(){
        return moves;
    }

    public void shuffle(){
        moves = 0;
        mBoard.shuffle();
    }

}

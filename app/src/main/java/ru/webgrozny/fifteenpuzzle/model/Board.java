package ru.webgrozny.fifteenpuzzle.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Board implements Serializable{
    int size;
    Puzzle[][] puzzles;
    boolean hardShuffle = false;

    public Board(int size) {
        this.size = size;
        puzzles = new Puzzle[size][size];

        initPuzzles();
        shuffle();
    }

    public void initPuzzles() {
        int num = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                puzzles[j][i] = new Puzzle(j, i, num++);
            }
        }
        puzzles[size - 1][size -1].setNumber(0);
    }

    public void shuffle() {
        if (hardShuffle) {
            hardShuffle();
        } else {
            softShuffle();
        }
    }

    public void setHardShuffle(boolean hardShuffle){
        this.hardShuffle = hardShuffle;
    }

    private void hardShuffle() {
        int n = 0;
        Puzzle[] flatPuzzles = new Puzzle[size * size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                flatPuzzles[n++] = puzzles[j][i];
            }
        }
        List<Puzzle> puzzlesList = Arrays.asList(flatPuzzles);
        Collections.shuffle(puzzlesList);
        n = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                puzzles[j][i] = puzzlesList.get(n++);
            }
        }
    }

    private void softShuffle() {
        initPuzzles();
        Random random = new Random();
        for(int i = 0; i < 1000; i++){
            int num = random.nextInt(4);
            Puzzle empty = findEmpty();
            switch (num){
                case 0: move(new Puzzle(empty.x, empty.y + 1)); break;
                case 1: move(new Puzzle(empty.x, empty.y - 1)); break;
                case 2: move(new Puzzle(empty.x + 1, empty.y)); break;
                case 3: move(new Puzzle(empty.x - 1, empty.y)); break;
            }
        }
    }

    public Puzzle findEmpty(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(isEmpty(new Puzzle(j, i))){
                    return puzzles[j][i];
                }
            }
        }
        return new Puzzle(0, 0);
    }

    public boolean move(Puzzle pzl){
        boolean movable = false;
        if(isPuzzleOnBoard(pzl) && !puzzles[pzl.x][pzl.y].empty()){
            //find move direction
            int curX = pzl.x;
            int curY = pzl.y;
            Puzzle moveTo = new Puzzle(curX + 1, curY);
            if(isEmpty(moveTo)){
                movable = true;
            }else{
                moveTo = new Puzzle(curX - 1, curY);
                if(isEmpty(moveTo)){
                    movable = true;
                }else{
                    moveTo = new Puzzle(curX, curY + 1);
                    if(isEmpty(moveTo)){
                        movable = true;
                    }else{
                        moveTo = new Puzzle(curX, curY - 1);
                        if(isEmpty(moveTo)){
                            movable = true;
                        }
                    }
                }
            }
            if(movable){
                int num = puzzles[pzl.x][pzl.y].number;
                puzzles[pzl.x][pzl.y].setNumber(0);
                puzzles[moveTo.x][moveTo.y].setNumber(num);
            }
        }
        return movable;
    }

    private boolean isPuzzleOnBoard(Puzzle puzzle){
        return (puzzle.x >= 0 && puzzle.x < size && puzzle.y >= 0 && puzzle.y < size);
    }

    private boolean isEmpty(Puzzle puzzle){
        return isPuzzleOnBoard(puzzle) && puzzles[puzzle.x][puzzle.y].empty();
    }

    public Puzzle getPuzzle(Puzzle pzl){
        return puzzles[pzl.x][pzl.y];
    }
}

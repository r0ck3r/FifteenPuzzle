package ru.webgrozny.fifteenpuzzle.model;

import java.io.Serializable;

public class Puzzle implements Serializable{
    int number;
    int x;
    int y;
    public Puzzle(int x, int y, int number){
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public Puzzle(int x, int y){
        this(x, y, 0);
    }

    public boolean empty(){
        return number == 0;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
}

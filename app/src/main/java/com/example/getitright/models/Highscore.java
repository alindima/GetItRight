package com.example.getitright.models;

public class Highscore implements Comparable<Highscore> {
    public String name;
    public Integer score;
    @Override
    public int compareTo(Highscore hs){
     return hs.score - this.score;
    }
}

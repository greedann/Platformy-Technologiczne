package com.lab2;

public class Answer {
    public boolean is_completed;
    public int persent_of_completion;
    public int id;
    public float ans;
    public Answer(int id) {
        this.id = id;
        ans =0;
        persent_of_completion =0;
        is_completed=false;
    }

    @Override
    public String toString() {
        return String.valueOf(id)+ ", "+String.valueOf(is_completed)+ ", "+String.valueOf(persent_of_completion)+ ", "+String.valueOf(ans);
    }
}
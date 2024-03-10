package lab3;

import java.io.Serializable;

public class Messsage implements Serializable {
    private int number;
    private String content;

    public Messsage(int number, String content) {
        this.number = number;
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "number=" + number + ", content='" + content + '\'';
    }
}

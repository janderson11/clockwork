package app.main.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Card implements Comparable {
    private int value;
    private int group;

    public Card(int value, int group) {
        this.value = value;
        this.group = group;
    }

    public int getValue() {
        return value;
    }

    public int getGroup() {
        return group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return value == card.value &&
                group == card.group;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        Card that = (Card) o;
        if (this.getValue() > that.getValue()) {
            return 1;
        } else if(this.getValue() < that.getValue()) {
            return -1;
        } else {
            return 0;
        }
    }
}

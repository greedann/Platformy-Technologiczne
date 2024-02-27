package com.lab;

import java.util.TreeMap;
import java.util.TreeSet;

public class Mage implements Comparable<Mage> {
    String name;
    int level;
    double power;
    private TreeSet<Mage> apprentices;

    public Mage(String name, int level, double power, TreeSet<Mage> apprentices) {
        this.name = name;
        this.level = level;
        this.power = power;
        this.apprentices = apprentices;
    }

    public Mage(String name, int level, double power) {
        this.name = name;
        this.level = level;
        this.power = power;
        this.apprentices = new TreeSet<>();
    }

    // text representation of the object Mage{name='', level=, power=}
    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", power=" + power +
                '}';
    }

    // equals() method compares the object with the specified object for equality
    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        Mage mage = (Mage) obj;
        return level == mage.level && Double.compare(mage.power, power) == 0 && name.equals(mage.name);
    }

    // hashCode() method returns a hash code value for the object
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + level;
        result = 31 * result + name.hashCode();
        result = 31 * result + (int) power;
        return result;
    }

    @Override
    public int compareTo(Mage mage) {
        if (name == mage.name) {
            if (level == mage.level) {
                if (power == mage.power) {
                    return 0;
                } else {
                    return power > mage.power ? 1 : -1;
                }
            } else {
                return level - mage.level;
            }
        } else {
            return name.compareTo(mage.name);
        }
    }

    public void print() {
        print(0);
    }

    public void print(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("-");
        }
        System.out.println(this);
        for (Mage apprentice : apprentices) {
            apprentice.print(depth + 1);
        }
    }

    public void addApprentice(Mage apprentice) {
        apprentices.add(apprentice);
    }

    public TreeMap<Mage, Integer> countApprentices() {
        TreeMap<Mage, Integer> stat;
        stat = new TreeMap<>();
        stat.put(this, countApprentices(stat));
        return stat;
    }

    private int countApprentices(TreeMap<Mage, Integer> stat) { // prints the number of apprentices uncluding the
                                                                // apprentices of
        // apprentices
        int count = 0;
        for (Mage apprentice : apprentices) {
            count++;
            count += apprentice.countApprentices(stat);
        }
        stat.put(this, count);
        return count;
    }
}

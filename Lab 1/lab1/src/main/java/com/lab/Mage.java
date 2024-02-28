package com.lab;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Mage implements Comparable<Mage> {
    String name;
    int level;
    double power;
    private Set<Mage> apprentices;

    public Mage(String name, int level, double power, String sorting) {
        this.name = name;
        this.level = level;
        this.power = power;
        if (sorting == "none") {
            apprentices = new HashSet<>();
        } else if (sorting == "natural") {
            apprentices = new TreeSet<>();
        } else if (sorting == "alternative") {
            apprentices = new TreeSet<>(new MageComparator());
        } else {
            System.err.println("unsupported sorting type");
        }
    }

    public Mage(String name, int level, double power) {
        this.name = name;
        this.level = level;
        this.power = power;
        this.apprentices = new HashSet<>();
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
        print(0, "1.");
    }

    private void print(int depth, String prefix) {
        System.out.print(prefix + ' ');
        System.out.println(this);
        int i = 0;
        for (Mage apprentice : apprentices) {
            i++;
            apprentice.print(depth + 1, prefix + i + '.');
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

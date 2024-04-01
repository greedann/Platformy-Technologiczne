package com.lab5;

public class Mage {
    private String name;
    private int level;

    public Mage(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Mage{" +
                "name='" + name + '\'' +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Mage mage = (Mage) obj;
        return level == mage.level && name.equals(mage.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + level;
        return result;
    }

}

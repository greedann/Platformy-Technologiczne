package com.lab;

import java.util.Comparator;

public class MageComparator implements Comparator<Mage> {
    @Override
    public int compare(Mage mage1, Mage mage2) {
        if (mage1.level == mage2.level) {
            if (mage1.name == mage2.name) {
                if (mage1.power == mage2.power) {
                    return 0;
                } else {
                    return mage1.power > mage2.power ? 1 : -1;
                }
            } else {
                return mage1.name.compareTo(mage2.name);
            }
        } else {
            return mage1.level - mage2.level;
        }
    }
}

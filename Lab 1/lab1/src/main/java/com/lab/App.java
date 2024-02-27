package com.lab;

import java.util.TreeSet;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( args[0]);
        Mage mage1 = new Mage("asdf", 34, 23);
        Mage mage2 = new Mage("csedfggh", 34, 65);
        Mage mage3 = new Mage("bdfggf", 94, 26);
        Mage mage5 = new Mage("sdf44gf", 24, 26);
        TreeSet<Mage> mages = new TreeSet<>(new MageComparator());
        mages.add(mage1);
        mages.add(mage2);
        mages.add(mage3);
        mage3.addApprentice(mage5);
        Mage mage4 = new Mage("sdfggf", 95, 26, mages);
        mage4.print();
        return;
    }
}

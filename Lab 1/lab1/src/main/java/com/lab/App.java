package com.lab;

public class App 
{
    public static void main( String[] args )
    {
        String sorting = "natural";
        if (args.length>0) {
            sorting = args[0];
        }
        Mage mage1 = new Mage("asdf", 34, 23, sorting);
        Mage mage2 = new Mage("csedfggh", 34, 65, sorting);
        Mage mage3 = new Mage("bdfggf", 94, 26, sorting);
        Mage mage4 = new Mage("sdfggf", 95, 26, sorting);
        Mage mage5 = new Mage("sdf44gf", 24, 26, sorting);
        Mage mage6 = new Mage("fgjhs", 83, 23, sorting);
        Mage mage7 = new Mage("gjsdfz", 69, 34, sorting);
        Mage mage8 = new Mage("bfdadn", 84, 87, sorting);
        Mage mage9 = new Mage("cnmxjs", 94, 37, sorting);
        Mage mage10 = new Mage("kbgjasv", 65, 27, sorting);
        mage10.addApprentice(mage9);
        mage9.addApprentice(mage8);
        mage9.addApprentice(mage7);
        mage7.addApprentice(mage6);
        mage10.addApprentice(mage5);
        mage5.addApprentice(mage4);
        mage5.addApprentice(mage3);
        mage3.addApprentice(mage2);
        mage2.addApprentice(mage1);
        mage10.print();
        System.out.println(mage10.countApprentices());
    }
}

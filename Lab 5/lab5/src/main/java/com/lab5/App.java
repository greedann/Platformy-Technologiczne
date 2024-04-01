package com.lab5;

public class App {
    public static void main(String[] args) {
        MageRepository mageRepository = new MageRepository();
        MageController mageController = new MageController(mageRepository);

        System.out.println(mageController.save("mage1", "12"));
        System.out.println(mageController.save("mage2", "13"));
        System.out.println(mageController.save("mage3", "12"));

        System.out.println(mageController.find("mage1"));
        System.out.println(mageRepository.find("mage4"));

        mageRepository.delete("mage4");
    }
}

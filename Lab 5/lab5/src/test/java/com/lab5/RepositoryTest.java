package com.lab5;

import org.junit.Test;

import static org.junit.Assert.*;

public class RepositoryTest {

    @Test
    public void findUnExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        assertFalse(mageRepository.find("mage4").isPresent());

    }

    @Test
    public void findExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        assertTrue(mageRepository.find("mage1").isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteUnExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        mageRepository.delete("mage4");
    }

    @Test
    public void deleteExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        mageRepository.delete("mage1");
        assertFalse(mageRepository.find("mage1").isPresent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        mageRepository.save(new Mage("mage1", 12));
    }

    @Test
    public void saveNewMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        assertTrue(mageRepository.find("mage1").isPresent());
    }
}

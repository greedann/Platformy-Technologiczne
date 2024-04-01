package com.lab5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RepositoryTest {

    @Test
    public void findUnexistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        assertEquals(mageRepository.find("mage4").isPresent(), false);

    }

    @Test
    public void findExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        assertEquals(mageRepository.find("mage1").isPresent(), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteUnexistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        mageRepository.delete("mage4");
    }

    @Test
    public void deleteExistingMage() {
        MageRepository mageRepository = new MageRepository();

        mageRepository.save(new Mage("mage1", 12));
        mageRepository.delete("mage1");
        assertEquals(mageRepository.find("mage1").isPresent(), false);
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
        assertEquals(mageRepository.find("mage1").isPresent(), true);
    }
}

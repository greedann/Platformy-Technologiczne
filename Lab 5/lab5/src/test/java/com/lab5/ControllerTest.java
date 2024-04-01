package com.lab5;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ControllerTest {

    @Test
    public void findUnexistingMage() {
        MageRepository mageRepository = mock(MageRepository.class);
        MageController mageController = new MageController(mageRepository);

        when(mageRepository.find("mage1")).thenReturn(java.util.Optional.of(new Mage("mage1", 12)));
        assertEquals(mageController.find("mage4"), "not found");
    }

    @Test
    public void findExistingMage() {
        MageRepository mageRepository = mock(MageRepository.class);
        MageController mageController = new MageController(mageRepository);

        Mage mage = new Mage("mage1", 12);
        when(mageRepository.find("mage1")).thenReturn(java.util.Optional.of(mage));
        assertEquals(mageController.find("mage1"), mage.toString());
    }

    @Test
    public void deleteUnexistingMage() {
        MageRepository mageRepository = mock(MageRepository.class);
        MageController mageController = new MageController(mageRepository);
        doThrow(new IllegalArgumentException()).when(mageRepository).delete("mage4");
        assertEquals(mageController.delete("mage4"), "not found");
       }

    @Test
    public void deleteExistingMage() {
        MageRepository mageRepository = mock(MageRepository.class);
        MageController mageController = new MageController(mageRepository);

        when(mageRepository.find("mage1")).thenReturn(java.util.Optional.of(new Mage("mage1", 12)));
        assertEquals(mageController.delete("mage1"), "done");

    }

    @Test
    public void saveExistingMage() {
        MageRepository mageRepository = mock(MageRepository.class);
        MageController mageController = new MageController(mageRepository);

        doThrow(new IllegalArgumentException()).when(mageRepository).save(new Mage("mage1", 12));
        assertEquals(mageController.save("mage1", "12"), "bad request");
    }

    @Test
    public void saveNewMage() {
        MageRepository mageRepository = mock(MageRepository.class);
        MageController mageController = new MageController(mageRepository);
        doNothing().when(mageRepository).save(new Mage("Marcus", 12));
        assertEquals(mageController.save("mage1", "12"), "done");
    }

}

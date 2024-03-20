package org.example;

import javax.persistence.EntityManager;
import java.util.List;

public class TowerRep {
    private EntityManager em;

    public TowerRep(EntityManager em) {
        this.em = em;
    }

    public List<Tower> showAll() {
        return em.createQuery("from Tower", Tower.class).getResultList();
    }

    public void update(Tower tower) {
        try {
            em.getTransaction().begin();
            em.refresh(tower);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Tower tower) {
        try {
            em.getTransaction().begin();
            em.persist(tower);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(Tower tower) {
        em.getTransaction().begin();
        for (Mage mage : tower.getMages()) {
            em.remove(mage);
        }
        em.remove(tower);
        em.getTransaction().commit();
    }
}

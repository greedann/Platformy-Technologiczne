package org.example;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class MageRep {
    private EntityManager em;

    public MageRep(EntityManager em) {
        this.em = em;
    }

    public List<Mage> showAll() {
        return em.createQuery("from Mage", Mage.class).getResultList();
    }

    public Optional<Mage> findName(String name) {
        Mage mage = em.createQuery("Select b FROM Mage b where b.name = :name", Mage.class).setParameter("name", name)
                .getSingleResult();
        return mage != null ? Optional.of(mage) : Optional.empty();
    }

    public void remove(Mage mage) {
        mage.getTower().getMages().remove(mage);
        em.getTransaction().begin();
        em.remove(mage);
        em.getTransaction().commit();
    }

}

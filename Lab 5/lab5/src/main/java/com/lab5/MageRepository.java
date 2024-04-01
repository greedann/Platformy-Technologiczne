package com.lab5;

import java.util.Collection;
import java.util.Optional;

public class MageRepository {
    private Collection<Mage> collection;

    public MageRepository() {
        collection = new java.util.ArrayList<>();
    }

    public Optional<Mage> find(String name) {
        return collection.stream()
                .filter(mage -> mage.getName().equals(name))
                .findFirst();
    }

    public void delete(String name) {
        if (!collection.removeIf(mage -> mage.getName().equals(name))) {
            throw new IllegalArgumentException();
        }
    }

    public void save(Mage mage) {
        if (collection.stream().anyMatch(m -> m.getName().equals(mage.getName()))) {
            throw new IllegalArgumentException();
        }
        collection.add(mage);
    }
}
package org.example;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@ToString
@Entity
@NamedQueries({ @NamedQuery(name = "Tower.findAll", query = "Select t From Tower t") })
public class Tower {
    @Id
    private String tower_name;

    private int height;

    @OneToMany(mappedBy = "name", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Mage> mages = new ArrayList<>();

    public String getTower_name() {
        return tower_name;
    }

    public void setTower_name(String tower_name) {
        this.tower_name = tower_name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void setMages(List<Mage> mages) {
        this.mages = mages;
    }

    public Tower(String name, int height) {
        this.tower_name = name;
        this.height = height;
    }

    public void addMage(Mage mage) {
        this.mages.add(mage);
        mage.setTower(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tower tower = (Tower) o;
        return height == tower.height && Objects.equals(tower_name, tower.tower_name)
                && Objects.equals(mages, tower.mages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tower_name, height, mages);
    }
}

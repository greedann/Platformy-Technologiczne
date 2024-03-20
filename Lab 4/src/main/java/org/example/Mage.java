package org.example;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@NamedQueries({
        @NamedQuery(name = "Mage.findAll", query = "SELECT m FROM Mage m")
})

public class Mage {
    @Id
    @Getter
    private String name;
    @Getter
    @Setter
    private int level;

    @ManyToOne
    @Getter
    @Setter
    @JoinColumn(name = "tower")
    private Tower tower;

    public Mage(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.xml.stream.events.EntityDeclaration;
import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        boolean first = false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("labPu");
        EntityManager em = emf.createEntityManager();
        MageRep magerep = new MageRep(em);
        TowerRep towerrep = new TowerRep(em);

        List<String> mages = new ArrayList<>();
        Map<String, Tower> towersMap = new HashMap<String, Tower>();
        if (first) {
            Tower tower1 = new Tower("tower1", 300);
            Tower tower2 = new Tower("tower2", 500);
            Tower tower3 = new Tower("tower3", 200);
            Mage mage1 = new Mage("mage1", 999);
            Mage mage2 = new Mage("mage2", 10);
            Mage mage3 = new Mage("mage3", 13);
            Mage mage4 = new Mage("mage4", 12);
            Mage mage5 = new Mage("mage5", 100);
            Mage mage6 = new Mage("mage6", 10);
            Mage mage7 = new Mage("mage7", 42);

            tower1.addMage(mage1);
            tower1.addMage(mage2);
            tower1.addMage(mage3);
            tower1.addMage(mage4);
            tower3.addMage(mage5);
            tower3.addMage(mage6);
            tower3.addMage(mage7);
            towerrep.save(tower1);
            towerrep.save(tower1);
            towerrep.save(tower2);
            towerrep.save(tower3);
        }
        List<Tower> towersFull = towerrep.showAll();

        for (Tower towerFull : towersFull) {
            towersMap.put(towerFull.getTower_name(), towerFull);
        }
        for (Mage mage : magerep.showAll()) {
            mages.add(mage.getName());
        }
        System.out.println("\n\n\n\n\n\n\t Start");
        String options = "Enter number what you want:\n1) New Element\n2) Get Elements\n3) Query\n4) Delete element\n5) End\n";
        System.out.println(options);
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        while (option != 5) {
            switch (option) {
                case 1: {
                    new_element(magerep, towerrep, towersMap, mages);
                    break;
                }
                case 2: {
                    showAll(magerep, towerrep);
                    break;
                }
                case 3: {
                    query(em, magerep, towerrep, towersMap, mages);
                    break;
                }
                case 4: {
                    delete(magerep, towerrep, towersMap, mages);
                    break;
                }
                default: {
                    System.out.println("Something gets wrong!\nPlease repeat");

                }
            }
            System.out.println(options);
            option = sc.nextInt();
        }

    }

    public static void new_element(MageRep magerep, TowerRep towerrep, Map<String, Tower> towersMap,
            List<String> mages) {
        Scanner sc = new Scanner(System.in);

        System.out.println("What new element?\n1) Tower\n2) Mage\n3) Exit\n");
        int option = sc.nextInt();
        while (true) {
            switch (option) {
                case 1: {
                    System.out.println("Tower =\n");

                    String str = sc.nextLine();
                    str = sc.nextLine();
                    while (true) {
                        if (towersMap.containsKey(str)) {
                            System.out.println("Can't add\n");
                            str = sc.nextLine();
                        } else {
                            System.out.println("Height =");
                            int height = sc.nextInt();

                            Tower tower = new Tower(str, height);
                            towersMap.put(str, tower);
                            towerrep.save(tower);
                            return;
                        }

                    }
                }
                case 2: {
                    System.out.println("Name =");
                    String name = sc.nextLine();
                    name = sc.nextLine();
                    while (true) {
                        if (mages.contains(name)) {
                            System.out.println("Can't add\n");
                            name = sc.nextLine();
                        } else {
                            System.out.println("Level =");
                            int level = sc.nextInt();

                            System.out.println("Tower =");
                            String str = sc.nextLine();
                            str = sc.nextLine();
                            while (true) {
                                if (towersMap.containsKey(str)) {
                                    Mage magic = new Mage(name, level);
                                    mages.add(magic.getName());
                                    towersMap.get(str).addMage(magic);
                                    towerrep.save(towersMap.get(str));
                                    return;
                                } else {
                                    System.out.println("Can't add");
                                    str = sc.nextLine();
                                }
                            }

                        }

                    }

                }
                case 3: {
                    break;
                }
                default: {
                    System.out.println("Wrong option\n");
                }
            }
        }

    }

    public static void showAll(MageRep magerep, TowerRep towerrep) {
        Scanner sc = new Scanner(System.in);
        System.out.println("To show\n1) Tower\n2) Mage\n3.Leave\n");

        int option = sc.nextInt();

        while (true) {
            switch (option) {
                case 1: {
                    List<Tower> towers = towerrep.showAll();
                    towers.forEach(System.out::println);
                    return;
                }
                case 2: {
                    List<Mage> magess = magerep.showAll();
                    magess.forEach(System.out::println);
                    return;
                }
                case 3: {
                    return;
                }
                default: {
                    System.out.println("Try again");
                    option = sc.nextInt();
                }

            }
        }

    }

    public static void query(EntityManager em, MageRep magerep, TowerRep towerrep, Map<String, Tower> towersMap,
            List<String> mages) {
        Scanner sc = new Scanner(System.in);
        System.out.println(
                "What query?\n1) Get all towers lower than\n2) Get all mages with level greater than in this tower\n3) Get all mages with level greater than\n4) Leave\n");
        int option = sc.nextInt();
        while (true) {
            switch (option) {
                case 1: {
                    System.out.println("Give x");
                    int number = sc.nextInt();
                    List<Tower> tmp = em.createQuery("SELECT t FROM Tower t WHERE t.height > :x", Tower.class)
                            .setParameter("x", number).getResultList();

                    tmp.forEach(System.out::println);
                    return;
                }
                case 2: {
                    System.out.println("Give tower name");
                    String name = sc.nextLine();
                    name = sc.nextLine();
                    while (true) {
                        if (towersMap.containsKey(name)) {
                            Tower tower = towersMap.get(name);
                            System.out.println("Give me maximum level of mage");
                            int number = sc.nextInt();
                            List<Mage> magess = em
                                    .createQuery("SELECT m FROM Mage m WHERE m.tower = :x AND m.level > :y", Mage.class)
                                    .setParameter("x", tower).setParameter("y", number).getResultList();
                            magess.forEach(System.out::println);
                            return;
                        } else {
                            System.out.println("Try again\n");
                            name = sc.nextLine();
                        }
                    }
                }
                case 3: {
                    System.out.println("Give x");
                    int number = sc.nextInt();
                    List<Mage> tmp = em.createQuery("SELECT m FROM Mage m WHERE m.level > :x", Mage.class)
                            .setParameter("x", number).getResultList();
                    tmp.forEach(System.out::println);
                    return;

                }
                case 4: {
                    return;
                }
                default: {
                    System.out.println("Try again!\n");
                }
            }
        }
    }

    public static void delete(MageRep magerep, TowerRep towerrep, Map<String, Tower> towersMap, List<String> mages) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Who do you want to delete?\n1) tower\n2) mage\n3) leave");
        int option = sc.nextInt();
        while (true) {
            switch (option) {
                case 1: {
                    System.out.println("Enter name of tower you want to delete");
                    String name = sc.nextLine();
                    name = sc.nextLine();
                    while (true) {
                        if (towersMap.containsKey(name)) {
                            for (Mage mage : magerep.showAll()) {
                                if (mage.getTower().getTower_name().equals(name)) {
                                    mages.remove(mage.getName());
                                    magerep.remove(mage);
                                }
                            }
                            towerrep.remove(towersMap.get(name));
                            towersMap.remove(name);
                            return;
                        } else {
                            System.out.println("Try again!");
                            name = sc.nextLine();
                        }
                    }

                }
                case 2: {
                    System.out.println("Enter name of mage you want to delete");
                    String name = sc.nextLine();
                    name = sc.nextLine();
                    while (true) {
                        if (mages.contains(name)) {
                            magerep.remove(magerep.findName(name).get());
                            mages.remove(name);
                            return;
                        } else {
                            System.out.println("Try another\n");
                            name = sc.nextLine();
                        }
                    }

                }
                case 3: {
                    return;
                }
                default: {
                    System.out.println("try again!\n");
                    option = sc.nextInt();
                }
            }
        }
    }

}
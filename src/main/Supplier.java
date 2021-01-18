package main;


import org.jspace.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Supplier {
    private Space catalogue;                        // (String item) || (String item, ArrayList<String>)
    private Space suppliers = new QueueSpace();                        // Queue space : (Supplier s, int distance)
    public String name;


    public Supplier(Space catalogue, String name) {
        this.catalogue = catalogue;
        this.name = name;
    }

    public void addConnection(Supplier s, int i) throws InterruptedException {
        suppliers.put(s, i);
    }

    private List<String> stringify(List s) throws InterruptedException {
        List<String> stringified = new ArrayList<>();
        Iterator q = s.iterator();
        while (q.hasNext()) {
            Object[] p = (Object[]) q.next();
            Supplier i = (Supplier) p[1];
            stringified.add(i.name);
        }
        return stringified;
    }

    public List search(String item, List<Supplier> alreadyVisited, int weight) throws InterruptedException {
        List possibleSuppliers = new ArrayList();
        List<Object[]> connections = suppliers.queryAll(new FormalField(Supplier.class), new FormalField(Integer.class));
        if (catalogue.queryp(new ActualField(item)) != null) {
            System.out.println("\u001B[36m" + name + "\u001B[0m can supply \u001B[32m" + item + "\u001B[0m");
            List toAdd = new ArrayList();
            toAdd.add(alreadyVisited);
            toAdd.add(this);
            toAdd.add(weight);
            possibleSuppliers.add(toAdd);
        } else {
            Object[] recipe = catalogue.queryp(new ActualField(item), new FormalField(ArrayList.class));
            if (recipe != null) {
                List<String> ingredients = (ArrayList<String>) recipe[1];
                System.out.println("\u001B[36m" + name + "\u001B[0m can supply \u001B[32m" + item + "\u001B[0m but needs \u001B[32m" + ingredients + "\u001B[0m");
                for (Object[] p : connections) {
                    Supplier i = (Supplier) p[0];
                    Integer newWeight = (Integer) p[1];
                    if (!alreadyVisited.contains(i)) {
                        for (String u : ingredients) {
                            List<Supplier> f = new ArrayList<Supplier>();
                            f.add(this);
                            List<String> stringified = stringify(i.search(u, f, newWeight));
                            System.out.println("\u001B[36m" + stringified + "\u001B[0m can supply " + u + " to \u001B[36m" + name + "\u001B[0m");
                        }
                    }
                }
                alreadyVisited.add(this);
                List toAdd = new ArrayList();
                toAdd.add(alreadyVisited);
                toAdd.add(this);
                toAdd.add(weight);
                possibleSuppliers.add(toAdd);
            }
            if (recipe == null) {
                alreadyVisited.add(this);
                System.out.println("\u001B[36m" + name + "\u001B[0m cannot make \u001B[32m" + item + "\u001B[0m");
                for (Object[] p : connections) {
                    Supplier i = (Supplier) p[0];
                    Integer newWeight = (Integer) p[1];
                    if (!alreadyVisited.contains(i)) {
                        System.out.println("\u001B[36m" + name + "\u001B[0m requested \u001B[32m" + item + "\u001B[0m from \u001B[36m" + i.name + "\u001B[0m");
                        List resend = i.search(item, alreadyVisited, weight + newWeight);
                        possibleSuppliers.addAll(resend);
                    }
                }
            }
        }
        return possibleSuppliers;
    }




}

    /*public void run() {
        /*
        while (true) {
            try{
            Object[] request = requests.get(new FormalField(String.class), new FormalField(Integer.class));
            Object itemInCatalogue = catalogue.queryp(new ActualField(request));
            String item    = (String)  request[0];
            Integer amount = (Integer) request[1];
            if (itemInCatalogue != null) {
                System.out.println("This item is not in the catalogue.");
            } else { System.out.println("Supplier item not available");
            }  } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
        */

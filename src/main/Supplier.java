package main;


import org.jspace.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

public class Supplier {
    private Space catalogue;                        // (String item) || (String item, ArrayList<String>)
    private Space suppliers = new QueueSpace();                        // Queue space : (Supplier s, int distance)
    public String name;

    public Supplier(Space catalogue, String name) {
        this.catalogue = catalogue;
        this.name = name;
    }
    public void addConnection(Supplier s, int i) throws InterruptedException {
        suppliers.put(s,i);
    }

    private List<Supplier> fetchConnections() throws InterruptedException {
        List<Object[]> connections = suppliers.queryAll(new FormalField(Supplier.class), new FormalField(Integer.class));
        List<Supplier> result = new ArrayList<Supplier>();
        for (Object[] s : connections) {
            Supplier i = (Supplier) s[0];
            result.add(i);
        }
        return result;
    }

    private List<String> stringify(List<Supplier> l) {
        Iterator o = l.iterator();
        List<String> stringified = new ArrayList<>();
        while(o.hasNext()) {
            Supplier p = (Supplier) o.next();
            stringified.add(p.name);
        }
        return stringified;
    }

    public List<Supplier> search(String item, ArrayList<Supplier> alreadyVisited) throws InterruptedException {
        ArrayList<Supplier> possibleSuppliers = new ArrayList<Supplier>();
        List<Supplier> connections = fetchConnections();
        alreadyVisited.add(this);
        if (catalogue.queryp(new ActualField(item)) != null) {
            System.out.println("\u001B[36m"+name+"\u001B[0m can supply \u001B[32m"+item+"\u001B[0m");
            possibleSuppliers.add(this);
        }else {
            Object[] recipe = catalogue.queryp(new ActualField(item), new FormalField(ArrayList.class));
            if (recipe != null) {
                ArrayList<String> ingredients = (ArrayList<String>) recipe[1];
                System.out.println("\u001B[36m"+name+"\u001B[0m needs \u001B[32m"+ingredients+"\u001B[0m to make \u001B[32m"+item+"\u001B[0m");
                for (Supplier i : connections) {
                    if (!alreadyVisited.contains(i)) {
                        for (String u : ingredients) {
                            List<Supplier> s = i.search(u, alreadyVisited);
                            List<String> stringified = stringify(s);
                            System.out.println("\u001B[36m" + stringified + "\u001B[0m can supply " + u + " to \u001B[36m" + name + "\u001B[0m");
                        }
                    }
                }
                possibleSuppliers.add(this);
            }
            if (recipe == null) {
                for (Supplier i : connections) {
                    if (!alreadyVisited.contains(i)) {
                        List<Supplier> s = i.search(item, alreadyVisited);
                        if (s.isEmpty()) {
                            System.out.println("\u001B[36m"+ this.name + "\u001B[0m cannot provide \u001B[32m"+ item + "\u001B[0m itself");
                        }else {
                            List<String> stringified = stringify(s);
                            System.out.println("\u001B[36m"+ this.name + "\u001B[0m requested \u001B[32m"+ item + "\u001B[0m from \u001B[36m" + i.name+ "\u001B[0m");
                            possibleSuppliers.addAll(s);
                        }
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

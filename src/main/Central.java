package main;

import org.jspace.*;

import java.util.*;

public class Central implements Runnable {
    private Space inventory = new SequentialSpace();
    private Tuple suppliers; // Suppliers
    private Space requests = new SequentialSpace();

    public Central(Tuple suppliers) { this.suppliers=suppliers;
    }

    public void addToInventory(String item, Integer amount) throws InterruptedException {
        inventory.put(item, amount);
    }

    public void request(String item, Integer amount, Store s) throws InterruptedException {
        requests.put(item, amount, s);
        System.out.println("Request sent for " + item);
    }

    public void supplierRequest(String item, Integer amount) throws InterruptedException {
        Iterator o =  suppliers.iterator();
        Set<Supplier> suppliersWithItem = new HashSet<Supplier>();
        while(o.hasNext()) {
            Supplier s = (Supplier) o.next();
            System.out.println("\u001B[31mCentral \u001B[0mrequest sent to \u001B[36m"+s.name+"\u001B[0m for \u001B[0m" + item+"\u001B[0m");
            suppliersWithItem.addAll(s.search(item,new ArrayList<Supplier>()));
        }
        o = suppliersWithItem.iterator();
        List<String> stringified = new ArrayList<>();
        while(o.hasNext()) {
            Supplier s = (Supplier) o.next();
            stringified.add(s.name);
        }
        System.out.println("Possible suppliers :\u001B[36m"+ stringified+"\u001B[0m");

        Space routes = new SequentialSpace();
        o =  suppliers.iterator();
        while(o.hasNext()) {
            Iterator u = suppliersWithItem.iterator();
            while(u.hasNext()) {
                Supplier start = (Supplier) o.next();
                Supplier finish = (Supplier) u.next();

            }
        }
    }



    public void run() {
        while (true) {
            try {
                Object[] r = requests.get(new FormalField(String.class),
                        new FormalField(Integer.class), new FormalField(Store.class));
                String item    = (String)  r[0];
                Integer amount = (Integer) r[1];
                Store store    = (Store)   r[2];
                supplierRequest(item, amount);
                Object[] i = inventory.get(new ActualField(item), new FormalField(Integer.class));
                store.addToInventory(item, amount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
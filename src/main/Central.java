package main;

import org.jspace.*;

import java.util.*;

public class Central {
    private Space inventory = new SequentialSpace();
    private Tuple suppliers; // Suppliers
    private Space requests = new SequentialSpace();

    public Central(Tuple suppliers) {
        this.suppliers = suppliers;
    }

    public void addToInventory(String item, Integer amount) throws InterruptedException {
        inventory.put(item, amount);
    }

    public void request(String item, Integer amount, Store s) throws InterruptedException {
        requests.put(item, amount, s);
        System.out.println("Request sent for " + item);
    }

    public void supplierRequest(String item, Integer amount) throws InterruptedException {
        Iterator o = suppliers.iterator();
        Space suppliersWithItem = new QueueSpace();
        int min = 100000;
        Supplier best=new Supplier(null,null);
        while (o.hasNext()) {
            Supplier s = (Supplier) o.next();
            System.out.println("\u001B[31mCentral \u001B[0mrequest sent to \u001B[36m" +
                    s.name + "\u001B[0m for \u001B[36m" +
                    item + "\u001B[0m");
            Iterator q = s.search(item, new ArrayList<Supplier>(), 0).iterator();
            while (q.hasNext()) {
                List r = (ArrayList) q.next();
                List<Supplier> route = (List<Supplier>) r.get(0);
                Supplier endGoal = (Supplier) r.get(1);
                Integer weight = (Integer) r.get(2);

                List<String> stringified = new ArrayList<>();
                suppliersWithItem.put(endGoal, weight);

                System.out.println("Possible supplier found : \u001B[32m" + endGoal.name +
                        "\u001B[0m to provide \u001B[36m" + item +
                        "\u001B[0m with weight " + weight +
                        "\u001B[0m.");
                if (min>weight) {
                    best=endGoal;
                }
            }
        }
        System.out.println("Best supplier found: \u001B[32m" + best.name +
                "\u001B[0m to provide "+amount+" \u001B[36m" + item + "\u001B[0m");
        Object[] actual = inventory.getp(new ActualField(item),new FormalField(int.class));
        if (actual!=null) {
            int currentAmount = (int) actual[1];
            inventory.put(item,currentAmount+amount);
        }else{
            inventory.put(item,amount);
        }
    }
}

   /* public void run() {
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
    }*/

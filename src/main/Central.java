package main;

import org.jspace.*;

import java.util.*;

public class Central implements Runnable {
    private Space inventory = new SequentialSpace();
    private Tuple suppliers; // Suppliers
    private Space requests = new SequentialSpace();
    private Space centralRequests = new SequentialSpace();
    private Space awaitingRequests = new SequentialSpace();

    public Central(Tuple suppliers) {
        this.suppliers = suppliers;
    }


    public List<String> stringify(List s) throws InterruptedException {
        List<String> stringified = new ArrayList<>();
        Iterator q = s.iterator();
        while (q.hasNext()) {
            Supplier p = (Supplier) q.next();
            stringified.add(p.name);
        }
        return stringified;
    }

    public List<String> stringify2(List s) throws InterruptedException {
        List<String> stringified = new ArrayList<>();
        Iterator q = s.iterator();
        while (q.hasNext()) {
            Object[] r = (Object[]) q.next();
            Supplier p = (Supplier) r[0];
            stringified.add(p.name);
        }
        return stringified;
    }

    public void routeFound(String item, List<Supplier> route, int weight) throws InterruptedException {
        System.out.println("\u001B[35mPossible route \u001B[0mfound : \u001B[32m" + stringify(route) +
                "\u001B[0m to provide \u001B[36m" + item +
                "\u001B[0m with cost \u001B[31m" + weight + "\u001B[0m");
        centralRequests.put(item, route, weight);
    }

    public void request(String item, Integer amount, Store s) throws InterruptedException {
        requests.put(item, amount, s);
        System.out.println("Store requested for " + amount + " \u001B[36m" + item + "\u001B[0m");
    }

    public void supplierRequest(String item, Store s, Integer a) throws InterruptedException {
        Iterator o = suppliers.iterator();
        while (o.hasNext()) {
            Supplier currentSupplier = (Supplier) o.next();
            System.out.println("\u001B[31mCentral \u001B[0mrequest sent to \u001B[32m" +
                    currentSupplier.name + "\u001B[0m for \u001B[36m" +
                    item + "\u001B[0m");
            currentSupplier.sendRequest(item, new ArrayList<Supplier>(), 0, new ArrayList<Supplier>());
        }
        awaitingRequests.put(item,s,a);
    }

    public void run() {
        while (true) {
            try {
                Object[] req = requests.getp(new FormalField(String.class),
                        new FormalField(Integer.class), new FormalField(Store.class));
                if (req != null) {
                    String item = (String) req[0];
                    Integer amount = (Integer) req[1];
                    Store store = (Store) req[2];
                    req = inventory.getp(new ActualField(item), new FormalField(Integer.class));
                    if (req != null) {
                        int inventoryAmount = (int) req[1];
                        if (inventoryAmount > amount) {
                            inventory.put(item, inventoryAmount - amount);
                            store.addToInventory(item, amount);
                        } else {
                            inventory.put(item, inventoryAmount);
                            supplierRequest(item,store,amount);
                            inventoryAmount = (int) req[1];
                        }
                    } else {
                        supplierRequest(item,store,amount);

                    }
                }
                req = awaitingRequests.getp(new FormalField(String.class), new FormalField(Store.class), new FormalField(Integer.class));
                if (req != null) {
                    String item = (String) req[0];
                    int time = 25;
                    int weight = 10000000;
                    Store s = (Store) req[1];
                    int amount = (int) req[2];
                    List<Supplier> route = new ArrayList<Supplier>();
                    while (time > 0) {
                        req = centralRequests.getp(new ActualField(item),
                                new FormalField(List.class),
                                new FormalField(Integer.class));
                        if (req != null) {
                            if ((int) req[2] < weight) {
                                weight = (int) req[2];
                                route = (List<Supplier>) req[1];
                            }
                        }
                        Thread.sleep(100);
                        time--;
                    }
                    System.out.println("\u001B[35mOptimal route \u001B[0mfound : \u001B[32m" + stringify(route) +
                            "\u001B[0m to provide \u001B[36m" + item +
                            "\u001B[0m with cost \u001B[31m" + weight + "\u001B[0m");
                    s.addToInventory(item, amount);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

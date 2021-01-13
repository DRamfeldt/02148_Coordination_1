package main;

import org.jspace.*;

public class Central implements Runnable {
    private Space inventory = new SequentialSpace();
    private Space suppliers = new SequentialSpace();
    private Space requests = new SequentialSpace();

    public Central(Space suppliers) { this.suppliers=suppliers;
    }

    public void addSupplier(Supplier S, String Item) throws InterruptedException {
        suppliers.put(S, Item);
    }

    public void addToInventory(String item, Integer amount) throws InterruptedException {
        inventory.put(item, amount);
    }

    public void request(String item, Integer amount, Store s) throws InterruptedException {
        requests.put(item, amount, s);
        System.out.println("Request sent for " + item);
    }

    public void supplierRequest(String item, Integer amount) throws InterruptedException {
        Object[] s = suppliers.query(new FormalField(Supplier.class), new ActualField(item));
        Supplier supplier = (Supplier) s[0];
        supplier.request(item,amount, this);
        System.out.println("Request sent to supplier for " + item);
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
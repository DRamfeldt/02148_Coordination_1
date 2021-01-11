package main;

import org.jspace.*;

public class Central {
    private static Space inventory;
    private Space Suppliers = new SequentialSpace();
    private static Space requests = new SequentialSpace();

    public Central(SequentialSpace inventory) {
        this.inventory = inventory;
    }

    public void AddSupplier(Supplier S, String Item) throws InterruptedException {
        Suppliers.put(S, Item);
    }

    public static void AddToInventory(String Item, Integer Amount) throws InterruptedException {
        inventory.put(new Tuple(Item, Amount));
    }

    public void SupplierRequest(String Item, Integer Amount) throws InterruptedException {
        Object[] S = Suppliers.query(new FormalField(Supplier.class), new ActualField(Item));
        Supplier supplier = (Supplier) S[0];
        supplier.Request(Item, Amount);
    }

    public static void Request(String Item, Integer Amount, Store S) throws InterruptedException {
        requests.put(Item, Amount, S);
    }

    public void run() throws InterruptedException {
        while (true) {
            Object[] r = requests.get(new FormalField(String.class),
                    new FormalField(Integer.class), new FormalField(Store.class));
            String item    = (String)  r[0];
            Integer amount = (Integer) r[1];
            Store store    = (Store)   r[2];
            SupplierRequest(item, amount);
            Object[] i = inventory.get(new ActualField(item), new FormalField(Integer.class));
            store.AddToInventory(item, amount);

        }
    }
}
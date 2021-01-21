package main;

import org.jspace.*;

public class Store {

    private Central central;
    private Space inventory;
    private Space standardInv;
    private String name;


    public Store(Space inventory, Space standardInv, Central c, String name) {   // Creates new inventory for a given store
        this.inventory = inventory;
        this.standardInv = standardInv;
        central = c;
        this.name = name;
    }

    public void addToInventory(String Item, Integer Amount) throws InterruptedException {
        inventory.put(Item, Amount);
    }

    public void sale(String item, Integer amount) throws InterruptedException {
        Object[] itemSaleT = inventory.getp(new ActualField(item), new FormalField(Integer.class));
        if (itemSaleT == null) {
            System.out.println("Not available - null");
        } else {
            int storagenumber = (int) itemSaleT[1];
            if (amount <= storagenumber) {
                storagenumber = storagenumber - amount;

                inventory.put(item, storagenumber);
                System.out.println("Sale successful");

                Object[] stdT = standardInv.queryp(new ActualField(item), new FormalField(Integer.class));
                int stdAmount = (int) stdT[1];
                if (storagenumber <= stdAmount) {
                    central.request(item, stdAmount - storagenumber, this);
                }
            } else {
                System.out.println("The store has an insufficient amount of " + item);
            }
        }

    }

    public String getName() {
        return name;
    }

}
package main;

import org.jspace.*;

public class main {
    public static void main(String[] args) throws InterruptedException {
        /*
        Space arlaProducts = new SequentialSpace();

        arlaProducts.put("milk");
        arlaProducts.put("yogurt");

        Space centralInv = new SequentialSpace();
        centralInv.put("milk",0);
        centralInv.put("yogurt",0);

        Central Netto = new Central(centralInv);
        Supplier Arla = new Supplier(arlaProducts);

        Netto.addSupplier(Arla, "milk");
        Netto.addSupplier(Arla, "yogurt");

        Space storeInv = new SequentialSpace();
        storeInv.put("milk", 20);
        storeInv.put("yogurt", 15);
        Space storeStdInv = new SequentialSpace();
        storeStdInv.put("milk", 15);
        storeStdInv.put("yogurt", 10);

        Store Søborg = new Store(storeInv, storeStdInv, Netto);


        Søborg.sale("yogurt", 12);
        new Thread(Netto).start();

        */

        Space arlaProducts = new SequentialSpace();
        Space plasticProducts = new SequentialSpace();

        plasticProducts.put("container");
        arlaProducts.put("milk");
        arlaProducts.put("yogurt");

        Supplier plasticFactory = new Supplier(plasticProducts, new QueueSpace());
        Supplier Arla = new Supplier(arlaProducts, new QueueSpace());

    }
}
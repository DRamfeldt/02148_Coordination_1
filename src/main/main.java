package main;

import org.jspace.*;

import java.util.ArrayList;

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
        ArrayList<String> o = new ArrayList<String>();
        o.add("container");
        arlaProducts.put("yogurt", o);

        Supplier plasticFactory = new Supplier(plasticProducts, new QueueSpace());
        Supplier Arla = new Supplier(arlaProducts, new QueueSpace());

        plasticFactory.addAdjacent(Arla,9);
        Arla.addAdjacent(plasticFactory,9);


        Central c = new Central(new SequentialSpace());
        c.addSupplier(Arla, "milk");
        c.addSupplier(Arla, "yogurt");
        c.addSupplier(plasticFactory, "container");

        c.supplierRequest("yogurt",1);



    }
}
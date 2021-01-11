package main;

import org.jspace.*;

public class main {
    public static void main(String[] args) throws InterruptedException {
        Space arlaProducts = new SequentialSpace();

        arlaProducts.put(new ActualField("milk"));
        arlaProducts.put(new ActualField("yogurt"));

        Supplier Arla = new Supplier(arlaProducts);
        Space storeInv = new SequentialSpace();
        storeInv.put(new ActualField("milk"), new ActualField(20));
        storeInv.put(new ActualField("yogurt"), new ActualField(15));
        Space storeStdInv = new SequentialSpace();
        storeStdInv.put(new ActualField("milk"), new ActualField(15));
        storeStdInv.put(new ActualField("yogurt"), new ActualField(10));

        Store Søborg = new Store(storeInv, storeStdInv);



    }
}
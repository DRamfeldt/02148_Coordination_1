package main;


import org.jspace.*;
import java.util.ArrayList;

import java.util.Iterator;

public class Supplier implements Runnable {
    private Space catalogue;                        // (String item) || (String item, ArrayList<String>)
    private Space suppliers;                        // Queue space : (Supplier s, int distance)

    public Supplier(Space catalogue, QueueSpace suppliers) {
        this.catalogue = catalogue;
        this.suppliers = suppliers;
    }

    public void request(String item, Integer amount, Central central) throws InterruptedException {
        if (catalogue.queryp(new ActualField(item)) != null) {
            central.addToInventory(item, amount);
        } else {

        }
    }


    public void run() {
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

         */
    }


}
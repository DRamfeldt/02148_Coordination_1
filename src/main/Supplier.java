package main;


import org.jspace.*;
import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;

public class Supplier implements Runnable {
    private Space catalogue;                        // (String item) || (String item, ArrayList<String>)
    private Space suppliers;                        // Queue space : (Supplier s, int distance)

    public Supplier(Space catalogue, QueueSpace suppliers) {
        this.catalogue = catalogue;
        this.suppliers = suppliers;
    }

    public Boolean hasItem(String item) throws InterruptedException {
        return catalogue.queryp(new ActualField(item)) != null;
    }

    public void request(String item, Integer amount, Central central) throws InterruptedException {
        if (hasItem(item)) {
            central.addToInventory(item, amount);
        } else {
            Object[] recipe = catalogue.queryp(new ActualField(item), new FormalField(ArrayList.class));
            ArrayList<String> ingredients = (ArrayList<String>) recipe[1];
            for (String ingredient : ingredients){
                initShortestPath(ingredient);
            }
            /*
            if (recipe != null) {
                ArrayList<String> ingredients = (ArrayList<String>) recipe[1];
                List<Object[]> tosendto = suppliers.queryAll();
                for (Object[] i : tosendto) {
                    Supplier s = (Supplier) i[0];
                    int distance = (int) i[1];
                    for (String j : ingredients) {
                        s.supplierRequest(j, 1, this)
                    }

                }
                central.addToInventory(item, amount);
            }

             */
        }
    }

    private void initShortestPath(String item) throws InterruptedException {
        List<Object[]> adjacentSuppliers = suppliers.queryAll();
        for (Object[] adj : adjacentSuppliers){
            Supplier s = (Supplier) adj[0];
            int distance = (int) adj[1];
        }
    }

    public boolean supplierRequest(String item, Integer amount, Supplier supplier) throws InterruptedException {
        if (catalogue.queryp(new ActualField(item)) != null) {
            return true;
        } else {
            Object[] recipe = catalogue.queryp(new ActualField(item), new FormalField(ArrayList.class));
            if (recipe != null) {
                supplierRequest()
                return
            }

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


    public void addAdjacent(Supplier s, Integer i) throws InterruptedException {
        suppliers.put(s,i);
    }
}
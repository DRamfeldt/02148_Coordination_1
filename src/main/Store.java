package main;
import org.jspace.*;

public class Store {

    Space inventory;
    Space standardInv;

    public Store(SequentialSpace inventory, SequentialSpace standardInv){    // Creates new inventory for a given store
        this.inventory = inventory;
        this.standardInv = standardInv;

    }


    public void sale( String item, Integer amount) throws InterruptedException {
        Object[]  itemSaleT= inventory.getp(new ActualField(item), new FormalField(Integer.class));


        if(itemSaleT == null){
            System.out.println("Not available");
        }else{
           int storagenumber = (int)itemSaleT[1];
            if (amount <= storagenumber){
                storagenumber = storagenumber - amount;

                inventory.put(new ActualField(item), new ActualField(storagenumber));
                System.out.println( "Sale successful");

                Object[]  stdT = standardInv.queryp(new ActualField(item), new FormalField(Integer.class));
                int stdAmount = (int)stdT[1];
                    if(storagenumber <= stdAmount){
                        // create a central request for item
                    }
            }else{
                System.out.println("The store has an insufficient amount of "+ item);
            }
        }

    }

}


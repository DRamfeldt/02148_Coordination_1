package main;

import org.jspace.*;

import java.util.Iterator;

public class Playground {

    public static void main(String[] args){
        Tuple t = new Tuple("Spa", 5, "10");

        Iterator itr = t.iterator();
        while (itr.hasNext()){
            System.out.println(itr.next());
        }

    }
}

package main;

import org.jspace.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class main {
    public static void main(String[] args) throws InterruptedException {


        Space arla = new SequentialSpace();
        arla.put("milk");
        arla.put("yogurt");
        Space egelykke = new SequentialSpace();
        egelykke.put("milk");
        egelykke.put("icecream");
        egelykke.put("cheese");
        Space stryhns = new SequentialSpace();
        stryhns.put("leverpostej");
        Space karenvolf = new SequentialSpace();
        karenvolf.put("snoefler");
        Space marabou = new SequentialSpace();
        marabou.put("chocolate");
        Space rittersport = new SequentialSpace();
        rittersport.put("chocolate");
        Space hansens = new SequentialSpace();
        hansens.put("icecream");
        Space smirnoff = new SequentialSpace();
        smirnoff.put("vodka");
        Space heinz = new SequentialSpace();
        heinz.put("ketchup");
        Space kellogs = new SequentialSpace();
        kellogs.put("corn flakes");

        Supplier Arla = new Supplier(arla,"Arla");
        Supplier Egelykke = new Supplier(egelykke,"Egelykke");
        Supplier Stryhns = new Supplier(stryhns,"Stryhns");
        Supplier KarenVolf = new Supplier(karenvolf,"Karen Volf");
        Supplier Marabou = new Supplier(marabou, "Marabou");
        Supplier RitterSport = new Supplier(rittersport,"Ritter Sport");
        Supplier Hansens = new Supplier(hansens,"Hansens flødeis");
        Supplier Smirnoff = new Supplier(smirnoff,"Smirnoff");
        Supplier Heinz = new Supplier(heinz,"Heinz");
        Supplier Kellogs = new Supplier(kellogs,"Kellogs");

        Stryhns.addConnection(Kellogs,5);
        Stryhns.addConnection(Heinz,10);
        Heinz.addConnection(Stryhns,10);
        Heinz.addConnection(RitterSport,2);
        Heinz.addConnection(Kellogs,2);
        Kellogs.addConnection(Stryhns,5);
        Kellogs.addConnection(Heinz,2);
        Kellogs.addConnection(Smirnoff,1);
        RitterSport.addConnection(Heinz,2);
        RitterSport.addConnection(Arla,2);
        Smirnoff.addConnection(Kellogs,1);
        Smirnoff.addConnection(Hansens,2);
        Smirnoff.addConnection(Marabou,5);
        Hansens.addConnection(Egelykke,7);
        Hansens.addConnection(Smirnoff,2);
        Egelykke.addConnection(KarenVolf,1);
        Egelykke.addConnection(Hansens,7);
        Egelykke.addConnection(Arla,5);
        Arla.addConnection(Egelykke,5);
        Arla.addConnection(RitterSport,2);
        KarenVolf.addConnection(Egelykke,1);
        Marabou.addConnection(Smirnoff,5);

        Tuple centralNeighbors = new Tuple(Stryhns,Hansens);
        Central c = new Central(centralNeighbors);

        Arla.setCentral(c);
        Egelykke.setCentral(c);
        Hansens.setCentral(c);
        Smirnoff.setCentral(c);
        RitterSport.setCentral(c);
        Kellogs.setCentral(c);
        Heinz.setCentral(c);
        Stryhns.setCentral(c);
        Marabou.setCentral(c);
        KarenVolf.setCentral(c);

        Space inventory = new SequentialSpace();
        inventory.put("milk", 10);
        inventory.put("licorice", 8);

        Space stdInv = new SequentialSpace();
        stdInv.put("milk", 5);
        stdInv.put("licorice", 5);

        Store søborg = new Store(inventory,stdInv,c, "Søborg");
        Store lyngby = new Store(inventory,stdInv,c, "Lyngby");


        (new Thread(Arla)).start();
        (new Thread(RitterSport)).start();
        (new Thread(Heinz)).start();
        (new Thread(Stryhns)).start();
        (new Thread(Kellogs)).start();
        (new Thread(Smirnoff)).start();
        (new Thread(Marabou)).start();
        (new Thread(Hansens)).start();
        (new Thread(Egelykke)).start();
        (new Thread(KarenVolf)).start();


        (new Thread(c)).start();











        Store[] sl = new Store[]{lyngby,søborg};
        JFrame initF = new JFrame("Chosing a store");
        initF.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JRadioButton rb = new JRadioButton();
        ButtonGroup bg = new ButtonGroup();

        for (Store s : sl) {
            rb = new JRadioButton(s.getName());
            bg.add(rb);
            initF.add(rb);

        }
        JButton b = new JButton("Select");
        initF.add(b);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();

                    if (button.isSelected()) {
                        for (Store store : sl){
                              if (button.getText().equals(store.getName())){
                                  try {
                                      new StoreFrame(store);
                                  } catch (InterruptedException interruptedException) {
                                      interruptedException.printStackTrace();
                                  }
                              }
                        }
                        break;
                    }
                }
            }
        });
        initF.setLayout(new FlowLayout());
        initF.setSize(300,300);
        initF.setVisible(true);




    }

}
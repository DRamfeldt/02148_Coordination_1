package main.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args){
        final JFrame f = new JFrame("Store Overview");

        // Show frame
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900,600);


        f.getContentPane().setLayout(null);

        // inventory
        String[] invColumnNames = {"Item", "Cost in $", "Stock Count"};
        Object[][] inventory = {
                {"Milk", 1, 16},
                {"Yogurt", 2, 18},
        };

        DefaultTableModel invModel = new DefaultTableModel(inventory, invColumnNames);
        JTable invTab = new JTable(invModel);
        JScrollPane invSP = new JScrollPane(invTab);
        invTab.setFillsViewportHeight(true);
        invSP.setBounds(60, 50, 300, 450);
        f.add(invSP);

        // inventory label text
        JLabel invLabel = new JLabel("Inventory");
        Font invFont = new Font("Courier", Font.BOLD, 18);
        invLabel.setFont(invFont);
        invLabel.setBounds(165, 20, 110, 25);
        f.add(invLabel);

        // Shopping Cart
        String[] cartColumnNames = {"Item", "amount"};
        Object[][] cart = {
                {"Milk", 3},
                {"Yogurt", 5}
        };

        DefaultTableModel cartModel = new DefaultTableModel(cart, cartColumnNames);
        JTable cartTab = new JTable(cartModel);
        JScrollPane cartSP = new JScrollPane(cartTab);
        cartTab.setFillsViewportHeight(true);cartSP.setBounds(525, 120, 300, 300);
        cartTab.getColumnModel().getColumn(0).setPreferredWidth(200);
        cartTab.getColumnModel().getColumn(1).setPreferredWidth(5);
        f.add(cartSP);

        // cart label text
        JLabel cartLabel = new JLabel("Shopping Cart");
        Font cartFont = new Font("Courier", Font.BOLD, 18);
        cartLabel.setFont(cartFont);
        cartLabel.setBounds(525, 90, 220, 25);
        f.add(cartLabel);

        // search bar
        JTextField searchBar = new JTextField();
        f.add(searchBar);
        searchBar.setBounds(525, 49, 225, 32);

        //Search button
        JButton searchButton = new JButton("Search");
        f.add(searchButton);
        searchButton.setBounds(750,50,80,30);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchedItem = searchBar.getText().toLowerCase();
                boolean searchedExists = false;
                for (int row = 0; row < invTab.getRowCount(); row++){
                    String item = ((String) invTab.getValueAt(row,0)).toLowerCase();
                    if (item.equals(searchedItem)){
                        invTab.setRowSelectionInterval(row,row);
                        searchedExists = true;
                        break;
                    }
                }
                if (!searchedExists){
                    JOptionPane.showMessageDialog(f, "\"" + searchedItem + "\" is not in the inventory.", "Notification", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        // Add button
        JButton addButton = new JButton("Add");
        f.add(addButton);
        addButton.setBounds(525,500,95,30);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowNum = invTab.getSelectedRow();
                int stockCount = (int) invTab.getValueAt(rowNum, 2);
                String item = (String) invTab.getValueAt(rowNum,0);
                int cartRow = -1;
                boolean cartHasItem = false;
                for (int row = 0; row < cartTab.getRowCount(); row++){
                    if (cartTab.getValueAt(row,0) == item){
                        cartRow = row;
                        cartHasItem = true;
                        break;
                    }
                }
                if (cartHasItem) {
                    int currentCartCount = (int) cartTab.getValueAt(cartRow,1);
                    cartModel.setValueAt(currentCartCount+1, cartRow, 1);
                } else {
                    cartModel.addRow(new Object[]{item, 1});
                }
                if (stockCount == 1){
                    invModel.removeRow(rowNum);
                } else {
                    invModel.setValueAt(stockCount-1, rowNum, 2);
                }
            }
        });

        // Remove button
        JButton removeButton = new JButton("Remove");
        f.add(removeButton);
        removeButton.setBounds(630,500,95,30);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowNum = cartTab.getSelectedRow();
                String item = (String) cartTab.getValueAt(rowNum, 0);
                int itemCount = (int) cartTab.getValueAt(rowNum, 1);
                int rowMatch = -1;
                boolean invHasItem = false;

                for (int row = 0; row < invTab.getRowCount(); row++){
                    if (invTab.getValueAt(row,0) == item){
                        rowMatch = row;
                        invHasItem = true;
                        break;
                    }
                }
                if (invHasItem){
                    int currentInvCount = (int) invTab.getValueAt(rowMatch,2);
                    invModel.setValueAt(currentInvCount+1,rowMatch,2);
                } else {
                    invModel.addRow(new Object[]{item,1});
                }
                if (itemCount == 1){
                    cartModel.removeRow(rowNum);
                } else {
                    cartModel.setValueAt(itemCount-1, rowNum, 1);
                }
            }
        });


        // Checkout button
        JButton checkoutButton = new JButton("Checkout");
        f.add(checkoutButton);
        checkoutButton.setBounds(735,500,95,30);
        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cartModel.getRowCount() != 0) {
                    JOptionPane.showMessageDialog(f, "You just bought something :-)");
                    cartModel.setRowCount(0);
                }
            }
        });



        // make visible
        f.setVisible(true);
    }
}


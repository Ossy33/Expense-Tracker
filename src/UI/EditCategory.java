package UI;

import Utils.Handler;
import customlist.Category;
import customlist.CategoryList;
import customlist.Field;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class EditCategory {
    private JTable fieldList;
    private JPanel panel1;
    private JComboBox categoryBox;
    private JTextArea categoryArea;
    private JTextField searchField;
    private JButton cancelButton;
    private JButton okButton;
    private JButton applyButton;
    private JButton btnChangeCategoryName;

    private static JFrame frame;

    private Handler handler;
    private CategoryList catgList;

    private DefaultComboBoxModel categoryModel;

    private int selectedIndex = 0;

    public EditCategory(){

    }

    public void run() {
        frame = new JFrame("EditCategory");
        frame.setContentPane(new EditCategory().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(600, 450));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        ListenForWindow lForWindow = new ListenForWindow();
        frame.addWindowListener(lForWindow);
    }


    private void createUIComponents() {

        handler = new Handler();
        catgList = new CategoryList();
        fillCatgList();

        ListenForChange lForChange = new ListenForChange();

        categoryModel = new DefaultComboBoxModel();

        //Buttons
        btnChangeCategoryName = new JButton();
        btnChangeCategoryName.addActionListener(lForChange);

        okButton = new JButton();
        cancelButton = new JButton();
        applyButton = new JButton();

        okButton.addActionListener(lForChange);
        cancelButton.addActionListener(lForChange);
        applyButton.addActionListener(lForChange);


        categoryBox = new JComboBox(categoryModel);
        updateCategoryBox();
        categoryBox.addActionListener(lForChange);


        categoryArea = new JTextArea();
        updateCategoryArea();
    }

    private void updateCategoryBox() {
        selectedIndex = categoryBox.getSelectedIndex() > -1 ? categoryBox.getSelectedIndex() : 0;

        categoryModel.removeAllElements();
        for (String i: catgList.getCategoryNames()) {
            categoryModel.addElement(i);
        }
    }

    private void updateCategoryArea(){
        try {
            categoryArea.setText(catgList.getCategory(selectedIndex).getName());

        }catch (NullPointerException e){
            System.out.println("Something is null");
            e.printStackTrace();
        }
    }

    private void fillCatgList(){

        for (Category i : handler.getCategoryList().getCategories()) {
            Category c = new Category(i.getName());

            for (Field f : i.getFields()) {
                c.addField(f.getDate(), f.getMoney(),f.getComment());
            }
            catgList.insertCategory(c);
        }
    }

    private class ListenForChange implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == categoryBox && categoryBox.getSelectedIndex() > -1){
                selectedIndex = categoryBox.getSelectedIndex();
                updateCategoryArea();

            }

            if (e.getSource() == btnChangeCategoryName){
                int tempIndex = selectedIndex;

                catgList.getCategory(selectedIndex).changeName(categoryArea.getText());
                updateCategoryBox();

                selectedIndex = tempIndex;
                categoryBox.setSelectedIndex(selectedIndex);
            }

            if (e.getSource() == okButton){
                handler.setCategoryList(catgList);
                handler.getGUILayout().updateCategoryList();
                handler.getGUILayout().updateFieldTable();
                handler.getGUILayout().updateTotal();
                frame.dispose();
            }

            if (e.getSource() == cancelButton){
                handler.getGUILayout().updateCategoryList();
                handler.getGUILayout().updateFieldTable();
                handler.getGUILayout().updateTotal();
                frame.dispose();
            }

            if (e.getSource() == applyButton){
                handler.setCategoryList(catgList);
                handler.getGUILayout().updateCategoryList();
                handler.getGUILayout().updateFieldTable();
                handler.getGUILayout().updateTotal();
            }
        }
    }

    private class ListenForWindow implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {}
        @Override
        public void windowClosing(WindowEvent e) {
            if (e.getSource() == frame){
                handler.getGUILayout().updateCategoryList();
                handler.getGUILayout().updateFieldTable();
                handler.getGUILayout().updateTotal();
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {}
        @Override
        public void windowIconified(WindowEvent e) {}
        @Override
        public void windowDeiconified(WindowEvent e) {}
        @Override
        public void windowActivated(WindowEvent e) {}
        @Override
        public void windowDeactivated(WindowEvent e) {}
    }

}

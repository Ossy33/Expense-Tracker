package UI;

import Utils.Handler;
import customlist.Category;
import customlist.CategoryList;
import customlist.Field;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

public class EditCategory {
	private JPanel panel1;
	private JComboBox categoryBox;
	private JTextArea categoryArea;
	private JTextField searchField;
	private JButton cancelButton;
	private JButton okButton;
	private JButton applyButton;
	private JButton btnChangeCategoryName;
	private JTable fieldsTable;

	private static JFrame frame;

	private Handler handler;
	private CategoryList catgList;

	private static DefaultComboBoxModel categoryModel;
	private static DefaultTableModel fieldModel;

	private static int selectedIndex = 0;

	public EditCategory() {

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
		fieldModel = new DefaultTableModel();
		fieldModel.addColumn("Date");
		fieldModel.addColumn("Money");
		fieldModel.addColumn("Note");

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
		categoryBox.setSelectedIndex(handler.getGUILayout().getSelected());
		selectedIndex = handler.getGUILayout().getSelected();

		categoryBox.addActionListener(lForChange);


		categoryArea = new JTextArea();
		updateCategoryArea();

		fieldsTable = new JTable(fieldModel);

		updateFieldTable();
	}

	private void updateCategoryBox() {
		selectedIndex = categoryBox.getSelectedIndex() > -1 ? categoryBox.getSelectedIndex() : 0;

		categoryModel.removeAllElements();
		for (String i : catgList.getCategoryNames()) {
			categoryModel.addElement(i);
		}

	}

	private void updateCategoryArea() {
		try {
			categoryArea.setText(catgList.getCategory(selectedIndex).getName());

		} catch (NullPointerException e) {
			System.out.println("Something is null");
			e.printStackTrace();
		}
	}

	private void updateFieldTable(){
		fieldModel.setNumRows(0);

		try {
			for (Object[] i : catgList.getCategory(selectedIndex).getData()) {
				fieldModel.addRow(i);
			}
		}catch (ArrayIndexOutOfBoundsException e){
			System.out.println("No selected category, can't find the data(in the updateFieldTable)");
		}
	}

	private void retrieveFieldsFromTable(){
		int index = selectedIndex;
		Vector e = fieldModel.getDataVector();
		catgList.getCategory(index).removeAllFields();
		if (e.size() > 0){
			for (Object i : e) {
				String k = i.toString().substring(1, i.toString().length() -1);
				String[] data = k.split(", ");
				catgList.getCategory(index).addField(data[0],Integer.parseInt(data[1]), data[2]);
			}
		}
	}

	private void fillCatgList() {

		for (Category i : handler.getCategoryList().getCategories()) {
			Category c = new Category(i.getName());
			catgList.insertCategory(c);

			for (Field f : i.getFields()) {
				c.addField(f.getDate(), f.getMoney(), f.getComment());
			}
		}
	}

	private class ListenForChange implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == categoryBox && categoryBox.getSelectedIndex() > -1) {
				//Retrieves the fields from the last selected index.
				retrieveFieldsFromTable();

				//Changes the index to the last one
				// and updates both the categoryarea and the fieldstable.
				selectedIndex = categoryBox.getSelectedIndex();
				updateCategoryArea();
				updateFieldTable();
			}

			if (e.getSource() == btnChangeCategoryName) {
				int tempIndex = selectedIndex;

				catgList.getCategory(selectedIndex).changeName(categoryArea.getText());
				updateCategoryBox();

				selectedIndex = tempIndex;
				categoryBox.setSelectedIndex(selectedIndex);
			}

			if (e.getSource() == okButton) {
				retrieveFieldsFromTable();
				frame.dispose();
				handler.setCategoryList(catgList);
				handler.getGUILayout().updateCategoryList();
				handler.getGUILayout().updateFieldTable();
			}

			if (e.getSource() == cancelButton) {
				frame.dispose();
			}

			if (e.getSource() == applyButton) {
				retrieveFieldsFromTable();
				handler.setCategoryList(catgList);
				handler.getGUILayout().updateCategoryList();
				handler.getGUILayout().updateFieldTable();
			}
		}
	}

	private class ListenForWindow implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
		}

		@Override
		public void windowClosing(WindowEvent e) {
			if (e.getSource() == frame) {
				handler.getGUILayout().updateCategoryList();
				handler.getGUILayout().updateFieldTable();
				handler.getGUILayout().updateTotal();
			}
		}

		@Override
		public void windowClosed(WindowEvent e) {
		}

		@Override
		public void windowIconified(WindowEvent e) {
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
		}

		@Override
		public void windowActivated(WindowEvent e) {
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
		}
	}
}

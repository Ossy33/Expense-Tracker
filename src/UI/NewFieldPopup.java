package UI;

import Utils.Handler;
import javafx.scene.control.Spinner;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewFieldPopup {
	private JPanel panel1;
	private JComboBox categoriesBox;
	private JTextField spend_earnTF;
	private JTextField commentTF;
	private JSpinner dateSpinner;
	private JButton addButton;
	private JButton cancelButton;
	private JButton addCategory;

	private static JFrame frame;
	private Handler handler;

	private int selCat = 0;

	private DefaultComboBoxModel<String> categoryModel;

	public NewFieldPopup(int selectedCategory){
		this.selCat = selectedCategory;
	}


	public void run() {

		frame = new JFrame("New Field");
		frame.setContentPane(new NewFieldPopup(selCat).panel1);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}

	private void createUIComponents() {

		 handler = new Handler();

		ListenForButton lForButton = new ListenForButton();

		categoryModel = new DefaultComboBoxModel<>();


		for (String i : handler.getCategoryList().getCategoryNames()){
			categoryModel.addElement(i);
		}


		categoriesBox = new JComboBox(categoryModel);

		try {
			categoriesBox.setSelectedIndex(selCat);
		}catch (IllegalArgumentException e){
			System.out.println("Selected index \"selCat\" is out if bounds.");
		}



		Date todaysDate = new Date();
		dateSpinner = new JSpinner(new SpinnerDateModel(todaysDate, null, null, Calendar.DAY_OF_MONTH));
		JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
		dateSpinner.setEditor(dateEditor);

		spend_earnTF = new JTextField();
		commentTF = new JTextField();


		addCategory = new JButton();
		addCategory.addActionListener(lForButton);
		addButton = new JButton();
		addButton.addActionListener(lForButton);
		cancelButton = new JButton();
		cancelButton.addActionListener(lForButton);


	}

	public void setSelectedCategory(int selectedCategory){
		selCat = selectedCategory;
		categoriesBox.setSelectedIndex(selCat);
	}


	public void update(){

		try {
			categoryModel.removeAllElements();
			for (String i : handler.getCategoryList().getCategoryNames()){
				categoryModel.addElement(i);
			}
		}catch (NullPointerException e){
			System.out.println("No names in the categorylist or there is no categorylist.");
		}

	}


	public class ListenForButton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == addButton && !(
					//Is an positive or negative number
					!spend_earnTF.getText().matches("^-?[0-9]{1,12}(?:\\.[0-9]{1,4})?$")
					//Is a text or only integers
					|| !commentTF.getText().matches("^\\p{L}+(?: \\p{L}+)*$")
					|| spend_earnTF.getText().isEmpty()
					|| commentTF.getText().isEmpty()

			)){

				//Converts the spinner value to today's date
				SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
				String spinnerValue = formater.format(dateSpinner.getValue());

				//Adds a new field to the chosen category and updates the fields
				handler.getCategoryList().getCategory(categoriesBox.getSelectedIndex()).addField(spinnerValue, Integer.parseInt(spend_earnTF.getText()), commentTF.getText());
				handler.getGUILayout().updateCategoryList();
				handler.getGUILayout().updateFieldTable();
				frame.dispose();

			}else if (e.getSource() == cancelButton){
				frame.dispose();
			}else if (e.getSource() == addCategory){

				//Creates the look of the popup window with a textfield
				JPanel panel = new JPanel();
				JLabel label = new JLabel("Category name");
				panel.add(label);
				JTextField text = new JTextField();
				text.setPreferredSize(new Dimension(85, 25));
				panel.add(text);

				Object[] object = {"Add", "Cancel"};
				int res;
				do {

					res = JOptionPane.showOptionDialog(null, panel, "New Category", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, object, null);
				}while (res == JOptionPane.YES_OPTION && !text.getText().matches("^\\p{L}+(?: \\p{L}+)*$"));


				if (res == JOptionPane.YES_OPTION){

					//adds a new category and updates them in the categorylist
					handler.getCategoryList().newCategory(text.getText());
					handler.getGUILayout().updateCategoryList();
					update();
				}

			}
		}

	}


}

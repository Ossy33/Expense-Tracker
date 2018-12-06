package UI;

import Utils.FileReader;
import Utils.FileWriter;
import com.bulenkov.darcula.DarculaLaf;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

import Utils.Handler;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;

public class GUILayout {
	private JPanel panel1;
	private JButton newFieldButton;
	private JButton deleteButton;
	private JButton editButton;

	private JLabel categoriesLable;
	private JTextField searchBar;
	private JList categories;
	private JTable fields;
	private JLabel totalLabel;
	private JLabel categoryTotalLabel;
	private JButton importButton;

	private Handler handler;

	private static DefaultListModel categoryModel;
	private static DefaultTableModel fieldModel;

	private static int categorySelected;
	private static JFrame frame;

	private String test;


	/**Constructor*/
	public GUILayout(){

	}

	/**Creates the frame and inserts all the components*/
	public void run() {
		//New LookAndFeel
		try {
			UIManager.setLookAndFeel(new DarculaLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		//Frame stuff
		frame = new JFrame("Expense Tracker");
		frame.setContentPane(new GUILayout().panel1);
		frame.setMinimumSize(new Dimension(600,500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		ListenForWindow lForWindow = new ListenForWindow();
		frame.addWindowListener(lForWindow);

	}

	/**Sets up all the needed components for the main window*/
	private void createUIComponents() {

		//Handler
		handler = new Handler();

		//Listeners
		ButtonListener lForButton = new ButtonListener();
		ListenForListSelection lForListSelection = new ListenForListSelection();

		//Labels with the sum of money
		totalLabel = new JLabel();
		categoryTotalLabel = new JLabel();

		totalLabel.setEnabled(true);

		//NewFieldButton --JButton
		newFieldButton = new JButton();
		newFieldButton.addActionListener(lForButton);

		KeyStroke keyStroke1 = KeyStroke.getKeyStroke('A', InputEvent.CTRL_MASK, false);
		newFieldButton.registerKeyboardAction(lForButton,keyStroke1,JComponent.WHEN_IN_FOCUSED_WINDOW);

		//DeleteButton --JButton
		deleteButton = new JButton();
		deleteButton.addActionListener(lForButton);

		KeyStroke keyStroke2 = KeyStroke.getKeyStroke('D', InputEvent.CTRL_MASK, false);
		deleteButton.registerKeyboardAction(lForButton,keyStroke2,JComponent.WHEN_IN_FOCUSED_WINDOW);

		//EditButton --JButton
		editButton = new JButton();
		editButton.addActionListener(lForButton);

		KeyStroke keyStroke3 = KeyStroke.getKeyStroke('E', InputEvent.CTRL_MASK, false);
		editButton.registerKeyboardAction(lForButton,keyStroke3,JComponent.WHEN_IN_FOCUSED_WINDOW);

		//ImportButton --JButton
		importButton = new JButton();
		importButton.addActionListener(lForButton);

		KeyStroke keyStroke4 = KeyStroke.getKeyStroke('I', InputEvent.CTRL_MASK, false);
		importButton.registerKeyboardAction(lForButton,keyStroke4,JComponent.WHEN_IN_FOCUSED_WINDOW);


		categoryModel = new DefaultListModel<String>();
		for (String i : handler.getCategoryList().getCategoryNames()) {
			categoryModel.addElement(i);
		}

		//Categories --JList
		categories = new JList(categoryModel);
		categories.addListSelectionListener(lForListSelection);

		//FieldModel
		fieldModel = new DefaultTableModel();
		fieldModel.addColumn("Date");
		fieldModel.addColumn("Money");
		fieldModel.addColumn("Note");

		//Fields --JTable
		fields = new JTable(fieldModel);
		//So you can't edit them
		fields.setEnabled(false);


	}

	/**Updates the categoryModel to the current names stored in the categorylist.*/
	public void updateCategoryList(){
		categoryModel.removeAllElements();

		for (String i: handler.getCategoryList().getCategoryNames()) {
			categoryModel.addElement(i);
		}
		updateTotal();
	}

	/**Updates the fieldModel with the current stored information in the categorylist*/
	public void updateFieldTable(){
		fieldModel.setNumRows(0);

		try {
			for (Object[] i : handler.getCategoryList().getCategory(categorySelected).getData()) {
				fieldModel.addRow(i);
			}
		}catch (ArrayIndexOutOfBoundsException e){
			System.out.println("No selected category, can't find the data(in the update)");
		}
		updateTotal();
	}

	public void updateTotal(){
		totalLabel.requestFocus();
		totalLabel.setText(Integer.toString(handler.getCategoryList().getTotalMoney()));
		categoryTotalLabel.setText(Integer.toString(handler.getCategoryList().getCategory(categorySelected).getCategoryMoney()));
		//test = Integer.toString(handler.getCategoryList().getTotalMoney());
	}


	public DefaultListModel getCategoryModel(){
		return categoryModel;
	}

	/**Returns the currently selected index*/
	public int getSelected(){
		return categorySelected;
	}

	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			/*Prompt the user the NewFieldPopup window which can do multiple things.*/
			if (e.getSource() == newFieldButton){

				NewFieldPopup nfp = new NewFieldPopup(categories.getSelectedIndex() > 0 ? categories.getSelectedIndex() : 0);
				nfp.run();

			}

			/*There are two option, either give them an option to not delete any category
			 * and if no category was selected from the begin tell them to select a category.*/
			if (e.getSource() == deleteButton){

				//If no category selected then show this window
				if (categories.getSelectedIndex() == -1){

					Object[] object = {"Ok"};
					JOptionPane.showOptionDialog(null, "Try select a category", "Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, object, null);

				//If a category was selected then ask them if they are you they want to delete it.
				//This is in case you click on it on accident.
				}else {

					Object[] object = {"Yes", "No"};
					int res;
					res = JOptionPane.showOptionDialog(null, "Are you sure you want to remove this?", "Info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, object, null);

					if (res == JOptionPane.OK_OPTION) {


						//Removes the selected category and updates it to the user
						handler.getCategoryList().removeCategory(categories.getSelectedIndex());

						//Checks if the selected index was the last in the list
						if (categories.getSelectedIndex() == handler.getCategoryList().getCategoryNames().size()) {
							categorySelected--;
						}

						//Updates both the fields and the list
						handler.getGUILayout().updateCategoryList();
						handler.getGUILayout().updateFieldTable();
						//Selecting the previously selected index if it exist otherwise select 1 index lower;
						categories.setSelectedIndex(categorySelected);
					}
				}
			}

			if (e.getSource() == editButton){


				EditCategory editCat = new EditCategory();
				editCat.run();

			}

			if (e.getSource() == importButton){

				//TODO (if you want more to do) -- import files and check if the category exist.
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				try {
				FileReader rf = new FileReader(chooser.getSelectedFile().getAbsolutePath(),handler.getCategoryList());
				rf.load();
				updateCategoryList();
				updateFieldTable();
				updateTotal();

				}catch (NullPointerException j){
					System.out.println("No such path exist. Try select a valid path.");
				}
			}
		}
	}

	private class ListenForListSelection implements ListSelectionListener{

		/**If the user changed the selected category then update the fieldsTable with the current information*/
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getSource() == categories && categories.getSelectedIndex() >= 0){

				categorySelected = categories.getSelectedIndex();

				updateFieldTable();
			}
		}
	}

	public class ListenForWindow implements WindowListener{
		@Override
		public void windowOpened(WindowEvent e) {}

		/**If the main window is closing then save all the stored information current information to a file*/
		@Override
		public void windowClosing(WindowEvent e) {
			if (e.getSource() == frame){
				FileWriter fw = new FileWriter("res/files/datafile.ext", handler);
				fw.write();

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

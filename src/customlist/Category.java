package customlist;

import java.util.ArrayList;

public class Category {

	private String name;
	private int totalMoney;
	private ArrayList<Field> fields;


	/**A Category need to have a:
	 * @param name which is this categories name*/
	public Category(String name){
		fields = new ArrayList<Field>();
		this.name = name;
		totalMoney = 0;

	}

	public ArrayList<Field> getFields(){
		return fields;
	}

	/**Adds a new field to the current category
	 * @param date the date for the transaction
	 * @param money the money transferred/received
	 * @param comment a comment which the user can refer to*/
	public void addField(String date, int money, String comment){
		fields.add(new Field(date,money, comment));
		totalMoney += money;
	}

	/**Returns the name of the category*/
	public String getName(){
		return name;
	}

	public void changeName(String name){
		this.name = name;
	}

	/**Removes the field in the category which has the
	 * date, money and comment entered if there is one*/
	public void removeField(String date, int money, String comment){
		for (Field f: fields) {
			if (f.getDate().equals(date) && f.getMoney() == money && f.getComment().equals(comment)){
				totalMoney -= f.getMoney();
				fields.remove(f);

			}
		}
	}

	/**Removes the field in the category which the specified index
	 * @param index the index of the field which shall be removed*/
	public void removeField(int index){
		fields.remove(index);
	}

	/**Returns the field with the entered date, money and comment
	 * if there is one*/
	public Field getField(String date, int money, String comment){
		for (Field f: fields) {
			if (f.getDate().toLowerCase().equals(date.toLowerCase())
					&& f.getMoney() == money
					&& f.getComment().toLowerCase().equals(comment.toLowerCase())){
				return f;
			}
		}
		return null;
	}

	/**Returns teh data in a Object matrix with
	 * [data from all the fields][3 field parameters]*/
	public Object[][] getData(){

		Object[][] dataTemp = new Object[fields.size()][3];

		for (int i = 0; i < fields.size(); i++){
			for (int j = 0; j < 3; j++){
				dataTemp[i] = fields.get(i).getData();
			}
		}
		return dataTemp;
	}

	/**Returns the amount of fields in the category*/
	public int getLength(){
		return fields.size();
	}

	/**Returns the data in a string array for better writing to files*/
	public String[] getFieldsData(){
		String[] fieldData = new String[fields.size()];

		for (int i = 0; i < fields.size();i++){
			fieldData[i] = fields.get(i).getStringData();
		}
		return fieldData;
	}

	public int getCategoryMoney(){
		return totalMoney;
	}
}

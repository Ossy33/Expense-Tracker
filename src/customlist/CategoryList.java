package customlist;

import java.util.ArrayList;

public class CategoryList {

	private ArrayList<Category> categories;
	private ArrayList<String> names;
	private int length;

	/**A new categorylist with a
	 * arraylist of categories and a arraylist of names*/
	public CategoryList(){
		categories = new ArrayList<>();
		names = new ArrayList<>();
		length = 0;
	}

	public void clearAll(){
		categories.clear();
		names.clear();
		length = 0;
	}


	/**Returns the category dependent of the name
	 * @param name the name of the category*/
	public Category getCategory(String name){

		for (Category c : categories) {
			if (c.getName().toLowerCase().equals(name.toLowerCase())){
				return c;
			}
		}
		return null;

	}

	/**Returns the category dependent of the index in the arraylist
	 * @param index the index of the category i the arraylist*/
	public Category getCategory(int index){

		if (index < length && length >= 0){
			return categories.get(index);
		}
		return null;
	}

	/**Creates a new category with a entered name.
	 * @param name the name of the category*/
	public void newCategory(String name){
		names.add(name);
		length++;
		categories.add(new Category(name));
	}

	/**Returns the names of all the categories in a arraylist of strings*/
	public ArrayList<String> getCategoryNames(){
		names.clear();
		for (Category i : categories){
			names.add(i.getName());
		}

		return names;
	}

	/**Returns the data in a object matrix
	 * @param name the name of the category which will be returned*/
	public Object[][] getData(String name){
		return getCategory(name).getData();
	}

	/**Returns the data in a object matrix
	 * @param index the index of the category in the arraylist which will be returned*/
	public Object[][] getData(int index){

		if (index >= 0 && index < length) {
			return getCategory(index).getData();
		}
		return null;
	}

	/**Removes a category
	 * @param index the index of the category in the arraylist which will be removed*/
	public void removeCategory(int index) {

		try {

			categories.remove(index);
			names.remove(index);
			categories.trimToSize();
			names.trimToSize();
			length--;
		}catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Try select a category, or check if there are any left");
		}

	}

	/**Returns all the data from all the categories in the arraylist*/
	public String getCategoryListData(){
		String temp = "";

		for (int i = 0; i < categories.size();i++){
			for (String s: categories.get(i).getFieldsData()) {
				temp += i + "'" + s + "\n";
			}
		}
		return temp;
	}

	public int getTotalMoney(){
		int total = 0;
		for (Category i : categories){
			total += i.getCategoryMoney();
		}
		return total;
	}

	public ArrayList<Category> getCategories(){
		return categories;
	}

	public void insertCategory(Category category){
		categories.add(category);
		names.add(category.getName());
		length++;
	}
}

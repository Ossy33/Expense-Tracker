package Utils;

import UI.GUILayout;
import customlist.CategoryList;

public class Handler {

	private static CategoryList categoryList;
	private static GUILayout layout;

	public Handler(){
	}

	/**Sets the CategoryList*/
	public void setCategoryList(CategoryList categoryList) {
		Handler.categoryList = categoryList;
	}

	/**Returns the CategoryList if it has been initialized*/
	public CategoryList getCategoryList(){
		try {
			return Handler.categoryList;
		}catch (NullPointerException e){
			System.out.println("The CategoryList have never been initialized");
		}
		return null;

	}

	/**Sets the layout*/
	public void setLayout(GUILayout layout){
		Handler.layout = layout;
	}

	/**Returns the layout if it has been initialized*/
	public GUILayout getGUILayout(){
		try {
			return Handler.layout;
		}catch (NullPointerException e){
			System.out.println("The Layout have never been initialized");
		}
		return null;
	}
}

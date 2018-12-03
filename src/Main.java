import UI.GUILayout;
import Utils.FileReader;
import Utils.Handler;
import customlist.CategoryList;

public class Main {

	public static void main(String[] args) {

		CategoryList cl = new CategoryList();

		Handler handler = new Handler();
		handler.setCategoryList(cl);

		FileReader fr = new FileReader("res/files/datafile.ext", handler.getCategoryList());
		fr.load();

		GUILayout layout = new GUILayout();
		handler.setLayout(layout);

		layout.run();

	}
}

package Utils;

import customlist.CategoryList;

import java.io.*;

public class FileReader {

	private String file;
	private CategoryList categoryList;


	public FileReader(String file, CategoryList categoryList){
		this.file = file;
		this.categoryList = categoryList;
	}

	/**Loads the information into the categorylist from the specified file*/
	public void load(){
		int index = categoryList.getCategories().size();
		int lineCount = 0;
		String line;
		String[] lineParts;
		try {
			BufferedReader br = new BufferedReader(new java.io.FileReader(file));

			while ((line = br.readLine()) != null){

				if (!line.equals("")){
					lineParts = line.split("\'");

					if (lineCount == 0){
						for (String i : lineParts){
							categoryList.newCategory(i);
						}
					}else {
						categoryList.getCategory(Integer.parseInt(lineParts[0]) + index).addField(lineParts[1], Integer.parseInt(lineParts[2]), lineParts[3]);
					}
					lineCount++;
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

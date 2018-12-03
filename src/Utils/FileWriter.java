package Utils;

import java.io.BufferedWriter;
import java.io.IOException;

public class FileWriter {

	private String file;
	private Handler handler;

	public FileWriter(String file, Handler handler){
		this.file = file;
		this.handler = handler;
	}

	public void write(){

		try {
			BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(file));

			for (String i :handler.getCategoryList().getCategoryNames()){
				bw.write(i + "'");
			}
			bw.write("\n");


			bw.write(handler.getCategoryList().getCategoryListData());


			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

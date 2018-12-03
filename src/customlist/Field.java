package customlist;

import java.util.Date;

public class Field {

	private String date;
	private int money;
	private String comment;

	/**A Field contains
	 * @param  date current date
	 * @param money money spent/received
	 * @param comment comment for the transaction*/
	public Field(String date, int money, String comment) {
		this.date = date;
		this.money = money;
		this.comment = comment;

	}

	/**Returns the date*/
	public String getDate(){
		return date;
	}

	/**Returns the money*/
	public int getMoney() {
		return money;
	}

	/**Returns the comment*/
	public String getComment() {
		return comment;
	}

	/**Returns the data in a Object[]*/
	public Object[] getData(){
		return new Object[]{date, Integer.toString(money), comment};
	}

	/**Returns the data in a String*/
	public String getStringData(){
		return (date + "'" + money + "'" + comment);
	}
}

package ewhine.models;

import com.dehuinet.activerecord.Base;
import com.dehuinet.activerecord.Model;
import com.dehuinet.activerecord.annotation.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;


public class BtcBaseMarket extends Base implements Serializable {
	final static private Logger LOG = LoggerFactory.getLogger(BtcBaseMarket.class
			.getName());

	public static final int TYPE_1HOUR = 0;
	public static final int TYPE_30MINUTES = 1;
	public static final int TYPE_15MINUTES = 2;
	public static final int TYPE_5MINUTES = 3;
	public static final int TYPE_1MINUTE = 4;

	public static Map<Integer, String> typeTime2String = new HashMap<>();
	static {
		typeTime2String.put(TYPE_1HOUR, "1H");
		typeTime2String.put(TYPE_30MINUTES, "30M");
		typeTime2String.put(TYPE_15MINUTES, "15M");
		typeTime2String.put(TYPE_5MINUTES, "5M");
		typeTime2String.put(TYPE_1MINUTE, "1M");
	}


	private static final long serialVersionUID = 3L;

	@Column(name = "id")
	private long id;


	@Column(name = "time")
	private Timestamp time;


	@Column(name = "type_time")
	private int type_time;


	public static Logger getLOG() {
		return LOG;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}


	public int getTypetime() {
		return type_time;
	}

	public void setTypeTime(int type_time) {
		this.type_time = type_time;
	}



	@Override
	public String toString() {
		return "BtcBaseMarket{" +
				"id=" + id +
				", time=" + time +
				", type_time=" + type_time +
				'}';
	}

	public static void main(String[] args) throws IOException{
//		BtcBaseMarket market = Model.of(BtcBaseMarket.class)
//								.where("id > 0").last();
//		System.out.println(market);
//
//		Model.of(BtcBaseMarket.class)
//				.where("ADABTC = 1");
		File file = new File("test/calender.csv");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line=reader.readLine()) != null) {
			String[] items = line.split(",");
			//close_days.add(toIntDate(2018, 12, 30));
			if (items.length == 2 && items[1].equals("False")) {
				String[] data = items[0].split("-");
				if (data.length==3) {
					String year = data[0];
					String month = data[1];
					if (month.startsWith("0")) {
						month = month.replace("0", "");
					}
					String day = data[2];
					if (day.startsWith("0")) {
						day = day.replace("0", "");
					}

					System.out.println("close_days.add(toIntDate(" + year + ", " + month + ", " + day
										+ "));");
				}
			}
		}


	}
}

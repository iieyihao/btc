package ewhine.models;

import com.dehuinet.activerecord.Model;
import com.dehuinet.activerecord.Session;
import com.dehuinet.activerecord.StoreManager;
import ewhine.service.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class FileArrayProcessor extends Thread {
	final static private Logger LOG = LoggerFactory
			.getLogger(FileArrayProcessor.class.getName());
	final private Session session = StoreManager.openSession();
	final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Thread t;
	private List<String> flist;

	public FileArrayProcessor(List<String> fileArray) {
		this.flist = fileArray;
	}

	public void run() {
		for (String file : flist) {
			String fileName = Main.BASE_DIR + "/" + file;
			System.out.println("start file:" + fileName + " ............");
			File f = new File(fileName);
			if (!f.exists()) {
				LOG.error(file + " not exist");
				continue;
			}
			if (!f.isFile()) {
				LOG.error(file + " is not a file");
				continue;
			}
			try {
				parseContent(f);
			} catch (Exception e) {
				LOG.error("file name:"  + fileName, e);
				continue;
			}
			if (!f.delete()) {
				LOG.error("delete file:" + file + " failed");
			}
		}
	}

	private void parseContent(File f) throws Exception{
		String fileName = f.getName();

		String marketName = fileName.split("_")[1];
		String marketType = fileName.split("_")[3].replace(".csv","");

		int n = 0;
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				if (!lineTxt.startsWith("2018") && !lineTxt.startsWith("2017")) {
					continue;
				}
				String[] items = lineTxt.split(",");
				String time = items[0];
				String open = items[1];
				String high = items[2];
				String low = items[3];
				String close = items[4];
				String volume = items[5];
//				String quote_volume = items[6];
//				String taker_buy_base_asset_volume = items[7];
//				String taker_buy_quote_asset_volume = items[8];
//				String trade_num = items[9];

				if (n == 0) {
					try {
						if (!isColumnExist("btc_open_market", marketName)) {
							session.executeUpdate("alter table btc_open_market add " + marketName + " varchar(20) CHARACTER SET   ascii;", new ArrayList<>());
						}
						if (!isColumnExist("btc_close_market", marketName)) {
							session.executeUpdate("alter table btc_close_market add " + marketName + " varchar(20) CHARACTER SET   ascii ;", new ArrayList<>());
						}
						if (!isColumnExist("btc_high_market", marketName)) {
							session.executeUpdate("alter table btc_high_market add " + marketName + " varchar(20) CHARACTER SET   ascii ;", new ArrayList<>());
						}
						if (!isColumnExist("btc_low_market", marketName)) {
							session.executeUpdate("alter table btc_low_market add " + marketName + " varchar(20) CHARACTER SET   ascii ;", new ArrayList<>());
						}
						if (!isColumnExist("btc_volume_market", marketName)) {
							session.executeUpdate("alter table btc_volume_market add " + marketName + " varchar(20) CHARACTER SET   ascii ;", new ArrayList<>());
						}

					} catch (Throwable t) {

					}
				}


				BtcBaseMarket market = new BtcBaseMarket();
//				market.setTime(time);
				if (marketType.equals("15T")) {
					market.setTypeTime(BtcBaseMarket.TYPE_15MINUTES);
				} else if (marketType.equals("30T")) {
					market.setTypeTime(BtcBaseMarket.TYPE_30MINUTES);
				} else if (marketType.equals("5T")) {
					market.setTypeTime(BtcBaseMarket.TYPE_5MINUTES);
				} else if (marketType.equals("1T")) {
					market.setTypeTime(BtcBaseMarket.TYPE_1MINUTE);
				} else if (marketType.equals("1H")) {
					market.setTypeTime(BtcBaseMarket.TYPE_1HOUR);
				} else {
					LOG.error("processing file:" + f.getAbsolutePath() + " type error:" + marketType);
					return;
				}

				int updated_cnt = Model.of(BtcOpenMarket.class)
										.where("time = ? and type = ? ", time, market.getTypetime())
										.update_all(marketName + " = ?", open);
				if (updated_cnt == 0) {
					session.executeUpdate("insert into btc_open_market (time, type, " + marketName + ") values ('" + time + "'," + market.getTypetime() + ",'" + open +"')",new ArrayList<>());
				}

				updated_cnt = Model.of(BtcCloseMarket.class)
								.where("time = ? and type = ? ", time, market.getTypetime())
								.update_all(marketName + " = ?", close);
				if (updated_cnt == 0) {
					session.executeUpdate("insert into btc_close_market (time, type, " + marketName + ") values ('" + time + "'," + market.getTypetime() + ",'" + close +"')",new ArrayList<>());
				}

				updated_cnt = Model.of(BtcHighMarket.class)
									.where("time = ? and type = ? ", time, market.getTypetime())
									.update_all(marketName + " = ?", high);
				if (updated_cnt == 0) {
					session.executeUpdate("insert into btc_high_market (time, type, " + marketName + ") values ('" + time + "'," + market.getTypetime() + ",'" + high +"')",new ArrayList<>());
				}

				updated_cnt = Model.of(BtcLowMarket.class)
									.where("time = ? and type = ? ", time, market.getTypetime())
									.update_all(marketName + " = ?", low);
				if (updated_cnt == 0) {
					session.executeUpdate("insert into btc_low_market (time, type, " + marketName + ") values ('" + time + "'," + market.getTypetime() + ",'" + low +"')",new ArrayList<>());
				}

				updated_cnt = Model.of(BtcVolumeMarket.class)
									.where("time = ? and type = ? ", time, market.getTypetime())
									.update_all(marketName + " = ?", volume);
				if (updated_cnt == 0) {
					session.executeUpdate("insert into btc_volume_market (time, type, " + marketName + ") values ('" + time + "'," + market.getTypetime() + ",'" + high +"')",new ArrayList<>());
				}
				System.out.println("file:" + fileName + " line:" + n++);
			}
		}

	}

	public void start () {
		if (t == null) {
			t = new Thread (this);
			t.start ();
		}
	}

	public boolean isColumnExist(String table, String column) {
		String sql = "select COUNT(COLUMN_NAME) as count from INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='btc' AND table_name='" + table + "' AND COLUMN_NAME='" + column + "'";
		boolean[] bos = {true};
		session.executeQuery(sql, new ArrayList<>(), (x) -> {
			if (x.getFetchSize() == 0) {
				bos[0] = false;
			} else {
				bos[0] = true;
			}
			return null;
		});
		return bos[0];

	}

	public static void main(String[] args) {
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(timeZone);
//new FileArrayProcessor("btc_files/").start();
//		if (new FileArrayProcessor("xx").isColumnExist("btc_open_market", "time")) {
//			System.out.println("no");
//		} else {
//			System.out.println("yes");
//		}

	}

}

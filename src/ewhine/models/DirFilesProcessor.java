package ewhine.models;

import com.dehuinet.activerecord.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DirFilesProcessor extends Thread {
	final static private Logger LOG = LoggerFactory
			.getLogger(DirFilesProcessor.class.getName());
	final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Thread t;
	private String dirName;

	public DirFilesProcessor(String dirName) {
		this.dirName = dirName;
	}

	public void run() {
		File dirFile = new File(dirName);
		if (!dirFile.exists()) {
			LOG.error(dirName + " not exist");
			return;
		}
		if (!dirFile.isDirectory()) {
			LOG.error(dirName + " is not a dirctory");
			return;
		}

		String[] fileList = dirFile.list();
		for (String file : fileList) {
			String fileName = dirName + "/" + file;
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

		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				if (!lineTxt.startsWith("2018")) {
					continue;
				}
				String[] items = lineTxt.split(",");
				Timestamp time = new Timestamp(sdf.parse(items[0]).getTime());
				String open = items[1];
				String high = items[2];
				String low = items[3];
				String close = items[4];
				String volume = items[5];
				String quote_volume = items[6];
				String taker_buy_base_asset_volume = items[7];
				String taker_buy_quote_asset_volume = items[8];
				String trade_num = items[9];

				BtcMarket market = new BtcMarket();
				market.setName(marketName);
				market.setTime(time);
				if (marketType.equals("15T")) {
					market.setType(BtcMarket.TYPE_15MINUTES);
				} else if (marketType.equals("30T")) {
					market.setType(BtcMarket.TYPE_30MINUTES);
				} else if (marketType.equals("5T")) {
					market.setType(BtcMarket.TYPE_5MINUTES);
				} else if (marketType.equals("1T")) {
					market.setType(BtcMarket.TYPE_1MINUTE);
				} else if (marketType.equals("1H")) {
					market.setType(BtcMarket.TYPE_1HOUR);
				} else {
					LOG.error("processing file:" + f.getAbsolutePath() + " type error:" + marketType);
					return;
				}
				market.setOpen(open);
				market.setHight(high);
				market.setLow(low);
				market.setClose(close);
				market.setQuote_volume(quote_volume);
				market.setTaker_buy_base_asset_volume(taker_buy_base_asset_volume);
				market.setTaker_buy_quote_asset_volume(taker_buy_quote_asset_volume);
				market.setTrade_num(trade_num);
				market.setVolume(volume);
				market.save();

			}
		}

	}

	public void start () {
		if (t == null) {
			t = new Thread (this);
			t.start ();
		}
	}

	public static void main(String[] args) {
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(timeZone);
		Model.of(BtcMarket.class).where("1=1").delete_all();
		new DirFilesProcessor("btc_files/").start();
	}

}

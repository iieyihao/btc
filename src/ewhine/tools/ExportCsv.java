package ewhine.tools;

import com.dehuinet.activerecord.Session;
import com.dehuinet.activerecord.StoreManager;
import ewhine.models.BtcBaseMarket;
import ewhine.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ExportCsv extends Thread {
	final static private Logger LOG = LoggerFactory
			.getLogger(ExportCsv.class.getName());

	private Thread t;
	private int type;
	private List<String> nameList;
	public ExportCsv(List<String> nameList,int type) {
		this.type = type;
		this.nameList = nameList;
	}

	public void run() {
		Session session = StoreManager.openSession();

		for (String name : nameList) {
			try {
				final String outFile = "/Users/mysql/" + name + "_" + BtcBaseMarket.typeTime2String.get(type) + ".csv";
				File file = new File(outFile);
				if (file.exists()) {
					if (file.length() == 0) {
						file.delete();
						LOG.warn("file exist:" + outFile + " but size 0,so delete it!");
					} else {
						LOG.warn("file exist:" + outFile);
						continue;
					}
				}
				final String sql = "select * into outfile '"+ outFile + "' fields terminated by ',' lines terminated by '\\n' " +
						"from (select 'time','open','high','low','close','volume','quote_volume','taker_buy_base_asset_volume','taker_buy_quote_asset_taker_buy_quote_asset_volume','trade_num' " +
						"union (select date_add(time,interval 8 hour),open,high,low,close,volume,quote_volume,taker_buy_base_asset_volume,taker_buy_quote_asset_volume,trade_num " +
						"from btc_market where name='" + name + "' and type=" + type + " group by time,open,high,low,close,volume,quote_volume,taker_buy_base_asset_volume,taker_buy_quote_asset_volume,trade_num order by time asc)) b " +
						";";
				session.executeQuery(sql, new ArrayList<>(), (x) -> {return null;});
				LOG.info("file:" + outFile + " ok");
			} catch (Throwable t) {
				LOG.error("name:" + name + " type:" + BtcBaseMarket.typeTime2String.get(type), t);
			}

		}
	}

	public void start () {
		if (t == null) {
			t = new Thread (this);
			t.start();
		}
	}
	public static void main(String[] args) throws Exception {
		// 启动顺序
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		TimeZone.setDefault(timeZone);

		// 1.打开配置服务
		ConfigService config_service = new ConfigService();
		config_service.start();

//		Session session = StoreManager.openSession();
//		List<String> nameList = new ArrayList<>();
//		session.executeQuery("select DISTINCT name from btc_market;", new ArrayList<>(), (rs) -> {
//			while (rs.next()) {
//				nameList.add(rs.getString(1));
//			}
//			return null;
//		});
//
//		final List<String> nameLeftList = nameList.subList(0, nameList.size()/2);
//		final List<String> nameRightList = nameList.subList(nameList.size()/2, nameList.size());
//		if (nameLeftList.size() + nameRightList.size() != nameList.size()) {
//			throw new Exception("fatal error");
//		}
//		for (int type = 0; type<= BtcBaseMarket.TYPE_1MINUTE; type++) {
//			new ExportCsv(nameLeftList, type).start();
//			new ExportCsv(nameRightList, type).start();
//		}

		Session session = StoreManager.openSession();
		String[] tables = {"btc_open_market","btc_close_market","btc_high_market","btc_low_market","btc_volume_market"};

		for (String table : tables) {
			List<String> nameList = new ArrayList<>();
			session.executeQuery("select COLUMN_NAME from information_schema.columns where table_name='" + table + "';", new ArrayList<>(), (rs) -> {
				while (rs.next()) {
					nameList.add(rs.getString(1));
				}
				return null;
			});
			System.out.println(nameList.size());
//			for (String colName : nameList) {
//				if ("id".equals(colName) || "time".equals(colName) || "type".equals(colName)) {
//					continue;
//				}
//				String sql = "alter table " + table + " modify column " + colName + " BLOB;";
//				session.executeUpdate(sql, new ArrayList<>());
//			}
		}


//		session.executeQuery("select * into outfile '/Users/mysql/t.csv' from btc_market limit 1", new ArrayList<>(), (x) -> {return null;});

	}


}

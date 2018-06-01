package ewhine.models;

import com.dehuinet.activerecord.Base;
import com.dehuinet.activerecord.Model;
import com.dehuinet.activerecord.annotation.Column;
import com.dehuinet.activerecord.annotation.Table;
import ewhine.redis.RedisServer;

import ewhine.util.CSVUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.SU;
import tools.Secure;
import tools.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Table(name = "btc_market")
public class BtcMarket extends Base implements Serializable {
	final static private Logger LOG = LoggerFactory.getLogger(BtcMarket.class
			.getName());

	public static final int TYPE_1HOUR = 0;
	public static final int TYPE_30MINUTES = 1;
	public static final int TYPE_15MINUTES = 2;
	public static final int TYPE_5MINUTES = 3;
	public static final int TYPE_1MINUTE = 4;

	public static Map<Integer, String> type2String= new HashMap<>();
	static {
		type2String.put(TYPE_1HOUR, "1H");
		type2String.put(TYPE_30MINUTES, "30M");
		type2String.put(TYPE_15MINUTES, "15M");
		type2String.put(TYPE_5MINUTES, "5M");
		type2String.put(TYPE_1MINUTE, "1M");
	}


	private static final long serialVersionUID = 3L;

	@Column(name = "id")
	private long id;

	@Column(name = "name")
	private String name;

	@Column(name = "time")
	private Timestamp time;

	@Column(name = "open")
	private String open;

	@Column(name = "high")
	private String hight;

	@Column(name = "low")
	private String low;

	@Column(name = "close")
	private String close;

	@Column(name = "volume")
	private String volume;

	@Column(name = "quote_volume")
	private String quote_volume;

	@Column(name = "taker_buy_base_asset_volume")
	private String taker_buy_base_asset_volume;

	@Column(name = "taker_buy_quote_asset_volume")
	private String taker_buy_quote_asset_volume;

	@Column(name = "trade_num")
	private String trade_num;

	@Column(name = "type")
	private int type;

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

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getHight() {
		return hight;
	}

	public void setHight(String hight) {
		this.hight = hight;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getQuote_volume() {
		return quote_volume;
	}

	public void setQuote_volume(String quote_volume) {
		this.quote_volume = quote_volume;
	}

	public String getTaker_buy_base_asset_volume() {
		return taker_buy_base_asset_volume;
	}

	public void setTaker_buy_base_asset_volume(String taker_buy_base_asset_volume) {
		this.taker_buy_base_asset_volume = taker_buy_base_asset_volume;
	}

	public String getTaker_buy_quote_asset_volume() {
		return taker_buy_quote_asset_volume;
	}

	public void setTaker_buy_quote_asset_volume(String taker_buy_quote_asset_volume) {
		this.taker_buy_quote_asset_volume = taker_buy_quote_asset_volume;
	}

	public String getTrade_num() {
		return trade_num;
	}

	public void setTrade_num(String trade_num) {
		this.trade_num = trade_num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BtcMarket{" +
				"id=" + id +
				", name='" + name + '\'' +
				", time=" + time +
				", open='" + open + '\'' +
				", hight='" + hight + '\'' +
				", low='" + low + '\'' +
				", close='" + close + '\'' +
				", volume='" + volume + '\'' +
				", quote_volume='" + quote_volume + '\'' +
				", taker_buy_base_asset_volume='" + taker_buy_base_asset_volume + '\'' +
				", taker_buy_quote_asset_volume='" + taker_buy_quote_asset_volume + '\'' +
				", trade_num='" + trade_num + '\'' +
				", type='" + type + '\'' +
				'}';
	}

	public static void main(String[] args) {
		BtcMarket market = Model.of(BtcMarket.class)
								.where("id > 0").last();
		System.out.println(market);
	}
}

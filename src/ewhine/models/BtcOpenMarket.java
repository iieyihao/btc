package ewhine.models;

import com.dehuinet.activerecord.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Table(name = "btc_open_market")
public class BtcOpenMarket extends BtcBaseMarket {
	final static private Logger LOG = LoggerFactory.getLogger(BtcOpenMarket.class
			.getName());

}

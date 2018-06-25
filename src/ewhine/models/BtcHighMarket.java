package ewhine.models;

import com.dehuinet.activerecord.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Table(name = "btc_high_market")
public class BtcHighMarket extends BtcBaseMarket {
	final static private Logger LOG = LoggerFactory.getLogger(BtcHighMarket.class
			.getName());

}

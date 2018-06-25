package ewhine.models;

import com.dehuinet.activerecord.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Table(name = "btc_low_market")
public class BtcLowMarket extends BtcBaseMarket {
	final static private Logger LOG = LoggerFactory.getLogger(BtcLowMarket.class
			.getName());

}

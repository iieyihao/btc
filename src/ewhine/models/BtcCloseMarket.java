package ewhine.models;

import com.dehuinet.activerecord.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Table(name = "btc_close_market")
public class BtcCloseMarket extends BtcBaseMarket {
	final static private Logger LOG = LoggerFactory.getLogger(BtcCloseMarket.class
			.getName());

}

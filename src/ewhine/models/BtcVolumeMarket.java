package ewhine.models;

import com.dehuinet.activerecord.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Table(name = "btc_volume_market")
public class BtcVolumeMarket extends BtcBaseMarket {
	final static private Logger LOG = LoggerFactory.getLogger(BtcVolumeMarket.class
			.getName());

}

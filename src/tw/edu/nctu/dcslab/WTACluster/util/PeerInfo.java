package tw.edu.nctu.dcslab.WTACluster.util;

import jade.core.AID;


public class PeerInfo {
	public String name;
	public String ip;

	public PeerInfo(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}

	public String toString() {
		return this.name + "@" + this.ip;
	}

	public AID getAID() {
		AID aid = new AID( AID.createGUID(this.name, this.ip + ":1099/JADE"), AID.ISGUID );
		aid.addAddresses( "http://" + this.ip + ":7778/acc");

		return aid;
	}
}

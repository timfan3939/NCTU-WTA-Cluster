package tw.edu.nctu.dcslab.WTACluster.util;


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
}

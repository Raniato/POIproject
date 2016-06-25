package main;


public class Memory {

	private static Memory memory;
	private String database;
	private String ip;
	private String port;
	private int T;
	private int R;

	private Memory(){
		//Initialize any fields required here
	}
	public static Memory getInstance() {
		if (memory == null) {
			synchronized (Memory.class) {
				memory = new Memory();
			}
		}
		return memory;
	}


	public String getDatabase() {
		return database;
	}


	public void setDatabase(String database) {
		this.database = database;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}
	
	public int getR() {
		return R;
	}
	
	public void setR(int R){
		this.R = R;
	}
	
	public int getT() {
		return T;
	}
	
	public void setT(int T){
		this.T = T;
	}
	
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		System.out.println("Clone not supported for singleton object");
		return null;
	}

	
	
}

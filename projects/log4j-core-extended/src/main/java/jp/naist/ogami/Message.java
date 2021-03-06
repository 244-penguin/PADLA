package jp.naist.ogami;

@org.msgpack.annotation.Message
public class Message {
	public Message() {
	}

	@org.msgpack.annotation.Index(0)
	public boolean ISFIRSTLEVEL = false;

	public boolean isISFIRSTLEVEL() {
		return ISFIRSTLEVEL;
	}

	public void setISFIRSTLEVEL(boolean iSFIRSTLEVEL) {
		ISFIRSTLEVEL = iSFIRSTLEVEL;
	}

	@org.msgpack.annotation.Index(1)
	public String BUFFEROUTPUT = null;

	public String getBUFFEROUTPUT() {
		return BUFFEROUTPUT;
	}

	public void setBUFFEROUTPUT(String bUFFEROUTPUT) {
		BUFFEROUTPUT = bUFFEROUTPUT;
	}

	@org.msgpack.annotation.Index(2)
	public int CACHESIZE = 0;

	public int getCACHESIZE() {
		return CACHESIZE;
	}

	public void setCACHESIZE(int cACHESIZE) {
		CACHESIZE = cACHESIZE;
	}

	@org.msgpack.annotation.Index(3)
	public int NUMOFMETHODS = -1;

	public int getNUMOFMETHODS() {
		return NUMOFMETHODS;
	}

	public void setNUMOFMETHODS(int numOfMethods) {
		this.NUMOFMETHODS = numOfMethods;
	}

	@org.msgpack.annotation.Index(4)
	private int BUFFEREDINTERVAL = 2;

	public int getBUFFEREDINTERVAL() {
		return BUFFEREDINTERVAL;
	}

	public void setBUFFEREDINTERVAL(int bUFFEREDINTERVAL) {
		BUFFEREDINTERVAL = bUFFEREDINTERVAL;
	}
}

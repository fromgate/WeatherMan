package fromgate.weatherman;

public class WMResult {
	int blocks;
	int chunks;
	Long time;
	
	public WMResult (int blocks, int chunks){
		this.blocks=blocks;
		this.chunks=chunks;
		this.time = System.currentTimeMillis();
	}
	
	public void calcTime(Long starttime){
		this.time = System.currentTimeMillis()-starttime;
	}
	
	public float getTimeSec(){
		float ftime = ((float) this.time)/1000;
		return ftime;
	}
}

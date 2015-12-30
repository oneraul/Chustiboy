package chustiboy.gameplay;

public abstract class AnimationData {
	
	public static class Direction {
		public static byte
		east 	  = 0,
		northEast = 1, 
		north 	  = 2, 
		northWest = 3, 
		west 	  = 4, 
		southWest = 5, 
		south 	  = 6, 
		southEast = 7;
	}
	
	protected byte direction;
	
	public byte getDirection() {
		return direction;
	}
	
	abstract AnimationFrame[] getFrames();
	abstract AnimationFrame[] setDirection(byte direction);
}

package chustiboy.gameplay;

public class FlechaAnimationData extends AnimationData {
	private AnimationFrame[][] stuff;
	private byte direction;
	
	FlechaAnimationData() {
		direction = Direction.east;
		float duration = 0.1f;
		
		stuff = new AnimationFrame[][] {
			// east
			new AnimationFrame[] {
				new AnimationFrame(0, 6, 9, 3, 5, 5, duration),
				new AnimationFrame(0, 9, 9, 3, 5, 5, duration)
			},
			
			// northEast
			new AnimationFrame[] {
				new AnimationFrame(37, 8, 8, 8, 4, 6, duration),
				new AnimationFrame(45, 8, 8, 8, 4, 6, duration)
			},
			
			//north
			new AnimationFrame[] {
				new AnimationFrame(15, 0, 3, 9, 2, 8, duration),
				new AnimationFrame(18, 0, 3, 9, 2, 8, duration)
			},
			
			//northWest
			new AnimationFrame[] {
				new AnimationFrame(21, 8, 8, 8, 4, 7, duration),
				new AnimationFrame(29, 8, 8, 8, 4, 7, duration)
			},
			
			//west
			new AnimationFrame[] {
				new AnimationFrame(0, 0, 9, 3, 5, 5, duration),
				new AnimationFrame(0, 3, 9, 3, 5, 5, duration)
			},
			
			//southWest
			new AnimationFrame[] {
				new AnimationFrame(21, 0, 8, 8, 4, 7, duration),
				new AnimationFrame(29, 0, 8, 8, 4, 7, duration)
			},
			
			//south
			new AnimationFrame[] {
				new AnimationFrame( 9, 0, 3, 9, 2, 8, duration),
				new AnimationFrame(12, 0, 3, 9, 2, 8, duration)
			},
			
			//southEast
			new AnimationFrame[] {
				new AnimationFrame(37, 0, 8, 8, 4, 7, duration),
				new AnimationFrame(45, 0, 8, 8, 4, 7, duration)
			}
		};
	}
	
	@Override
	public AnimationFrame[] getFrames() {
		return stuff[direction];
	}
	
	@Override
	public AnimationFrame[] setDirection(byte direction) {
		this.direction = direction;
		return getFrames();
	}
}

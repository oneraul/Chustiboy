package chustiboy.gameplay;

public class ChustillaAnimationData extends AnimationData {
	
	public static class State {
		public static byte
		idle   = 0,
		walk   = 1,
		attack = 2,
		dying  = 3,
		dead   = 4;
	}
	
	private AnimationFrame[][][] stuff;
	private byte state;
	
	public ChustillaAnimationData() {
		int size = 48;
		int originX = 24;
		int originY = 14;
		direction = Direction.east;
		state = State.idle;
		
		//dying
		AnimationFrame[] dying = new AnimationFrame[] {
			new AnimationFrame(size*8, size*0, size, size, originX, originY, 0.125f),
			new AnimationFrame(size*9, size*0, size, size, originX, originY, 0.125f),
			new AnimationFrame(size*8, size*1, size, size, originX, originY, 0.125f),
			new AnimationFrame(size*9, size*1, size, size, originX, originY, 0.125f),
			new AnimationFrame(size*8, size*2, size, size, originX, originY, 0.125f),
			new AnimationFrame(size*9, size*2, size, size, originX, originY, 0.125f),
			new AnimationFrame(size*8, size*3, size, size, originX, originY, 0.175f),
			new AnimationFrame(size*9, size*3, size, size, originX, originY, 0.550f)
		};
		
		//dead
		AnimationFrame[] dead = new AnimationFrame[] {
			new AnimationFrame(size*9, size*3, size, size, originX, originY, 0)
		};
		
		stuff = new AnimationFrame[][][] {
			// east
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(size*0, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*0, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*0, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*0, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*0, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			// northEast
			new AnimationFrame[][] {
				// idle
				new AnimationFrame[] {
					new AnimationFrame(size*1, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*1, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*1, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*1, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*1, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//north
			new AnimationFrame[][] {
				new AnimationFrame[] {
					new AnimationFrame(size*2, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*2, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*2, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*2, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*2, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//northWest
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(size*3, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*3, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*3, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*3, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*3, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//west
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(size*4, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*4, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*4, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*4, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*4, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//southWest
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(size*5, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*5, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*5, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*5, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*5, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//south
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(size*6, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*6, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*6, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*6, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*6, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//southEast
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(size*7, size*0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(size*7, size*1, size, size, originX, originY, 0.125f),
					new AnimationFrame(size*7, size*2, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(size*7, size*3, size, size, originX, originY, 0.1f),
					new AnimationFrame(size*7, size*4, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
		};
	}
	
	@Override
	public AnimationFrame[] getFrames() {
		return stuff[direction][state];
	}
	
	@Override
	public AnimationFrame[] setDirection(byte direction) {
		this.direction = direction;
		return getFrames();
	}
	
	public AnimationFrame[] setState(byte state) {
		this.state = state;
		return getFrames();
	}
	
	byte getState() {
		return state;
	}
}

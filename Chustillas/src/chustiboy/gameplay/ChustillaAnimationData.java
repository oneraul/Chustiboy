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
	private byte direction, state;
	
	ChustillaAnimationData() {
		int size = 24;
		int originX = 12;
		int originY = 10;
		direction = Direction.east;
		state = State.idle;
		
		//dying
		AnimationFrame[] dying = new AnimationFrame[] {
			new AnimationFrame(192,  0, size, size, originX, originY, 0.125f),
			new AnimationFrame(216,  0, size, size, originX, originY, 0.125f),
			new AnimationFrame(192, 24, size, size, originX, originY, 0.125f),
			new AnimationFrame(216, 24, size, size, originX, originY, 0.125f),
			new AnimationFrame(192, 48, size, size, originX, originY, 0.125f),
			new AnimationFrame(216, 48, size, size, originX, originY, 0.125f),
			new AnimationFrame(192, 72, size, size, originX, originY, 0.175f),
			new AnimationFrame(216, 72, size, size, originX, originY, 0.550f)
		};
		
		//dead
		AnimationFrame[] dead = new AnimationFrame[] {
			new AnimationFrame(216, 72, size, size, originX, originY, 0)
		};
		
		stuff = new AnimationFrame[][][] {
			// east
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(0, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(0, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(0, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(0, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(0, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			// northEast
			new AnimationFrame[][] {
				// idle
				new AnimationFrame[] {
					new AnimationFrame(24, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(24, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(24, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(24, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(24, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//north
			new AnimationFrame[][] {
				new AnimationFrame[] {
					new AnimationFrame(48, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(48, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(48, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(48, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(48, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//northWest
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(72, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(72, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(72, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(72, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(72, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//west
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(96, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(96, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(96, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(96, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(96, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//southWest
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(120, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(120, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(120, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(120, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(120, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//south
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(144, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(144, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(144, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(144, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(144, 96, size, size, originX, originY, 0.1f)
				},
				dying, dead
			},
			
			//southEast
			new AnimationFrame[][] {
				//idle
				new AnimationFrame[] {
					new AnimationFrame(168, 0, size, size, originX, originY, 0)
				},
				//walk
				new AnimationFrame[] {
					new AnimationFrame(168, 24, size, size, originX, originY, 0.125f),
					new AnimationFrame(168, 48, size, size, originX, originY, 0.125f)
				},
				//attack
				new AnimationFrame[] {
					new AnimationFrame(168, 72, size, size, originX, originY, 0.1f),
					new AnimationFrame(168, 96, size, size, originX, originY, 0.1f)
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
	
	byte getDirection() {
		return direction;
	}
	
	byte getState() {
		return state;
	}
}

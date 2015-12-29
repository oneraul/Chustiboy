package chustiboy.gameplay;

public class AnimationFrame {
	int x, y, width, height, originX, originY;
	float duration;
	
	public AnimationFrame(int x, int y, int w, int h, int originX, int originY, float duration) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.originX = originX;
		this.originY = originY;
		this.duration = duration;
	}
}
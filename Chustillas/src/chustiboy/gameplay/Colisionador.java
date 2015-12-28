package chustiboy.gameplay;

public class Colisionador {
	
	public static boolean colision(Rectangle a, Rectangle b) {
		if(a.pos.x - a.w >= b.pos.x + b.w ||
		   a.pos.x + a.w <= b.pos.x - b.w ||
		   a.pos.y 		 >= b.pos.y + b.h ||
		   a.pos.y + a.h <= b.pos.y)
				return false;
		
		return true;
	}
	
	public static boolean colision(Rectangle r, Circle c) {
		// TODO
		
		return false;
	}
	
	public static boolean colision(Circle a, Circle b) {
		if(a.pos.dst2(b.pos) >= (a.r + b.r)*(a.r + b.r)) {
			return false;
		}
		
		return true;
	}
	
	public static boolean colision(Point p, Rectangle r) {
		if(p.pos.x <= r.pos.x - r.w
		|| p.pos.x >= r.pos.x + r.w
		|| p.pos.y <= r.pos.y
		|| p.pos.y >= r.pos.y + r.h)
			return false;
				
		return true;
	}
	
	public static boolean colision(Point p, Circle c) {
		if(p.pos.dst2(c.pos) >= c.r*c.r) {
			return false;
		}
		
		return true;
	}
	
	public static boolean colision(Point a, Point b) {
		float epsilon = 0.05f;
		if(a.pos.dst(b.pos) <= epsilon)
			return true;
		
		return false;
	}
}

package chustiboy.gameplay;

import com.badlogic.gdx.utils.Array;

public abstract class Pool<T> {

	public Array<T> active, pool;
	boolean whenEmptyReturnOldestActiveItem;
	
	public Pool(int max, boolean whenEmptyReturnOldestActiveItem) {
		this.whenEmptyReturnOldestActiveItem = whenEmptyReturnOldestActiveItem;
		
		active = new Array<T>();
		pool = new Array<T>();
		
		for(int i = 0; i < max; i++) {
			pool.add(newItem());
		}
	}
	
	public abstract T newItem();
	
	public T obtain() {
		if(!isPoolEmpty()) {
			T item = pool.pop();
			active.add(item);
			return item;
		} 
		else if(whenEmptyReturnOldestActiveItem) {
			free(active.get(0));
			return obtain();
		}
		
		return null;
	}
	
	public void free(T item) {
		pool.add(item);
		active.removeValue(item, true);
		((Poolable)item).reset();
	}
	
	public void freeAll() {
		pool.addAll(active);
		active.clear();
		
		for(T item : pool) ((Poolable)item).reset();
	}
	
	public boolean isPoolEmpty() {
		if(pool.size > 0) 
			 return false;
		else return true;
	}
	
	public interface Poolable {
		public void reset();
	}
}

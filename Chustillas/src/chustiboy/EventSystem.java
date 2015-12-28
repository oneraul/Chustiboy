package chustiboy;

import com.badlogic.gdx.utils.Array;

public class EventSystem
{
	public static void produceMessage(Object o, Array<Object> messagesQueue) {
		messagesQueue.add(o);
	}
	
	public static void consumeMessages(Array<Object> messagesQueue, EventConsumer consumer) {
   		synchronized(messagesQueue) {
   			
   			// I don't know why but it gives ConcurrentModificationException with enhanced for loop
			for(int i = 0; i < messagesQueue.size; i++) {
				consumer.consumeMessage(messagesQueue.get(i));
			}

			messagesQueue.clear();
		}
	}
	
	public interface EventConsumer {
		public void consumeMessage(Object o);
	}
}

package tl.tudelft.gbot.chat.server;

import java.util.Queue;

/**
 * Queue implementation where everything is synchronized around the SynchronizedQueue instance.
 * @param <E> Element type.
 */
public class SynchronizedQueue<E> extends SynchronizedCollection<E> implements Queue<E> {
	private final Queue<E> queue;
	public SynchronizedQueue(Queue<E> queue) {
		super(queue);
		this.queue = queue;
	}
	@Override
	public final boolean offer(E e) {
		synchronized (mutex) {
			return queue.offer(e);
		}
	}
	@Override
	public final E remove() {
		synchronized (mutex) {
			return queue.remove();
		}
	}
	@Override
	public final E poll() {
		synchronized (mutex) {
			return queue.poll();
		}
	}
	@Override
	public final E element() {
		synchronized (mutex) {
			return queue.element();
		}
	}
	@Override
	public final E peek() {
		synchronized (mutex) {
			return queue.peek();
		}
	}
}

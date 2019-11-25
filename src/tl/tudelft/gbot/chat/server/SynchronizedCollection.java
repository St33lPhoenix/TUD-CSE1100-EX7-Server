package tl.tudelft.gbot.chat.server;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Collection implementation where everything is synchronized around the SynchronizedCollection instance.
 * @param <E> Element type.
 */
public class SynchronizedCollection<E> implements Collection<E> {
	private final Collection<E> collection;
	protected Object mutex = this;
	public SynchronizedCollection(Collection<E> collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Queue cannot be null");
		}
		this.collection = collection;
	}
	@Override
	public final int size() {
		synchronized (mutex) {
			return collection.size();
		}
	}
	@Override
	public final boolean isEmpty() {
		synchronized (mutex) {
			return collection.isEmpty();
		}
	}
	@Override
	public final boolean contains(Object o) {
		synchronized (mutex) {
			return collection.contains(o);
		}
	}
	/**
	 * Has to be manually synchronized by the user.
	 */
	@Override
	public Iterator<E> iterator() {
		return collection.iterator();
	}
	@Override
	public final Object[] toArray() {
		synchronized (mutex) {
			return collection.toArray();
		}
	}
	@Override
	public final <T> T[] toArray(T[] a) {
		synchronized (mutex) {
			return collection.toArray(a);
		}
	}
	@Override
	public final boolean add(E e) {
		synchronized (mutex) {
			return collection.add(e);
		}
	}
	@Override
	public final boolean remove(Object o) {
		synchronized (mutex) {
			return collection.remove(o);
		}
	}
	@Override
	public final boolean containsAll(Collection<?> c) {
		synchronized (mutex) {
			return collection.containsAll(c);
		}
	}
	@Override
	public final boolean addAll(Collection<? extends E> c) {
		synchronized (mutex) {
			return collection.addAll(c);
		}
	}
	@Override
	public final boolean removeAll(Collection<?> c) {
		synchronized (mutex) {
			return collection.removeAll(c);
		}
	}
	@Override
	public final boolean retainAll(Collection<?> c) {
		synchronized (mutex) {
			return collection.retainAll(c);
		}
	}
	@Override
	public final void clear() {
		synchronized (mutex) {
			collection.clear();
		}
	}
	@Override
	public final void forEach(Consumer<? super E> action) {
		synchronized (collection) {
			collection.forEach(action);
		}
	}
	@Override
	public final <T> T[] toArray(IntFunction<T[]> generator) {
		synchronized (collection) {
			return collection.toArray(generator);
		}
	}
	@Override
	public final boolean removeIf(Predicate<? super E> filter) {
		synchronized (collection) {
			return collection.removeIf(filter);
		}
	}
	/**
	 * Has to be manually synchronized by the user.
	 */
	@Override
	public final Spliterator<E> spliterator() {
		return collection.spliterator();
	}
	/**
	 * Has to be manually synchronized by the user.
	 */
	@Override
	public final Stream<E> stream() {
		return collection.stream();
	}
	/**
	 * Has to be manually synchronized by the user.
	 */
	@Override
	public Stream<E> parallelStream() {
		return collection.parallelStream();
	}
	@Override
	public final int hashCode() {
		synchronized (mutex) {
			return collection.hashCode();
		}
	}
	@Override
	public final boolean equals(Object other) {
		synchronized (mutex) {
			return other == this || collection.equals(other);
		}
	}
	@Override
	public final String toString() {
		synchronized (mutex) {
			return collection.toString();
		}
	}
}

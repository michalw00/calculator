import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomStack<T> implements Iterable<T> {
	private Object[] elements;
	private int size;
	private static final int INITIAL_SIZE = 10;

	@Override
	public Iterator<T> iterator() {
		return new StackIterator();
	}

	private class StackIterator implements Iterator<T> {
		private int index = size - 1;

		@Override
		public boolean hasNext() {
			return index >= 0;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return (T) elements[index--];
		}
	}



	public CustomStack() {
		elements = new Object[INITIAL_SIZE];
		size = 0;
	}

	void push(T item) {
		ensureCapacity();
		elements[size] = item;
		size++;
	}

	T pop() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		T item = (T) elements[size - 1];
		elements[size - 1] = null;
		size--;
		return item;
	}

	T peek() {
		if (isEmpty()) {
			throw new EmptyStackException();
		}
		return (T) elements[size - 1];
	}

	int size() {
		return size;
	}

	boolean isEmpty() {
		return size == 0;
	}

	T get(int i) {
		return (T) elements[i];
	}

	void ensureCapacity() {
		if (elements.length == size) {
			elements = Arrays.copyOf(elements, size * 2);
		}
	}

}

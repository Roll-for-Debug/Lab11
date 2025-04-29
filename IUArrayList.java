import java.util.*;

/**
 * Array-based implementation of IndexedUnsortedList.
 *
 * @author
 *
 * @param <E> type to store
 */
public class IUArrayList<E> implements IndexedUnsortedList<E> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;

	private E[] array;
	private int rear;
	private int modCount; // DO NOT REMOVE ME

	/** Creates an empty array with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Creates an empty array with the given initial capacity
	 * 
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (E[]) (new Object[initialCapacity]);
		rear = 0;
		modCount = 0; // DO NOT REMOVE ME
	}

	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length * 2);
	}

	@Override
	public void addToFront(E element) {
		// REVIEW Zion
		// if (this.size() == this.array.length) { expandCapacity(); } - CH: Included in
		// shiftEntries
		add(0, element);
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addToRear(E element) {
		// REVIEW Colin
		add(rear, element);
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(E element) {
		// REVIEW Tyler
		add(rear, element);
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addAfter(E element, E target) {
		// REVIEW Kelsi
		emptyCheck();
		if (!contains(target)) {
			throw new NoSuchElementException();
		}
		add(indexOf(target) + 1, element);
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(int index, E element) {
		// TODO Tyra
		rangeCheck(index);
		shiftEntries(index);
		array[index] = element;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E removeFirst() {
		// REVIEW Zion
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		E retVal = this.remove(first());
		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public E removeLast() {
		// REVIEW Colin
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		E retVal = this.remove(rear - 1);
		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public E remove(E element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}

		E retVal = array[index];

		rear--;
		// shift elements
		for (int i = index; i <= rear; i++) {
			array[i] = array[i + 1];
		}
		array[rear] = null;

		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	public E remove(int index) {
		// REVIEW Tyler
		if (index < 0 || index >= rear) { // ***CH- Corrected corrections***
			throw new IndexOutOfBoundsException();
		}

		E retVal = array[index];

		rear--;
		// shift elements
		for (int i = index; i <= rear; i++) {
			array[i] = array[i + 1];
		}
		array[rear] = null;

		modCount++; // DO NOT REMOVE ME
		return retVal;
	}

	@Override
	public void set(int index, E element) {
		// REVIEW Kelsi
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + rear);
		}
		array[index] = element;
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E get(int index) {
		// TODO Tyra
		if (index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}
		return array[index];
	}

	@Override
	public int indexOf(E element) {
		if (isEmpty()) {
			return NOT_FOUND;
		}
		for (int i = 0; i < rear; i++) {
			if (element.equals(array[i])) {
				return i;
			}
		}

		return NOT_FOUND;
	}

	@Override
	public E first() {
		// REVIEW Zion
		emptyCheck();
		return array[0];
	}

	@Override
	public E last() {
		// REVIEW Colin
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return get(rear - 1);
	}

	@Override
	public boolean contains(E target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
		// REVIEW Tyler
		return size() == 0;
	}

	@Override
	public int size() {
		// REVIEW Kelsi
		return rear;
	}

	@Override
	public String toString() {
		String result = "[";
		// TODO Tyra
		for (E e : array) {
			if (e != null) {
				result += e + ", ";
			}
		}
		return result + "]";
	}

	private void shiftEntries(int startingIndex) {
		if (rear == array.length) {
			expandCapacity();
		}
		for (int i = rear; i > startingIndex && i != 0; i--) {
			array[i] = array[i - 1];
		}
		this.rear++;

	}

	private void emptyCheck() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
	}

	private void rangeCheck(int index) {
		if (index < 0 || index > rear) {
			throw new IndexOutOfBoundsException();
		}
	}
	// POSSIBLY MORE CORRECT IMPLMENTATION FOR THIS ONE?? ~ Kelsi
	// if (rear == array.length) {
	// expandCapacity();
	// }
	// for (int i = rear; i > startingIndex; i--) {
	// array[i] = array[i - 1];
	// }
	// rear++;
	// }

	// IGNORE THE FOLLOWING COMMENTED OUT CODE UNTIL LAB 10
	// DON'T DELETE ME, HOWEVER!!!
	public Iterator<E> iterator() {
		// return new IUArrayarrayIterator(); // UNCOMMENT ME IN LAB 10
		return null; // REMOVE ME IN LAB 10
	}

	// UNCOMMENT THE CODE BELOW IN LAB 10

	// private class IUArrayarrayIterator implements Iterator<E> {

	// private int iterModCount, current;
	// private boolean canRemove;

	// public IUArrayarrayIterator() {
	// iterModCount = modCount;
	// current = 0;
	// canRemove = false;
	// }

	// @Override
	// public boolean hasNext() {
	// if (iterModCount != modCount) {
	// throw new ConcurrentModificationException();
	// }
	// return current < rear;
	// }

	// @Override
	// public E next() {
	// if (!hasNext()) {
	// throw new NoSuchElementException();
	// }
	// E item = array[current];
	// current++;
	// canRemove = true;
	// return item;
	// }

	// @Override
	// public void remove() {
	// if (iterModCount != modCount) {
	// throw new ConcurrentModificationException();
	// }
	// if (!canRemove) {
	// throw new IllegalStateException();
	// }
	// // remove the element in the array at index current-1
	// // presumably decrement the rear
	// // presumably the modCount is getting incremented
	// // all indices have to back up by one
	// current--;
	// rear--;
	// // shift elements to the left
	// for (int i = current; i < rear; i++) {
	// array[i] = array[i + 1];
	// }
	// array[rear] = null;
	// modCount++;
	// iterModCount++;
	// // Can only remove the LAST "seen" element
	// // set back to a non-removal state
	// canRemove = false;
	// }

	// }

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}

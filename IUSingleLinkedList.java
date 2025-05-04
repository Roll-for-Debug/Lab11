import java.util.*;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 *
 * @author
 *
 * @param <E> type to store
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
	private LinearNode<E> front, rear;
	private int count;
	private int modCount;

	/** Creates an empty list */
	public IUSingleLinkedList() {
		front = rear = null;
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(E element) { // Colin
		LinearNode<E> node = new LinearNode<E>(element);
		if (isEmpty()) {
			front = rear = node;
			count++;
			return;
		}
		node.setNext(front);
		front = node; // One for the garbage man
		count++;
		modCount++;
	}

	@Override
	public void addToRear(E element) { // Zion
		LinearNode<E> newNode = new LinearNode<E>(element);
		if (isEmpty()) {
			front = rear = newNode;
			count++;
		} else {
			rear.setNext(newNode);
			rear = rear.getNext();
			newNode = null;
			count++;
		}
	}

	@Override
	public void add(E element) {
		// Review Tyler
		addToRear(element);
	}

	@Override
	public void addAfter(E element, E target) { // Kelsi
		LinearNode<E> current = front;
		LinearNode<E> newNode = new LinearNode<E>(element);

		while (current != null) {
			if (current.getElement().equals(target)) {
				newNode.setNext(current.getNext());
				current.setNext(newNode);

				if (current == rear) {
					rear = newNode;
				}
				count++;
				modCount++;
				return;
			}
			// Next Node
			current = current.getNext();
		}
		throw new NoSuchElementException();

	}

	@Override
	public void add(int index, E element) {
		// TODO Tyra
		LinearNode<E> current = front;
		LinearNode<E> addMe = new LinearNode<E>(element);
		if(index == 0) {
			addToFront(element);
			return;
		}
		if (index < 0 || index > size()) { throw new IndexOutOfBoundsException(); }
		for (int i = 0; i < index - 1; i++) {
			current = current.getNext();
		}
		if (current.getNext() == null) {
			current.setNext(addMe);
			rear = addMe;
		} else {
			addMe.setNext(current.getNext());
			current.setNext(addMe);
		}
		count++;
		modCount++;
	}

	@Override
	public E removeFirst() { // Colin
		if (front == null) {
			throw new NoSuchElementException();
		}
		return remove(front.getElement());
	}

	/*
	 * - Kelsi
	 * Will the return front.getElement(); throw NullPointerException?
	 * - Colin
	 * Good catch, was returning the wrong element, only NPE on single element list
	 * worked in my testing surprisingly.
	 */

	@Override
	public E removeLast() { // Zion
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return remove(rear.getElement());
	}

	@Override
	public E remove(E element) { // this was given to us do not touch
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<E> current = front, previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}
		return removeElement(previous, current);
	}

	@Override
	public E remove(int index) {
		// TODO Kelsi
		if (index > count || index < 0) {
			throw new IndexOutOfBoundsException();
		}
		return remove(get(index));
	}

	@Override
	public void set(int index, E element) {
		// TODO Tyra
		if (isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<E> current = front;
		int i = 0;
		while (current != null && i < index) {
			current = current.getNext();
			i++;
		}
		if (current == null) {
			throw new IndexOutOfBoundsException();
		}
		current.setElement(element);
	}

	@Override
	public E get(int index) { // Colin
		if (isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<E> current = front;
		int i = 0;
		while (current != null && i < index) {
			current = current.getNext();
			i++;
		}
		if (current == null) {
			throw new IndexOutOfBoundsException();
		} // Not sure if this is necessary - Colin
		modCount++;
		return current.getElement();
	}

	@Override
	public int indexOf(E element) { // Zion
		if (isEmpty()) {
			return -1;
		}
		LinearNode<E> temp = this.front;
		int indexCounter = 0;
		do {
			if (temp.getElement().equals(element)) {
				return indexCounter;
			}
			temp = temp.getNext();
			indexCounter++;
			if (temp == null) {
				return -1;
			}
		} while (!(temp == null));
		return -1;
	}

	@Override
	public E first() {
		// Review Tyler
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return front.getElement();
	}

	@Override
	public E last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		// TODO Kelsi
		return rear.getElement();
	}

	@Override
	public boolean contains(E target) {
		// TODO Tyra
		return indexOf(target) != -1;
	}

	@Override
	public boolean isEmpty() { // Colin
		return count == 0;
	}

	@Override
	public int size() { // Zion
		return count;
	}

	@Override
	public String toString() {
		// Review Tyler
		String result = "[";
		LinearNode<E> current = front;
		while (current != null) {
			result += current.getElement();
			current = current.getNext();
			if (current != null) {
				result += ", ";
			}
		}
		return result += "]";
	}

	private E removeElement(LinearNode<E> previous, LinearNode<E> current) { // given, don't much
		// Grab element
		E result = current.getElement();
		// If not the first element in the list
		if (previous != null) {
			previous.setNext(current.getNext());
		} else { // If the first element in the list
			front = current.getNext();
		}
		// If the last element in the list
		if (current.getNext() == null) {
			rear = previous;
		}
		count--;
		modCount++;
		// System.out.println(result);
		return result;
	}

	@Override
	public Iterator<E> iterator() {
		return new SLLIterator();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<E> {
		private LinearNode<E> previous;
		private LinearNode<E> current;
		private LinearNode<E> next;
		private int iterModCount;
		private boolean didNext;

		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
			didNext = false;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
			return next != null;
		}

		@Override
		public E next() {
			if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
			if (next == null) { throw new NoSuchElementException(); }

			previous = current;
			current = next;
			if (current.getNext() != null){
				next = next.getNext();
			} else {
				next = null;
			}
			didNext = true;
			return current.getElement();
		}

		@Override
		public void remove() { // Colin
			if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
			if (!didNext) { throw new IllegalStateException(); }
			if (current == front) {
				removeFirst();
			} else if (current == rear) {
				removeLast();
			} else {
				removeElement(previous, current);
			}
			didNext = false;
			iterModCount++;
		}
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}

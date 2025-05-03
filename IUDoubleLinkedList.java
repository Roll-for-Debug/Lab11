import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {
    private BidirectionalNode<E> front, rear;
    private int count;
	private int modCount;

    public IUDoubleLinkedList() {
        front = rear = null;
        count = 0;
        modCount = 0;
    }

    @Override
    public void addToFront(E element) { // Colin
        BidirectionalNode<E> node = new BidirectionalNode<E>(element);
		if (isEmpty()) {
			front = rear = node;
			count++;
			return;
		}
        front.setPrevious(node);
		node.setNext(front);
		front = node; // One for the garbage man
		count++;
		modCount++;
    }

    @Override
    public void addToRear(E element) { // REVIEW Zion
        BidirectionalNode<E> newNode = new BidirectionalNode<E>(element);
		if (isEmpty()) {
			front = rear = newNode;
			count++;
		} else {
			rear.setNext(newNode);
            newNode.setPrevious(rear);
			rear = rear.getNext();
			newNode = null;
			count++;
		}
    }

    @Override
    public void add(E element) { // Tyler
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) { // Kelsi
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
    }

    @Override
    public void add(int index, E element) { // Tyra
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public E removeFirst() { // Colin
        if (front == null) {
			throw new NoSuchElementException();
		}
		return remove(front.getElement());
    }

    @Override
    public E removeLast() { // Zion
        if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return remove(rear.getElement());
    }

    @Override
    public E remove(E element) { // Tyler
        if (isEmpty()) {
			throw new NoSuchElementException();
		}
		BidirectionalNode<E> current = front, previous = null;
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
    public E remove(int index) { // Kelsi
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, E element) { // Tyra
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public E get(int index) { // Colin
        if (isEmpty()) {
			throw new IndexOutOfBoundsException();
		}
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException();
		}
		BidirectionalNode<E> current = front;
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
		BidirectionalNode<E> temp = this.front;
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
    public E first() { // Tyler
        if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return front.getElement();
    }

    @Override
    public E last() { // Kelsi
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'last'");
    }

    @Override
    public boolean contains(E target) { // Tyra
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public boolean isEmpty() { // Colin
        return count == 0;
    }

    @Override
    public int size() { // Zion
        return count;
    }

    private E removeElement(BidirectionalNode<E> previous, BidirectionalNode<E> current) { // given, don't much
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
        public String toString() {
		// Review Tyler
		String result = "[";
		BidirectionalNode<E> current = front;
		while (current != null) {
			result += current.getElement();
			current = current.getNext();
			if (current != null) {
				result += ", ";
			}
		}
		return result += "]";
	}

    @Override
    public Iterator iterator() { // Tyler
        return new DLLIterator();
    }

    private class DLLIterator implements Iterator<E> {
		private BidirectionalNode<E> previous;
		private BidirectionalNode<E> current;
		private BidirectionalNode<E> next;
		private int iterModCount;
		private boolean didNext;

		/** Creates a new iterator for the list */
		public DLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
			didNext = false;
		}

		@Override
		public boolean hasNext() {
			// TODO Kelsi
			return next != null;
		}

		public void add() {
			// TODO Kelsi
		}

		public void set() {
			// TODO Kelsi
		}

		@Override
		public E next() {
			// TODO Tyra
			if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
			if (next == null) { throw new NoSuchElementException(); }

			previous = current;
			current = next;
			if (current.getNext() != null){
				next = next.getNext();
			} else {
				next = null;
			}
			didNext = true; // Allow remove
			// System.out.println("__CURRENT__:" + current.getElement());
			return current.getElement();
		}

		@Override
		public void remove() { // Colin
			if (!didNext) { throw new IllegalStateException(); }
			if (current == front) {
				removeFirst();
			} else if (current == rear) {
				removeLast();
			} else {
				removeElement(previous, current);
			}
			didNext = false; // disallow remove until next, next()
			iterModCount++;
		}
	}

    @Override
    public ListIterator listIterator() { // Kelsi
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator listIterator(int startingIndex) { // Tyra
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

}

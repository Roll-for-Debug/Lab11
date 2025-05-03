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
			modCount++;
			return;
		}
        front.setPrevious(node);
		node.setNext(front);
		front = node;
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
		modCount++;
    }

	private BidirectionalNode<E> findNodeIndex(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }

		BidirectionalNode<E> current = front;
        for (int i = 0; i < count; i++) {
            if (i == index) {
                return current;
            }
            current = current.getNext();
        }

        return null;
    }

    @Override
    public void add(E element) { // Tyler
        addToRear(element);
    }

    @Override
    public void addAfter(E element, E target) { // Kelsi
		add(indexOf(target), element);
    }

	public void add(int index, E element) {
		if (index < 0 || index > size()) { throw new IndexOutOfBoundsException(); } // TODO Tyra
		BidirectionalNode<E> current = front;
		BidirectionalNode<E> addMe = new BidirectionalNode<E>(element);

		if (index == 0) {
			addToFront(element);
			return;
		} else if (index == count) {
			addToRear(element);
			return;
		}
		for (int i = 0; i < index - 1; i++) {
			current = current.getNext();
		}

		addMe.setNext(current.getNext());
		current.getNext().setPrevious(addMe);
		current.setNext(addMe);
		addMe.setPrevious(current);

		count++;
		modCount++;
	}

    @Override
    public E removeFirst() { // Colin
        if (front == null) {
			throw new NoSuchElementException();
		}
		count++;
		return remove(front.getElement());
    }

    @Override
    public E removeLast() { // Zion
        if (isEmpty()) { throw new NoSuchElementException(); }
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
		// Checking index & if it is valid
		return remove(get(index));
    }

    @Override
    public void set(int index, E element) { // Tyra
		findNodeIndex(index).setElement(element);
    }

    @Override
    public E get(int index) { // Colin
        if (isEmpty() || index < 0 || index > count) {
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
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return rear.getElement();
    }

    @Override
    public boolean contains(E target) { // Tyra
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
        return new DLLListIterator();
    }

	private class ListCursor {
        private int virtualNextIndex;

        public ListCursor(int nextVirtualIndex) {
            if (nextVirtualIndex < 0 || nextVirtualIndex > count) {
                throw new IndexOutOfBoundsException();
            }
            this.virtualNextIndex = nextVirtualIndex;
        }

        public int getNextIndex() {
            return virtualNextIndex;
        }

        public int getPreviousIndex() {
            return virtualNextIndex - 1;
        }

        public void rightShift() {
            if (virtualNextIndex < count) {
                virtualNextIndex++;
            }
        }

        public void leftShift() {
            if (virtualNextIndex > 0) {
                virtualNextIndex--;
            }
        }

    }

    private class DLLListIterator implements ListIterator<E> {
		private ListCursor cursor;
		private BidirectionalNode<E> previous;
		private BidirectionalNode<E> current;
		private BidirectionalNode<E> next;
		private int listIterModCount;
		private boolean didNext;

		public DLLListIterator() {
            this(0);
        }

        public DLLListIterator(int nextVirtualIndex) {
            cursor = new ListCursor(nextVirtualIndex);
            listIterModCount = modCount;
            // state = ListIteratorState.NEITHER;
        }

		/** Creates a new iterator for the list */
		// public DLLIterator() {
		// 	previous = null;
		// 	current = null;
		// 	next = front;
		// 	iterModCount = modCount;
		// 	didNext = false;
		// }

		// @Override
		// public boolean hasNext() {
		// 	// TODO Kelsi
		// 	return next != null;
		// }

		// public void add() {
		// 	// TODO Kelsi
		// }

		// public void set() {
		// 	// TODO Kelsi

		// }

		// @Override
		// public E next() {
		// 	// TODO Tyra
		// 	if (iterModCount != modCount) { throw new ConcurrentModificationException(); }
		// 	if (next == null) { throw new NoSuchElementException(); }

		// 	previous = current;
		// 	current = next;
		// 	if (current.getNext() != null){
		// 		next = next.getNext();
		// 	} else {
		// 		next = null;
		// 	}
		// 	didNext = true;
		// 	return current.getElement();
		// }

		// @Override
		// public void remove() { // Colin
		// 	if (!didNext) { throw new IllegalStateException(); }
		// 	if (current == front) {
		// 		removeFirst();
		// 	} else if (current == rear) {
		// 		removeLast();
		// 	} else {
		// 		removeElement(previous, current);
		// 	}
		// 	didNext = false; // disallow remove until next, next()
		// 	iterModCount++;
		// }

		@Override
		public void add(E e) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'add'");
		}

		@Override
		public boolean hasPrevious() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return cursor.getPreviousIndex() > -1;
		}

		@Override
		public int nextIndex() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			return cursor.getPreviousIndex();
		}

		@Override
		public E previous() {
			if (!hasPrevious()) { throw new NoSuchElementException(); }
			E element = get(cursor.getPreviousIndex());
			cursor.leftShift();
			return element;
		}

		@Override
		public int previousIndex() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			return cursor.getPreviousIndex();
		}

		@Override
		public void set(E e) {
			// TODO Auto-generated method stub
			throw new UnsupportedOperationException("Unimplemented method 'set'");
		}

		@Override
		public boolean hasNext() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return cursor.getPreviousIndex() < count;
		}

		@Override
		public E next() {
			if (!hasNext()) { throw new NoSuchElementException(); }
			E element = get(cursor.getNextIndex());
			cursor.rightShift();
			return element;
		}

		@Override
		public void remove() {
			if (!didNext) { throw new IllegalStateException(); }
			if (current == front) {
				removeFirst();
			} else if (current == rear) {
				removeLast();
			} else {
				removeElement(previous, current);
			}
			didNext = false; // disallow remove until next, next()
			listIterModCount++;
		}
	}

    @Override
    public ListIterator<E> listIterator() { // Kelsi
        return new DLLListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) { // Tyra
        return new DLLListIterator(startingIndex); // thought this would make sense unless its DLLIterator
    }

}

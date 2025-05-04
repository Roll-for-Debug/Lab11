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
			rear = newNode;
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
		if (isEmpty()) { throw new NoSuchElementException(); }
		int index = indexOf(target);
		if (index == -1) { throw new NoSuchElementException(); }
		add(index + 1, element);
    }

	public void add(int index, E element) {
		if (index < 0 || index > count) { throw new IndexOutOfBoundsException(); } // TODO Tyra
		BidirectionalNode<E> current = front, prev;
		BidirectionalNode<E> addMe = new BidirectionalNode<E>(element);

		if (index == 0) {
			addToFront(element);
			return;
		} else if (index == count) {
			addToRear(element);
			return;
		}
		for (int i = -1; i < index - 1; i++) {
			current = current.getNext();
		}
		prev = current.getPrevious();

		prev.setNext(addMe);
		addMe.setNext(current);
		current.setPrevious(addMe);
		addMe.setPrevious(prev);

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

    @Override
    public E removeLast() { // Zion
        if (isEmpty()) { throw new NoSuchElementException(); }
		modCount++;
		E retval = rear.getElement();
		if (count == 1 ) {
			front = rear = null;
			count = 0;
			return retval;
		}
		BidirectionalNode<E> pineappleOnPizza = rear.getPrevious();
		pineappleOnPizza.setNext(null);
		rear.setPrevious(null);
		rear = pineappleOnPizza;
		count --;
		if (count == 1) {
			rear = front;
		}

		return retval;

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
			if (current == rear && current.getElement().equals(element)) {
				return removeLast();
			}
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
			current.getNext().setPrevious(previous);
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
			current = current.getNext(); // Kelsi - Fixed 
		}
		return result += "]";
	}

    @Override
    public Iterator iterator() { // Tyler
        return new DLLListIterator();
    }

	private void iteratorAddAtIndex(int index, E element) {
		if (index < 0 || index > count) { throw new IndexOutOfBoundsException(); } // TODO Tyra
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

	private BidirectionalNode<E> getNode(int index) {
		if (isEmpty() || index > count || index < 0) { return null; }
		BidirectionalNode<E> current = this.front, previous = null, next = null;
		int i = 0;
		while (current != null && i < index) {
			current = current.getNext();
			i++;
		}
		return current;
	}

    private class DLLListIterator implements ListIterator<E> {
		private BidirectionalNode<E> previous;
		private BidirectionalNode<E> jumped;
		private BidirectionalNode<E> next;
		private int listIterModCount, pointer;
		private boolean didNext, didPrevious;

		public DLLListIterator() {
            this(0);
        }

        public DLLListIterator(int nextVirtualIndex) {
            pointer = nextVirtualIndex;
			listIterModCount = modCount;
			next = getNode(nextVirtualIndex);
			previous = getNode(nextVirtualIndex-1);
			didNext = didPrevious = false;
			jumped = new BidirectionalNode<E>();
        }

		@Override
		public void add(E e) {
			iteratorAddAtIndex(pointer, e);
			listIterModCount++;
		}

		@Override
		public boolean hasPrevious() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return previous != null;
		}


		@Override
		public int nextIndex() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			return pointer;
		}

		@Override
		public E previous() {
			if (!hasPrevious()) { throw new NoSuchElementException(); }
			jumped = previous;
			next = previous;
			previous = previous.getPrevious();
			pointer--;
			didPrevious = true;
			return jumped.getElement();
		}

		@Override
		public int previousIndex() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			return pointer-1;
		}

		@Override
		public void set(E e) {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			BidirectionalNode<E> set;
			if (didNext) {
				set = previous;
			} else if (didPrevious) {
				set = next;
			} else {
				throw new IllegalStateException();
			}
			set.setElement(e); // maybe previous
		}

		@Override
		public boolean hasNext() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return next != null;
		}

		@Override
		public E next() {
			if (!hasNext()) { throw new NoSuchElementException(); }
			jumped = next;
			previous = next;
			next = next.getNext();
			pointer++;
			didNext = true;
			return jumped.getElement();
		}

		@Override
		public void remove() {
			BidirectionalNode<E> rmrf;
			if (didNext) {
				rmrf = previous;
			} else if (didPrevious) {
				rmrf = next;
			} else {
				throw new IllegalStateException();
			}
			if (rmrf == front) {
				removeFirst();
			} else if (rmrf == rear) {
				removeLast();
			} else {
				// if (0 < pointer || pointer > count) {
				// 	rmrf.getPrevious().setNext(rmrf.getNext()); // set previous next to skip element being removed
				// 	rmrf.getNext().setPrevious(rmrf.getPrevious()); // Set next previous to skip element being removed
				// 	rmrf.setNext(null); // kill it
				// 	rmrf.setPrevious(null); // with fire
				// 	count--;
				// }
				removeElement(previous, rmrf);
			}
			didNext = didPrevious = false;
			listIterModCount++;
		}
	}

    @Override
    public ListIterator<E> listIterator() { // Kelsi
        return new DLLListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) { // Tyra
		if (startingIndex < 0 || startingIndex > count) { throw new IndexOutOfBoundsException(); }
        return new DLLListIterator(startingIndex); // thought this would make sense unless its DLLIterator
    }

}

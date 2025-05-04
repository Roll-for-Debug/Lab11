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
		} else {
			rear.setNext(newNode);
            newNode.setPrevious(rear);
			rear = newNode;
		}
		count++;
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
    public void addAfter(E element, E target) { // Kelsi - Now 64 Failed tests I guess?
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        BidirectionalNode<E> current = front;
        while (current != null && !current.getElement().equals(target)) {
            current = current.getNext();
        }
        if (current == null) {
            throw new NoSuchElementException();
        }
        BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
        newNode.setPrevious(current);
        newNode.setNext(current.getNext());
        if (current.getNext() != null) {
            current.getNext().setPrevious(newNode);
        } else {
            rear = newNode;
        }
        current.setNext(newNode);
        count++;
        modCount++;
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
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		E element = front.getElement();
		front = front.getNext();
		if (front != null) {
			front.setPrevious(null);
		} else {
			rear = null;
		}
		count--;
		modCount++;
		return element;
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
    public E remove(int index) { // Kelsi ~ 65 Failing tests now??
		// Checking index & if it is valid
		if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        if (index == 0) {
            return removeFirst();
        } else if (index == count - 1) {
            return removeLast();
        } else {
            BidirectionalNode<E> current = front;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return removeElement(current.getPrevious(), current);
        }
    }

    @Override
    public void set(int index, E element) { // Tyra
		findNodeIndex(index).setElement(element);
    }

    @Override
    // public E get(int index) { // Colin
    //     if (index < 0 || index > count) {
	// 		throw new IndexOutOfBoundsException();
	// 	}
	// 	BidirectionalNode<E> current = front;
	// 	int i = 0;
	// 	while (current != null && i < index) {
	// 		current = current.getNext();
	// 		i++;
	// 	}
	// 	if (current == null) {
	// 		throw new IndexOutOfBoundsException();
	// 	} // Not sure if this is necessary - Colin
	// 	modCount++;
	// 	return current.getElement();
    // }

	// Edit - Kelsi, 66 Failed tests now 
	public E get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        BidirectionalNode<E> current = front;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getElement();
    }


    @Override
    // public int indexOf(E element) { // Zion
    //     if (isEmpty()) {
	// 		return -1;
	// 	}
	// 	BidirectionalNode<E> temp = this.front;
	// 	int indexCounter = 0;
	// 	do {
	// 		if (temp.getElement().equals(element)) {
	// 			return indexCounter;
	// 		}
	// 		temp = temp.getNext();
	// 		indexCounter++;
	// 		if (temp == null) {
	// 			return -1;
	// 		}
	// 	} while (!(temp == null));
	// 	return -1;
    // }
	public int indexOf(E element) {
		if (isEmpty()) {
			return -1;
		}
		BidirectionalNode<E> temp = front;
		int indexCounter = 0;
		while (temp != null) {
			if (temp.getElement().equals(element)) {
				return indexCounter;
			}
			temp = temp.getNext();
			indexCounter++;
		}
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
		    E element = current.getElement();
    if (previous == null) {
        front = current.getNext();
        if (front != null) {
            front.setPrevious(null);
        } else {
            rear = null;
        }
    } else {
        previous.setNext(current.getNext());
        if (current.getNext() != null) {
            current.getNext().setPrevious(previous);
        } else {
            rear = previous;
        }
    }
    count--;
    modCount++;
    return element;
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
    if (index < 0 || index > count) {
        throw new IndexOutOfBoundsException();
    }
    BidirectionalNode<E> newNode = new BidirectionalNode<>(element);
    if (index == 0) {
        addToFront(element);
    } else if (index == count) {
        addToRear(element);
    } else {
        BidirectionalNode<E> current = front;
        for (int i = 0; i < index; i++) { // 'current' will be the node at 'index'
            current = current.getNext();
        }
        newNode.setPrevious(current.getPrevious());
        newNode.setNext(current);
        current.getPrevious().setNext(newNode);
        current.setPrevious(newNode);
    }
    count++;
    modCount++;
}

	// private BidirectionalNode<E> getNode(int index) {
	// 	if (isEmpty() || index > count || index < 0) { return null; }
	// 	BidirectionalNode<E> current = this.front, previous = null, next = null;
	// 	int i = 0;
	// 	while (current != null && i < index) {
	// 		current = current.getNext();
	// 		i++;
	// 	}
	// 	return current;
	// }

	private BidirectionalNode<E> getNode(int index) {
        if (index < 0 || index >= count) { // Changed the upper bound to >= count
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        BidirectionalNode<E> current = front;
        for (int i = 0; i < index; i++) { // Changed to a standard for loop
            current = current.getNext();
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
			} else { // - Kelsi added in this section only 50 failing 
				BidirectionalNode<E> prevNode = rmrf.getPrevious();
				BidirectionalNode<E> nextNode = rmrf.getNext();
				prevNode.setNext(nextNode);
				nextNode.setPrevious(prevNode);
				count--;
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

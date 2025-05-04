import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * IUDoubleLinkedList is a doubly-linked list implementation of the IndexedUnsortedList interface.
 * It supports indexed and unsorted list operations with bidirectional traversal.
 *
 * @param <E> the type of elements held in this list
 */
public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E> {
    private BidirectionalNode<E> front, rear;
    private int count;
	private int modCount;

    /**
     * Constructs an empty IUDoubleLinkedList.
     */
    public IUDoubleLinkedList() {
        front = rear = null;
        count = 0;
        modCount = 0;
    }

    /**
     * Adds the specified element to the front of this list.
     *
     * @param element the element to be added to the front of this list
     */
    @Override
    public void addToFront(E element) {
        BidirectionalNode<E> node = new BidirectionalNode<E>(element);
		if (isEmpty()) {
			front = rear = node;
		} else {
			front.setPrevious(node);
			node.setNext(front);
			front = node;
		}
		count++;
		modCount++;
    }

    /**
     * Adds the specified element to the rear of this list.
     *
     * @param element the element to be added to the rear of this list
     */
    @Override
    public void addToRear(E element) {
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

    /**
     * Finds and returns the node at the specified index.
     *
     * @param index the index of the node to find
     * @return the node at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
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

    /**
     * Adds the specified element to the rear of this list.
     *
     * @param element the element to be added
     */
    @Override
    public void add(E element) {
        addToRear(element);
    }

    /**
     * Adds the specified element after the target element in this list.
     *
     * @param element the element to be added
     * @param target the element after which the new element will be added
     * @throws NoSuchElementException if the target element is not found
     */
    @Override
    public void addAfter(E element, E target) {
		if (isEmpty()) { throw new NoSuchElementException(); }
		int index = indexOf(target);
		if (index == -1) { throw new NoSuchElementException(); }
		add(index + 1, element);
    }

    /**
     * Adds the specified element at the specified index in this list.
     *
     * @param index the index at which the element is to be inserted
     * @param element the element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
	public void add(int index, E element) {
		if (index < 0 || index > count) { throw new IndexOutOfBoundsException(); }
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

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if the list is empty
     */
    @Override
    public E removeFirst() {
        if (front == null) {
			throw new NoSuchElementException();
		}
		return remove(front.getElement());
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if the list is empty
     */
    @Override
    public E removeLast() {
        if (isEmpty()) { throw new NoSuchElementException(); }
		E retVal = rear.getElement();
		if (count == 1 ) {
			front = rear = null;
			count = 0;
			modCount++;
			return retVal;
		}
		BidirectionalNode<E> pineappleOnPizza = rear.getPrevious();
		pineappleOnPizza.setNext(null);
		rear.setPrevious(null);
		rear = pineappleOnPizza;
		count--;
		if (count == 1) {
			rear = front;
		}
		modCount++;
		return retVal;

    }

    /**
     * Removes the first occurrence of the specified element from this list.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws NoSuchElementException if the element is not found
     */
    @Override
    public E remove(E element) {
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

    /**
     * Helper method to remove the specified element from this list.
     *
     * @param element the element to be removed
     * @return the removed element
     */
    private E removeHelper(E element) {
        return remove(element);
    }

    /**
     * Removes and returns the element at the specified index in this list.
     *
     * @param index the index of the element to be removed
     * @return the removed element
     * @throws NoSuchElementException if the index is out of range
     */
    @Override
    public E remove(int index) {
		// Checking index & if it is valid
		return remove(get(index));
    }

    /**
     * Replaces the element at the specified index in this list with the specified element.
     *
     * @param index the index of the element to replace
     * @param element the element to be stored at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void set(int index, E element) {
		findNodeIndex(index).setElement(element);
		modCount++;
    }

    /**
     * Helper method to set the element at the specified index.
     *
     * @param index the index of the element to set
     * @param element the element to set
     */
    private void setHelper(int index, E element) {
		set(index, element);
	}

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E get(int index) {
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
		}
		return current.getElement();
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list, or -1 if not found.
     *
     * @param element the element to search for
     * @return the index of the first occurrence, or -1 if not found
     */
    @Override
    public int indexOf(E element) {
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

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if the list is empty
     */
    @Override
    public E first() {
        if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return front.getElement();
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if the list is empty
     */
    @Override
    public E last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return rear.getElement();
    }

    /**
     * Returns true if this list contains the specified element.
     *
     * @param target the element whose presence in this list is to be tested
     * @return true if this list contains the specified element, false otherwise
     */
    @Override
    public boolean contains(E target) {
        return indexOf(target) != -1;
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return count;
    }

    /**
     * Removes the specified node from the list and returns its element.
     *
     * @param prev the node before the node to be removed
     * @param remove the node to be removed
     * @return the element of the removed node
     */
    private E removeElement(BidirectionalNode<E> prev, BidirectionalNode<E> remove) {
		// Grab element
		E result = remove.getElement();
		// If not the first element in the list
		if (prev != null) {
			prev.setNext(remove.getNext());
			remove.getNext().setPrevious(prev);
		} else { // If the first element in the list
			front = remove.getNext();
		}
		// If the last element in the list
		if (remove.getNext() == null) {
			rear = prev;
			// rear.setNext(null);
		}
		count--;
		modCount++;
		return result;
	}

    /**
     * Returns a string representation of this list.
     *
     * @return a string representation of this list
     */
    @Override
    public String toString() {
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

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list
     */
    @Override
    public Iterator iterator() {
        return new DLLListIterator();
    }

    /**
     * Helper method to add an element at the specified index for the iterator.
     *
     * @param index the index at which the element is to be inserted
     * @param element the element to be inserted
     */
    private void iteratorAddAtIndex(int index, E element) {
		if (index < 0 || index > count) { throw new IndexOutOfBoundsException(); }
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

    /**
     * Returns the node at the specified index, or null if out of bounds or list is empty.
     *
     * @param index the index of the node to retrieve
     * @return the node at the specified index, or null if not found
     */
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

    /**
     * DLLListIterator provides a ListIterator implementation for the IUDoubleLinkedList.
     * Supports bidirectional traversal and modification of the list during iteration.
     */
    private class DLLListIterator implements ListIterator<E> {
		private BidirectionalNode<E> previous;
		private BidirectionalNode<E> jumped;
		private BidirectionalNode<E> next;
		private int listIterModCount, pointer;
		private boolean didNext, didPrevious;

        /**
         * Constructs a DLLListIterator starting at the beginning of the list.
         */
		public DLLListIterator() {
            this(0);
        }

        /**
         * Constructs a DLLListIterator starting at the specified index.
         *
         * @param nextVirtualIndex the index to start the iterator at
         */
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
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
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
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			if (!hasPrevious()) { throw new NoSuchElementException(); }
			jumped = previous;
			next = previous;
			previous = previous.getPrevious();
			pointer--;
			didNext = false;
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
			setHelper(indexOf(set.getElement()), e);
		}

		@Override
		public boolean hasNext() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
            return next != null;
		}

		@Override
		public E next() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
			if (!hasNext()) { throw new NoSuchElementException(); }
			jumped = next;
			previous = next;
			next = next.getNext();
			pointer++;
			didPrevious = false;
			didNext = true;
			return jumped.getElement();
		}

		@Override
		public void remove() {
			if (listIterModCount != modCount) { throw new ConcurrentModificationException(); }
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
				removeHelper(rmrf.getElement());
			}
			didNext = didPrevious = false;
			listIterModCount++;
		}
	}

    /**
     * Returns a list iterator over the elements in this list (in proper sequence).
     *
     * @return a list iterator over the elements in this list
     */
    @Override
    public ListIterator<E> listIterator() {
        return new DLLListIterator();
    }

    /**
     * Returns a list iterator over the elements in this list, starting at the specified position.
     *
     * @param startingIndex the index to start the iterator at
     * @return a list iterator over the elements in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public ListIterator<E> listIterator(int startingIndex) {
		if (startingIndex < 0 || startingIndex > count) { throw new IndexOutOfBoundsException(); }
        return new DLLListIterator(startingIndex);
    }

}

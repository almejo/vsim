package cl.almejo.vsim.simulation;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class Heap {
	private static final int MIN_SIZE = 100;

	private HeapElement[] elements = new HeapElement[MIN_SIZE + 1];
	private int size;

	void insert(HeapElement element) {
		if (isFull()) {
			enlarge();
		}
		if (size == 0) {
			size++;
			elements[1] = element;
			return;
		}
		size++;
		elements[size] = element;
		int current = size;
		while (current > 1) {
			int parentIndex = current / 2;
			if (elements[parentIndex].getValue() <= elements[current].getValue()) {
				break;
			}
			elements[current] = elements[parentIndex];
			elements[parentIndex] = element;
			current = parentIndex;
		}
	}

	public HeapElement remove() throws EmptyHeapException {
		if (size == 0) {
			throw new EmptyHeapException();
		}
		HeapElement element = elements[1];
		if (size > 1) {
			elements[1] = elements[size];
			size--;
			sinkElement(1);
		} else {
			size = 0;
		}
		return element;
	}

	private void sinkElement(int index) {

		if (index == size) {
			return;
		}

		int tempIndex = index;
		int childIndex = tempIndex * 2;
		while (childIndex <= size) {
			if (childIndex < size && elements[childIndex + 1].getValue() < elements[childIndex].getValue()) {
				childIndex = childIndex + 1;
			}
			if (elements[tempIndex].getValue() > elements[childIndex].getValue()) {
				HeapElement tmp = elements[tempIndex];
				elements[tempIndex] = elements[childIndex];
				elements[childIndex] = tmp;
			}
			tempIndex = childIndex;
			childIndex = tempIndex * 2;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < size + 1; i++) {
			builder.append(" ").append(elements[i].getValue()).append("[").append(elements[i].getClass().getName()).append("]");
		}
		return builder.append(" [").append(size).append("]").toString();
	}

	private void enlarge() {
		HeapElement[] newArray = new HeapElement[elements.length + 2];
		System.arraycopy(elements, 0, newArray, 0, elements.length);
		elements = newArray;
	}

	private boolean isFull() {
		return size == elements.length;
	}

	boolean contains(HeapElement element) {
		for (int i = 1; i < size; i++) {
			if (elements[i] == element) {
				return true;
			}
		}
		return false;
	}
}

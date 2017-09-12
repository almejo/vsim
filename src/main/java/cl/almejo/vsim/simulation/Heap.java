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

	private HeapElement[] _elements = new HeapElement[MIN_SIZE + 1];
	private int _size;

	void insert(HeapElement element) {
		if (isFull()) {
			enlarge();
		}
		if (_size == 0) {
			_size++;
			_elements[1] = element;
			return;
		}
		_size++;
		_elements[_size] = element;
		int current = _size;
		while (current > 1) {
			int parentIndex = current / 2;
			if (_elements[parentIndex].getValue() <= _elements[current].getValue()) {
				break;
			}
			_elements[current] = _elements[parentIndex];
			_elements[parentIndex] = element;
			current = parentIndex;
		}
	}

	public HeapElement remove() throws EmptyHeapException {
		if (_size == 0) {
			throw new EmptyHeapException();
		}
		HeapElement element = _elements[1];
		if (_size > 1) {
			_elements[1] = _elements[_size];
			_size--;
			sinkElement(1);
		} else {
			_size = 0;
		}
		return element;
	}

	private void sinkElement(int index) {

		if (index == _size) {
			return;
		}

		int tempIndex = index;
		int childIndex = tempIndex * 2;
		while (childIndex <= _size) {
			if (childIndex < _size && _elements[childIndex + 1].getValue() < _elements[childIndex].getValue()) {
				childIndex = childIndex + 1;
			}
			if (_elements[tempIndex].getValue() > _elements[childIndex].getValue()) {
				HeapElement tmp = _elements[tempIndex];
				_elements[tempIndex] = _elements[childIndex];
				_elements[childIndex] = tmp;
			}
			tempIndex = childIndex;
			childIndex = tempIndex * 2;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < _size + 1; i++) {
			builder.append(" ").append(_elements[i].getValue()).append("[").append(_elements[i].getClass().getName()).append("]");
		}
		return builder.append(" [").append(_size).append("]").toString();
	}

	private void enlarge() {
		HeapElement[] newArray = new HeapElement[_elements.length + 2];
		System.arraycopy(_elements, 0, newArray, 0, _elements.length);
		_elements = newArray;
	}

	private boolean isFull() {
		return _size == _elements.length;
	}

	boolean contains(HeapElement element) {
		for (int i = 1; i < _size; i++) {
			if (_elements[i] == element) {
				return true;
			}
		}
		return false;
	}
}

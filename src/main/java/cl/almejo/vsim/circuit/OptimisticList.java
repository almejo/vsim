package cl.almejo.vsim.circuit;

import java.util.LinkedList;
import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class OptimisticList<T> {

	private T _element;
	private List<T> _list;

	public void add(T element) {
		if (_element == null) {
			_element = element;
		} else {
			if (_list == null) {
				_list = new LinkedList<>();
			}
			_list.add(element);
		}
	}

	boolean contains(T element) {
		return _element != null && _element.equals(element) || _list != null && _list.contains(element);
	}

	List<T> elements() {
		List<T> list = new LinkedList<>();

		if (_element != null) {
			list.add(_element);
		}

		if (_list != null) {
			list.addAll(_list);
		}

		return list;
	}

	private int indexOf(T element) {
		if (_element != null && _element.equals(element)) {
			return 0;
		}

		if (_list != null) {
			int index = 1;
			for (T listElement : _list) {
				if (listElement.equals(element)) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	boolean isEmpty() {
		return _element == null;
	}

	public T remove(T element) {
		return remove(indexOf(element));
	}

	public T remove(int i) {

		if (i < 0) {
			return null;
		}

		if (i == 0) {
			T element = _element;
			_element = removeAt(0);
			return element;
		}
		return removeAt(i - 1);
	}

	private T removeAt(int index) {
		if (_list != null) {
			T element = _list.remove(index);
			if (_list.size() < 1) {
				_list = null;
			}
			return element;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		if (_element != null) {
			builder.append(_element);
			if (_list != null) {
				for (T element : _list) {
					builder.append(", ").append(element);
				}
			}
		}
		builder.append("]");
		return builder.toString();
	}
}

/**
 *
 * vsim
 *
 * Created on Aug 15, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit;

import java.util.LinkedList;
import java.util.List;

public class OptimisticList<T> {

	private T _element;
	private List<T> _list;

	public void add(T element) {
		if (_element == null) {
			_element = element;
		} else {
			if (_list == null) {
				_list = new LinkedList<T>();
			}
			_list.add(element);
		}
	}

	public boolean contains(T element) {

		if (_element != null && _element.equals(element)) {
			return true;
		}

		if (_list != null) {
			for (T listElement : _list) {
				if (listElement.equals(element)) {
					return true;
				}
			}
		}
		return false;
	}

	public List<T> elements() {
		List<T> list = new LinkedList<T>();

		if (_element != null) {
			list.add(_element);
		}

		if (_list != null) {
			list.addAll(_list);
		}

		return list;
	}

	public int indexOf(T element) {
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

	public boolean isEmpty() {
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
		return removeAt(i);
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
					builder.append(", ").append(element.toString());
				}
			}
		}
		builder.append("]");
		return builder.toString();
	}
}

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

	private T element;
	private List<T> list;

	public void add(T element) {
		if (this.element == null) {
			this.element = element;
		} else {
			if (list == null) {
				list = new LinkedList<>();
			}
			list.add(element);
		}
	}

	boolean contains(T element) {
		return this.element != null && this.element.equals(element) || list != null && list.contains(element);
	}

	List<T> elements() {
		List<T> list = new LinkedList<>();

		if (element != null) {
			list.add(element);
		}

		if (this.list != null) {
			list.addAll(this.list);
		}

		return list;
	}

	private int indexOf(T element) {
		if (this.element != null && this.element.equals(element)) {
			return 0;
		}

		if (list != null) {
			int index = 1;
			for (T listElement : list) {
				if (listElement.equals(element)) {
					return index;
				}
				index++;
			}
		}
		return -1;
	}

	boolean isEmpty() {
		return element == null;
	}

	public T remove(T element) {
		return remove(indexOf(element));
	}

	public T remove(int i) {

		if (i < 0) {
			return null;
		}

		if (i == 0) {
			T element = this.element;
			this.element = removeAt(0);
			return element;
		}
		return removeAt(i - 1);
	}

	private T removeAt(int index) {
		if (list != null) {
			T element = list.remove(index);
			if (list.size() < 1) {
				list = null;
			}
			return element;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		if (element != null) {
			builder.append(element);
			if (list != null) {
				for (T element : list) {
					builder.append(", ").append(element);
				}
			}
		}
		builder.append("]");
		return builder.toString();
	}
}

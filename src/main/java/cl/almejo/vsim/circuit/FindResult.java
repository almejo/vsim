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
class FindResult<T extends Point> {
	private final T hit;
	private final T previous;
	private final T next;

	FindResult(T hit, T previous, T next) {
		this.hit = hit;
		this.previous = previous;
		this.next = next;

	}

	T getHit() {
		return hit;
	}

	T getPrevious() {
		return previous;
	}

	T getNext() {
		return next;
	}

	public List<T> list() {
		List<T> list = new LinkedList<>();
		if (previous != null) {
			list.add(previous);
		}
		if (hit != null) {
			list.add(hit);
		}
		if (next != null) {
			list.add(next);
		}
		return list;
	}

	@Override
	public String toString() {
		return "(" + previous + ", " + hit + ", " + next + ")";
	}
}

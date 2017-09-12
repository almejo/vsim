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
	private final T _hit;
	private final T _previous;
	private final T _next;

	FindResult(T hit, T previous, T next) {
		_hit = hit;
		_previous = previous;
		_next = next;

	}

	T getHit() {
		return _hit;
	}

	T getPrevious() {
		return _previous;
	}

	T getNext() {
		return _next;
	}

	public List<T> list() {
		List<T> list = new LinkedList<>();
		if (_previous != null) {
			list.add(_previous);
		}
		if (_hit != null) {
			list.add(_hit);
		}
		if (_next != null) {
			list.add(_next);
		}
		return list;
	}

	@Override
	public String toString() {
		return "(" + _previous + ", " + _hit + ", " + _next + ")";
	}
}

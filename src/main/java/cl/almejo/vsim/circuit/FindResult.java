/**
 *
 * vsim
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

public class FindResult<T extends Point> {
	T _hit;
	T _previous;
	T _next;

	public FindResult(T hit, T previous, T next) {
		_hit = hit;
		_previous = previous;
		_next = next;

	}

	public T getHit() {
		return _hit;
	}

	public T getPrevious() {
		return _previous;
	}

	public T getNext() {
		return _next;
	}

	public List<T> list() {
		List<T> list = new LinkedList<T>();
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

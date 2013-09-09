/**
 *
 * vsim
 *
 * Created on Aug 25, 2013
 *
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit;

public class Connection<T> {
	T _first;
	T _last;

	public Connection(T first, T last) {
		_first = first;
		_last = last;
	}

	public T getFirst() {
		return _first;
	}

	public T getLast() {
		return _last;
	}

	@Override
	public String toString() {
		return "[[" + _first.toString() + " --> " + _last + "]]";
	}
}

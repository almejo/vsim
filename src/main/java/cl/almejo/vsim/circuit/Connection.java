package cl.almejo.vsim.circuit;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class Connection<T> {
	private final T _first;
	private final T _last;

	Connection(T first, T last) {
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
		return "[[" + _first + " --> " + _last + "]]";
	}
}

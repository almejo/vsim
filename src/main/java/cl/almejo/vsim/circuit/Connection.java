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
	private final T first;
	private final T last;

	Connection(T first, T last) {
		this.first = first;
		this.last = last;
	}

	public T getFirst() {
		return first;
	}

	public T getLast() {
		return last;
	}

	@Override
	public String toString() {
		return "[[" + first + " --> " + last + "]]";
	}
}

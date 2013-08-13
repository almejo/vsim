/**
 *
 * vsim
 *
 * Created on Aug 1, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.simulation;

public class HeapElement {
	private long _value;

	/**
	 * @return the value
	 */
	public long getValue() {
		return _value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(long value) {
		_value = value;
	}

	@Override
	public String toString() {
		return "" + getValue();
	}
}

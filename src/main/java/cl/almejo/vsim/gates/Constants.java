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


package cl.almejo.vsim.gates;

public class Constants {

	public static final byte OFF = 0;
	public static final byte ON = 1;
	public static byte THREE_STATE = -1;

	public static final byte NORTH = 1;
	public static final byte EAST = 1 << 1;
	public static final byte SOUTH = 1 << 2;
	public static final byte WEST = 1 << 3;
}

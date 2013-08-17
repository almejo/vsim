/**
 *
 * vsim
 *
 * Created on Aug 2, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gates;

import java.awt.Color;
import java.util.Hashtable;

public class Constants {
	
	public static final byte OFF = 0;
	public static final byte ON = 1;
	public static byte THREE_STATE = -1;
	
	public static final byte NORTH = 1;
	public static final byte EAST = 1 << 1;
	public static final byte SOUTH = 1 << 2;
	public static final byte WEST = 1 << 3;
	public static Hashtable<Byte, Color> STATECOLORS = new Hashtable<>();
	
	static {
		STATECOLORS.put(Constants.THREE_STATE, new Color(0, 200, 0));
		STATECOLORS.put(Constants.ON, new Color(200, 0, 0));
		STATECOLORS.put(Constants.OFF, new Color(0, 0, 0));
	}
}

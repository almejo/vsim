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

import java.awt.geom.AffineTransform;

public class Constants {

	public static final byte OFF = 0;
	public static final byte ON = 1;
	public static byte THREE_STATE = -1;

	public static final byte NORTH = 1;
	public static final byte EAST = 1 << 1;
	public static final byte SOUTH = 1 << 2;
	public static final byte WEST = 1 << 3;
	public static AffineTransform TRANSFORM_IDENTITY;

	static {
		TRANSFORM_IDENTITY = new AffineTransform();
		TRANSFORM_IDENTITY.setToIdentity();
	}
}

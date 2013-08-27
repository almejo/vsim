/**
 *
 * vsim
 *
 * Created on Aug 16, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gates;

import java.awt.Dimension;
import java.awt.Graphics2D;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

public abstract class GateDescriptor {

	protected Point[] _pinPosition;

	protected int _pinCount = 0;

	protected GateTypes _type;

	public void paint(Graphics2D graphics, IconGate iconGate) {
		drawGate(graphics, iconGate, 0, 0);// Circuit.gridTrunc((int) iconGate.getX()), Circuit.gridTrunc((int) iconGate.getY()));
	}

	public abstract void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y);

	public abstract Dimension getSize();

	public Point getPinPosition(byte pinId) {
		return _pinPosition[pinId];
	}

	public abstract Gate make(Circuit circuit, GateParameters params);
}

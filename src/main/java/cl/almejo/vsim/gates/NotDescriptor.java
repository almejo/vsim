/**
 *
 * vsim
 *
 * Created on Aug 17, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.gates;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

public class NotDescriptor extends GateDescriptor {
	public NotDescriptor() {
		_pinPosition = new Point[3];
		_pinPosition[0] = new Point(Circuit.gridTrunc(0), Circuit.gridTrunc(16));
		_pinPosition[1] = new Point(Circuit.gridTrunc(32), Circuit.gridTrunc(16));
		_type = GateTypes.NORMAL;
		_pinCount = 2;
	}
	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.blue);
		graphics.drawLine(0, 0, 32, 16);
		graphics.drawLine(32, 16, 0, 32);
		graphics.drawLine(0, 32, 0, 0);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(32, 16);
	}
	
	@Override
	public Gate make(Circuit circuit, GateParameters params) {
		return new Not(circuit, (GateParameters) params.clone(), this);
	}
}

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

public class ClockDescriptor extends GateDescriptor {
	
	public ClockDescriptor() {
		_pinPosition = new Point[1];
		_pinPosition[0] = new Point(Circuit.gridTrunc(10), Circuit.gridTrunc(10));
		_type = GateTypes.NORMAL;
		_pinCount = 1;
	}
	
	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(Color.blue);
		graphics.fillRect(x, y, 16, 16);
		graphics.setColor(Constants.STATECOLORS.get(iconGate.getPin(0).getInValue()));
		graphics.fillRect(x + 3, y + 3, 10, 10);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(16, 16);
	}

}

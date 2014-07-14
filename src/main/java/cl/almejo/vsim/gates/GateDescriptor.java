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

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

import java.awt.*;

public abstract class GateDescriptor {

	protected Point[] _pinPosition;

	protected int _pinCount = 0;

	protected GateTypes _gateType;

	protected String _type;

	protected GateParameters _originalParameters;

	public GateDescriptor(GateParameters parameters, String type) {
		_originalParameters = parameters;
		_type = type;
	}

	public void paint(Graphics2D graphics, IconGate iconGate) {
		drawGate(graphics, iconGate, 0, 0);
	}

	public abstract void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y);

	public abstract Dimension getSize();

	public Point getPinPosition(byte pinId) {
		return _pinPosition[pinId];
	}

	public abstract Gate make(Circuit circuit, GateParameters params);

	public int getPinCount() {
		return _pinCount;
	}

	public GateParameters getOriginalParameters() {
		return _originalParameters;
	}

	public String getType() {
		return _type;
	}

	public Point[] getPinPositions() {
		return _pinPosition;
	}

	public boolean isNormal() {
		return _gateType == GateTypes.NORMAL;
	}
}

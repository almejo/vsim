package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Point;

import java.awt.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public abstract class GateDescriptor {

	protected Point[] _pinPosition;

	protected int _pinCount;

	protected GateTypes _gateType;

	private final String _type;

	GateParameters _originalParameters;

	public GateDescriptor(GateParameters parameters, String type) {
		_originalParameters = parameters;
		_type = type;
	}

	void paint(Graphics2D graphics, IconGate iconGate) {
		drawGate(graphics, iconGate, 0, 0);
	}

	public abstract void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y);

	public abstract Dimension getSize();

	Point getPinPosition(byte pinId) {
		return _pinPosition[pinId];
	}

	public abstract Gate make(Circuit circuit, GateParameters params);

	public int getPinCount() {
		return _pinCount;
	}

	GateParameters getOriginalParameters() {
		return _originalParameters;
	}

	public String getType() {
		return _type;
	}
}

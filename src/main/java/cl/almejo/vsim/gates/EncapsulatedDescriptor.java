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
import cl.almejo.vsim.gui.ColorScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class EncapsulatedDescriptor extends GateDescriptor {
	private GateData[] _gatesData;
	private int[][] _outConnections;
	private Dimension _size;

	private static final Logger LOGGER = LoggerFactory.getLogger(Encapsulated.class);

	public EncapsulatedDescriptor(GateData[] gatesData, int[][] outConnections, List<Point> positions, int minSize) {
		super(null, UUID.randomUUID().toString());
		_gatesData = gatesData;
		_outConnections = outConnections;
		_pinCount = positions.size();
		_pinPosition = positions.toArray(new Point[positions.size()]);
		_size = computeSize(positions, minSize);
	}

	private Dimension computeSize(List<Point> points, int minSize) {
		int maxx = 0;
		int maxy = 0;
		for (Point point: points) {
			maxx = Math.max(point.getX(),maxx);
			maxy = Math.max(point.getY(),maxy);
		}
		return new Dimension(Circuit.gridTrunc(Math.max(maxx, minSize)), Circuit.gridTrunc(Math.max(maxy, minSize)));
	}

	@Override
	public void drawGate(Graphics2D graphics, IconGate iconGate, int x, int y) {
		graphics.setColor(ColorScheme.getGates());
		Dimension dimension = getSize();
		graphics.fillRect(0, 0, dimension.width, dimension.height);
		graphics.setColor(ColorScheme.getLabel());
		int i = 0;
		for (Point point : _pinPosition) {
			int xposition = point.getX() == 0 ? point.getX() + 3 : point.getX() - 30;
			graphics.drawString("#" + i, xposition, point.getY() + 10);
			i++;
		}
	}

	@Override
	public Dimension getSize() {
		return _size;
	}

	@Override
	public Gate make(Circuit circuit, GateParameters parameters) {
		Encapsulated encapsultated = new Encapsulated(circuit, null, this);

		encapsultated.setGates(createGates(circuit));

		encapsultated.setPins(createPins(encapsultated.getGates()));

		connectGates(encapsultated.getGates());

		return encapsultated;
	}

	private void connectGates(Gate[] gates) {
		int index = 0;
		for (GateData gateData : _gatesData) {
			Gate sourceGate = gates[index];
			for (int pinId = 0; pinId < sourceGate.getPinCount(); pinId++) {
				if (gateData.getConnections()[pinId][0] < 0) {
					continue;
				}
				LOGGER.debug("Connecting " + sourceGate + "[" + pinId + "] -----> " + gates[gateData.getConnections()[pinId][0]] + "[" + gateData.getConnections()[pinId][1] + "]");
				connectGatePin(gates, gateData.getConnections()[pinId][0], gateData.getConnections()[pinId][1], sourceGate, pinId);
			}

			index++;
		}
		LOGGER.debug("All gates connected");
	}

	private void connectGatePin(Gate[] gates, int destinationGateId, int destinationPinId, Gate sourceGate, int pinId) {
		Gate destinationGate = gates[destinationGateId];
		if (sourceGate != destinationGate) {
			Pin destinationPin = destinationGate.getPin(destinationPinId);
			Pin sourcePin = sourceGate.getPin(pinId);
			if (sourcePin != destinationPin) {
				sourcePin.connect(destinationPin);
			}
		}
	}

	private Pin[] createPins(Gate[] gates) {
		Pin[] pins = new Pin[_pinCount];
		for (int i = 0; i < _pinCount; i++) {
			pins[i] = gates[_outConnections[i][0]].getPin(_outConnections[i][1]);
		}
		return pins;
	}

	private Gate[] createGates(Circuit circuit) {
		Gate[] gates = new Gate[_gatesData.length];
		int index = 0;
		try {
			for (GateData gateDate : _gatesData) {
				GateParameters clonedParameters = gateDate.getParameters().clone();
				gates[index] = gateDate.getDescriptor().make(circuit, clonedParameters);
				index++;
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return gates;
	}
}

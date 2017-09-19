package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Connection;
import cl.almejo.vsim.circuit.Contact;

import java.util.LinkedList;
import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class ConnectCommand implements Command {

	private final Circuit circuit;

	private final List<UndoableConnection> undoableConnections;

	private class UndoableConnection {
		private List<Connection<Contact>> previousConnections;
		private final int xi;
		private final int yi;
		private final int xf;
		private final int yf;

		UndoableConnection(int xi, int yi, int xf, int yf) {
			this.xi = xi;
			this.yi = yi;
			this.xf = xf;
			this.yf = yf;
		}

		void apply() {
			previousConnections = circuit.findBeforeConnect(xi, yi, xf, yf);
			circuit.connect(xi, yi, xf, yf);
		}

		void undo() {
			circuit.disconnectBetween(xi, yi, xf, yf);
			if (!previousConnections.isEmpty()) {
				previousConnections.forEach(connection -> circuit.connect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(), connection.getLast().getY()));
			}
		}
	}

	public ConnectCommand(Circuit circuit) {
		this.circuit = circuit;
		undoableConnections = new LinkedList<>();
	}

	public void connect(int xi, int yi, int xf, int yf) {
		undoableConnections.add(new UndoableConnection(xi, yi, xf, yf));
	}

	@Override
	public boolean apply() {
		undoableConnections.forEach(UndoableConnection::apply);
		return true;
	}

	@Override
	public void unDo() {
		undoableConnections.forEach(UndoableConnection::undo);
	}
}
package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Connection;
import cl.almejo.vsim.circuit.Contact;

import java.util.List;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class DisconnectCommand implements Command {

	private final Circuit circuit;
	private final List<Connection<Contact>> connections;

	public DisconnectCommand(Circuit circuit, int x, int y) {
		this.circuit = circuit;
		connections = this.circuit.findToDisconnect(x, y);
	}

	@Override
	public boolean apply() {
		connections.forEach(connection -> circuit.disconnect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(), connection.getLast().getY()));
		return true;
	}

	@Override
	public void unDo() {
		connections.forEach(connection -> circuit.connect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(), connection
				.getLast().getY()));
	}
}

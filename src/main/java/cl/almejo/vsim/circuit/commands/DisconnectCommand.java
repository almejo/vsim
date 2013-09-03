/**
 *
 * vsim
 *
 * Created on Aug 25, 2013
 * 
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 * 
 * @author: Alejandro Vera
 *
 */

package cl.almejo.vsim.circuit.commands;

import java.util.List;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.Connection;
import cl.almejo.vsim.circuit.Contact;

public class DisconnectCommand implements Command {

	private final Circuit _circuit;
	private List<Connection<Contact>> _connections;

	public DisconnectCommand(Circuit circuit, int x, int y) {
		_circuit = circuit;
		_connections = _circuit.findToDisconnect(x, y);
	}

	@Override
	public void apply() {
		for (Connection<Contact> connection : _connections) {
			_circuit.disconnect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(), connection.getLast().getY());
		}
	}

	@Override
	public void unDo() {
		for (Connection<Contact> connection : _connections) {
			_circuit.connect(connection.getFirst().getX(), connection.getFirst().getY(), connection.getLast().getX(), connection
				.getLast().getY());
		}
	}
}

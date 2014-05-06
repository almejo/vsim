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

import java.util.LinkedList;
import java.util.List;

public class CommandManager {

	private List<Command> _done = new LinkedList<Command>();
	private List<Command> _undone = new LinkedList<Command>();

	public void apply(Command command) {
		command.apply();
		_done.add(command);
		_undone.clear();
	}

	public void undo() {
		if (_done.isEmpty()) {
			return;
		}

		Command command = _done.remove(_done.size() - 1);
		command.unDo();
		_undone.add(0, command);
	}

	public void redo() {
		if (_undone.isEmpty()) {
			return;
		}
		Command command = _undone.remove(0);
		command.apply();
		_done.add(command);
	}

	public boolean canUndo() {
		return _done.size() > 0;
	}

	public boolean canRedo() {
		return _undone.size() > 0;
	}
}

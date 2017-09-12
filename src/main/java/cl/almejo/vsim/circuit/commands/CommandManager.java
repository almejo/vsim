package cl.almejo.vsim.circuit.commands;

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
public class CommandManager {

	private final List<Command> _done = new LinkedList<>();
	private final List<Command> _undone = new LinkedList<>();

	public void apply(Command command) {
		if (command.apply()) {
			_done.add(command);
			_undone.clear();
		}
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
		return !_done.isEmpty();
	}

	public boolean canRedo() {
		return !_undone.isEmpty();
	}

	public void cleanHistory() {
		_undone.clear();
		_done.clear();
	}


	public Command getLastApplied() {
		if (_done.isEmpty()) {
			return null;
		}
		return _done.get(_done.size() - 1);
	}
}

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

	private final List<Command> done = new LinkedList<>();
	private final List<Command> undone = new LinkedList<>();

	public void apply(Command command) {
		if (command.apply()) {
			done.add(command);
			undone.clear();
		}
	}

	public void undo() {
		if (done.isEmpty()) {
			return;
		}

		Command command = done.remove(done.size() - 1);
		command.unDo();
		undone.add(0, command);
	}

	public void redo() {
		if (undone.isEmpty()) {
			return;
		}
		Command command = undone.remove(0);
		command.apply();
		done.add(command);
	}

	public boolean canUndo() {
		return !done.isEmpty();
	}

	public boolean canRedo() {
		return !undone.isEmpty();
	}

	public void cleanHistory() {
		undone.clear();
		done.clear();
	}

	public Command getLastApplied() {
		if (done.isEmpty()) {
			return null;
		}
		return done.get(done.size() - 1);
	}
}
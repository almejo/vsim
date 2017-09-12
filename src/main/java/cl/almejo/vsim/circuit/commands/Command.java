package cl.almejo.vsim.circuit.commands;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface Command {
	boolean apply();

	void unDo();
}

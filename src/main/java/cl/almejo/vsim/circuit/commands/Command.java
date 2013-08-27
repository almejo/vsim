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

public interface Command {
	public void apply();
	public void unDo();
}

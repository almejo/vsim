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

package cl.almejo.vsim.circuit;

public interface CircuitStateListener {

	public void onPause(CircuitEvent event);

	public void onResume(CircuitEvent event);

	void onChanged(CircuitEvent event);

	void onUndo(CircuitEvent event);

	void onRedo(CircuitEvent event);

	void onMarkedAsUnmodified(CircuitEvent event);
}

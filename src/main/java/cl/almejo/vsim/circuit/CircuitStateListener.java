package cl.almejo.vsim.circuit;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface CircuitStateListener {

	void onPause(CircuitEvent event);

	void onResume(CircuitEvent event);

	void onChanged(CircuitEvent event);

	void onUndo(CircuitEvent event);

	void onRedo(CircuitEvent event);

	void onSaved(CircuitEvent event);
}
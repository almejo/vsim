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

import java.util.List;
import java.util.Map;

public interface Configurable {

	public void rotateClockwise();

	public void rotateCounterClockwise();

	public List<ConfigVariable> getConfigVariables();

	public boolean isConfigurable();

	void setValues(Map<String, Object> parametersInstance);
}

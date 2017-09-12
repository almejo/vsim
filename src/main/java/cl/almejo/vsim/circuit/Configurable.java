package cl.almejo.vsim.circuit;

import java.util.List;
import java.util.Map;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public interface Configurable {

	void rotateClockwise();

	void rotateCounterClockwise();

	List<ConfigVariable> getConfigVariables();

	boolean isConfigurable();

	void setValues(Map<String, Object> parametersInstance);

}

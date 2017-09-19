package cl.almejo.vsim.circuit.commands;

import cl.almejo.vsim.circuit.ConfigVariable;
import cl.almejo.vsim.circuit.Configurable;

import java.util.Map;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */

public class ConfigCommand implements Command {

	private final Configurable configurable;
	private final Map<String, Object> newParameters;
	private final Map<String, Object> oldParameters;


	public ConfigCommand(Configurable configurable, Map<String, Object> parameters) {
		this.configurable = configurable;
		newParameters = parameters;
		oldParameters = ConfigVariable.toMap(configurable.getConfigVariables());
	}

	@Override
	public boolean apply() {
		configurable.setValues(newParameters);
		return true;
	}

	@Override
	public void unDo() {
		configurable.setValues(oldParameters);
	}
}

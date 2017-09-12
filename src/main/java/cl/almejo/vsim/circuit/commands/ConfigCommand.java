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

	private final Configurable _configurable;
	private final Map<String, Object> _newParameters;
	private final Map<String, Object> _oldParameters;


	public ConfigCommand(Configurable configurable, Map<String, Object> parameters) {
		_configurable = configurable;
		_newParameters = parameters;
		_oldParameters = ConfigVariable.toMap(configurable.getConfigVariables());
	}

	@Override
	public boolean apply() {
		_configurable.setValues(_newParameters);
		return true;
	}

	@Override
	public void unDo() {
		_configurable.setValues(_oldParameters);
	}
}

package cl.almejo.vsim.gates;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.ConfigVariable;

import java.util.LinkedList;
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
class LedParameters extends GateParameters {

	private String text;

	LedParameters(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		text = (String) parameters.get("text");
	}

	@Override
	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("text", Messages.t("config.text.label"), text));
		return variables;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}

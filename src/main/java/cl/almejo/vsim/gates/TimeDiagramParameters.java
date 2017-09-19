package cl.almejo.vsim.gates;

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
class TimeDiagramParameters extends GateParameters {

	private String text;

	@Override
	public boolean isConfigurable() {
		return true;
	}

	@Override
	public void setValues(Map<String, Object> parameters) {
		text = (String) parameters.get("text");
	}

	@Override
	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<>();
		variables.add(new ConfigVariable("text", "Text", text));
		return variables;
	}

	public String getText() {
		return text;
	}
}

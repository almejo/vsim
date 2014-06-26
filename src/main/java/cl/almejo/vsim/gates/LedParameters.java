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


package cl.almejo.vsim.gates;

import cl.almejo.vsim.circuit.ConfigVariable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LedParameters extends GateParameters {

	private String _text;

	public LedParameters(String text) {
		_text = text;
	}

	public String getText() {
		return _text;
	}

	public void setValues(Map<String, Object> parameters) {
		_text = (String) parameters.get("text");
	}

	public List<ConfigVariable> getValues() {
		List<ConfigVariable> variables = new LinkedList<ConfigVariable>();
		variables.add(new ConfigVariable("text", "Text", _text));
		return variables;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}

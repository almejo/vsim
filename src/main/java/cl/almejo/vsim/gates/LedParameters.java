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

public class LedParameters extends GateParameters {

	private final String _text;

	public LedParameters(String text) {
		super(0);
		_text = text;
	}

	public String getText() {
		return _text;
	}
}

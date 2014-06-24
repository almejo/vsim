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

package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.ConfigValueType;
import cl.almejo.vsim.circuit.ConfigVariable;
import cl.almejo.vsim.circuit.Configurable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationDialog extends JDialog {

	private Configurable _configurable;
	private HashMap<String, Component> _components = new HashMap<String, Component>();

	public ConfigurationDialog(Configurable configurable) {
		_configurable = configurable;
		setTitle(Messages.t("config.configurable.title"));
		setMinimumSize(new Dimension(400, 500));
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(addVariables(configurable));
		panel.add(addButtons());
		getContentPane().add(panel);
		pack();
	}

	private JPanel addVariables(Configurable configurable) {
		JPanel variablesPanel = new JPanel();
		List<ConfigVariable> variables = configurable.getConfigVariables();
		variablesPanel.setLayout(new GridLayout(variables.size(), 2));
		for (ConfigVariable variable : variables) {
			variablesPanel.add(new JLabel(variable.getLabel()));
			if (variable.getType() == ConfigValueType.INT || variable.getType() == ConfigValueType.BYTE) {
				variablesPanel.add(getNumberSpinner(variable.getName(), variable.getValue()));
			}
		}
		return variablesPanel;
	}

	private JPanel addButtons() {
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		JButton closeButton = new JButton(Messages.t("action.default.close"));
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigurationDialog.this.setVisible(false);
			}
		});
		buttons.add(closeButton);

		JButton button = new JButton(Messages.t("action.default.save"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				for(String name: _components.keySet()) {
					if (_components.get(name) instanceof  JSpinner) {
						parameters.put(name, ((JSpinner)_components.get(name)).getValue());
					}
				}
				_configurable.setValues(parameters);
				ConfigurationDialog.this.setVisible(false);
				ConfigurationDialog.this.dispose();
			}
		});
		buttons.add(button);
		return buttons;
	}

	private JSpinner getNumberSpinner(String name, String value) {
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Integer.parseInt(value), 1, Integer.MAX_VALUE, 1));
		spinner.setEditor(new JSpinner.NumberEditor(spinner, "##"));
		//((NumberFormatter) ((JSpinner.NumberEditor) spinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
		_components.put(name, spinner);
		return spinner;
	}
}

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
import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.ConfigValueType;
import cl.almejo.vsim.circuit.ConfigVariable;
import cl.almejo.vsim.circuit.Configurable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationDialog extends JDialog {

	private final Circuit _circuit;
	private final Configurable _configurable;
	private HashMap<String, Component> _components = new HashMap<String, Component>();

	public ConfigurationDialog(Circuit circuit, Configurable configurable) {
		_circuit = circuit;
		_configurable = configurable;
		setTitle(Messages.t("config.configurable.title"));
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(addVariables(configurable));
		panel.add(addButtons());
		getContentPane().add(panel);
		setPreferredSize(new Dimension(200, 200));
		pack();
	}

	private JPanel addVariables(Configurable configurable) {
		JPanel variablesPanel = new JPanel();
		variablesPanel.setLayout(new GridBagLayout());
		List<ConfigVariable> variables = configurable.getConfigVariables();
		//	variablesPanel.setLayout(new GridLayout(variables.size(), 2));
		final KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER){
					updateAndClose();
				}
			}
		};

		int i = 0;
		for (ConfigVariable variable : variables) {
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.gridx = 0;
			constraints.gridy = i;
			constraints.ipadx = 4;
			constraints.ipady = 4;
			JLabel label = new JLabel(variable.getLabel());
			label.setPreferredSize(new Dimension(80, 20));
			variablesPanel.add(label, constraints);

			constraints = new GridBagConstraints();
			constraints.gridx = 1;
			constraints.gridy = i;
			constraints.ipadx = 4;
			constraints.ipady = 4;
			if (variable.getType() == ConfigValueType.INT || variable.getType() == ConfigValueType.BYTE) {
				variablesPanel.add(getNumberSpinner(variable.getName(), variable.getValue(), keyListener), constraints);
			} else {
				variablesPanel.add(getStringField(variable.getName(), variable.getValue(), keyListener), constraints);
			}
			i++;
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
				updateAndClose();
			}
		});
		buttons.add(button);
		return buttons;
	}

	private void updateAndClose() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (String name : _components.keySet()) {
			if (_components.get(name) instanceof JSpinner) {
				parameters.put(name, ((JSpinner) _components.get(name)).getValue());
			} else {
				parameters.put(name, ((JTextField) _components.get(name)).getText());
			}
		}
		_circuit.undoableConfig(_configurable, parameters);
		this.setVisible(false);
		this.dispose();
	}

	private JSpinner getNumberSpinner(String name, String value, KeyListener listener) {
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Math.max(Integer.parseInt(value), 1), 1, Integer.MAX_VALUE, 1));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "##");
		spinner.setEditor(editor);
		spinner.setPreferredSize(new Dimension(80, 20));
		editor.getTextField().addKeyListener(listener);
		//((NumberFormatter) ((JSpinner.NumberEditor) spinner.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
		_components.put(name, spinner);
		return spinner;
	}


	private JTextField getStringField(String name, String value, KeyListener listener) {
		JTextField textField = new JTextField(value);
		textField.setPreferredSize(new Dimension(80, 20));
		textField.addKeyListener(listener);
		_components.put(name, textField);
		return textField;
	}

}

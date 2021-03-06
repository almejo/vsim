package cl.almejo.vsim.gui.actions.state;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.circuit.ConfigValueType;
import cl.almejo.vsim.circuit.ConfigVariable;
import cl.almejo.vsim.circuit.Configurable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
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
class ConfigurationDialog extends JDialog {

	static class ConfigComboItem {
		private final String value;
		private final String label;

		ConfigComboItem(String value, String label) {
			this.value = value;
			this.label = label;
		}

		String getValue() {
			return value;
		}

		public String getLabel() {
			return label;
		}

		@Override
		public String toString() {
			return label;
		}
	}

	private final Circuit circuit;
	private final Configurable configurable;
	private final HashMap<String, Component> components = new HashMap<>();

	private final KeyListener KEY_ADAPTER = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent event) {
			if (event.getKeyCode() == KeyEvent.VK_ENTER) {
				updateAndClose();
			} else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
				closeWindow();
			}
		}
	};

	ConfigurationDialog(Circuit circuit, Configurable configurable) {
		this.circuit = circuit;
		this.configurable = configurable;
		setTitle(Messages.t("config.configurable.title"));
		setModalityType(ModalityType.DOCUMENT_MODAL);
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);

		addKeyListener(KEY_ADAPTER);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(addVariables(configurable));
		panel.add(addButtons());
		getContentPane().add(panel);
		setPreferredSize(new Dimension(250, 200));
		pack();
	}

	private JPanel addVariables(Configurable configurable) {
		JPanel variablesPanel = new JPanel(new GridBagLayout());
		List<ConfigVariable> variables = configurable.getConfigVariables();
		int i = 0;
		JComponent firstComponent = null;
		for (ConfigVariable variable : variables) {
			JComponent component = createVariableEditor(variable);
			if (i == 0) {
				firstComponent = component;
			}
			variablesPanel.add(component, createConstraint(1, i));

			JLabel label = new JLabel(variable.getLabel());
			label.setLabelFor(component);
			variablesPanel.add(label, createConstraint(0, i));
			i++;
		}
		if (firstComponent != null) {
			firstComponent.requestFocus();
		}
		return variablesPanel;
	}

	private GridBagConstraints createConstraint(int column, int row) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.ipadx = 4;
		constraints.ipady = 4;
		return constraints;
	}

	private JComponent createVariableEditor(ConfigVariable variable) {
		if (variable.getType() == ConfigValueType.INT || variable.getType() == ConfigValueType.BYTE) {
			return getNumberSpinner(variable.getName(), variable.getValue(), ConfigVariable.MIN, ConfigVariable.MAX, variable.getStep());
		}
		if (variable.getType() == ConfigValueType.LIST) {
			return getListField(variable.getName(), variable.getValue(), variable.getLabels(), variable.getValues());
		}
		return getStringField(variable.getName(), variable.getValue());
	}

	private JComponent getListField(String name, String value, String[] labels, String[] values) {
		JComboBox<ConfigComboItem> comboBox = new JComboBox<>();
		ConfigComboItem selectedItem = null;
		for (int i = 0; i < labels.length; i++) {
			ConfigComboItem comboItem = new ConfigComboItem(values[i], labels[i]);
			if (comboItem.getValue().equalsIgnoreCase(value)) {
				selectedItem = comboItem;
			}
			comboBox.addItem(comboItem);
		}
		if (selectedItem != null) {
			comboBox.setSelectedItem(selectedItem);
		}
		comboBox.setPreferredSize(new Dimension(80, 20));
		comboBox.addKeyListener(KEY_ADAPTER);
		components.put(name, comboBox);
		return comboBox;
	}

	private JPanel addButtons() {
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

		JButton closeButton = new JButton(Messages.t("action.default.close"));
		closeButton.addActionListener(e -> closeWindow());
		buttons.add(closeButton);

		JButton button = new JButton(Messages.t("action.default.save"));

		button.addActionListener(e -> updateAndClose());
		buttons.add(button);
		return buttons;
	}

	private void updateAndClose() {
		circuit.undoableConfig(configurable, getValuesAsMap());
		closeWindow();
	}

	private Map<String, Object> getValuesAsMap() {
		Map<String, Object> parameters = new HashMap<>();
		for (String name : components.keySet()) {
			parameters.put(name, getValue(name));
		}
		return parameters;
	}

	private Object getValue(String name) {
		if (components.get(name) instanceof JSpinner) {
			return ((JSpinner) components.get(name)).getValue();
		}
		if (components.get(name) instanceof JComboBox) {
			return ((ConfigComboItem) ((JComboBox) components.get(name)).getSelectedItem()).getValue();
		}
		return ((JTextField) components.get(name)).getText();
	}

	private void closeWindow() {
		setVisible(false);
		dispose();
	}

	private JSpinner getNumberSpinner(String name, String value, int min, int max, int step) {
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(Math.max(Integer.parseInt(value), 1), min, max, step));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "##");
		spinner.setEditor(editor);
		spinner.setPreferredSize(new Dimension(80, 20));
		editor.getTextField().addKeyListener(KEY_ADAPTER);
		components.put(name, spinner);
		return spinner;
	}

	private JTextField getStringField(String name, String value) {
		JTextField textField = new JTextField(value);
		textField.setPreferredSize(new Dimension(80, 20));
		textField.addKeyListener(KEY_ADAPTER);
		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textField.selectAll();
			}
		});
		components.put(name, textField);
		return textField;
	}
}

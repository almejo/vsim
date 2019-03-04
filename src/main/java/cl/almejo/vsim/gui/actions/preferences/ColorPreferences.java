package cl.almejo.vsim.gui.actions.preferences;

import cl.almejo.vsim.Config;
import cl.almejo.vsim.Messages;
import cl.almejo.vsim.gui.ColorScheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class ColorPreferences extends JPanel {

	private final Component component;
	private final JComboBox<String> comboBox;

	ColorPreferences(Component parent) {
		component = parent;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel pickers = new JPanel();
		pickers.setLayout(new GridLayout(10, 2));

		pickers.add(new Label(Messages.t("preferences.color.scheme.current.label")));
		comboBox = createSchemeComboBox();
		pickers.add(comboBox);

		addColorChooser(pickers, "gates");
		addColorChooser(pickers, "background");
		addColorChooser(pickers, "bus-on");
		addColorChooser(pickers, "ground");
		addColorChooser(pickers, "off");
		addColorChooser(pickers, "wires-on");
		addColorChooser(pickers, "signal");
		addColorChooser(pickers, "grid");
		addColorChooser(pickers, "label");
		add(pickers);

		JPanel buttons = new JPanel();
		JButton saveButton = new JButton(Messages.t("preferences.color.scheme.save.label"));
		buttons.add(saveButton);
		saveButton.addActionListener(event -> {
			try {
				Config.writeColors( ColorScheme.save());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		JButton newButton = new JButton(Messages.t("preferences.color.scheme.new.label"));
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String themeName = JOptionPane.showInputDialog(Messages.t("color.scheme.dialog.new.title"), "");
				if (themeName != null) {
					while (ColorScheme.exists(themeName)) {
						JOptionPane.showMessageDialog(component, Messages.t("color.scheme.dialog.already.exists.error"), "Error", JOptionPane.ERROR_MESSAGE);
						themeName = JOptionPane.showInputDialog(this, themeName);
					}
					ColorScheme.add(themeName);
					comboBox.addItem(themeName);
					comboBox.setSelectedItem(themeName);
				}
			}
		});
		buttons.add(newButton);
		add(buttons);
	}

	private JComboBox<String> createSchemeComboBox() {
		JComboBox<String> comboBox = new JComboBox<>(ColorScheme.getNames());
		comboBox.addActionListener(event -> ColorScheme.setCurrent((String) ((JComboBox) event.getSource()).getSelectedItem()));
		comboBox.setSelectedItem("default");
		return comboBox;
	}

	private void addColorChooser(JPanel panel, String item) {
		JLabel label = new JLabel();
		label.setText(Messages.t("preferences.color.scheme." + item + ".label"));
		panel.add(label);
		JButton button = new JButton(Messages.t("default.change.label"));
		button.addActionListener(event -> {
			JButton button1 = (JButton) event.getSource();
			String themeName = ColorScheme.getCurrent().getName();
			Color color = JColorChooser.showDialog(button1, Messages.t("preferences.color.choose.title"), ColorScheme.getScheme(themeName).get(item));
			if (color != null) {
				ColorScheme.getScheme(themeName).set(item, color);
			}
		});
		panel.add(button);
	}

}

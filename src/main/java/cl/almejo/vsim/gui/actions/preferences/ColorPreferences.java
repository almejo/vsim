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
package cl.almejo.vsim.gui.actions.preferences;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.gui.ColorScheme;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class ColorPreferences extends JPanel {

	private final Component _parent;
	private JComboBox<String> _comboBox;

	public ColorPreferences(Component parent) {
		_parent = parent;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel pickers = new JPanel();
		pickers.setLayout(new GridLayout(8, 2));

		pickers.add(new Label(Messages.t("preferences.color.scheme.current.label")));
		_comboBox = createSchemeCombobox();
		pickers.add(_comboBox);

		addColorChooser(pickers, "gates");
		addColorChooser(pickers, "background");
		addColorChooser(pickers, "bus-on");
		addColorChooser(pickers, "ground");
		addColorChooser(pickers, "off");
		addColorChooser(pickers, "wires-on");
		addColorChooser(pickers, "signal");
		add(pickers);

		JPanel buttons = new JPanel();
		JButton button = new JButton(Messages.t("preferences.color.scheme.save.label"));
		buttons.add(button);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					FileUtils.writeStringToFile(new File("colors.json"), ColorScheme.save());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button = new JButton(Messages.t("preferences.color.scheme.new.label"));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String themeName = JOptionPane.showInputDialog(Messages.t("color.scheme.dialog.new.title"), "");
				if (themeName != null) {
					while (ColorScheme.exists(themeName)) {
						JOptionPane.showMessageDialog(_parent, Messages.t("color.scheme.dialog.already.exists.error"), "Error", JOptionPane.ERROR_MESSAGE);
						themeName = JOptionPane.showInputDialog(this, themeName);
					}
					ColorScheme.add(themeName);
					_comboBox.addItem(themeName);
					_comboBox.setSelectedItem(themeName);
				}
			}
		});
		buttons.add(button);
		add(buttons);
	}

	private JComboBox<String> createSchemeCombobox() {
		JComboBox<String> _comboBox = new JComboBox<String>(ColorScheme.getNames());
		_comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox combobox = (JComboBox) e.getSource();
				ColorScheme.setCurrent((String) combobox.getSelectedItem());
			}
		});
		_comboBox.setSelectedItem("default");
		return _comboBox;
	}

	private void addColorChooser(JPanel panel, final String item) {
		JLabel label = new JLabel();
		label.setText(Messages.t("preferences.color.scheme." + item + ".label"));
		panel.add(label);
		JButton button = new JButton(Messages.t("default.change.label"));
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				String themeName = ColorScheme.getCurrent().getName();
				Color color = JColorChooser.showDialog(button, Messages.t("preferences.color.choose.title"), ColorScheme.getScheme(themeName).get(item));
				if (color != null) {
					ColorScheme.getScheme(themeName).set(item, color);
				}
			}
		};

		button.addActionListener(actionListener);
		panel.add(button);
	}

}

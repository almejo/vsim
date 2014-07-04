package cl.almejo.vsim.gui.actions.preferences;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.gui.ColorScheme;
import com.alee.laf.button.WebButton;
import com.alee.laf.colorchooser.WebColorChooserDialog;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.DialogOptions;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

	private final Component _parent;
	private final JComboBox<String> _comboBox;

	ColorPreferences(Component parent) {
		_parent = parent;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel pickers = new JPanel();
		pickers.setLayout(new GridLayout(10, 2));

		pickers.add(new Label(Messages.t("preferences.color.scheme.current.label")));
		_comboBox = createSchemeComboBox();
		pickers.add(_comboBox);

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
				FileUtils.writeStringToFile(new File("colors.json"), ColorScheme.save());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		JButton newButton = new JButton(Messages.t("preferences.color.scheme.new.label"));
		newButton.addActionListener(new ActionListener() {
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
		buttons.add(newButton);
		add(buttons);
	}

	private JComboBox<String> createSchemeComboBox() {
		JComboBox<String> _comboBox = new JComboBox<>(ColorScheme.getNames());
		_comboBox.addActionListener(event -> {
			JComboBox comboBox = (JComboBox) event.getSource();
			ColorScheme.setCurrent((String) comboBox.getSelectedItem());
		});
		_comboBox.setSelectedItem("default");
		return _comboBox;
	}

	private void addColorChooser(final JPanel panel, final String item) {
		JLabel label = new JLabel();
		label.setText(Messages.t("preferences.color.scheme." + item + ".label"));
		panel.add(label);

		final String themeName = ColorScheme.getCurrent().getName();
		final Color initialColor = ColorScheme.getScheme(themeName).get(item);
		final WebButton button = new WebButton(getColorText(initialColor), ImageUtils.createColorIcon(initialColor));
		button.setLeftRightSpacing(0);
		button.setAlignmentX(1);
		button.setMargin(0, 0, 0, 3);
		button.addActionListener(new ActionListener() {
			private WebColorChooserDialog chooser = null;
			private Color lastColor = initialColor;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chooser == null) {
					chooser = new WebColorChooserDialog(panel);
				}
				chooser.setColor(lastColor);
				chooser.setVisible(true);

				if (chooser.getResult() == DialogOptions.OK_OPTION) {
					Color color = chooser.getColor();
					lastColor = color;

					button.setIcon(ImageUtils.createColorIcon(color));
					button.setText(getColorText(color));
					ColorScheme.getScheme(themeName).set(item, color);
				}
			}
		});
//		JButton button = new JButton(Messages.t("default.change.label"));
//		ActionListener actionListener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JButton button = (JButton) e.getSource();
//				String themeName = ColorScheme.getCurrent().getName();
//				Color color = JColorChooser.showDialog(button, Messages.t("preferences.color.choose.title"), ColorScheme.getScheme(themeName).get(item));
//				if (color != null) {
//					ColorScheme.getScheme(themeName).set(item, color);
//				}
//			}
//		};

		//button.addActionListener(actionListener);
		panel.add(button);
	}

	private String getColorText(Color color) {
		return color.getRed() + ", " + color.getGreen() + ", " + color.getBlue();
	}
}

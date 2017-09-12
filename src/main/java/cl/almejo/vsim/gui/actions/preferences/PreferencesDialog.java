package cl.almejo.vsim.gui.actions.preferences;

import cl.almejo.vsim.Messages;
import cl.almejo.vsim.gui.SimWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class PreferencesDialog extends JDialog {

	public PreferencesDialog(SimWindow window) {
		setTitle(Messages.t("preferences.title"));
		setMinimumSize(new Dimension(400, 500));
		setLocationRelativeTo(window);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setAlwaysOnTop(true);
		pack();

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		tabbedPane.add(Messages.t("preferences.title.color.preferences.label"), new ColorPreferences(tabbedPane));
		panel.add(tabbedPane);

		JButton closeButton = new JButton(Messages.t("action.default.close"));
		closeButton.addActionListener(event -> this.setVisible(false));
		panel.add(closeButton);
		getContentPane().add(panel);
	}

}

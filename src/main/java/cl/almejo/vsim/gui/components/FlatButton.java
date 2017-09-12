package cl.almejo.vsim.gui.components;

import javax.swing.*;
import javax.swing.border.Border;
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

class FlatButton extends JButton {

	FlatButton(String text) {
		super(text);
		Border raisedBevelBorder = BorderFactory.createRaisedBevelBorder();
		Insets insets = raisedBevelBorder.getBorderInsets(this);
		EmptyBorder emptyBorder = new EmptyBorder(insets);
		setBorder(emptyBorder);
		setFocusPainted(false);
		setOpaque(false);
		setContentAreaFilled(false);
		getModel().addChangeListener(event -> {
			ButtonModel model = (ButtonModel) event.getSource();
			if (model.isRollover()) {
				this.setBorder(raisedBevelBorder);
			} else {
				this.setBorder(emptyBorder);
			}
		});
	}
}

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

package cl.almejo.vsim.gui.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class FlatButton extends JButton {

	public FlatButton(String text) {
		super(text);
		final Border raisedBevelBorder = BorderFactory.createRaisedBevelBorder();
		final Insets insets = raisedBevelBorder.getBorderInsets(this);
		final EmptyBorder emptyBorder = new EmptyBorder(insets);
		setBorder(emptyBorder);
		setFocusPainted(false);
		setOpaque(false);
		setContentAreaFilled(false);
		getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel model = (ButtonModel) e.getSource();
				if (model.isRollover()) {
					FlatButton.this.setBorder(raisedBevelBorder);
				} else {
					FlatButton.this.setBorder(emptyBorder);
				}
			}
		});
	}
}

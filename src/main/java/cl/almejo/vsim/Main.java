package cl.almejo.vsim;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
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
public class Main {

	public Main() throws IOException {
		File file = new File("circuit.json");
		Circuit circuit = Circuit.fromJSon(FileUtils.readFileToString(file, "UTF-8"), file.getAbsolutePath());
		if ("native".equalsIgnoreCase(System.getProperty("vsim.look"))) {
			setNativeLookAndFeel();
		} else if ("Nimbus".equalsIgnoreCase(System.getProperty("vsim.look"))) {
			setNimbusLookAndFeel();
		}
		new SimWindow(circuit);
	}

	private void setNimbusLookAndFeel() {
		for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	private void setNativeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			GTKHelper.installGtkPopupBugWorkaround();
		} catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new Main();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

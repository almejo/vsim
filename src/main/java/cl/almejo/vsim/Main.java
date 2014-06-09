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


package cl.almejo.vsim;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {

	public Main() throws IOException {
		File file = new File("circuit.json");
		Circuit circuit = Circuit.fromJSon(FileUtils.readFileToString(file), file.getAbsolutePath());
		if ("native".equalsIgnoreCase(System.getProperty("vsim.look"))) {

			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				GTKHelper.installGtkPopupBugWorkaround();
			} catch (UnsupportedLookAndFeelException ex) {
				System.out.println("Unable to fromJSon native look and feel");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else if ("Nimbus".equalsIgnoreCase(System.getProperty("vsim.look"))) {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					try {
						UIManager.setLookAndFeel(info.getClassName());
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		new SimWindow(circuit);
	}

	public static void main(String[] args) {
		try {
			new Main();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

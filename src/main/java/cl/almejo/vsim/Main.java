package cl.almejo.vsim;

import cl.almejo.vsim.circuit.Circuit;
import cl.almejo.vsim.gui.SimWindow;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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

	public Main(String[] args) throws IOException {
		CommandLine commandLine;
		try {
			commandLine = parseCommandArguments(args);
		} catch (ParseException exp) {
			System.err.println("Error: " + exp.getMessage());
			System.exit(0);
			return;
		}

		Config.init();

		Circuit circuit = getCircuit(commandLine);

		if ("native".equalsIgnoreCase(System.getProperty("vsim.look"))) {
			setNativeLookAndFeel();
		} else if ("Nimbus".equalsIgnoreCase(System.getProperty("vsim.look"))) {
			setNimbusLookAndFeel();
		}
		new SimWindow(circuit);
	}

	private Circuit getCircuit(CommandLine commandLine) throws IOException {
		if (commandLine.getArgList().size() > 0 && new File(commandLine.getArgs()[0]).exists()) {
			File file = new File(commandLine.getArgs()[0]);
			return Circuit.fromJSon(FileUtils.readFileToString(file, "UTF-8"), file.getAbsolutePath());
		}
		return new Circuit();
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
			new Main(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static CommandLine parseCommandArguments(String[] args) throws ParseException {
		return new DefaultParser().parse(new Options(), args);
	}
}

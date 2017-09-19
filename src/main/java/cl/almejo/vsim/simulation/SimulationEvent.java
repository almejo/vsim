package cl.almejo.vsim.simulation;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public abstract class SimulationEvent extends HeapElement {

	Scheduler scheduler;

	public SimulationEvent(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void schedule(long delay) {
		scheduler.schedule(this, delay);
	}

	public abstract void happen();

	long getTime() {
		return getValue();
	}

	public boolean isProgrammed() {
		return scheduler.isProgrammed(this);
	}
}

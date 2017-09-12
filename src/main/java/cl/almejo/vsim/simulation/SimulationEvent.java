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

	Scheduler _scheduler;

	public SimulationEvent(Scheduler scheduler) {
		_scheduler = scheduler;
	}

	public void schedule(long delay) {
		_scheduler.schedule(this, delay);
	}

	public abstract void happen();

	long getTime() {
		return getValue();
	}

	public boolean isProgrammed() {
		return _scheduler.isProgrammed(this);
	}
}

package cl.almejo.vsim.simulation;

/**
 * vsim
 * <p>
 * Created on Aug 1, 2013
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
public class Scheduler {

	private static class StopEvent extends SimulationEvent {
		StopEvent(Scheduler scheduler) {
			super(scheduler);
		}

		@Override
		public void happen() {
			_scheduler.stop();
		}

	}

	private final Heap _heap = new Heap();

	private long _time;

	@SuppressWarnings("InfiniteLoopStatement")
	public void run(long simulationTime) {
		schedule(new StopEvent(this), simulationTime);
		try {
			while (true) {
				SimulationEvent event = (SimulationEvent) _heap.remove();
				_time = event.getTime();
				event.happen();
			}
		} catch (EmptyHeapException e) {
			e.printStackTrace();
		} catch (StopSimulationException e) {
			// System.out.println("Simulacion detenida");
		}
	}

	private void stop() throws StopSimulationException {
		throw new StopSimulationException();
	}

	void schedule(HeapElement element, long delay) {
		element.setValue(_time + delay);
		_heap.insert(element);
	}

	boolean isProgrammed(SimulationEvent simulationEvent) {
		return _heap.contains(simulationEvent);
	}

}

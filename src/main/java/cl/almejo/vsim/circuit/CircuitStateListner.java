package cl.almejo.vsim.circuit;

/**
 * Created by alejo on 5/4/14.
 */
public interface CircuitStateListner {

	public void onPause(CircuitEvent event);

	public void onResume(CircuitEvent event);
}

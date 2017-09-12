package cl.almejo.vsim.circuit;

import java.util.*;

/**
 * vsim
 * <p>
 * This program is distributed under the terms of the GNU General Public License
 * The license is included in license.txt
 *
 * @author Alejandro Vera
 */
class Matrix<T extends Point> {

	private final Hashtable<Integer, List<T>> _verticalTable = new Hashtable<>();
	private final Hashtable<Integer, List<T>> _horizontalTable = new Hashtable<>();

	private final Comparator<T> COMPARATOR_X = (o1, o2) -> {
		if (o1.getX() < o2.getX()) {
			return -1;
		}
		return o1.getX() == o2.getX() ? 0 : 1;
	};

	private final Comparator<T> COMPARATOR_Y = (o1, o2) -> {
		if (o1.getY() < o2.getY()) {
			return -1;
		}
		return o1.getY() == o2.getY() ? 0 : 1;
	};

	public void add(T point) {
		insertIntoPointsTable(point, _horizontalTable, COMPARATOR_Y, point.getX());
		insertIntoPointsTable(point, _verticalTable, COMPARATOR_X, point.getY());
	}

	private void insertIntoPointsTable(T point, Hashtable<Integer, List<T>> table, Comparator<T> comparator, int coordinate) {
		List<T> points = table.computeIfAbsent(coordinate, key -> new LinkedList<>());

		int index = Collections.binarySearch(points, point, comparator);
		if (index < 0) {
			points.add(-index - 1, point);
		}
	}

	FindResult<T> findHorizontal(int x, int y) {
		if (!_verticalTable.containsKey(y)) {
			return new FindResult<>(null, null, null);
		}

		T previous = null;
		T next = null;
		List<T> points = _verticalTable.get(y);
		int i = 0;
		for (T point : points) {
			if (point.getX() == x) {
				if (i < points.size() - 1) {
					next = points.get(i + 1);
				}
				return new FindResult<>(point, previous, next);
			}
			if (point.getX() > x) {
				return new FindResult<>(null, previous, point);
			}
			previous = point;
			i++;
		}
		return new FindResult<>(null, previous, null);
	}

	FindResult<T> findVertical(int x, int y) {
		if (!_horizontalTable.containsKey(x)) {
			return new FindResult<>(null, null, null);
		}

		T previous = null;
		T next = null;
		List<T> points = _horizontalTable.get(x);
		int i = 0;
		for (T point : points) {
			if (point.getY() == y) {
				if (i < points.size() - 1) {
					next = points.get(i + 1);
				}
				return new FindResult<>(point, previous, next);
			}
			if (point.getY() > y) {
				return new FindResult<>(null, previous, point);
			}
			previous = point;
			i++;
		}
		return new FindResult<>(null, previous, null);
	}

	List<T> getBetween(T a, T b) {

		if (a == null || b == null) {
			return new LinkedList<>();
		}

		List<T> list;
		Comparator<T> comparator;
		if (a.getX() == b.getX()) {
			list = _horizontalTable.get(a.getX());
			comparator = COMPARATOR_Y;
		} else {
			list = _verticalTable.get(a.getY());
			comparator = COMPARATOR_X;
		}

		List<T> points = new LinkedList<>();
		int ndx = Collections.binarySearch(list, a, comparator);
		ndx++;
		T point = list.get(ndx++);
		while (point != null && point != b) {
			points.add(point);
			point = list.get(ndx++);
		}
		return points;
	}

	List<T> getSublist(T a, T b) {

		if (a == null || b == null) {
			return new LinkedList<>();
		}

		List<T> list;
		Comparator<T> comparator;
		if (a.getX() == b.getX()) {
			list = _horizontalTable.get(a.getX());
			comparator = COMPARATOR_Y;
		} else {
			list = _verticalTable.get(a.getY());
			comparator = COMPARATOR_X;
		}

		List<T> points = new LinkedList<>();
		int ndx = Collections.binarySearch(list, a, comparator);
		T point = list.get(ndx++);
		while (point != null) {
			points.add(point);
			if (point == b) {
				break;
			}
			point = list.get(ndx++);
		}
		return points;
	}

	@Override
	public String toString() {
		return drawPointTable(_horizontalTable, "horizontal") + "\n\n" + drawPointTable(_verticalTable, "vertical");
	}

	private String drawPointTable(Hashtable<Integer, List<T>> table, String title) {
		StringBuilder builder = new StringBuilder();
		builder.append(title).append("\n");
		for (Integer key : table.keySet()) {
			builder.append(key).append(": ");
			for (Point point : table.get(key)) {
				builder.append(point);
				builder.append(" ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	public void remove(T point) {
		if (_horizontalTable.containsKey(point.getX())) {
			int index = Collections.binarySearch(_horizontalTable.get(point.getX()), point, COMPARATOR_Y);
			if (index >= 0) {
				_horizontalTable.get(point.getX()).remove(index);
			}
		}

		if (_verticalTable.containsKey(point.getY())) {
			int index = Collections.binarySearch(_verticalTable.get(point.getY()), point, COMPARATOR_X);
			if (index >= 0) {
				_verticalTable.get(point.getY()).remove(index);
			}
		}
	}

	Set<Integer> getXCoordinates() {
		return _horizontalTable.keySet();
	}

	Set<Integer> getYCoordinates() {
		return _verticalTable.keySet();
	}

	List<T> getVerticalContacts(Integer x) {
		return _horizontalTable.get(x);
	}

	List<T> getHorizontalContacts(Integer y) {
		return _verticalTable.get(y);
	}
}

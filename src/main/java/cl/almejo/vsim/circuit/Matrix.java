package cl.almejo.vsim.circuit;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Matrix<T extends Point> {

	private Hashtable<Integer, List<T>> _verticalTable = new Hashtable<Integer, List<T>>();
	private Hashtable<Integer, List<T>> _horizontalTable = new Hashtable<Integer, List<T>>();

	private final Comparator<T> COMPARATORX = new Comparator<T>() {

		public int compare(T o1, T o2) {
			return o1.getX() < o2.getX() ? -1 : (o1.getX() == o2.getX() ? 0 : 1);
		}
	};

	private final Comparator<T> COMPARATORY = new Comparator<T>() {

		public int compare(T o1, T o2) {
			return o1.getY() < o2.getY() ? -1 : (o1.getY() == o2.getY() ? 0 : 1);
		}
	};

	public void add(T point) {
		insertIntoTable(point, _horizontalTable, COMPARATORY, point.getX());
		insertIntoTable(point, _verticalTable, COMPARATORX, point.getY());
	}

	private void insertIntoTable(T point, Hashtable<Integer, List<T>> table, Comparator<T> comparator, int coord) {
		List<T> points = table.get(coord);
		if (points == null) {
			points = new LinkedList<T>();
			table.put(coord, points);
		}

		int index = Collections.binarySearch(points, point, comparator);
		if (index < 0) {
			points.add(-index - 1, point);
		}
	}

	public FindResult<T> findHorizontal(int x, int y) {
		if (!_verticalTable.containsKey(y)) {
			return new FindResult<T>(null, null, null);
		}

		T previous = null;
		T hit = null;
		T next = null;
		List<T> points = _verticalTable.get(y);
		int i = 0;
		for (T point : points) {
			if (point.getX() == x) {
				hit = point;
				if (i < points.size() - 1) {
					next = points.get(i + 1);
				}
				return new FindResult<T>(hit, previous, next);
			}
			previous = point;
			i++;
		}
		return new FindResult<T>(null, null, null);
	}
	
	public FindResult<T> findVertical(int x, int y) {
		if (!_horizontalTable.containsKey(x)) {
			return new FindResult<T>(null, null, null);
		}

		T previous = null;
		T hit = null;
		T next = null;
		List<T> points = _horizontalTable.get(y);
		int i = 0;
		for (T point : points) {
			if (point.getY() == y) {
				hit = point;
				if (i < points.size() - 1) {
					next = points.get(i + 1);
				}
				return new FindResult<T>(hit, previous, next);
			}
			previous = point;
			i++;
		}
		return new FindResult<T>(null, null, null);
	}

	@Override
	public String toString() {
		return drawTable(_horizontalTable, "horizontal") + "\n\n" + drawTable(_verticalTable, "vertical");
	}

	private String drawTable(Hashtable<Integer, List<T>> table, String title) {
		StringBuilder builder = new StringBuilder();
		builder.append(title + "\n");
		for (Integer key : table.keySet()) {
			builder.append(key + ": ");
			for (Point point : table.get(key)) {
				builder.append(point);
				builder.append(" ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	public static void main(String[] args) {
		Matrix<Point> matrix = new Matrix<Point>();
		matrix.add(new Point(-80, 30));
		matrix.add(new Point(-80, 10));
		matrix.add(new Point(-80, 30));
		matrix.add(new Point(-80, 30));
		matrix.add(new Point(-80, 60));
		matrix.add(new Point(-80, 0));
		matrix.add(new Point(0, 0));
		matrix.add(new Point(10, 0));
		matrix.add(new Point(24, 10));
		matrix.add(new Point(0, 10));
		matrix.add(new Point(10, 10));
		matrix.add(new Point(10, 11));
		matrix.add(new Point(10, 15));
		matrix.add(new Point(10, 15));
		matrix.add(new Point(15, 0));
		matrix.add(new Point(-11, 0));
		matrix.add(new Point(40, 20));
		matrix.add(new Point(-40, 0));
		matrix.add(new Point(-10, 0));

		System.out.println(matrix);
		System.out.println(matrix.findHorizontal(10, 10));
		System.out.println(matrix.findHorizontal(24, 10));
		System.out.println(matrix.findHorizontal(0, 10));
		System.out.println(matrix.findHorizontal(-80, 10));
	}
}

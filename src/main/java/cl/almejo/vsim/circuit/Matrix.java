package cl.almejo.vsim.circuit;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Matrix{

	private Hashtable<Integer, List<Point>> _verticalTable = new Hashtable<Integer, List<Point>>();
	private Hashtable<Integer, List<Point>> _horizontalTable = new Hashtable<Integer, List<Point>>();

	private final Comparator<Point> COMPARAPointORX = new Comparator<Point>() {

		public int compare(Point o1, Point o2) {
			return o1.getX() < o2.getX() ? -1 : (o1.getX() == o2.getX() ? 0 : 1);
		}
	};

	private final Comparator<Point> COMPARAPointORY = new Comparator<Point>() {

		public int compare(Point o1, Point o2) {
			return o1.getY() < o2.getY() ? -1 : (o1.getY() == o2.getY() ? 0 : 1);
		}
	};

	public void add(Point point) {
		insertIntoPointable(point, _horizontalTable, COMPARAPointORY, point.getX());
		insertIntoPointable(point, _verticalTable, COMPARAPointORX, point.getY());
	}

	private void insertIntoPointable(Point point, Hashtable<Integer, List<Point>> table, Comparator<Point> comparator, int coord) {
		List<Point> points = table.get(coord);
		if (points == null) {
			points = new LinkedList<Point>();
			table.put(coord, points);
		}

		int index = Collections.binarySearch(points, point, comparator);
		if (index < 0) {
			points.add(-index - 1, point);
		}
	}

	public FindResult findHorizontal(int x, int y) {
		if (!_verticalTable.containsKey(y)) {
			return new FindResult(null, null, null);
		}

		Point previous = null;
		Point hit = null;
		Point next = null;
		List<Point> points = _verticalTable.get(y);
		int i = 0;
		for (Point point : points) {
			if (point.getX() == x) {
				hit = point;
				if (i < points.size() - 1) {
					next = points.get(i + 1);
				}
				return new FindResult(hit, previous, next);
			}
			previous = point;
			i++;
		}
		return new FindResult(null, null, null);
	}
	
	public FindResult findVertical(int x, int y) {
		if (!_horizontalTable.containsKey(x)) {
			return new FindResult(null, null, null);
		}

		Point previous = null;
		Point hit = null;
		Point next = null;
		List<Point> points = _horizontalTable.get(x);
		int i = 0;
		for (Point point : points) {
			if (point.getY() == y) {
				hit = point;
				if (i < points.size() - 1) {
					next = points.get(i + 1);
				}
				return new FindResult(hit, previous, next);
			}
			previous = point;
			i++;
		}
		return new FindResult(null, null, null);
	}

	@Override
	public String toString() {
		return drawPointable(_horizontalTable, "horizontal") + "\n\n" + drawPointable(_verticalTable, "vertical");
	}

	private String drawPointable(Hashtable<Integer, List<Point>> table, String title) {
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
		Matrix matrix = new Matrix();
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

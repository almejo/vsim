package cl.almejo.vsim.circuit;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class Matrix {

	private Hashtable<Integer, List<Point>> _verticalTable = new Hashtable<Integer, List<Point>>();
	private Hashtable<Integer, List<Point>> _horizontalTable = new Hashtable<Integer, List<Point>>();

	private static final Comparator<Point> COMPARATORX = new Comparator<Point>() {

		public int compare(Point o1, Point o2) {
			return o1.getX() < o2.getX() ? -1 : (o1.getX() == o2.getX() ? 0 : 1);
		}
	};

	private static final Comparator<Point> COMPARATORY = new Comparator<Point>() {

		public int compare(Point o1, Point o2) {
			return o1.getY() < o2.getY() ? -1 : (o1.getY() == o2.getY() ? 0 : 1);
		}
	};

	public void add(Point point) {
		insertIntoTable(point, _horizontalTable, COMPARATORY, point.getX());
		insertIntoTable(point, _verticalTable, COMPARATORX, point.getY());
	}

	private void insertIntoTable(Point point, Hashtable<Integer, List<Point>> table, Comparator<Point> comparator, int coord) {
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
	
	@Override
	public String toString() {
		return drawTable(_horizontalTable, "horizontal") + "\n\n" + drawTable(_verticalTable, "vertical");
	}
	
	private String drawTable(Hashtable<Integer, List<Point>> table, String title) {
		StringBuilder builder= new StringBuilder();
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
		matrix.add(new Point(-80,30));
		matrix.add(new Point(-80,10));
		matrix.add(new Point(-80,30));
		matrix.add(new Point(-80,30));
		matrix.add(new Point(-80,60));
		matrix.add(new Point(-80,0));
		matrix.add(new Point(0,0));
		matrix.add(new Point(10,0));
		matrix.add(new Point(0,10));
		matrix.add(new Point(10,10));
		matrix.add(new Point(10,11));
		matrix.add(new Point(10,15));
		matrix.add(new Point(10,15));
		matrix.add(new Point(15,0));
		matrix.add(new Point(-11,0));
		matrix.add(new Point(40,20));
		matrix.add(new Point(-40,0));
		matrix.add(new Point(-10,0));

		System.out.println(matrix);
	}
}

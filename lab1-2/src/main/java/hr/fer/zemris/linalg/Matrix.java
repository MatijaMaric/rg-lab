package hr.fer.zemris.linalg;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matrix extends AbstractMatrix {
	double[][] elements;
	int rows;
	int cols;

	public Matrix(int rows, int cols) {
		this(rows, cols, new double[rows][cols], true);
	}
	
	public Matrix(int rows, int cols, double[][] elements, boolean useGiven) {
		if (useGiven) {
			this.elements = elements;
		} else {
			this.elements = new double[rows][cols];
			for (int i = 0; i < rows; i++) {
				System.arraycopy(elements[i], 0, this.elements[i], 0, cols);
			}
		}
		this.rows = rows;
		this.cols = cols;
	}

	public static IMatrix defaultMatrix(int rows, int cols) {
		return new Matrix(rows, cols, new double[rows][cols], true);
	}

	@Override
	public int getRowsCount() {
		return rows;
	}

	@Override
	public int getColsCount() {
		return cols;
	}

	@Override
	public double get(int row, int col) {
		return elements[row][col];
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		elements[row][col] = value;
		return this;
	}

	@Override
	public IMatrix copy() {
		return new Matrix(rows, cols, elements, false);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return defaultMatrix(rows, cols);
	}

	public static Matrix parseSimple(String s) {
		String regex = "(?<num>[\\+\\-]?[\\d]+\\.?[\\d]*)";
		regex += "|(?<newRow>\\|)";
		regex += "|(?<other>[\\S]*)";
		final Matcher match = Pattern.compile(regex).matcher(s);
		List<ArrayList<Double>> values = new ArrayList<>();
		ArrayList<Double> row = new ArrayList<>();
		while (match.find()) {
			if (!match.group().isEmpty()) {
				if (match.group("newRow") != null) {
					values.add(row);
					row = new ArrayList<>();
				} else if (match.group("num") != null) {
					row.add(Double.valueOf(match.group()));
				}
			}
		}
		values.add(row);

		int rows = values.size();
		int cols = values.get(0).size();

		double[][] elements = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				elements[i][j] = values.get(i).get(j);
			}
		}

		return new Matrix(rows, cols, elements, true);
	}
}

package hr.fer.zemris.linalg;

public abstract class AbstractMatrix implements IMatrix {

	@Override
	public IMatrix nTranspose(boolean liveView) {
		if (liveView) {
			return new MatrixTransposeView(this);
		} else {
			int rows = getRowsCount();
			int cols = getColsCount();
			IMatrix transposed = newInstance(cols, rows);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					transposed.set(j, i, get(i, j));
				}
			}
			return transposed;
		}
	}

	@Override
	public IMatrix add(IMatrix other) {
		iterateMatrix((row, col) -> this.set(row, col, this.get(row, col) + other.get(row, col)));
		return this;
	}

	@Override
	public IMatrix nAdd(IMatrix other) {
		return copy().add(other);
	}

	@Override
	public IMatrix sub(IMatrix other) {
		iterateMatrix((row, col) -> this.set(row, col, this.get(row, col) - other.get(row, col)));
		return this;
	}

	@Override
	public IMatrix nSub(IMatrix other) {
		return copy().sub(other);
	}

	@Override
	public IMatrix nMultiply(IMatrix other) {
		IMatrix newMatrix = newInstance(this.getRowsCount(), other.getColsCount());
		
		for (int i = 0, rows = getRowsCount(); i < rows; i++) {
			for (int j = 0, cols = other.getColsCount(); j < cols; j++) {
				double sum = 0;
				for (int k = 0, mutual = getColsCount(); k < mutual; k++) {
					sum += get(i, k) * other.get(k, j);
				}
				newMatrix.set(i, j, sum);
			}
		}
		return newMatrix;
	}

	@Override
	public double determinant() {
		int rows = getRowsCount();
		if (rows == 1) {
			return get(0, 0);
		} 
		
		if (rows == 2) {
			return get(0, 0)*get(1, 1) - get(0, 1)*get(1, 0);
		} 
		
		double determinant = 0;
		for (int j = 0; j < rows; j++) {
			determinant += Math.pow(-1, j)*get(0, j)*subMatrix(0, j, true).determinant(); 
		}
		
		return determinant;
	}

	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		if (liveView) {
			return new MatrixSubMatrixView(this, row, col);
		} else {
			int rows = getRowsCount();
			int cols = getColsCount();
			int newRows = rows-1;
			int newCols = cols-1;

			IMatrix subMatrix = newInstance(newRows, newCols);
			for (int i = 0; i < rows; i++) {
				if (i == row) {
					continue;
				}
				for (int j = 0; j < cols; j++) {
					if (j == col) {
						continue;
					}
					subMatrix.set(i > row ? i-1 : i, j > col ? j-1 : j, get(i, j));
				}
			}
			
			return subMatrix;
		}
	}

	public IMatrix nCofactor() {
		int rows = getRowsCount();
		IMatrix cofMatrix = newInstance(rows, rows);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				cofMatrix.set(i, j, Math.pow(-1, i+j)*subMatrix(i, j, true).determinant());
			}
		}
		
		return cofMatrix;
	}

	@Override
	public IMatrix nInvert() {
		double determinant = determinant();

		return nCofactor().nTranspose(false).nScalarMultiply(1/determinant);
	}

	@Override
	public double[][] toArray() {
		int rows = getRowsCount();
		int cols = getColsCount();
		double[][] matrix = new double[rows][cols];
		iterateMatrix((i, j) -> matrix[i][j] = get(i, j));
		return matrix;
	}

	@Override
	public IVector toVector(boolean liveView) {
		if (liveView) {
			return new VectorMatrixView(this);
		} else {
			int rows = getRowsCount();
			int cols = getColsCount();
			boolean rowMatrix;

			rowMatrix = rows == 1;
			int dimension = rowMatrix ? cols : rows;
			
			double[] elements = new double[dimension];
			
			for (int i = 0; i < dimension; i++) {
				elements[i] = get(rowMatrix ? 0 : i, rowMatrix ? i : 0);
			}
			
			return new Vector(false, true, elements);
		}
	}

	@Override
	public IMatrix nScalarMultiply(double value) {
		return copy().scalarMultiply(value);
	}

	@Override
	public IMatrix scalarMultiply(double value) {
		iterateMatrix((row, col) -> set(row, col, get(row, col)*value));
		return this;
	}

	@Override
	public IMatrix makeIdentity() {
		iterateMatrix((row, col) -> {
			if (row == col) {
				set(row, col, 1);
			} else {
				set(row, col, 0);
			}
		});
		return this;
	}

	@Override
	public String toString() {
		return toString(3);
	}
	
	public String toString(int decimals) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0, rows = getRowsCount(); i < rows; i++) {
			builder.append("[");
			for (int j = 0, cols = getColsCount(); j < cols; j++) {
				builder.append(String.format(" %."+ decimals + "f", get(i,j)));
			}
			builder.append(" ]\n");
		}
		
		return builder.toString();
	}

	private interface Action {
		void doAction(int row, int col);
	}
	
	private void iterateMatrix(Action action) {
		int rowCount = this.getRowsCount();
		int colsCount = this.getColsCount();
		for (int row = 0; row < rowCount; row++) {
			for (int col = 0; col < colsCount; col++) {
				action.doAction(row, col);
			}
		}
	}

}

package hr.fer.zemris.linalg;

public class MatrixSubMatrixView extends AbstractMatrix {

	private IMatrix original;
	private int[] rowIndexes;
	private int[] colIndexes;
	
	public MatrixSubMatrixView(IMatrix original, int row, int col) {
		this(original,
			withoutIndex(defaultIndexes(original.getRowsCount()), row),
			withoutIndex(defaultIndexes(original.getColsCount()), col)
		);
	}	
	
	private MatrixSubMatrixView(IMatrix original, int[] rowIndexes, int[] colIndexes) {
		this.original = original;
		this.rowIndexes = rowIndexes;
		this.colIndexes = colIndexes;
	}
	
	@Override
	public int getRowsCount() {
		return rowIndexes.length;
	}

	@Override
	public int getColsCount() {
		return colIndexes.length;
	}

	@Override
	public double get(int row, int col) {
		return original.get(rowIndexes[row], colIndexes[col]);
	}

	@Override
	public IMatrix set(int row, int col, double value) {
		original.set(rowIndexes[row], colIndexes[col], value);
		return this;
	}

	@Override
	public IMatrix copy() {
		return new MatrixSubMatrixView(original.copy(), rowIndexes, colIndexes);
	}

	@Override
	public IMatrix newInstance(int rows, int cols) {
		return new MatrixSubMatrixView(original.newInstance(rows, cols), rowIndexes, colIndexes);
	}
	
	@Override
	public IMatrix subMatrix(int row, int col, boolean liveView) {
		int[] newRowIndexes = withoutIndex(rowIndexes, row);
		int[] newColIndexes = withoutIndex(colIndexes, col);
		
		if (liveView) {
			return new MatrixSubMatrixView(original, newRowIndexes, newColIndexes);
		} else {
			int newRows = newRowIndexes.length;
			int newCols = newColIndexes.length;
			double[][] elements = new double[newRows][newCols];
			for (int i = 0; i < newRows; i++) {
				for (int j = 0; j < newCols; j++) {
					elements[i][j] = original.get(newRowIndexes[i], newColIndexes[j]);
				}
			}
			return new Matrix(newRows, newCols, elements, true);
		}
	}
	
	private static int[] withoutIndex(int[] indexes, int index) {
		int size = indexes.length;

		int[] newIndexes = new int[size-1];
		
		for (int i = 0; i < size; i++) {
			if (i == index) {
				continue;
			}
			newIndexes[i > index ? i-1 : i] = indexes[i];
		}
		
		return newIndexes;
	}
	
	private static int[] defaultIndexes(int size) {
		int[] indexes = new int[size];
		for(int i = 0; i < size; i++) {
			indexes[i] = i;
		}
		return indexes;
	}
}

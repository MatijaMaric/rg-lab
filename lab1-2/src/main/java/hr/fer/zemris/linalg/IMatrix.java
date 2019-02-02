package hr.fer.zemris.linalg;


public interface IMatrix {
    int getRowsCount();
    int getColsCount();
    double get(int row, int col);
    IMatrix set(int row, int col, double value);
    IMatrix copy();
    IMatrix newInstance(int rows, int cols);
    IMatrix nTranspose(boolean liveView);
    IMatrix add(IMatrix other);
    IMatrix nAdd(IMatrix other);
    IMatrix sub(IMatrix other);
    IMatrix nSub(IMatrix other);
    IMatrix nMultiply(IMatrix other);
    double determinant();
    IMatrix subMatrix(int row, int col, boolean liveView);
    IMatrix nInvert();
    double[][] toArray();
    IVector toVector(boolean liveView);
    IMatrix scalarMultiply(double value);
    IMatrix nScalarMultiply(double value);
    IMatrix makeIdentity();
}

package hr.fer.zemris.linalg;

public interface IVector {
    double get(int index);
    IVector set(int index, double value) ;
    int getDimension();
    IVector copy();
    IVector copyPart(int n);
    IVector newInstance(int dimension);
    IVector add(IVector other);
    IVector nAdd(IVector other);
    IVector sub(IVector other);
    IVector nSub(IVector other);
    IVector scalarMultiply(double byValue);
    IVector nScalarMultiply(double byValue);
    double norm();
    IVector normalize();
    IVector nNormalize();
    double cosine(IVector other);
    double scalarProduct(IVector other);
    IVector nVectorProduct(IVector other);
    IVector nFromHomogeneous();
    IMatrix toRowMatrix(boolean liveView);
    IMatrix toColumnMatrix(boolean liveView);
    double[] toArray();
}

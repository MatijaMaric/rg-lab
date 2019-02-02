package hr.fer.zemris.graphics.common;

import hr.fer.zemris.linalg.IVector;

import java.util.Arrays;
import java.util.List;

public class Face3D {
    private Vertex3D vertex1;
    private Vertex3D vertex2;
    private Vertex3D vertex3;

    private int[] indexes;

    private IVector normal;

    public Face3D(Vertex3D vertex1, Vertex3D vertex2, Vertex3D vertex3) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    public List<Vertex3D> getVertices() {
        return Arrays.asList(vertex1, vertex2, vertex3);
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

    public int[] getIndexes() {
        return indexes;
    }

    public IVector getNormal() {
        if (normal == null) {
            normal = calculateNormal();
        }
        return normal;
    }

    private IVector calculateNormal() {
        IVector vector1 = vertex1.vector();
        IVector vector2 = vertex2.vector();
        IVector vector3 = vertex3.vector();

        return vector2.sub(vector1).nVectorProduct(vector3.sub(vector1)).normalize();
    }
}

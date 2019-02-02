package hr.fer.zemris.curve;

import hr.fer.zemris.graphics.common.GraphicsUtil;
import hr.fer.zemris.graphics.common.Vertex3D;
import hr.fer.zemris.linalg.IMatrix;
import hr.fer.zemris.linalg.IVector;
import hr.fer.zemris.linalg.Matrix;
import hr.fer.zemris.linalg.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CubicBSpline {
    public static float EPSILON = 0.01f;
    public static final IMatrix B_MATRIX;
    public static final IMatrix B_DASH_MATRIX;

    static {
        double[][] bMatrixData = new double[][] {{-1, 3, -3, 1}, {3, -6, 3, 0}, {-3, 0, 3, 0}, {1, 4, 1, 0}};
        IMatrix b = new Matrix(4, 4, bMatrixData, true);
        B_MATRIX = b.scalarMultiply(1.0/6);

        double[][] bDashMatrixData = new double[][] {{-1, 3, -3, 1}, {2, -4, 2, 0}, {-1, 0, 1, 0}};
        IMatrix bDash = new Matrix(3, 4, bDashMatrixData, true);
        B_DASH_MATRIX = bDash.scalarMultiply(1.0/2);
    }

    private List<Vertex3D> curveDefinitionPoints;

    public CubicBSpline(List<Vertex3D> curveDefinitionPoints) {
        if (curveDefinitionPoints == null || curveDefinitionPoints.size() < 4) {
            throw new IllegalArgumentException();
        }
        this.curveDefinitionPoints = curveDefinitionPoints;
    }

    public List<PointAndTangent> getPointsAndTangents() {
        List<PointAndTangent> pointsAndTangents = new ArrayList<>();
        for (int i = 1; i < curveDefinitionPoints.size() - 2; i++) {
            Vertex3D r0 = this.curveDefinitionPoints.get(i - 1);
            Vertex3D r1 = this.curveDefinitionPoints.get(i);
            Vertex3D r2 = this.curveDefinitionPoints.get(i + 1);
            Vertex3D r3 = this.curveDefinitionPoints.get(i + 2);
            IMatrix r = GraphicsUtil.fromVectorList(Arrays.asList(
                    r0.vector(), r1.vector(), r2.vector(), r3.vector())
            );

            for (float t = 0; t < 1; t += EPSILON) {
                IVector tVector = new Vector(new double[]{ Math.pow(t, 3), Math.pow(t, 2), t, 1});
                IVector point = tVector.toRowMatrix(true).nMultiply(B_MATRIX).nMultiply(r).toVector(true);

                IVector tTangentVector = new Vector(new double[]{ Math.pow(t, 2), t, 1});
                IVector tangent = tTangentVector.toRowMatrix(true).nMultiply(B_DASH_MATRIX).nMultiply(r).toVector(true);
                pointsAndTangents.add(new PointAndTangent(
                        new Vertex3D(point.get(0), point.get(1), point.get(2)),
                        tangent));
            }
        }
        return pointsAndTangents;
    }

    public static class PointAndTangent {
        public Vertex3D point;
        public IVector tangent;

        public PointAndTangent(Vertex3D point, IVector tangent) {
            this.point = point;
            this.tangent = tangent;
        }
    }
}

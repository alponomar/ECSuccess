//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Jama;

import Jama.CholeskyDecomposition;
import Jama.EigenvalueDecomposition;
import Jama.LUDecomposition;
import Jama.QRDecomposition;
import Jama.SingularValueDecomposition;
import Jama.util.Maths;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;

public class Matrix implements Cloneable, Serializable {
    private double[][] A;
    private int m;
    private int n;
    private static final long serialVersionUID = 1L;

    public Matrix(int var1, int var2) {
        this.m = var1;
        this.n = var2;
        this.A = new double[var1][var2];
    }

    public Matrix(int var1, int var2, double var3) {
        this.m = var1;
        this.n = var2;
        this.A = new double[var1][var2];

        for(int var5 = 0; var5 < var1; ++var5) {
            for(int var6 = 0; var6 < var2; ++var6) {
                this.A[var5][var6] = var3;
            }
        }

    }

    public Matrix(double[][] var1) {
        this.m = var1.length;
        this.n = var1[0].length;

        for(int var2 = 0; var2 < this.m; ++var2) {
            if(var1[var2].length != this.n) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }

        this.A = var1;
    }

    public Matrix(double[][] var1, int var2, int var3) {
        this.A = var1;
        this.m = var2;
        this.n = var3;
    }

    public Matrix(double[] var1, int var2) {
        this.m = var2;
        this.n = var2 != 0?var1.length / var2:0;
        if(var2 * this.n != var1.length) {
            throw new IllegalArgumentException("Array length must be a multiple of m.");
        } else {
            this.A = new double[var2][this.n];

            for(int var3 = 0; var3 < var2; ++var3) {
                for(int var4 = 0; var4 < this.n; ++var4) {
                    this.A[var3][var4] = var1[var3 + var4 * var2];
                }
            }

        }
    }

    public static Matrix constructWithCopy(double[][] var0) {
        int var1 = var0.length;
        int var2 = var0[0].length;
        Matrix var3 = new Matrix(var1, var2);
        double[][] var4 = var3.getArray();

        for(int var5 = 0; var5 < var1; ++var5) {
            if(var0[var5].length != var2) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }

            for(int var6 = 0; var6 < var2; ++var6) {
                var4[var5][var6] = var0[var5][var6];
            }
        }

        return var3;
    }

    public Matrix copy() {
        Matrix var1 = new Matrix(this.m, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                var2[var3][var4] = this.A[var3][var4];
            }
        }

        return var1;
    }

    public Object clone() {
        return this.copy();
    }

    public double[][] getArray() {
        return this.A;
    }

    public double[][] getArrayCopy() {
        double[][] var1 = new double[this.m][this.n];

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                var1[var2][var3] = this.A[var2][var3];
            }
        }

        return var1;
    }

    public double[] getColumnPackedCopy() {
        double[] var1 = new double[this.m * this.n];

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                var1[var2 + var3 * this.m] = this.A[var2][var3];
            }
        }

        return var1;
    }

    public double[] getRowPackedCopy() {
        double[] var1 = new double[this.m * this.n];

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                var1[var2 * this.n + var3] = this.A[var2][var3];
            }
        }

        return var1;
    }

    public int getRowDimension() {
        return this.m;
    }

    public int getColumnDimension() {
        return this.n;
    }

    public double get(int var1, int var2) {
        return this.A[var1][var2];
    }

    public Matrix getMatrix(int var1, int var2, int var3, int var4) {
        Matrix var5 = new Matrix(var2 - var1 + 1, var4 - var3 + 1);
        double[][] var6 = var5.getArray();

        try {
            for(int var7 = var1; var7 <= var2; ++var7) {
                for(int var8 = var3; var8 <= var4; ++var8) {
                    var6[var7 - var1][var8 - var3] = this.A[var7][var8];
                }
            }

            return var5;
        } catch (ArrayIndexOutOfBoundsException var9) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public Matrix getMatrix(int[] var1, int[] var2) {
        Matrix var3 = new Matrix(var1.length, var2.length);
        double[][] var4 = var3.getArray();

        try {
            for(int var5 = 0; var5 < var1.length; ++var5) {
                for(int var6 = 0; var6 < var2.length; ++var6) {
                    var4[var5][var6] = this.A[var1[var5]][var2[var6]];
                }
            }

            return var3;
        } catch (ArrayIndexOutOfBoundsException var7) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public Matrix getMatrix(int var1, int var2, int[] var3) {
        Matrix var4 = new Matrix(var2 - var1 + 1, var3.length);
        double[][] var5 = var4.getArray();

        try {
            for(int var6 = var1; var6 <= var2; ++var6) {
                for(int var7 = 0; var7 < var3.length; ++var7) {
                    var5[var6 - var1][var7] = this.A[var6][var3[var7]];
                }
            }

            return var4;
        } catch (ArrayIndexOutOfBoundsException var8) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public Matrix getMatrix(int[] var1, int var2, int var3) {
        Matrix var4 = new Matrix(var1.length, var3 - var2 + 1);
        double[][] var5 = var4.getArray();

        try {
            for(int var6 = 0; var6 < var1.length; ++var6) {
                for(int var7 = var2; var7 <= var3; ++var7) {
                    var5[var6][var7 - var2] = this.A[var1[var6]][var7];
                }
            }

            return var4;
        } catch (ArrayIndexOutOfBoundsException var8) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public void set(int var1, int var2, double var3) {
        this.A[var1][var2] = var3;
    }

    public void setMatrix(int var1, int var2, int var3, int var4, Matrix var5) {
        try {
            for(int var6 = var1; var6 <= var2; ++var6) {
                for(int var7 = var3; var7 <= var4; ++var7) {
                    this.A[var6][var7] = var5.get(var6 - var1, var7 - var3);
                }
            }

        } catch (ArrayIndexOutOfBoundsException var8) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public void setMatrix(int[] var1, int[] var2, Matrix var3) {
        try {
            for(int var4 = 0; var4 < var1.length; ++var4) {
                for(int var5 = 0; var5 < var2.length; ++var5) {
                    this.A[var1[var4]][var2[var5]] = var3.get(var4, var5);
                }
            }

        } catch (ArrayIndexOutOfBoundsException var6) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public void setMatrix(int[] var1, int var2, int var3, Matrix var4) {
        try {
            for(int var5 = 0; var5 < var1.length; ++var5) {
                for(int var6 = var2; var6 <= var3; ++var6) {
                    this.A[var1[var5]][var6] = var4.get(var5, var6 - var2);
                }
            }

        } catch (ArrayIndexOutOfBoundsException var7) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public void setMatrix(int var1, int var2, int[] var3, Matrix var4) {
        try {
            for(int var5 = var1; var5 <= var2; ++var5) {
                for(int var6 = 0; var6 < var3.length; ++var6) {
                    this.A[var5][var3[var6]] = var4.get(var5 - var1, var6);
                }
            }

        } catch (ArrayIndexOutOfBoundsException var7) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
    }

    public Matrix transpose() {
        Matrix var1 = new Matrix(this.n, this.m);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                var2[var4][var3] = this.A[var3][var4];
            }
        }

        return var1;
    }

    public double norm1() {
        double var1 = 0.0D;

        for(int var3 = 0; var3 < this.n; ++var3) {
            double var4 = 0.0D;

            for(int var6 = 0; var6 < this.m; ++var6) {
                var4 += Math.abs(this.A[var6][var3]);
            }

            var1 = Math.max(var1, var4);
        }

        return var1;
    }

    public double norm2() {
        return (new SingularValueDecomposition(this)).norm2();
    }

    public double normInf() {
        double var1 = 0.0D;

        for(int var3 = 0; var3 < this.m; ++var3) {
            double var4 = 0.0D;

            for(int var6 = 0; var6 < this.n; ++var6) {
                var4 += Math.abs(this.A[var3][var6]);
            }

            var1 = Math.max(var1, var4);
        }

        return var1;
    }

    public double normF() {
        double var1 = 0.0D;

        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                var1 = Maths.hypot(var1, this.A[var3][var4]);
            }
        }

        return var1;
    }

    public Matrix uminus() {
        Matrix var1 = new Matrix(this.m, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                var2[var3][var4] = -this.A[var3][var4];
            }
        }

        return var1;
    }

    public Matrix plus(Matrix var1) {
        this.checkMatrixDimensions(var1);
        Matrix var2 = new Matrix(this.m, this.n);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < this.m; ++var4) {
            for(int var5 = 0; var5 < this.n; ++var5) {
                var3[var4][var5] = this.A[var4][var5] + var1.A[var4][var5];
            }
        }

        return var2;
    }

    public Matrix plusEquals(Matrix var1) {
        this.checkMatrixDimensions(var1);

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                this.A[var2][var3] += var1.A[var2][var3];
            }
        }

        return this;
    }

    public Matrix minus(Matrix var1) {
        this.checkMatrixDimensions(var1);
        Matrix var2 = new Matrix(this.m, this.n);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < this.m; ++var4) {
            for(int var5 = 0; var5 < this.n; ++var5) {
                var3[var4][var5] = this.A[var4][var5] - var1.A[var4][var5];
            }
        }

        return var2;
    }

    public Matrix minusEquals(Matrix var1) {
        this.checkMatrixDimensions(var1);

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                this.A[var2][var3] -= var1.A[var2][var3];
            }
        }

        return this;
    }

    public Matrix arrayTimes(Matrix var1) {
        this.checkMatrixDimensions(var1);
        Matrix var2 = new Matrix(this.m, this.n);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < this.m; ++var4) {
            for(int var5 = 0; var5 < this.n; ++var5) {
                var3[var4][var5] = this.A[var4][var5] * var1.A[var4][var5];
            }
        }

        return var2;
    }

    public Matrix arrayTimesEquals(Matrix var1) {
        this.checkMatrixDimensions(var1);

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                this.A[var2][var3] *= var1.A[var2][var3];
            }
        }

        return this;
    }

    public Matrix arrayRightDivide(Matrix var1) {
        this.checkMatrixDimensions(var1);
        Matrix var2 = new Matrix(this.m, this.n);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < this.m; ++var4) {
            for(int var5 = 0; var5 < this.n; ++var5) {
                var3[var4][var5] = this.A[var4][var5] / var1.A[var4][var5];
            }
        }

        return var2;
    }

    public Matrix arrayRightDivideEquals(Matrix var1) {
        this.checkMatrixDimensions(var1);

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                this.A[var2][var3] /= var1.A[var2][var3];
            }
        }

        return this;
    }

    public Matrix arrayLeftDivide(Matrix var1) {
        this.checkMatrixDimensions(var1);
        Matrix var2 = new Matrix(this.m, this.n);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < this.m; ++var4) {
            for(int var5 = 0; var5 < this.n; ++var5) {
                var3[var4][var5] = var1.A[var4][var5] / this.A[var4][var5];
            }
        }

        return var2;
    }

    public Matrix arrayLeftDivideEquals(Matrix var1) {
        this.checkMatrixDimensions(var1);

        for(int var2 = 0; var2 < this.m; ++var2) {
            for(int var3 = 0; var3 < this.n; ++var3) {
                this.A[var2][var3] = var1.A[var2][var3] / this.A[var2][var3];
            }
        }

        return this;
    }

    public Matrix times(double var1) {
        Matrix var3 = new Matrix(this.m, this.n);
        double[][] var4 = var3.getArray();

        for(int var5 = 0; var5 < this.m; ++var5) {
            for(int var6 = 0; var6 < this.n; ++var6) {
                var4[var5][var6] = var1 * this.A[var5][var6];
            }
        }

        return var3;
    }

    public Matrix timesEquals(double var1) {
        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                this.A[var3][var4] = var1 * this.A[var3][var4];
            }
        }

        return this;
    }

    public Matrix times(Matrix var1) {
        if(var1.m != this.n) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        } else {
            Matrix var2 = new Matrix(this.m, var1.n);
            double[][] var3 = var2.getArray();
            double[] var4 = new double[this.n];

            for(int var5 = 0; var5 < var1.n; ++var5) {
                int var6;
                for(var6 = 0; var6 < this.n; ++var6) {
                    var4[var6] = var1.A[var6][var5];
                }

                for(var6 = 0; var6 < this.m; ++var6) {
                    double[] var7 = this.A[var6];
                    double var8 = 0.0D;

                    for(int var10 = 0; var10 < this.n; ++var10) {
                        var8 += var7[var10] * var4[var10];
                    }

                    var3[var6][var5] = var8;
                }
            }

            return var2;
        }
    }

    public LUDecomposition lu() {
        return new LUDecomposition(this);
    }

    public QRDecomposition qr() {
        return new QRDecomposition(this);
    }

    public CholeskyDecomposition chol() {
        return new CholeskyDecomposition(this);
    }

    public SingularValueDecomposition svd() {
        return new SingularValueDecomposition(this);
    }

    public EigenvalueDecomposition eig() {
        return new EigenvalueDecomposition(this);
    }

    public Matrix solve(Matrix var1) {
        return this.m == this.n?(new LUDecomposition(this)).solve(var1):(new QRDecomposition(this)).solve(var1);
    }

    public Matrix solveTranspose(Matrix var1) {
        return this.transpose().solve(var1.transpose());
    }

    public Matrix inverse() {
        return this.solve(identity(this.m, this.m));
    }

    public double det() {
        return (new LUDecomposition(this)).det();
    }

    public int rank() {
        return (new SingularValueDecomposition(this)).rank();
    }

    public double cond() {
        return (new SingularValueDecomposition(this)).cond();
    }

    public double trace() {
        double var1 = 0.0D;

        for(int var3 = 0; var3 < Math.min(this.m, this.n); ++var3) {
            var1 += this.A[var3][var3];
        }

        return var1;
    }

    public static Matrix random(int var0, int var1) {
        Matrix var2 = new Matrix(var0, var1);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < var0; ++var4) {
            for(int var5 = 0; var5 < var1; ++var5) {
                var3[var4][var5] = Math.random();
            }
        }

        return var2;
    }

    public static Matrix identity(int var0, int var1) {
        Matrix var2 = new Matrix(var0, var1);
        double[][] var3 = var2.getArray();

        for(int var4 = 0; var4 < var0; ++var4) {
            for(int var5 = 0; var5 < var1; ++var5) {
                var3[var4][var5] = var4 == var5?1.0D:0.0D;
            }
        }

        return var2;
    }

    public void print(int var1, int var2) {
        this.print(new PrintWriter(System.out, true), var1, var2);
    }

    public void print(PrintWriter var1, int var2, int var3) {
        DecimalFormat var4 = new DecimalFormat();
        var4.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        var4.setMinimumIntegerDigits(1);
        var4.setMaximumFractionDigits(var3);
        var4.setMinimumFractionDigits(var3);
        var4.setGroupingUsed(false);
        this.print(var1, var4, var2 + 2);
    }

    public void print(NumberFormat var1, int var2) {
        this.print(new PrintWriter(System.out, true), var1, var2);
    }

    public void print(PrintWriter var1, NumberFormat var2, int var3) {
        var1.println();

        for(int var4 = 0; var4 < this.m; ++var4) {
            for(int var5 = 0; var5 < this.n; ++var5) {
                String var6 = var2.format(this.A[var4][var5]);
                int var7 = Math.max(1, var3 - var6.length());

                for(int var8 = 0; var8 < var7; ++var8) {
                    var1.print(' ');
                }

                var1.print(var6);
            }

            var1.println();
        }

        var1.println();
    }

    public static Matrix read(BufferedReader var0) throws IOException {
        StreamTokenizer var1 = new StreamTokenizer(var0);
        var1.resetSyntax();
        var1.wordChars(0, 255);
        var1.whitespaceChars(0, 32);
        var1.eolIsSignificant(true);
        Vector var2 = new Vector();

        while(var1.nextToken() == 10) {
            ;
        }

        if(var1.ttype == -1) {
            throw new IOException("Unexpected EOF on matrix read.");
        } else {
            do {
                var2.addElement(Double.valueOf(var1.sval));
            } while(var1.nextToken() == -3);

            int var3 = var2.size();
            double[] var4 = new double[var3];

            for(int var5 = 0; var5 < var3; ++var5) {
                var4[var5] = ((Double)var2.elementAt(var5)).doubleValue();
            }

            Vector var8 = new Vector();
            var8.addElement(var4);

            int var6;
            do {
                if(var1.nextToken() != -3) {
                    var6 = var8.size();
                    double[][] var7 = new double[var6][];
                    var8.copyInto(var7);
                    return new Matrix(var7);
                }

                var8.addElement(var4 = new double[var3]);
                var6 = 0;

                do {
                    if(var6 >= var3) {
                        throw new IOException("Row " + var8.size() + " is too long.");
                    }

                    var4[var6++] = Double.valueOf(var1.sval).doubleValue();
                } while(var1.nextToken() == -3);
            } while(var6 >= var3);

            throw new IOException("Row " + var8.size() + " is too short.");
        }
    }

    private void checkMatrixDimensions(Matrix var1) {
        if(var1.m != this.m || var1.n != this.n) {
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        }
    }
}

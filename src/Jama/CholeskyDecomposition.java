//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Jama;

import Jama.Matrix;
import java.io.Serializable;

public class CholeskyDecomposition implements Serializable {
    private double[][] L;
    private int n;
    private boolean isspd;
    private static final long serialVersionUID = 1L;

    public CholeskyDecomposition(Matrix var1) {
        double[][] var2 = var1.getArray();
        this.n = var1.getRowDimension();
        this.L = new double[this.n][this.n];
        this.isspd = var1.getColumnDimension() == this.n;

        for(int var3 = 0; var3 < this.n; ++var3) {
            double[] var4 = this.L[var3];
            double var5 = 0.0D;

            int var7;
            for(var7 = 0; var7 < var3; ++var7) {
                double[] var8 = this.L[var7];
                double var9 = 0.0D;

                for(int var11 = 0; var11 < var7; ++var11) {
                    var9 += var8[var11] * var4[var11];
                }

                var4[var7] = var9 = (var2[var3][var7] - var9) / this.L[var7][var7];
                var5 += var9 * var9;
                this.isspd &= var2[var7][var3] == var2[var3][var7];
            }

            var5 = var2[var3][var3] - var5;
            this.isspd &= var5 > 0.0D;
            this.L[var3][var3] = Math.sqrt(Math.max(var5, 0.0D));

            for(var7 = var3 + 1; var7 < this.n; ++var7) {
                this.L[var3][var7] = 0.0D;
            }
        }

    }

    public boolean isSPD() {
        return this.isspd;
    }

    public Matrix getL() {
        return new Matrix(this.L, this.n, this.n);
    }

    public Matrix solve(Matrix var1) {
        if(var1.getRowDimension() != this.n) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if(!this.isspd) {
            throw new RuntimeException("Matrix is not symmetric positive definite.");
        } else {
            double[][] var2 = var1.getArrayCopy();
            int var3 = var1.getColumnDimension();

            int var4;
            int var5;
            int var6;
            for(var4 = 0; var4 < this.n; ++var4) {
                for(var5 = 0; var5 < var3; ++var5) {
                    for(var6 = 0; var6 < var4; ++var6) {
                        var2[var4][var5] -= var2[var6][var5] * this.L[var4][var6];
                    }

                    var2[var4][var5] /= this.L[var4][var4];
                }
            }

            for(var4 = this.n - 1; var4 >= 0; --var4) {
                for(var5 = 0; var5 < var3; ++var5) {
                    for(var6 = var4 + 1; var6 < this.n; ++var6) {
                        var2[var4][var5] -= var2[var6][var5] * this.L[var6][var4];
                    }

                    var2[var4][var5] /= this.L[var4][var4];
                }
            }

            return new Matrix(var2, this.n, var3);
        }
    }
}

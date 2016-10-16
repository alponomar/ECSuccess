//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Jama;

import Jama.Matrix;
import java.io.Serializable;

public class LUDecomposition implements Serializable {
    private double[][] LU;
    private int m;
    private int n;
    private int pivsign;
    private int[] piv;
    private static final long serialVersionUID = 1L;

    public LUDecomposition(Matrix var1) {
        this.LU = var1.getArrayCopy();
        this.m = var1.getRowDimension();
        this.n = var1.getColumnDimension();
        this.piv = new int[this.m];

        for(int var2 = 0; var2 < this.m; this.piv[var2] = var2++) {
            ;
        }

        this.pivsign = 1;
        double[] var3 = new double[this.m];

        for(int var4 = 0; var4 < this.n; ++var4) {
            int var5;
            for(var5 = 0; var5 < this.m; ++var5) {
                var3[var5] = this.LU[var5][var4];
            }

            int var6;
            double var7;
            for(var5 = 0; var5 < this.m; ++var5) {
                double[] var10 = this.LU[var5];
                var6 = Math.min(var5, var4);
                var7 = 0.0D;

                for(int var9 = 0; var9 < var6; ++var9) {
                    var7 += var10[var9] * var3[var9];
                }

                var10[var4] = var3[var5] -= var7;
            }

            var5 = var4;

            for(var6 = var4 + 1; var6 < this.m; ++var6) {
                if(Math.abs(var3[var6]) > Math.abs(var3[var5])) {
                    var5 = var6;
                }
            }

            if(var5 != var4) {
                for(var6 = 0; var6 < this.n; ++var6) {
                    var7 = this.LU[var5][var6];
                    this.LU[var5][var6] = this.LU[var4][var6];
                    this.LU[var4][var6] = var7;
                }

                var6 = this.piv[var5];
                this.piv[var5] = this.piv[var4];
                this.piv[var4] = var6;
                this.pivsign = -this.pivsign;
            }

            if(var4 < this.m & this.LU[var4][var4] != 0.0D) {
                for(var6 = var4 + 1; var6 < this.m; ++var6) {
                    this.LU[var6][var4] /= this.LU[var4][var4];
                }
            }
        }

    }

    public boolean isNonsingular() {
        for(int var1 = 0; var1 < this.n; ++var1) {
            if(this.LU[var1][var1] == 0.0D) {
                return false;
            }
        }

        return true;
    }

    public Matrix getL() {
        Matrix var1 = new Matrix(this.m, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                if(var3 > var4) {
                    var2[var3][var4] = this.LU[var3][var4];
                } else if(var3 == var4) {
                    var2[var3][var4] = 1.0D;
                } else {
                    var2[var3][var4] = 0.0D;
                }
            }
        }

        return var1;
    }

    public Matrix getU() {
        Matrix var1 = new Matrix(this.n, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.n; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                if(var3 <= var4) {
                    var2[var3][var4] = this.LU[var3][var4];
                } else {
                    var2[var3][var4] = 0.0D;
                }
            }
        }

        return var1;
    }

    public int[] getPivot() {
        int[] var1 = new int[this.m];

        for(int var2 = 0; var2 < this.m; ++var2) {
            var1[var2] = this.piv[var2];
        }

        return var1;
    }

    public double[] getDoublePivot() {
        double[] var1 = new double[this.m];

        for(int var2 = 0; var2 < this.m; ++var2) {
            var1[var2] = (double)this.piv[var2];
        }

        return var1;
    }

    public double det() {
        if(this.m != this.n) {
            throw new IllegalArgumentException("Matrix must be square.");
        } else {
            double var1 = (double)this.pivsign;

            for(int var3 = 0; var3 < this.n; ++var3) {
                var1 *= this.LU[var3][var3];
            }

            return var1;
        }
    }

    public Matrix solve(Matrix var1) {
        if(var1.getRowDimension() != this.m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if(!this.isNonsingular()) {
            throw new RuntimeException("Matrix is singular.");
        } else {
            int var2 = var1.getColumnDimension();
            Matrix var3 = var1.getMatrix(this.piv, 0, var2 - 1);
            double[][] var4 = var3.getArray();

            int var5;
            int var6;
            int var7;
            for(var5 = 0; var5 < this.n; ++var5) {
                for(var6 = var5 + 1; var6 < this.n; ++var6) {
                    for(var7 = 0; var7 < var2; ++var7) {
                        var4[var6][var7] -= var4[var5][var7] * this.LU[var6][var5];
                    }
                }
            }

            for(var5 = this.n - 1; var5 >= 0; --var5) {
                for(var6 = 0; var6 < var2; ++var6) {
                    var4[var5][var6] /= this.LU[var5][var5];
                }

                for(var6 = 0; var6 < var5; ++var6) {
                    for(var7 = 0; var7 < var2; ++var7) {
                        var4[var6][var7] -= var4[var5][var7] * this.LU[var6][var5];
                    }
                }
            }

            return var3;
        }
    }
}

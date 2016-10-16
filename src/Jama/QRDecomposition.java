//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Jama;

import Jama.Matrix;
import Jama.util.Maths;
import java.io.Serializable;

public class QRDecomposition implements Serializable {
    private double[][] QR;
    private int m;
    private int n;
    private double[] Rdiag;
    private static final long serialVersionUID = 1L;

    public QRDecomposition(Matrix var1) {
        this.QR = var1.getArrayCopy();
        this.m = var1.getRowDimension();
        this.n = var1.getColumnDimension();
        this.Rdiag = new double[this.n];

        for(int var2 = 0; var2 < this.n; ++var2) {
            double var3 = 0.0D;

            int var5;
            for(var5 = var2; var5 < this.m; ++var5) {
                var3 = Maths.hypot(var3, this.QR[var5][var2]);
            }

            if(var3 != 0.0D) {
                if(this.QR[var2][var2] < 0.0D) {
                    var3 = -var3;
                }

                for(var5 = var2; var5 < this.m; ++var5) {
                    this.QR[var5][var2] /= var3;
                }

                ++this.QR[var2][var2];

                for(var5 = var2 + 1; var5 < this.n; ++var5) {
                    double var6 = 0.0D;

                    int var8;
                    for(var8 = var2; var8 < this.m; ++var8) {
                        var6 += this.QR[var8][var2] * this.QR[var8][var5];
                    }

                    var6 = -var6 / this.QR[var2][var2];

                    for(var8 = var2; var8 < this.m; ++var8) {
                        this.QR[var8][var5] += var6 * this.QR[var8][var2];
                    }
                }
            }

            this.Rdiag[var2] = -var3;
        }

    }

    public boolean isFullRank() {
        for(int var1 = 0; var1 < this.n; ++var1) {
            if(this.Rdiag[var1] == 0.0D) {
                return false;
            }
        }

        return true;
    }

    public Matrix getH() {
        Matrix var1 = new Matrix(this.m, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.m; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                if(var3 >= var4) {
                    var2[var3][var4] = this.QR[var3][var4];
                } else {
                    var2[var3][var4] = 0.0D;
                }
            }
        }

        return var1;
    }

    public Matrix getR() {
        Matrix var1 = new Matrix(this.n, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.n; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                if(var3 < var4) {
                    var2[var3][var4] = this.QR[var3][var4];
                } else if(var3 == var4) {
                    var2[var3][var4] = this.Rdiag[var3];
                } else {
                    var2[var3][var4] = 0.0D;
                }
            }
        }

        return var1;
    }

    public Matrix getQ() {
        Matrix var1 = new Matrix(this.m, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = this.n - 1; var3 >= 0; --var3) {
            int var4;
            for(var4 = 0; var4 < this.m; ++var4) {
                var2[var4][var3] = 0.0D;
            }

            var2[var3][var3] = 1.0D;

            for(var4 = var3; var4 < this.n; ++var4) {
                if(this.QR[var3][var3] != 0.0D) {
                    double var5 = 0.0D;

                    int var7;
                    for(var7 = var3; var7 < this.m; ++var7) {
                        var5 += this.QR[var7][var3] * var2[var7][var4];
                    }

                    var5 = -var5 / this.QR[var3][var3];

                    for(var7 = var3; var7 < this.m; ++var7) {
                        var2[var7][var4] += var5 * this.QR[var7][var3];
                    }
                }
            }
        }

        return var1;
    }

    public Matrix solve(Matrix var1) {
        if(var1.getRowDimension() != this.m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if(!this.isFullRank()) {
            throw new RuntimeException("Matrix is rank deficient.");
        } else {
            int var2 = var1.getColumnDimension();
            double[][] var3 = var1.getArrayCopy();

            int var4;
            int var5;
            for(var4 = 0; var4 < this.n; ++var4) {
                for(var5 = 0; var5 < var2; ++var5) {
                    double var6 = 0.0D;

                    int var8;
                    for(var8 = var4; var8 < this.m; ++var8) {
                        var6 += this.QR[var8][var4] * var3[var8][var5];
                    }

                    var6 = -var6 / this.QR[var4][var4];

                    for(var8 = var4; var8 < this.m; ++var8) {
                        var3[var8][var5] += var6 * this.QR[var8][var4];
                    }
                }
            }

            for(var4 = this.n - 1; var4 >= 0; --var4) {
                for(var5 = 0; var5 < var2; ++var5) {
                    var3[var4][var5] /= this.Rdiag[var4];
                }

                for(var5 = 0; var5 < var4; ++var5) {
                    for(int var9 = 0; var9 < var2; ++var9) {
                        var3[var5][var9] -= var3[var4][var9] * this.QR[var5][var4];
                    }
                }
            }

            return (new Matrix(var3, this.n, var2)).getMatrix(0, this.n - 1, 0, var2 - 1);
        }
    }
}

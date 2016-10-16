//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Jama;

import Jama.Matrix;
import Jama.util.Maths;
import java.io.Serializable;

public class EigenvalueDecomposition implements Serializable {
    private int n;
    private boolean issymmetric;
    private double[] d;
    private double[] e;
    private double[][] V;
    private double[][] H;
    private double[] ort;
    private transient double cdivr;
    private transient double cdivi;
    private static final long serialVersionUID = 1L;

    private void tred2() {
        int var1;
        for(var1 = 0; var1 < this.n; ++var1) {
            this.d[var1] = this.V[this.n - 1][var1];
        }

        double var2;
        for(var1 = this.n - 1; var1 > 0; --var1) {
            var2 = 0.0D;
            double var4 = 0.0D;

            int var6;
            for(var6 = 0; var6 < var1; ++var6) {
                var2 += Math.abs(this.d[var6]);
            }

            if(var2 == 0.0D) {
                this.e[var1] = this.d[var1 - 1];

                for(var6 = 0; var6 < var1; ++var6) {
                    this.d[var6] = this.V[var1 - 1][var6];
                    this.V[var1][var6] = 0.0D;
                    this.V[var6][var1] = 0.0D;
                }
            } else {
                for(var6 = 0; var6 < var1; ++var6) {
                    this.d[var6] /= var2;
                    var4 += this.d[var6] * this.d[var6];
                }

                double var15 = this.d[var1 - 1];
                double var8 = Math.sqrt(var4);
                if(var15 > 0.0D) {
                    var8 = -var8;
                }

                this.e[var1] = var2 * var8;
                var4 -= var15 * var8;
                this.d[var1 - 1] = var15 - var8;

                int var10;
                for(var10 = 0; var10 < var1; ++var10) {
                    this.e[var10] = 0.0D;
                }

                var10 = 0;

                label156:
                while(true) {
                    if(var10 >= var1) {
                        var15 = 0.0D;

                        for(var10 = 0; var10 < var1; ++var10) {
                            this.e[var10] /= var4;
                            var15 += this.e[var10] * this.d[var10];
                        }

                        double var16 = var15 / (var4 + var4);

                        int var12;
                        for(var12 = 0; var12 < var1; ++var12) {
                            this.e[var12] -= var16 * this.d[var12];
                        }

                        var12 = 0;

                        while(true) {
                            if(var12 >= var1) {
                                break label156;
                            }

                            var15 = this.d[var12];
                            var8 = this.e[var12];

                            for(int var13 = var12; var13 <= var1 - 1; ++var13) {
                                this.V[var13][var12] -= var15 * this.e[var13] + var8 * this.d[var13];
                            }

                            this.d[var12] = this.V[var1 - 1][var12];
                            this.V[var1][var12] = 0.0D;
                            ++var12;
                        }
                    }

                    var15 = this.d[var10];
                    this.V[var10][var1] = var15;
                    var8 = this.e[var10] + this.V[var10][var10] * var15;

                    for(int var11 = var10 + 1; var11 <= var1 - 1; ++var11) {
                        var8 += this.V[var11][var10] * this.d[var11];
                        this.e[var11] += this.V[var11][var10] * var15;
                    }

                    this.e[var10] = var8;
                    ++var10;
                }
            }

            this.d[var1] = var4;
        }

        for(var1 = 0; var1 < this.n - 1; ++var1) {
            this.V[this.n - 1][var1] = this.V[var1][var1];
            this.V[var1][var1] = 1.0D;
            var2 = this.d[var1 + 1];
            int var14;
            if(var2 != 0.0D) {
                for(var14 = 0; var14 <= var1; ++var14) {
                    this.d[var14] = this.V[var14][var1 + 1] / var2;
                }

                for(var14 = 0; var14 <= var1; ++var14) {
                    double var5 = 0.0D;

                    int var7;
                    for(var7 = 0; var7 <= var1; ++var7) {
                        var5 += this.V[var7][var1 + 1] * this.V[var7][var14];
                    }

                    for(var7 = 0; var7 <= var1; ++var7) {
                        this.V[var7][var14] -= var5 * this.d[var7];
                    }
                }
            }

            for(var14 = 0; var14 <= var1; ++var14) {
                this.V[var14][var1 + 1] = 0.0D;
            }
        }

        for(var1 = 0; var1 < this.n; ++var1) {
            this.d[var1] = this.V[this.n - 1][var1];
            this.V[this.n - 1][var1] = 0.0D;
        }

        this.V[this.n - 1][this.n - 1] = 1.0D;
        this.e[0] = 0.0D;
    }

    private void tql2() {
        for(int var1 = 1; var1 < this.n; ++var1) {
            this.e[var1 - 1] = this.e[var1];
        }

        this.e[this.n - 1] = 0.0D;
        double var34 = 0.0D;
        double var3 = 0.0D;
        double var5 = Math.pow(2.0D, -52.0D);

        int var7;
        int var8;
        for(var7 = 0; var7 < this.n; ++var7) {
            var3 = Math.max(var3, Math.abs(this.d[var7]) + Math.abs(this.e[var7]));

            for(var8 = var7; var8 < this.n && Math.abs(this.e[var8]) > var5 * var3; ++var8) {
                ;
            }

            if(var8 > var7) {
                int var9 = 0;

                do {
                    ++var9;
                    double var10 = this.d[var7];
                    double var12 = (this.d[var7 + 1] - var10) / (2.0D * this.e[var7]);
                    double var14 = Maths.hypot(var12, 1.0D);
                    if(var12 < 0.0D) {
                        var14 = -var14;
                    }

                    this.d[var7] = this.e[var7] / (var12 + var14);
                    this.d[var7 + 1] = this.e[var7] * (var12 + var14);
                    double var16 = this.d[var7 + 1];
                    double var18 = var10 - this.d[var7];

                    for(int var20 = var7 + 2; var20 < this.n; ++var20) {
                        this.d[var20] -= var18;
                    }

                    var34 += var18;
                    var12 = this.d[var8];
                    double var36 = 1.0D;
                    double var22 = var36;
                    double var24 = var36;
                    double var26 = this.e[var7 + 1];
                    double var28 = 0.0D;
                    double var30 = 0.0D;

                    for(int var32 = var8 - 1; var32 >= var7; --var32) {
                        var24 = var22;
                        var22 = var36;
                        var30 = var28;
                        var10 = var36 * this.e[var32];
                        var18 = var36 * var12;
                        var14 = Maths.hypot(var12, this.e[var32]);
                        this.e[var32 + 1] = var28 * var14;
                        var28 = this.e[var32] / var14;
                        var36 = var12 / var14;
                        var12 = var36 * this.d[var32] - var28 * var10;
                        this.d[var32 + 1] = var18 + var28 * (var36 * var10 + var28 * this.d[var32]);

                        for(int var33 = 0; var33 < this.n; ++var33) {
                            var18 = this.V[var33][var32 + 1];
                            this.V[var33][var32 + 1] = var28 * this.V[var33][var32] + var36 * var18;
                            this.V[var33][var32] = var36 * this.V[var33][var32] - var28 * var18;
                        }
                    }

                    var12 = -var28 * var30 * var24 * var26 * this.e[var7] / var16;
                    this.e[var7] = var28 * var12;
                    this.d[var7] = var36 * var12;
                } while(Math.abs(this.e[var7]) > var5 * var3);
            }

            this.d[var7] += var34;
            this.e[var7] = 0.0D;
        }

        for(var7 = 0; var7 < this.n - 1; ++var7) {
            var8 = var7;
            double var35 = this.d[var7];

            int var11;
            for(var11 = var7 + 1; var11 < this.n; ++var11) {
                if(this.d[var11] < var35) {
                    var8 = var11;
                    var35 = this.d[var11];
                }
            }

            if(var8 != var7) {
                this.d[var8] = this.d[var7];
                this.d[var7] = var35;

                for(var11 = 0; var11 < this.n; ++var11) {
                    var35 = this.V[var11][var7];
                    this.V[var11][var7] = this.V[var11][var8];
                    this.V[var11][var8] = var35;
                }
            }
        }

    }

    private void orthes() {
        byte var1 = 0;
        int var2 = this.n - 1;

        int var3;
        for(var3 = var1 + 1; var3 <= var2 - 1; ++var3) {
            double var4 = 0.0D;

            for(int var6 = var3; var6 <= var2; ++var6) {
                var4 += Math.abs(this.H[var6][var3 - 1]);
            }

            if(var4 != 0.0D) {
                double var15 = 0.0D;

                for(int var8 = var2; var8 >= var3; --var8) {
                    this.ort[var8] = this.H[var8][var3 - 1] / var4;
                    var15 += this.ort[var8] * this.ort[var8];
                }

                double var16 = Math.sqrt(var15);
                if(this.ort[var3] > 0.0D) {
                    var16 = -var16;
                }

                var15 -= this.ort[var3] * var16;
                this.ort[var3] -= var16;

                int var10;
                double var11;
                int var13;
                for(var10 = var3; var10 < this.n; ++var10) {
                    var11 = 0.0D;

                    for(var13 = var2; var13 >= var3; --var13) {
                        var11 += this.ort[var13] * this.H[var13][var10];
                    }

                    var11 /= var15;

                    for(var13 = var3; var13 <= var2; ++var13) {
                        this.H[var13][var10] -= var11 * this.ort[var13];
                    }
                }

                for(var10 = 0; var10 <= var2; ++var10) {
                    var11 = 0.0D;

                    for(var13 = var2; var13 >= var3; --var13) {
                        var11 += this.ort[var13] * this.H[var10][var13];
                    }

                    var11 /= var15;

                    for(var13 = var3; var13 <= var2; ++var13) {
                        this.H[var10][var13] -= var11 * this.ort[var13];
                    }
                }

                this.ort[var3] = var4 * this.ort[var3];
                this.H[var3][var3 - 1] = var4 * var16;
            }
        }

        int var14;
        for(var3 = 0; var3 < this.n; ++var3) {
            for(var14 = 0; var14 < this.n; ++var14) {
                this.V[var3][var14] = var3 == var14?1.0D:0.0D;
            }
        }

        for(var3 = var2 - 1; var3 >= var1 + 1; --var3) {
            if(this.H[var3][var3 - 1] != 0.0D) {
                for(var14 = var3 + 1; var14 <= var2; ++var14) {
                    this.ort[var14] = this.H[var14][var3 - 1];
                }

                for(var14 = var3; var14 <= var2; ++var14) {
                    double var5 = 0.0D;

                    int var7;
                    for(var7 = var3; var7 <= var2; ++var7) {
                        var5 += this.ort[var7] * this.V[var7][var14];
                    }

                    var5 = var5 / this.ort[var3] / this.H[var3][var3 - 1];

                    for(var7 = var3; var7 <= var2; ++var7) {
                        this.V[var7][var14] += var5 * this.ort[var7];
                    }
                }
            }
        }

    }

    private void cdiv(double var1, double var3, double var5, double var7) {
        double var9;
        double var11;
        if(Math.abs(var5) > Math.abs(var7)) {
            var9 = var7 / var5;
            var11 = var5 + var9 * var7;
            this.cdivr = (var1 + var9 * var3) / var11;
            this.cdivi = (var3 - var9 * var1) / var11;
        } else {
            var9 = var5 / var7;
            var11 = var7 + var9 * var5;
            this.cdivr = (var9 * var1 + var3) / var11;
            this.cdivi = (var9 * var3 - var1) / var11;
        }

    }

    private void hqr2() {
        int var1 = this.n;
        int var2 = var1 - 1;
        byte var3 = 0;
        int var4 = var1 - 1;
        double var5 = Math.pow(2.0D, -52.0D);
        double var7 = 0.0D;
        double var9 = 0.0D;
        double var11 = 0.0D;
        double var13 = 0.0D;
        double var15 = 0.0D;
        double var17 = 0.0D;
        double var27 = 0.0D;

        int var29;
        int var30;
        for(var29 = 0; var29 < var1; ++var29) {
            if(var29 < var3 | var29 > var4) {
                this.d[var29] = this.H[var29][var29];
                this.e[var29] = 0.0D;
            }

            for(var30 = Math.max(var29 - 1, 0); var30 < var1; ++var30) {
                var27 += Math.abs(this.H[var29][var30]);
            }
        }

        var29 = 0;

        while(true) {
            double var21;
            double var23;
            double var25;
            int var31;
            int var32;
            while(var2 >= var3) {
                for(var30 = var2; var30 > var3; --var30) {
                    var15 = Math.abs(this.H[var30 - 1][var30 - 1]) + Math.abs(this.H[var30][var30]);
                    if(var15 == 0.0D) {
                        var15 = var27;
                    }

                    if(Math.abs(this.H[var30][var30 - 1]) < var5 * var15) {
                        break;
                    }
                }

                if(var30 == var2) {
                    this.H[var2][var2] += var7;
                    this.d[var2] = this.H[var2][var2];
                    this.e[var2] = 0.0D;
                    --var2;
                    var29 = 0;
                } else if(var30 == var2 - 1) {
                    var21 = this.H[var2][var2 - 1] * this.H[var2 - 1][var2];
                    var9 = (this.H[var2 - 1][var2 - 1] - this.H[var2][var2]) / 2.0D;
                    var11 = var9 * var9 + var21;
                    var17 = Math.sqrt(Math.abs(var11));
                    this.H[var2][var2] += var7;
                    this.H[var2 - 1][var2 - 1] += var7;
                    var23 = this.H[var2][var2];
                    if(var11 >= 0.0D) {
                        if(var9 >= 0.0D) {
                            var17 += var9;
                        } else {
                            var17 = var9 - var17;
                        }

                        this.d[var2 - 1] = var23 + var17;
                        this.d[var2] = this.d[var2 - 1];
                        if(var17 != 0.0D) {
                            this.d[var2] = var23 - var21 / var17;
                        }

                        this.e[var2 - 1] = 0.0D;
                        this.e[var2] = 0.0D;
                        var23 = this.H[var2][var2 - 1];
                        var15 = Math.abs(var23) + Math.abs(var17);
                        var9 = var23 / var15;
                        var11 = var17 / var15;
                        var13 = Math.sqrt(var9 * var9 + var11 * var11);
                        var9 /= var13;
                        var11 /= var13;

                        for(var31 = var2 - 1; var31 < var1; ++var31) {
                            var17 = this.H[var2 - 1][var31];
                            this.H[var2 - 1][var31] = var11 * var17 + var9 * this.H[var2][var31];
                            this.H[var2][var31] = var11 * this.H[var2][var31] - var9 * var17;
                        }

                        for(var31 = 0; var31 <= var2; ++var31) {
                            var17 = this.H[var31][var2 - 1];
                            this.H[var31][var2 - 1] = var11 * var17 + var9 * this.H[var31][var2];
                            this.H[var31][var2] = var11 * this.H[var31][var2] - var9 * var17;
                        }

                        for(var31 = var3; var31 <= var4; ++var31) {
                            var17 = this.V[var31][var2 - 1];
                            this.V[var31][var2 - 1] = var11 * var17 + var9 * this.V[var31][var2];
                            this.V[var31][var2] = var11 * this.V[var31][var2] - var9 * var17;
                        }
                    } else {
                        this.d[var2 - 1] = var23 + var9;
                        this.d[var2] = var23 + var9;
                        this.e[var2 - 1] = var17;
                        this.e[var2] = -var17;
                    }

                    var2 -= 2;
                    var29 = 0;
                } else {
                    var23 = this.H[var2][var2];
                    var25 = 0.0D;
                    var21 = 0.0D;
                    if(var30 < var2) {
                        var25 = this.H[var2 - 1][var2 - 1];
                        var21 = this.H[var2][var2 - 1] * this.H[var2 - 1][var2];
                    }

                    if(var29 == 10) {
                        var7 += var23;

                        for(var31 = var3; var31 <= var2; ++var31) {
                            this.H[var31][var31] -= var23;
                        }

                        var15 = Math.abs(this.H[var2][var2 - 1]) + Math.abs(this.H[var2 - 1][var2 - 2]);
                        var23 = var25 = 0.75D * var15;
                        var21 = -0.4375D * var15 * var15;
                    }

                    if(var29 == 30) {
                        var15 = (var25 - var23) / 2.0D;
                        var15 = var15 * var15 + var21;
                        if(var15 > 0.0D) {
                            var15 = Math.sqrt(var15);
                            if(var25 < var23) {
                                var15 = -var15;
                            }

                            var15 = var23 - var21 / ((var25 - var23) / 2.0D + var15);

                            for(var31 = var3; var31 <= var2; ++var31) {
                                this.H[var31][var31] -= var15;
                            }

                            var7 += var15;
                            var21 = 0.964D;
                            var25 = 0.964D;
                            var23 = 0.964D;
                        }
                    }

                    ++var29;

                    for(var31 = var2 - 2; var31 >= var30; --var31) {
                        var17 = this.H[var31][var31];
                        var13 = var23 - var17;
                        var15 = var25 - var17;
                        var9 = (var13 * var15 - var21) / this.H[var31 + 1][var31] + this.H[var31][var31 + 1];
                        var11 = this.H[var31 + 1][var31 + 1] - var17 - var13 - var15;
                        var13 = this.H[var31 + 2][var31 + 1];
                        var15 = Math.abs(var9) + Math.abs(var11) + Math.abs(var13);
                        var9 /= var15;
                        var11 /= var15;
                        var13 /= var15;
                        if(var31 == var30 || Math.abs(this.H[var31][var31 - 1]) * (Math.abs(var11) + Math.abs(var13)) < var5 * Math.abs(var9) * (Math.abs(this.H[var31 - 1][var31 - 1]) + Math.abs(var17) + Math.abs(this.H[var31 + 1][var31 + 1]))) {
                            break;
                        }
                    }

                    for(var32 = var31 + 2; var32 <= var2; ++var32) {
                        this.H[var32][var32 - 2] = 0.0D;
                        if(var32 > var31 + 2) {
                            this.H[var32][var32 - 3] = 0.0D;
                        }
                    }

                    for(var32 = var31; var32 <= var2 - 1; ++var32) {
                        boolean var33 = var32 != var2 - 1;
                        if(var32 != var31) {
                            var9 = this.H[var32][var32 - 1];
                            var11 = this.H[var32 + 1][var32 - 1];
                            var13 = var33?this.H[var32 + 2][var32 - 1]:0.0D;
                            var23 = Math.abs(var9) + Math.abs(var11) + Math.abs(var13);
                            if(var23 == 0.0D) {
                                continue;
                            }

                            var9 /= var23;
                            var11 /= var23;
                            var13 /= var23;
                        }

                        var15 = Math.sqrt(var9 * var9 + var11 * var11 + var13 * var13);
                        if(var9 < 0.0D) {
                            var15 = -var15;
                        }

                        if(var15 != 0.0D) {
                            if(var32 != var31) {
                                this.H[var32][var32 - 1] = -var15 * var23;
                            } else if(var30 != var31) {
                                this.H[var32][var32 - 1] = -this.H[var32][var32 - 1];
                            }

                            var9 += var15;
                            var23 = var9 / var15;
                            var25 = var11 / var15;
                            var17 = var13 / var15;
                            var11 /= var9;
                            var13 /= var9;

                            int var34;
                            for(var34 = var32; var34 < var1; ++var34) {
                                var9 = this.H[var32][var34] + var11 * this.H[var32 + 1][var34];
                                if(var33) {
                                    var9 += var13 * this.H[var32 + 2][var34];
                                    this.H[var32 + 2][var34] -= var9 * var17;
                                }

                                this.H[var32][var34] -= var9 * var23;
                                this.H[var32 + 1][var34] -= var9 * var25;
                            }

                            for(var34 = 0; var34 <= Math.min(var2, var32 + 3); ++var34) {
                                var9 = var23 * this.H[var34][var32] + var25 * this.H[var34][var32 + 1];
                                if(var33) {
                                    var9 += var17 * this.H[var34][var32 + 2];
                                    this.H[var34][var32 + 2] -= var9 * var13;
                                }

                                this.H[var34][var32] -= var9;
                                this.H[var34][var32 + 1] -= var9 * var11;
                            }

                            for(var34 = var3; var34 <= var4; ++var34) {
                                var9 = var23 * this.V[var34][var32] + var25 * this.V[var34][var32 + 1];
                                if(var33) {
                                    var9 += var17 * this.V[var34][var32 + 2];
                                    this.V[var34][var32 + 2] -= var9 * var13;
                                }

                                this.V[var34][var32] -= var9;
                                this.V[var34][var32 + 1] -= var9 * var11;
                            }
                        }
                    }
                }
            }

            if(var27 == 0.0D) {
                return;
            }

            for(var2 = var1 - 1; var2 >= 0; --var2) {
                var9 = this.d[var2];
                var11 = this.e[var2];
                double var19;
                if(var11 == 0.0D) {
                    var30 = var2;
                    this.H[var2][var2] = 1.0D;

                    for(var31 = var2 - 1; var31 >= 0; --var31) {
                        var21 = this.H[var31][var31] - var9;
                        var13 = 0.0D;

                        for(var32 = var30; var32 <= var2; ++var32) {
                            var13 += this.H[var31][var32] * this.H[var32][var2];
                        }

                        if(this.e[var31] < 0.0D) {
                            var17 = var21;
                            var15 = var13;
                        } else {
                            var30 = var31;
                            if(this.e[var31] == 0.0D) {
                                if(var21 != 0.0D) {
                                    this.H[var31][var2] = -var13 / var21;
                                } else {
                                    this.H[var31][var2] = -var13 / (var5 * var27);
                                }
                            } else {
                                var23 = this.H[var31][var31 + 1];
                                var25 = this.H[var31 + 1][var31];
                                var11 = (this.d[var31] - var9) * (this.d[var31] - var9) + this.e[var31] * this.e[var31];
                                var19 = (var23 * var15 - var17 * var13) / var11;
                                this.H[var31][var2] = var19;
                                if(Math.abs(var23) > Math.abs(var17)) {
                                    this.H[var31 + 1][var2] = (-var13 - var21 * var19) / var23;
                                } else {
                                    this.H[var31 + 1][var2] = (-var15 - var25 * var19) / var17;
                                }
                            }

                            var19 = Math.abs(this.H[var31][var2]);
                            if(var5 * var19 * var19 > 1.0D) {
                                for(var32 = var31; var32 <= var2; ++var32) {
                                    this.H[var32][var2] /= var19;
                                }
                            }
                        }
                    }
                } else if(var11 < 0.0D) {
                    var30 = var2 - 1;
                    if(Math.abs(this.H[var2][var2 - 1]) > Math.abs(this.H[var2 - 1][var2])) {
                        this.H[var2 - 1][var2 - 1] = var11 / this.H[var2][var2 - 1];
                        this.H[var2 - 1][var2] = -(this.H[var2][var2] - var9) / this.H[var2][var2 - 1];
                    } else {
                        this.cdiv(0.0D, -this.H[var2 - 1][var2], this.H[var2 - 1][var2 - 1] - var9, var11);
                        this.H[var2 - 1][var2 - 1] = this.cdivr;
                        this.H[var2 - 1][var2] = this.cdivi;
                    }

                    this.H[var2][var2 - 1] = 0.0D;
                    this.H[var2][var2] = 1.0D;

                    for(var31 = var2 - 2; var31 >= 0; --var31) {
                        double var41 = 0.0D;
                        double var42 = 0.0D;

                        int var40;
                        for(var40 = var30; var40 <= var2; ++var40) {
                            var41 += this.H[var31][var40] * this.H[var40][var2 - 1];
                            var42 += this.H[var31][var40] * this.H[var40][var2];
                        }

                        var21 = this.H[var31][var31] - var9;
                        if(this.e[var31] < 0.0D) {
                            var17 = var21;
                            var13 = var41;
                            var15 = var42;
                        } else {
                            var30 = var31;
                            if(this.e[var31] == 0.0D) {
                                this.cdiv(-var41, -var42, var21, var11);
                                this.H[var31][var2 - 1] = this.cdivr;
                                this.H[var31][var2] = this.cdivi;
                            } else {
                                var23 = this.H[var31][var31 + 1];
                                var25 = this.H[var31 + 1][var31];
                                double var36 = (this.d[var31] - var9) * (this.d[var31] - var9) + this.e[var31] * this.e[var31] - var11 * var11;
                                double var38 = (this.d[var31] - var9) * 2.0D * var11;
                                if(var36 == 0.0D & var38 == 0.0D) {
                                    var36 = var5 * var27 * (Math.abs(var21) + Math.abs(var11) + Math.abs(var23) + Math.abs(var25) + Math.abs(var17));
                                }

                                this.cdiv(var23 * var13 - var17 * var41 + var11 * var42, var23 * var15 - var17 * var42 - var11 * var41, var36, var38);
                                this.H[var31][var2 - 1] = this.cdivr;
                                this.H[var31][var2] = this.cdivi;
                                if(Math.abs(var23) > Math.abs(var17) + Math.abs(var11)) {
                                    this.H[var31 + 1][var2 - 1] = (-var41 - var21 * this.H[var31][var2 - 1] + var11 * this.H[var31][var2]) / var23;
                                    this.H[var31 + 1][var2] = (-var42 - var21 * this.H[var31][var2] - var11 * this.H[var31][var2 - 1]) / var23;
                                } else {
                                    this.cdiv(-var13 - var25 * this.H[var31][var2 - 1], -var15 - var25 * this.H[var31][var2], var17, var11);
                                    this.H[var31 + 1][var2 - 1] = this.cdivr;
                                    this.H[var31 + 1][var2] = this.cdivi;
                                }
                            }

                            var19 = Math.max(Math.abs(this.H[var31][var2 - 1]), Math.abs(this.H[var31][var2]));
                            if(var5 * var19 * var19 > 1.0D) {
                                for(var40 = var31; var40 <= var2; ++var40) {
                                    this.H[var40][var2 - 1] /= var19;
                                    this.H[var40][var2] /= var19;
                                }
                            }
                        }
                    }
                }
            }

            for(var30 = 0; var30 < var1; ++var30) {
                if(var30 < var3 | var30 > var4) {
                    for(var31 = var30; var31 < var1; ++var31) {
                        this.V[var30][var31] = this.H[var30][var31];
                    }
                }
            }

            for(var30 = var1 - 1; var30 >= var3; --var30) {
                for(var31 = var3; var31 <= var4; ++var31) {
                    var17 = 0.0D;

                    for(var32 = var3; var32 <= Math.min(var30, var4); ++var32) {
                        var17 += this.V[var31][var32] * this.H[var32][var30];
                    }

                    this.V[var31][var30] = var17;
                }
            }

            return;
        }
    }

    public EigenvalueDecomposition(Matrix var1) {
        double[][] var2 = var1.getArray();
        this.n = var1.getColumnDimension();
        this.V = new double[this.n][this.n];
        this.d = new double[this.n];
        this.e = new double[this.n];
        this.issymmetric = true;

        int var3;
        int var4;
        for(var3 = 0; var3 < this.n & this.issymmetric; ++var3) {
            for(var4 = 0; var4 < this.n & this.issymmetric; ++var4) {
                this.issymmetric = var2[var4][var3] == var2[var3][var4];
            }
        }

        if(this.issymmetric) {
            for(var3 = 0; var3 < this.n; ++var3) {
                for(var4 = 0; var4 < this.n; ++var4) {
                    this.V[var3][var4] = var2[var3][var4];
                }
            }

            this.tred2();
            this.tql2();
        } else {
            this.H = new double[this.n][this.n];
            this.ort = new double[this.n];

            for(var3 = 0; var3 < this.n; ++var3) {
                for(var4 = 0; var4 < this.n; ++var4) {
                    this.H[var4][var3] = var2[var4][var3];
                }
            }

            this.orthes();
            this.hqr2();
        }

    }

    public Matrix getV() {
        return new Matrix(this.V, this.n, this.n);
    }

    public double[] getRealEigenvalues() {
        return this.d;
    }

    public double[] getImagEigenvalues() {
        return this.e;
    }

    public Matrix getD() {
        Matrix var1 = new Matrix(this.n, this.n);
        double[][] var2 = var1.getArray();

        for(int var3 = 0; var3 < this.n; ++var3) {
            for(int var4 = 0; var4 < this.n; ++var4) {
                var2[var3][var4] = 0.0D;
            }

            var2[var3][var3] = this.d[var3];
            if(this.e[var3] > 0.0D) {
                var2[var3][var3 + 1] = this.e[var3];
            } else if(this.e[var3] < 0.0D) {
                var2[var3][var3 - 1] = this.e[var3];
            }
        }

        return var1;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Jama.util;

public class Maths {
    public Maths() {
    }

    public static double hypot(double var0, double var2) {
        double var4;
        if(Math.abs(var0) > Math.abs(var2)) {
            var4 = var2 / var0;
            var4 = Math.abs(var0) * Math.sqrt(1.0D + var4 * var4);
        } else if(var2 != 0.0D) {
            var4 = var0 / var2;
            var4 = Math.abs(var2) * Math.sqrt(1.0D + var4 * var4);
        } else {
            var4 = 0.0D;
        }

        return var4;
    }
}

/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.21
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Utils;


public class Tuple<A, B> {

    A var1;
    B var2;

    public Tuple(A var1, B var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    public A getFirst() {
        return var1;
    }
    public void setFirst(A var1) {
        this.var1 = var1;
    }

    public B getSecond() { return var2; }
    public void setSecond(B var2) {
        this.var2 = var2;
    }


    /* End */
}
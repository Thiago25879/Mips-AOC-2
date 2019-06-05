//Memória de dados

package mips_multiciclo;

public class Memoria_dados {

    public static int[] memoria = new int[128];
    
    public static String toString(int x) {
        return x * 4 + ": " + memoria[x];
    }

    public static String[] paraString() {
        String temp[] = new String[128];
        for (int x = 0; x < 128; x++) {
            temp[x] = toString(x);
        }
        return temp;
    }

}

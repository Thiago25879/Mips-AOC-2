package mips_multiciclo;

public class Memoria_principal {

    public static String memoria[] = new String[128];

    public static void inicializarMemoria() {
        for (int x = 0; x < 128; x++) {
            memoria[x] = "";
        }
    }

    public static void setMemoria(int local, String instrucao) {
        if (Memoria_instrucoes.decode(instrucao) != 0) {
            Memoria_principal.memoria[local] = instrucao;
        }
    }

    public static String toString(int x) {
        // You can change this to suite the presentation of a list item
        return x * 4 + ": " + memoria[x].toUpperCase();
    }

    public static String[] paraString() {
        String temp[] = new String[128];
        for (int x = 0; x < 128; x++) {
            temp[x] = toString(x);
        }
        return temp;
    }
}

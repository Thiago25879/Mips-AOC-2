package mips_multiciclo;

public class Memoria_principal {

    public static String memoria[] = new String[Mips_Multiciclo.tamPrincipal/2];

    public static String setMemoriaMult(int local, String instrucoes) {
        boolean test = true;
        String[] temp = instrucoes.split("\n");
        for (int x = 0; x < temp.length; x++) {
            if (!(setMemoria(local + x, temp[x].trim()))) {
                temp[x] = "  " + temp[x];
                test = false;
            }
        }
        if (!test) {
            String retorno = "";
            for (int x = 0; x < temp.length; x++) {
                retorno = retorno + temp[x] + "\n";
            }
            return retorno;
        } else {
            return "";
        }
    }

    public static boolean setMemoria(int local, String instrucao) {
        try {
            if (Memoria_instrucoes.decode(instrucao) != 0) {
                Memoria_principal.memoria[local] = instrucao;
                return true;
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return false;
    }

    public static boolean setMemoriaDado(int local, int dado) {
        try {
            Memoria_principal.memoria[local] = String.valueOf(dado);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return false;
    }

    public static String toString(int x) {
        // You can change this to suite the presentation of a list item
        return x * 4 + ": " + memoria[x].toUpperCase();
    }

    public static String[] paraString() {
        String temp[] = new String[Mips_Multiciclo.tamPrincipal/2];
        for (int x = 0; x < Mips_Multiciclo.tamPrincipal/2; x++) {
            temp[x] = toString(x);
        }
        return temp;
    }
}

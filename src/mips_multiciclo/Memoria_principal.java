package mips_multiciclo;

public class Memoria_principal {

    public static String memoria[] = new String[Mips_Multiciclo.tamPrincipal];

    public static String setMemoriaMult(int local, String instrucoes) {
        boolean test = true;
        String[] temp = instrucoes.split("\n");
        for (int x = 0; x < temp.length; x++) {
            if (!(setMemoria(true, local + x, temp[x].trim()))) {
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

    public static boolean setMemoria(boolean isInst, int local, String instrucao) {
        try {
            if (isInst) {
                if (Memoria_instrucoes.decode(instrucao) != 0) {
                    Memoria_principal.memoria[local] = instrucao;
                    return true;
                }
            } else {
                if (Integer.parseInt(instrucao) != 0) {
                    if(local < (Mips_Multiciclo.tamPrincipal / 2)){
                        local += (Mips_Multiciclo.tamPrincipal / 2);
                    }
                    Memoria_principal.memoria[local] = instrucao;
                    return true;
                }
            }

        } catch (ArrayIndexOutOfBoundsException e) {

        } catch (NumberFormatException e) {

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
        int y = x;
        if (x >= Mips_Multiciclo.tamPrincipal / 2) {
            x -= Mips_Multiciclo.tamPrincipal / 2;
            return (x * 4) + " (" + (y * 4) + "): " + memoria[y].toUpperCase();
        } else {
            return x * 4 + ": " + memoria[y].toUpperCase();
        }
    }

    public static String[] paraString(int tipo) {
        int x = 0;
        String temp[];
        if (tipo == 0) {
            temp = new String[Mips_Multiciclo.tamPrincipal];
            for (x = 0; x < Mips_Multiciclo.tamPrincipal; x++) {
                temp[x] = toString(x);
            }
        } else {
            temp = new String[Mips_Multiciclo.tamPrincipal / 2];
            if (tipo == 1) {
                for (x = 0; x < Mips_Multiciclo.tamPrincipal / 2; x++) {
                    temp[x] = toString(x);
                }
            } else {
                try {
                    for (x = Mips_Multiciclo.tamPrincipal / 2; x < Mips_Multiciclo.tamPrincipal; x++) {
                        temp[x - Mips_Multiciclo.tamPrincipal / 2] = toString(x);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    
                }
            }

        }

        return temp;
    }
}

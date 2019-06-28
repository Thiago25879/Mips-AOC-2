package mips_multiciclo;

public class MemoriaPrincipal {

    //Aloca a memoria principal de modo estático do tamanho da variavel na main
    public static String memoria[] = new String[Mips.tamPrincipal];

    //Recebe o local inicial e instruções de multiplas linhas para ser inserido na memoria principal
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

    //Recebe um booleano se a String é instrução, o local e a string a ser inserida
    public static boolean setMemoria(boolean isInst, int local, String instrucao) {
        try {
            if (isInst) {
                if (CacheInstrucoes.decodificarInst(instrucao) != 0) {
                    MemoriaPrincipal.memoria[local] = instrucao;
                    return true;
                }
            } else {
                if (Integer.parseInt(instrucao) != 0) {
                    if(local < (Mips.tamPrincipal / 2)){
                        local += (Mips.tamPrincipal / 2);
                    }
                    MemoriaPrincipal.memoria[local] = instrucao;
                    return true;
                }
            }

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {

        }
        return false;
    }

    //Insere o dado na memoria principal (apenas dados, sem instruções)
    public static boolean setMemoriaDado(int local, int dado) {
        try {
            MemoriaPrincipal.memoria[local] = String.valueOf(dado);
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {

        }
        return false;
    }

    //Retorna o valor da memoria principal no espaço inserido
    public static String retornaTexto(int x) {
        int y = x;
        if (x >= Mips.tamPrincipal / 2) {
            x -= Mips.tamPrincipal / 2;
            return (x * 4) + " (" + (y * 4) + "): " + memoria[y].toUpperCase();
        } else {
            return x * 4 + ": " + memoria[y].toUpperCase();
        }
    }

    //Recebe se deve ser retornado como texto apenas instruções, apenas dados ou tudo
    public static String[] comoTexto(int tipo) {
        int x = 0;
        String temp[];
        if (tipo == 0) {
            temp = new String[Mips.tamPrincipal];
            for (x = 0; x < Mips.tamPrincipal; x++) {
                temp[x] = retornaTexto(x);
            }
        } else {
            temp = new String[Mips.tamPrincipal / 2];
            if (tipo == 1) {
                for (x = 0; x < Mips.tamPrincipal / 2; x++) {
                    temp[x] = retornaTexto(x);
                }
            } else {
                try {
                    for (x = Mips.tamPrincipal / 2; x < Mips.tamPrincipal; x++) {
                        temp[x - Mips.tamPrincipal / 2] = retornaTexto(x);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    
                }
            }

        }

        return temp;
    }
}

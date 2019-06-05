package mips_multiciclo;

public class Memoria_principal {

    public static String memoria[] = new String[128];

    public static String setMemoriaMult(int local, String instrucoes){
        boolean test = true;
        String[] temp = instrucoes.split("\n");
        for(int x = 0;x < temp.length;x++){
            if(!(setMemoria(local+x,temp[x].trim()))){
                temp[x] = "  "+temp[x];
                test = false;
            }
        }
        if(!test){
            String retorno = "";
            for(int x = 0;x < temp.length;x++){
                retorno = retorno + temp[x] + "\n";
            } 
            return retorno;
        }else{
            return "";
        }
    }

    public static boolean setMemoria(int local, String instrucao) {
        if (Memoria_instrucoes.decode(instrucao) != 0) {
            Memoria_principal.memoria[local] = instrucao;
            return true;
        }
        return false;
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

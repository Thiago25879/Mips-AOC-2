//Registradores

package mips_multiciclo;

public class Registradores {
    public static int Registradores[] = new int[32];
    private static String Nomes[] = {"$Zero", "$at  ", "$v0  ", "$v1  ", "$a0  ", "$a1  ", "$a2  ", "$a3  ", "$t0  ", "$t1  ", "$t2  ", "$t3  ", "$t4  ", "$t5  ", "$t6  ", "$t7  ", "$s0  ", "$s1  ", "$s2  ", "$s3  ", "$s4  ", "$s5  ", "$s6  ", "$s7  ", "$t8  ", "$t9  ", "$k0  ", "$k1  ", "$gp  ", "$sp  ", "$fp  ", "$ra"};
    

        public static String toString(int x) {
    // You can change this to suite the presentation of a list item
        return Nomes[x] + ": " + Registradores[x];
    }
    
    public static String[] tostring(){
        String temp[] = new String[32];
        for(int x = 0;x < 32;x++){
            temp[x] = toString(x);
        }
        return temp;
    }
    
    public static void setRegs(String dados){
        String[] div = new String[32];
        dados = dados.substring(18);
        div = dados.split("\n");
        for(int x = 0;x < 32;x++){
            Registradores[x] = Integer.parseInt(div[x].substring(5).replaceAll("[^0-9-]", ""));
        }
    }
}


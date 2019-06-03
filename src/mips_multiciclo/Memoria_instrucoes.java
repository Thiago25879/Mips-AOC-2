//Memória de Instruções

package mips_multiciclo;

public class Memoria_instrucoes {

    private static int[] memoria = new int[128];

    public Memoria_instrucoes() {
    }

    public static int decode(String instrucao) {
        //Tipo: 0 = Aritmetico, 1 = Imediato, 2 = Jump, 3 = Memoria, 4 = LUI, 5 = Beq, 6 = Jr , 7 = Shifts
        int instBit = 0, tipo;
        String separado[] = limparInstrucao(instrucao);
        switch (separado[0].toLowerCase()) {
            case "lw":
                instBit = instBit | 0b10001100000000000000000000000000;
                tipo = 3;
                break;
            case "sw":
                instBit = instBit | 0b10101100000000000000000000000000;
                tipo = 3;
                break;
            case "beq":
                instBit = instBit | 0b00010000000000000000000000000000;
                tipo = 5;
                break;
            case "bne":
                instBit = instBit | 0b00010100000000000000000000000000;
                tipo = 5;
                break;
            case "add":
                instBit = instBit | 0b00000000000000000000000000100000;
                tipo = 0;
                break;
            case "addi":
                instBit = instBit | 0b00100000000000000000000000000000;
                tipo = 1;
                break;
            case "sub":
                instBit = instBit | 0b00000000000000000000000000100010;
                tipo = 0;
                break;
            case "and":
                instBit = instBit | 0b00000000000000000000000000100100;
                tipo = 0;
                break;
            case "or":
                instBit = instBit | 0b00000000000000000000000000100101;
                tipo = 0;
                break;
            case "ori":
                instBit = instBit | 0b00110100000000000000000000000000;
                tipo = 1;
                break;
            case "nor":
                instBit = instBit | 0b00000000000000000000000000100111;
                tipo = 0;
                break;
            case "slt":
                instBit = instBit | 0b00000000000000000000000000101010;
                tipo = 0;
                break;
            case "slti":
                instBit = instBit | 0b00101000000000000000000000000000;
                tipo = 1;
                break;
            case "j":
                instBit = instBit | 0b00001000000000000000000000000000;
                tipo = 2;
                break;
            case "jr":
                instBit = instBit | 0b00000000000000000000000000001000;
                tipo = 6;
                break;
            case "sll":
                instBit = instBit | 0b00000000000000000000000000000000;
                tipo = 7;
                break;
            case "srl":
                instBit = instBit | 0b00000000000000000000000000000010;
                tipo = 7;
                break;
            case "jal":
                instBit = instBit | 0b00001100000000000000000000000000;
                tipo = 2;
                break;
            case "lui":
                instBit = instBit | 0b00111100000000000000000000000000;
                tipo = 4;
                break;
            default:
                return 0;
        }
        int reg1, reg2, reg3;
        switch (tipo) {
            case 0:
                reg1 = decodificarReg(separado[1]);
                reg2 = decodificarReg(separado[2]);
                reg3 = decodificarReg(separado[3]);
                reg1 = reg1 << 11;
                reg2 = reg2 << 21;
                reg3 = reg3 << 16;
                instBit = instBit | reg1;
                instBit = instBit | reg2;
                instBit = instBit | reg3;
                break;
            case 1:
                reg1 = decodificarReg(separado[1]);
                reg2 = decodificarReg(separado[2]);
                reg1 = reg1 << 16;
                reg2 = reg2 << 21;
                instBit = instBit | reg1;
                instBit = instBit | reg2;
                instBit = instBit | Integer.parseInt(separado[3]);
                break;
            case 2:
                instBit = instBit | Integer.parseInt(separado[1]);
                break;
            case 3:
                reg1 = decodificarReg(separado[1]);
                reg2 = decodificarReg(separado[3]);
                reg1 = reg1 << 16;
                reg2 = reg2 << 21;
                instBit = instBit | reg1;
                instBit = instBit | reg2;
                instBit = instBit | Integer.parseInt(separado[2]);
                break;
            case 4:
                reg1 = decodificarReg(separado[1]);
                reg1 = reg1 << 16;
                instBit = instBit | reg1;
                instBit = instBit | Integer.parseInt(separado[2]);
                break;
            case 5:
                reg1 = decodificarReg(separado[1]);
                reg2 = decodificarReg(separado[2]);
                reg1 = reg1 << 21;
                reg2 = reg2 << 16;
                instBit = instBit | reg1;
                instBit = instBit | reg2;
                instBit = instBit | Integer.parseInt(separado[3]);
                break;
            case 6:
                reg1 = decodificarReg(separado[1]);
                reg1 = reg1 << 21;
                instBit = instBit | reg1;
                break;
            case 7:
                reg1 = decodificarReg(separado[1]);
                reg2 = decodificarReg(separado[2]);
                reg1 = reg1 << 11;
                reg2 = reg2 << 16;
                reg3 = Integer.parseInt(separado[3]) << 6;
                instBit = instBit | reg1;
                instBit = instBit | reg2;
                instBit = instBit | reg3;
                break;
        }
        return instBit;
    }

    private static String[] limparInstrucao(String instrucao) {
        instrucao = instrucao.replaceAll(",", " ");
        instrucao = instrucao.replaceAll("\\(", " ");
        instrucao = instrucao.replaceAll("\\)", "");
        instrucao = instrucao.replaceAll("  ", " ");
        String separado[] = instrucao.split(" ");
        return separado;
    }

    private static int decodificarReg(String temp) {
        temp = temp.replaceAll("\\$", "");
        temp = temp.toLowerCase();
        switch (temp) {
            case "zero":
                return 0;
            case "gp":
                return 28;
            case "sp":
                return 29;
            case "fp":
                return 30;
            case "ra":
                return 31;
        }
        String temp2[] = temp.split("");
        switch (temp2[0]) {
            case "v":
                return (Integer.parseInt(temp2[1]) + 2);
            case "a":
                return (Integer.parseInt(temp2[1]) + 4);
            case "t":
                if (Integer.parseInt(temp2[1]) <= 7) {
                    return (Integer.parseInt(temp2[1]) + 8);
                } else {
                    if (Integer.parseInt(temp2[1]) == 8) {
                        return 24;
                    } else {
                        return 25;
                    }
                }
            case "s":
                return (Integer.parseInt(temp2[1]) + 16);

        }
        return 0;
    }

    public static int[] getMemoria() {
        return memoria;
    }

    public static void setMemoria(int local, int instrucao) {
        Memoria_instrucoes.memoria[local] = instrucao;
    }
    
    

}

//Memória de Instruções
package mips_multiciclo;

import static java.lang.Math.pow;
import java.util.ArrayList;

public class Memoria_instrucoes {

    public Bloco[][] Blocos;

    public Memoria_instrucoes(int numIndices, int numVias) {
        this.Blocos = new Bloco[numIndices][numVias];
        for (int x = 0; x < numIndices; x++) {
            for (int y = 0; y < numVias; y++) {
                this.Blocos[x][y] = new Bloco();
                this.Blocos[x][y].LRU = y + 1;
            }
        }
    }

    public void setMemoriaInst(String dados) {
        int S = 0;
        ArrayList list = new ArrayList();
        ArrayList list2 = new ArrayList();
        ArrayList list3 = new ArrayList();
        S = dados.replaceAll("[^0-9.|:\n]", "").split("[|.:]").length;
        for (int x = 0; x < S; x++) {
            if (!"".equals((dados.replaceAll("[^0-9.|:\n]", "").split("[|.:]")[x]).trim())) {
                if (!"\n".equals((dados.replaceAll("[^0-9.|:\n]", "").split("[|.:]")[x]).trim())) {
                    list.add(dados.replaceAll("[^0-9.|:\n]", "").split("[|.:]")[x].trim());
                }
            }
        }

        for (int x = 0; x < list.size(); x += (Mips_Multiciclo.tamCache * 12) + 1) {
            list2.add(x);
        }
        for (int x = 0; x < list.size(); x++) {
            if (!list2.contains(x)) {
                list3.add(list.get(x));
            }
        }
        list.clear();
        for (int x = 2; x < list3.size(); x += 3) {
            list.add(list3.get(x));
        }
        for (int via = 0; via < Mips_Multiciclo.vias; via++) {
            for (int indice = 0; indice < Mips_Multiciclo.tamCache; indice++) {
                for (int palavra = 0; palavra < 4; palavra++) {
                    this.Blocos[indice][via].Palavra[palavra]=Integer.parseInt((String) list.get(0));
                    list.remove(0);
                }
            }
        }
    }

    public String toString(int indice, int bloco, int palavra) {
        return "Ind. " + indice + ", Palav. " + palavra + " : " + Integer.toHexString(Blocos[indice][bloco].Palavra[palavra]).toUpperCase();
    }

    public String toStringD(int indice, int bloco, int palavra) {
        return "Ind. " + indice + ", Palav. " + palavra + " : " + Integer.toString(Blocos[indice][bloco].Palavra[palavra]).toUpperCase();
    }

    public String[] tostring(int bloco) {
        String temp[] = new String[Mips_Multiciclo.tamCache * 4];
        for (int indice = 0; indice < Mips_Multiciclo.tamCache; indice++) {
            for (int palavra = 0; palavra < 4; palavra++) {
                temp[(indice * 4) + palavra] = new String();
                temp[(indice * 4) + palavra] = toString(indice, bloco, palavra);
            }
        }
        return temp;
    }

    public String[] tostringD(int bloco) {
        String temp[] = new String[Mips_Multiciclo.tamCache * 4];
        for (int indice = 0; indice < Mips_Multiciclo.tamCache; indice++) {
            for (int palavra = 0; palavra < 4; palavra++) {
                temp[(indice * 4) + palavra] = new String();
                temp[(indice * 4) + palavra] = toString(indice, bloco, palavra);
            }
        }
        return temp;
    }

    public int buscarEnd(int endereco) {
        int indice, tag;
        tag = endereco >> (2 + Mips_Multiciclo.indiceTam);
        indice = (endereco >> 2) & ((int) (pow(2, Mips_Multiciclo.indiceTam))) - 1;
        for (int bloco = 0; bloco < Mips_Multiciclo.vias; bloco++) {
            if (this.Blocos[indice][bloco].Tag == tag && this.Blocos[indice][bloco].Validade) {
                return bloco;
            }
        }
        return -1;
    }

    public int setMemoria(int endereco) {

        int via = encontrarBloco(endereco);
        Bloco bloco = this.Blocos[(endereco >> 2) & ((int) (pow(2, Mips_Multiciclo.indiceTam))) - 1][via];
        bloco.Validade = true;
        bloco.Tag = endereco >> (2 + Mips_Multiciclo.indiceTam);
        switch (endereco & 0b11) {
            case 1:
                endereco -= 1;
                break;
            case 2:
                endereco -= 2;
                break;
            case 3:
                endereco -= 3;
                break;
        }
        bloco.Palavra[0] = Memoria_instrucoes.decode(Memoria_principal.memoria[endereco]);
        bloco.Palavra[1] = Memoria_instrucoes.decode(Memoria_principal.memoria[endereco + 1]);
        bloco.Palavra[2] = Memoria_instrucoes.decode(Memoria_principal.memoria[endereco + 2]);
        bloco.Palavra[3] = Memoria_instrucoes.decode(Memoria_principal.memoria[endereco + 3]);
        return via;
    }

    public int encontrarBloco(int endereco) {
        if (Mips_Multiciclo.vias != 1) {
            int indice = (endereco >> 2) & ((int) (pow(2, Mips_Multiciclo.indiceTam))) - 1;
            int bloco = 0;
            for (int x = 0; x < Mips_Multiciclo.vias; x++) {
                if (this.Blocos[indice][x].LRU == 1) {
                    this.Blocos[indice][x].LRU = Mips_Multiciclo.vias + 1;
                    bloco = x;
                }
                this.Blocos[indice][x].LRU--;

            }
            return bloco;
        } else {
            return 0;
        }
    }

    public static int decode(String instrucao) {
        //Tipo: 0 = Aritmetico, 1 = Imediato, 2 = Jump, 3 = Memoria, 4 = LUI, 5 = Beq, 6 = Jr , 7 = Shifts
        try {
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
                    if (Integer.parseInt(separado[1]) >= (Mips_Multiciclo.tamPrincipal / 2) * 4) {
                        throw new Exception("");
                    }
                    break;
                case 3:
                    reg1 = decodificarReg(separado[1]);
                    reg2 = decodificarReg(separado[3]);
                    reg1 = reg1 << 16;
                    reg2 = reg2 << 21;
                    instBit = instBit | reg1;
                    instBit = instBit | reg2;
                    instBit = instBit | Integer.parseInt(separado[2]);
                    if (Integer.parseInt(separado[2]) >= (Mips_Multiciclo.tamPrincipal / 2) * 4) {
                        throw new Exception("");
                    }
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
        } catch (Exception e) {
            return 0;
        }
    }

    private static String[] limparInstrucao(String instrucao) {
        instrucao = instrucao.replaceAll(",", " ");
        instrucao = instrucao.replaceAll("\\(", " ");
        instrucao = instrucao.replaceAll("\\)", "");
        instrucao = instrucao.replaceAll("  ", " ");
        String separado[] = instrucao.split(" ");
        return separado;
    }

    private static int decodificarReg(String temp) throws Exception {
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
        if (temp2.length > 2 || Integer.parseInt(temp2[1]) < 0) {
            throw new Exception("");
        }
        switch (temp2[0]) {
            case "v":
                if (Integer.parseInt(temp2[1]) > 1) {
                    throw new Exception("");
                }
                return (Integer.parseInt(temp2[1]) + 2);
            case "a":
                if (Integer.parseInt(temp2[1]) > 3) {
                    throw new Exception("");
                }
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
                if (Integer.parseInt(temp2[1]) > 7) {
                    throw new Exception("");
                }
                return (Integer.parseInt(temp2[1]) + 16);
            default:
                throw new Exception("");
        }
    }

}

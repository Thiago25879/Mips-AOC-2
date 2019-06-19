//Memória de dados
package mips_multiciclo;

import static java.lang.Math.pow;
import java.util.ArrayList;

public class Memoria_dados {

    public Bloco[][] Blocos;

    public Memoria_dados(int numIndices, int numVias) {
        this.Blocos = new Bloco[numIndices][numVias];
        for (int x = 0; x < numIndices; x++) {
            for (int y = 0; y < numVias; y++) {
                this.Blocos[x][y] = new Bloco();
                this.Blocos[x][y].LRU = y + 1;
            }
        }
    }

    public String toString(int indice, int bloco, int palavra) {
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
    
    public void setMemoriaDados(String dados) {
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
        endereco += (Mips_Multiciclo.tamPrincipal / 2);
        bloco.Palavra[0] = Integer.parseInt(Memoria_principal.memoria[endereco]);
        bloco.Palavra[1] = Integer.parseInt(Memoria_principal.memoria[endereco + 1]);
        bloco.Palavra[2] = Integer.parseInt(Memoria_principal.memoria[endereco + 2]);
        bloco.Palavra[3] = Integer.parseInt(Memoria_principal.memoria[endereco + 3]);
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

}

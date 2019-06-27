//Unidade de Controle
package mips_multiciclo;

import static java.lang.Math.pow;

public class UnidadeDeControle {

    //private static final byte NOME_DA_CONSTANTE = 0b100000;
    /*private static int RegDst;
    private static int Branch;
    private static int LeMem;
    private static int MemparaReg;
    private static int OpULA;
    private static int EscreveMem;
    private static int OrigULA;
    private static int EscreveReg;*/
    public static void decodeULA(int inst) {
        if (inst != 0) {
            int funct, opcode, FonteA, FonteB, tipo;
            opcode = inst >> 26;
            if ((opcode == 0b10) || (opcode == 0b11)) {
                tipo = 4;
            } else {
                if ((0b100000 == (opcode & 0b100000))) {
                    tipo = 3;
                } else {
                    if ((0b1000 == (opcode & 0b1000)) || (opcode == 0b000101) || (opcode == 0b000100)) {
                        tipo = 2;
                    } else {
                        tipo = 1;
                    }
                }
            }
            switch (tipo) {
                case 1://Operações tipo R
                    funct = inst & 0b111111;
                    if (funct == 0 || funct == 0b10) {
                        FonteA = (inst >> 6) & 0b11111;
                    } else {
                        FonteA = BancoRegs.Registradores[(inst >> 21) & 0b11111];
                    }
                    if (funct == 0b001000) {
                        FonteB = BancoRegs.Registradores[0];
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                        PC.Contador = ((ULA.Operacao(opcode, funct, FonteA, FonteB)) - 1) / 4;
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                        return;
                    }
                    FonteB = BancoRegs.Registradores[(inst >> 16) & 0b11111];
                    if (funct == 0b101010) {
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                        if (ULA.Operacao(opcode, funct, FonteA, FonteB) > 0) {
                            Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                            BancoRegs.Registradores[(inst >> 11) & 0b11111] = 0;
                            return;
                        } else {
                            Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                            BancoRegs.Registradores[(inst >> 11) & 0b11111] = 1;
                            return;
                        }
                    }
                    Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                    BancoRegs.Registradores[(inst >> 11) & 0b11111] = ULA.Operacao(opcode, funct, FonteA, FonteB);
                    Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                    break;
                case 2://Operações Imediatas
                    funct = opcode & 0b1111;
                    FonteA = BancoRegs.Registradores[(inst >> 21) & 0b11111];
                    if (opcode == 0b000100) {
                        FonteB = BancoRegs.Registradores[(inst >> 16) & 0b11111];
                        if ((ULA.Operacao(opcode, funct, FonteA, FonteB)) == 0) {
                            Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                            PC.Contador += (inst & 0b1111111111111111);
                        }
                    } else {
                        if (opcode == 0b000101) {
                            FonteB = BancoRegs.Registradores[(inst >> 16) & 0b11111];
                            if ((ULA.Operacao(opcode, funct, FonteA, FonteB)) != 0) {
                                Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                                PC.Contador += (inst & 0b1111111111111111);
                            }
                        } else {
                            FonteB = inst & 0b1111111111111111;
                            if (opcode == 0b001010) {
                                Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                                if (ULA.Operacao(opcode, funct, FonteA, FonteB) > 0) {
                                    BancoRegs.Registradores[(inst >> 16) & 0b11111] = 0;
                                } else {
                                    BancoRegs.Registradores[(inst >> 16) & 0b11111] = 1;
                                }
                                Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                            } else {
                                Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                                BancoRegs.Registradores[(inst >> 16) & 0b11111] = ULA.Operacao(opcode, funct, FonteA, FonteB);
                                Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                            }
                        }
                    }
                    break;
                case 3:
                    int indice,
                     via,
                     endereco;
                    funct = 0;
                    FonteA = BancoRegs.Registradores[(inst >> 21) & 0b11111];
                    FonteB = inst & 0b1111111111111111;
                    endereco = ((ULA.Operacao(opcode, funct, FonteA, FonteB)) / 4);
                    indice = ((endereco >> 2) & ((int) (pow(2, Mips.indiceTam))) - 1);
                    via = Mips.frame.dadosMem.buscarEnd(endereco);
                    if (via == -1) {
                        Mips.frame.falhaDados++;
                        Mips.frame.acertoDados++;
                        via = Mips.frame.dadosMem.setMemoria(endereco);
                        Mips.frame.inserirTexto("Dados da instrução " + (PC.Contador * 4) + " não encontrada na cache, inserindo na cache de dados " + via + ".\n");
                        Mips.frame.ciclosCont+=50;
                    } else {
                        Mips.frame.acertoDados++;
                    }
                    if ((opcode & 0b101000) == 0b101000) {//If = SW    else = LW
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                        Mips.frame.dadosMem.Blocos[indice][via].Palavra[(FonteB / 4) & 0b11] = BancoRegs.Registradores[(inst >> 16) & 0b11111];
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                    } else {
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                        BancoRegs.Registradores[(inst >> 16) & 0b11111] = Mips.frame.dadosMem.Blocos[indice][via].Palavra[(FonteB / 4) & 0b11];
                        Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 3\n");
                        Mips.frame.ciclosCont++;
                    }
                    switch ((FonteB / 4) & 0b11) {
                        case 1:
                            endereco--;
                            break;
                        case 2:
                            endereco -= 2;
                            break;
                        case 3:
                            endereco -= 3;
                            break;
                    }
                    MemoriaPrincipal.setMemoriaDado((endereco + Mips.tamPrincipal / 2), Mips.frame.dadosMem.Blocos[indice][via].Palavra[0]);
                    MemoriaPrincipal.setMemoriaDado((endereco + Mips.tamPrincipal / 2) + 1, Mips.frame.dadosMem.Blocos[indice][via].Palavra[1]);
                    MemoriaPrincipal.setMemoriaDado((endereco + Mips.tamPrincipal / 2) + 2, Mips.frame.dadosMem.Blocos[indice][via].Palavra[2]);
                    MemoriaPrincipal.setMemoriaDado((endereco + Mips.tamPrincipal / 2) + 3, Mips.frame.dadosMem.Blocos[indice][via].Palavra[3]);
                    Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 4\n");
                        Mips.frame.ciclosCont++;
                    break;
                case 4:
                    Mips.frame.inserirTexto("Instruc. " + (PC.Contador) + " : Executando Ciclo 2\n");
                        Mips.frame.ciclosCont++;
                    if (opcode == 0b11) {
                        BancoRegs.Registradores[31] = (PC.Contador + 1) * 4;
                    }
                    PC.Contador = ((inst & 0b00000011111111111111111111111111) - 1) / 4;
                    break;
            }

        }

    }

}

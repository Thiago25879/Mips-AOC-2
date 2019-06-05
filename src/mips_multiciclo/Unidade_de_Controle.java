//Unidade de Controle
package mips_multiciclo;

public class Unidade_de_Controle {

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
                        FonteA = Registradores.Registradores[(inst >> 21) & 0b11111];
                    }
                    if (funct == 0b001000) {
                        FonteB = Registradores.Registradores[0];
                        PC.Contador = ((ULA.Operacao(opcode, funct, FonteA, FonteB)) - 1) / 4;
                        return;
                    }
                    FonteB = Registradores.Registradores[(inst >> 16) & 0b11111];
                    if (funct == 0b101010) {
                        if (ULA.Operacao(opcode, funct, FonteA, FonteB) > 0) {
                            Registradores.Registradores[(inst >> 11) & 0b11111] = 0;
                            return;
                        } else {
                            Registradores.Registradores[(inst >> 11) & 0b11111] = 1;
                            return;
                        }
                    }
                    Registradores.Registradores[(inst >> 11) & 0b11111] = ULA.Operacao(opcode, funct, FonteA, FonteB);
                    break;
                case 2://Operações Imediatas
                    funct = opcode & 0b1111;
                    FonteA = Registradores.Registradores[(inst >> 21) & 0b11111];
                    if (opcode == 0b000100) {
                        FonteB = Registradores.Registradores[(inst >> 16) & 0b11111];
                        if ((ULA.Operacao(opcode, funct, FonteA, FonteB)) == 0) {
                            PC.Contador += (inst & 0b1111111111111111);
                        }
                    } else {
                        if (opcode == 0b000101) {
                            FonteB = Registradores.Registradores[(inst >> 16) & 0b11111];
                            if ((ULA.Operacao(opcode, funct, FonteA, FonteB)) != 0) {
                                PC.Contador += (inst & 0b1111111111111111);
                            }
                        } else {
                            FonteB = inst & 0b1111111111111111;
                            if (opcode == 0b001010) {
                                if (ULA.Operacao(opcode, funct, FonteA, FonteB) > 0) {
                                    Registradores.Registradores[(inst >> 16) & 0b11111] = 0;
                                } else {
                                    Registradores.Registradores[(inst >> 16) & 0b11111] = 1;
                                }
                            } else {
                                Registradores.Registradores[(inst >> 16) & 0b11111] = ULA.Operacao(opcode, funct, FonteA, FonteB);
                            }
                        }
                    }
                    break;
                case 3:
                    funct = 0;
                    FonteA = Registradores.Registradores[(inst >> 21) & 0b11111];
                    FonteB = inst & 0b1111111111111111;
                    if ((opcode & 0b101000) == 0b101000) {//If = SW    else = LW
                        Memoria_dados.memoria[(ULA.Operacao(opcode, funct, FonteA, FonteB))/4] = Registradores.Registradores[(inst >> 16) & 0b11111];
                    } else {
                        Registradores.Registradores[(inst >> 16) & 0b11111] = Memoria_dados.memoria[(ULA.Operacao(opcode, funct, FonteA, FonteB))/4];
                    }
                    break;
                case 4:
                    if (opcode == 0b11) {
                        Registradores.Registradores[31] = PC.Contador + 1;
                    }
                    PC.Contador = ((inst & 0b00000011111111111111111111111111) - 1)/4;
                    break;
            }

        }

    }

}

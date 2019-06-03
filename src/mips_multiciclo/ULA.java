// ULA
package mips_multiciclo;

public class ULA {

    public static int Operacao(int Opcode, int funct, int FonteA, int FonteB) {
        int tipo;
        if (0b100000 == (Opcode & 0b100000)) {
            tipo = 0;
        } else {
            if ((Opcode == 0b000100) || (Opcode == 0b000101)) {
                tipo = 1;
            } else {
                if ((funct == 0b0) || (funct == 0b10) || (funct == 0b1111)) {
                    tipo = 3;
                } else {
                    tipo = 2;
                }
            }
        }
        switch (tipo) {
            case 0:
                return FonteA + FonteB;
            case 1:
                return FonteA - FonteB;
            case 2:
                if (funct == 0b100000 || funct == 0b1000) {
                    return FonteA + FonteB;
                }
                if (funct == 0b100010 || funct == 0b1010) {
                    return FonteA - FonteB;
                }
                if (funct == 0b100100 || funct == 0b1100) {
                    return FonteA & FonteB;
                }
                if (funct == 0b100101 || funct == 0b1101) {
                    return FonteA | FonteB;
                }
                if (funct == 0b100111) {
                    return (~(FonteA | FonteB));
                }
            case 3://Para Shifts
                if (funct == 0) {
                    return FonteB << FonteA;
                }
                if (funct == 0b10) {
                    return FonteB >> FonteA;
                }
                if (funct == 0b1111) {
                    return FonteB << 16;
                }
        }
        return 0;
    }
}

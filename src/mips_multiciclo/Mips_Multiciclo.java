//Main

package mips_multiciclo;

import java.util.Scanner;

public class Mips_Multiciclo { 

    public static void main(String[] args) {
        int opt = 1;
        String inst;
        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("-----Menu-----\n  1-Inserir instrução\n  2-Mostrar Regs\n  3-Mostrar Memoria Inst\n  4-Mostrar Memoria dados\n  5-Executar\n  0-Encerrar execução\nEscolha a opção desejada");
            opt = scan.nextInt();
            switch(opt){
                case 1:
                    scan.nextLine();
                    System.out.println("\nDigite a instrução a ser inserida");
                    inst = scan.nextLine();
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode(inst));
                    System.out.println("\nInserido com sucesso\n");
                    PC.Contador++;
                    break;
                case 2:
                    System.out.println("\n\n--Registradores--\n");
                        System.out.println("\n$zero "+Registradores.Registradores[0]);
                        //System.out.println("\n$at "+Registradores.Registradores[1]);
                        //System.out.println("\n$v0 "+Registradores.Registradores[2]);
                        //System.out.println("\n$v1 "+Registradores.Registradores[3]);
                        //System.out.println("\n$a0 "+Registradores.Registradores[4]);
                        //System.out.println("\n$a1 "+Registradores.Registradores[5]);
                        //System.out.println("\n$a2 "+Registradores.Registradores[6]);
                        //System.out.println("\n$a3 "+Registradores.Registradores[7]);
                        System.out.println("\n$t0 "+Registradores.Registradores[8]);
                        System.out.println("\n$t1 "+Registradores.Registradores[9]);
                        System.out.println("\n$t2 "+Registradores.Registradores[10]);
                        System.out.println("\n$t3 "+Registradores.Registradores[11]);
                        System.out.println("\n$t4 "+Registradores.Registradores[12]);
                        System.out.println("\n$t5 "+Registradores.Registradores[13]);
                        System.out.println("\n$t6 "+Registradores.Registradores[14]);
                        System.out.println("\n$t7 "+Registradores.Registradores[15]);
                        System.out.println("\n$t8 "+Registradores.Registradores[24]);
                        System.out.println("\n$t9 "+Registradores.Registradores[25]);
                        System.out.println("\n$s0 "+Registradores.Registradores[16]);
                        System.out.println("\n$s1 "+Registradores.Registradores[17]);
                        System.out.println("\n$s2 "+Registradores.Registradores[18]);
                        System.out.println("\n$s3 "+Registradores.Registradores[19]);
                        System.out.println("\n$s4 "+Registradores.Registradores[20]);
                        System.out.println("\n$s5 "+Registradores.Registradores[21]);
                        System.out.println("\n$s6 "+Registradores.Registradores[22]);
                        System.out.println("\n$s7 "+Registradores.Registradores[23]);
                        //System.out.println("\n$k0 "+Registradores.Registradores[26]);
                        //System.out.println("\n$k1 "+Registradores.Registradores[27]);
                        System.out.println("\n$gp "+Registradores.Registradores[28]);
                        System.out.println("\n$sp "+Registradores.Registradores[29]);
                        System.out.println("\n$fp "+Registradores.Registradores[30]);
                        System.out.println("\n$ra "+Registradores.Registradores[31]);
                    break;
                case 3:
                    System.out.println("\n\n--Memória de instruções--\n");
                    for(int x = 0;x < 32;x++){
                        System.out.println("\nSlot "+x+" :"+Memoria_instrucoes.getMemoria()[x]);
                    }
                    break;
                case 4:
                    System.out.println("\n\n--Memória de dados--\n");
                    for(int x = 0;x < 32;x++){
                        System.out.println("\nSlot "+x+" :"+Memoria_dados.memoria[x]);
                    }
                    break;
                case 5:
                    PC.Contador = 0;
                    for(PC.Contador = 0; PC.Contador < 40;PC.Contador++){
                        Unidade_de_Controle.decodeULA(Memoria_instrucoes.getMemoria()[PC.Contador]);
                    }
                    break;
                case 6:
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s0, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s1, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s2, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s3, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s4, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s5, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s6, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s7, $zero, 0"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s0, $zero, 1116"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("BNE $s0, $zero, 2"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("JAL 12"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s1, $zero, 1000"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("J 20"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s6, $s2, 2"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s1, $s1, 3"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s2, $s2, 5"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s3, $s3, 9"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s4, $s4, 14"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s5, $s1, 23"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("JR $ra"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("ADDI $s6, $s2, 3700"));PC.Contador++;
                    Memoria_instrucoes.setMemoria(PC.Contador,Memoria_instrucoes.decode("SLL $s6, $s6, 2"));PC.Contador++;
                    break;
                case 0:
                    System.out.println("\n\nEncerrando programa ............\n\n");
                    break;
                default:
                    System.out.println("Opção indisponível, tente novamente");
                    break;
            }
        }while(opt != 0);
        
    }

}

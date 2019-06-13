//Main
package mips_multiciclo;

import static java.lang.Math.pow;
import java.util.Scanner;
import javax.swing.JFrame;

public class Mips_Multiciclo {

    static int tamCache = 0;
    static int vias = 1;
    static int indiceTam = 0;
    static int tamPrincipal = 1024;
    static Frame frame;
    
    public static void main(String[] args) {
        Mips_Multiciclo main = new Mips_Multiciclo();
        main.inicializar();
    }    
        
    public void inicializar(){
        frame = new Frame();
        frame.inicializarMemoria();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 616);
        frame.mostrarConfig();
        frame.setVisible(false);
    }

}

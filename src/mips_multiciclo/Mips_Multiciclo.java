//Main
package mips_multiciclo;

import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import static java.lang.Math.pow;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Mips_Multiciclo {
    
    static int tamCache = 1;
    static int vias = 1;
    static int indiceTam = 0;
    static int tamPrincipal = 2048;
    static Frame frame;
    
    public static void main(String[] args) {
        Mips_Multiciclo main = new Mips_Multiciclo();
        main.inicializar();
    }
    
    public void inicializar() {
        ImageIcon img = new ImageIcon("./src/Img/web_hi_res_512.png");
        frame = new Frame();
        frame.inicializarMemoria();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 616);
        frame.mostrarConfig();
        frame.setIconImage(img.getImage());
        frame.setVisible(false);
    }
    
    
    
}

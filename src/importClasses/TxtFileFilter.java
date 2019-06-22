/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importClasses;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TxtFileFilter extends javax.swing.filechooser.FileFilter {
    //Exemplo para o futuro
    //private final String[] okFileExtensions = new String[] {"txt", "png", "gif"};

    private final String[] okFileExtensions = new String[]{"txt"};

    @Override
    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                try {
                    FileReader reader = new FileReader(file);
                    String test = "|Informações|";
                    int i, cont = 0;
                    while (cont < 13) {
                        
                        if((int)reader.read() != (int) test.charAt(cont)){
                            System.out.println((int) reader.read()+" - "+ (int) test.charAt(cont));
                            return false;
                        }
                        cont++;
                    }
                    return true;
                } catch (FileNotFoundException ex) {
                    System.out.println("FNFE");
                } catch (IOException ex) {
                    System.out.println("IOE");
                }

            }
            if (file.isDirectory()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Only accepts txt";
    }

}

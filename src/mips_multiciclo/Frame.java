package mips_multiciclo;

import com.sun.glass.ui.Cursor;
import importClasses.TxtFileFilter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Math.pow;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mips_multiciclo.Mips.tamCache;

/**
 *
 * @author thiago
 */
public class Frame extends JFrame {

    final JFileChooser fc = new JFileChooser();
    File file;
    String dadoArq;
    public CacheInstrucoes instrucMem;
    public CacheDados dadosMem;
    int blocoMem;
    float falhaDados, falhaInst, acertoDados, acertoInst;

    public Frame() {
        super("Simulador MIPS");
        initComponents();
        jDialog2.setVisible(false);
        jDialog2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jDialog2.setSize(565, 60);
        jDialog2.setLocation(150, 230);
        jDialog2.getRootPane().setBorder(null);
        jDialog1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        jDialog1.setSize(590, 245);
        jDialog1.setLocation(100, 160);
    }

    public void inserirInterface() {
        if (jLabel2.getText() == " Memória Principal") {
            jList1.setListData(MemoriaPrincipal.paraString(0));
        }
        if (jLabel2.getText() == "Memória Principal I") {
            jList1.setListData(MemoriaPrincipal.paraString(1));
        }
        if (jLabel2.getText() == "Memória Principal D") {
            jList1.setListData(MemoriaPrincipal.paraString(2));
        }
        jList2.setListData(BancoRegs.tostring());
        atualizarCaches();
        atualizarPC();
    }

    public void atualizarCaches() {
        int blocoInst = Integer.parseInt(jSpinner1.getValue().toString());
        int blocoDados = Integer.parseInt(jSpinner2.getValue().toString());
        jList3.setListData(this.instrucMem.tostring(blocoInst));
        jList4.setListData(this.dadosMem.tostring(blocoDados));
    }

    public void atualizarPC() {
        jLabel6.setText(PC.paraString());
    }

    public void mostrarConfig() {
        jTextField3.setText(String.valueOf(Mips.vias));
        jTextField4.setText(String.valueOf(Mips.tamCache));
        jLabel10.setVisible(false);
        jDialog1.setVisible(true);
    }

    public void executar() {
        if (!"Desbloquear".equals(jButton1.getText())) {
            int via = 0;
            PC.Contador = 0;
            this.falhaDados = 0;
            this.falhaInst = 0;
            this.acertoDados = 0;
            this.acertoInst = 0;
            jTextArea1.setFocusable(false);
            jTextArea1.setEditable(false);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            jTextArea1.setText("                    Iniciando Execução passo a passo\n");
            this.instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
            for (PC.Contador = 0; PC.Contador < Mips.tamPrincipal / 2; PC.Contador++) {
                if (CacheInstrucoes.decode(MemoriaPrincipal.memoria[PC.Contador]) != 0) {
                    via = instrucMem.buscarEnd(PC.Contador);
                    if (via == -1) {
                        this.falhaInst++;
                        this.acertoInst++;
                        via = instrucMem.setMemoria(PC.Contador);
                        jTextArea1.append("Instrução " + (PC.Contador * 4) + " não encontrada na cache, inserindo na cache " + via + ".\n");

                    } else {
                        this.acertoInst++;
                    }
                    UnidadeDeControle.decodeULA(instrucMem.Blocos[(PC.Contador >> 2) & ((int) (pow(2, Mips.indiceTam))) - 1][via].Palavra[PC.Contador & 0b11]);
                }
            }
            if (acertoInst != 0) {
                float resultado = ((falhaInst / acertoInst) * 100);
                jTextArea1.append("Execução terminada\n Porcentagem de falhas de instrução:" + resultado + "%.");
                resultado = ((falhaDados / acertoDados) * 100);
                jTextArea1.append("\n Porcentagem de falhas de dados:" + resultado + "%.");
            }
            PC.Contador--;
            jButton1.setText("Desbloquear");
            jButton3.setText("Salvar Log");
        } else {
            jButton1.setText("Rodar");
            jButton3.setText("Inserir Instruções");
            jTextArea1.setFocusable(true);
            jTextArea1.setEditable(true);
            jTextField1.setEnabled(true);
            jTextField2.setEnabled(true);
            jTextArea1.setText("");
        }
        inserirInterface();
    }

    public void passo_a_passo() {
        int via;
        if ("Passo a Passo".equals(jButton2.getText())) {
            jButton2.setText("Próximo Passo");
            jTextArea1.setFocusable(false);
            jTextArea1.setEditable(false);
            jTextArea1.setText("                    Iniciando Execução passo a passo\n");
            this.falhaDados = 0;
            this.falhaInst = 0;
            this.acertoDados = 0;
            this.acertoInst = 0;
            PC.Contador = 0;
            jLabel2.setText(" Memória Principal");
            jList1.setEnabled(false);
            jTextField1.setEnabled(false);
            jTextField2.setEnabled(false);
            jButton1.setText("Parar");
            jButton3.setText("Salvar Log");
            this.instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
            this.dadosMem = new CacheDados(Mips.tamCache, Mips.vias);
        } else {
            while (PC.Contador < Mips.tamPrincipal / 2 && CacheInstrucoes.decode(MemoriaPrincipal.memoria[PC.Contador]) == 0) {
                PC.Contador++;
            }
            if (PC.Contador == Mips.tamPrincipal / 2) {
                PC.Contador--;
            } else {
                via = instrucMem.buscarEnd(PC.Contador);
                if (via == -1) {
                    this.falhaInst++;
                    jTextArea1.append("Instrução " + (PC.Contador * 4) + " não encontrada na cache\n");
                    via = instrucMem.setMemoria(PC.Contador);
                } else {
                    this.acertoInst++;
                }
                UnidadeDeControle.decodeULA(instrucMem.Blocos[(PC.Contador >> 2) & ((int) (pow(2, Mips.indiceTam))) - 1][via].Palavra[PC.Contador & 0b11]);

            }
            if (PC.Contador == Mips.tamPrincipal / 2 - 1) {
                if (acertoInst != 0) {
                    float resultado = ((falhaInst / acertoInst) * 100);
                    jTextArea1.append("Execução terminada\n Porcentagem de falhas de instrução:" + resultado + "%.");
                    resultado = ((falhaDados / acertoDados) * 100);
                    jTextArea1.append("\n Porcentagem de falhas de dados:" + resultado + "%.");
                }
            } else {
                PC.Contador++;
            }
        }
        inserirInterface();
        jList1.setSelectedIndex(PC.Contador);
    }

    public void inicializarMemoria() {
        for (int x = 0; x < Mips.tamPrincipal / 2; x++) {
            MemoriaPrincipal.memoria[x] = "";
        }
        for (int x = Mips.tamPrincipal / 2; x < Mips.tamPrincipal; x++) {
            MemoriaPrincipal.memoria[x] = "0";
        }
    }

    public void inicializarRegistradores() {
        for (int x = 0; x < BancoRegs.Registradores.length; x++) {
            BancoRegs.Registradores[x] = 0;
        }
    }

    public void zerarSimulador() {
        inicializarMemoria();
        inicializarRegistradores();
        PC.Contador = 0;
        instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
        dadosMem = new CacheDados(Mips.tamCache, Mips.vias);
        jTextField1.setEnabled(true);
        jTextField2.setEnabled(true);
    }

    public void salvarEstado() {
        fc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //
            String lista[];
            String dados = new String();
            String insere = new String();
            insere = insere = "|Informações|\n Tamanho_da_Cache:" + Mips.tamCache + "\n Número_de_vias:" + Mips.vias + "\n Tamanho_do_Indice:" + Mips.indiceTam + "\n Tamanho_da_memoria_principal:" + Mips.tamPrincipal + "\n";
            dados = dados.concat(insere);
            insere = insere = "\n|Registradores|\n  ";
            lista = new String[32];
            lista = BancoRegs.tostring();
            for (int x = 0; x < 32; x++) {
                insere = insere.concat(lista[x] + "\n  ");
            }
            dados = dados.concat(insere);
            insere = insere = "\n|Cache_instruções|";
            lista = new String[tamCache];
            for (int y = 0; y < Mips.vias; y++) {
                insere = insere.concat("\n |Cache " + y + ":\n  ");
                lista = Mips.frame.instrucMem.tostringD(y);
                for (int x = 0; x < tamCache * 4; x++) {
                    insere = insere.concat(lista[x] + "\n  ");
                }
            }
            dados = dados.concat(insere);
            insere = "\n|Cache_dados|";
            lista = new String[tamCache];
            for (int y = 0; y < Mips.vias; y++) {
                insere = insere.concat("\n |Cache " + y + ":\n  ");
                lista = Mips.frame.dadosMem.tostring(y);
                for (int x = 0; x < tamCache * 4; x++) {
                    insere = insere.concat(lista[x] + "\n  ");
                }
            }
            dados = dados.concat(insere);
            insere = "\n|Memória_Principal|\n  ";
            lista = new String[Mips.tamPrincipal];
            lista = MemoriaPrincipal.paraString(0);
            for (int x = 0; x < 2048; x++) {
                insere = insere.concat(lista[x] + "\n  ");
            }
            dados = dados.concat(insere);
            //
            file = fc.getSelectedFile();
            jDialog2.setVisible(true);
            jDialog2.setAlwaysOnTop(true);
            jTextField5.requestFocusInWindow();
            dadoArq = dados;
        } else {
        }
    }

    public void inserirEstado(String informacoes) {
        int inicio = 0, fim = 0, x = 0;
        String[] dados = new String[5], interno;
        dados[0] = new String("|Informações|\n");
        dados[1] = new String("\n\n|Registradores|\n");
        dados[2] = new String("\n  \n|Cache_instruções|\n");
        dados[3] = new String("\n  \n|Cache_dados|");
        dados[4] = new String("\n  \n|Memória_Principal|");
        for (x = 0; x < 4; x++) {
            inicio = informacoes.indexOf(dados[x]);
            fim = informacoes.indexOf(dados[x + 1]);
            dados[x] = informacoes.substring(inicio, fim);
        }
        inicio = informacoes.indexOf(dados[x]);
        dados[x] = informacoes.substring(inicio);
        /*--------------------------------------------------------*/
        //try {
        interno = new String[5];
        dados[0] = dados[0].replaceAll("[^0-9:-]", "");
        interno = dados[0].split(":");
        Mips.vias = Integer.parseInt(interno[2]);
        Mips.tamCache = Integer.parseInt(interno[1]);
        Mips.indiceTam = Integer.parseInt(interno[3]);
        Mips.tamPrincipal = Integer.parseInt(interno[4]);

        this.instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
        this.dadosMem = new CacheDados(Mips.tamCache, Mips.vias);

        BancoRegs.setRegs(dados[1]);
        instrucMem.setMemoriaInst(dados[2]);
        dadosMem.setMemoriaDados(dados[3]);

        String novo;
        novo = dados[4].replaceAll("(?<=\n)(.*)(?=: )[:]", "").trim();
        novo = novo.substring(20);
        String[] sep = novo.split("\n");

        for (int i = 0; i < sep.length; i++) {
            if (i < Mips.tamPrincipal / 2) {
                MemoriaPrincipal.setMemoria(true, i, sep[i].trim());
            } else {
                MemoriaPrincipal.setMemoria(false, i, sep[i].trim());
            }
        }

        inserirInterface();
        //} catch (Exception e) {

        //}
    }

    public void carregarEstado() throws FileNotFoundException, IOException {
        fc.setFileSelectionMode(JFileChooser.OPEN_DIALOG);
        TxtFileFilter filter = new TxtFileFilter();
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (filter.accept(file)) {
                FileReader reader = new FileReader(file);
                dadoArq = "";
                int i = 0;
                while ((i = reader.read()) != -1) {
                    dadoArq = dadoArq.concat(String.valueOf((char) i));
                }
                inserirEstado(dadoArq);
            }
        }
    }

    public void inserirTexto(String texto) {
        jTextArea1.append(texto);
    }
    
    public void salvarDados(){
        fc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String dados = jTextArea1.getText();
            file = fc.getSelectedFile();
            jDialog2.setVisible(true);
            jDialog2.setAlwaysOnTop(true);
            jTextField5.requestFocusInWindow();
            dadoArq = dados;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchec0ked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jDialog2 = new javax.swing.JDialog();
        jLabel11 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList4 = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        jDialog1.setAlwaysOnTop(true);
        jDialog1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jDialog1ComponentHidden(evt);
            }
        });
        jDialog1.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                jDialog1WindowOpened(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("URW Palladio L", 1, 18)); // NOI18N
        jLabel7.setText("Configurações");

        jLabel8.setFont(new java.awt.Font("URW Palladio L", 0, 18)); // NOI18N
        jLabel8.setText("Número de vias:");

        jLabel9.setFont(new java.awt.Font("URW Palladio L", 0, 18)); // NOI18N
        jLabel9.setText("Tam. caches (Em bytes):");

        jSlider1.setMaximum(20);
        jSlider1.setMinimum(1);
        jSlider1.setValue(1);
        jSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSlider1MouseDragged(evt);
            }
        });

        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });

        jButton4.setText("Salvar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("URW Palladio L", 0, 10)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("* Preencha ambos os campos com valores válidos");
        jLabel10.setFocusable(false);

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog1Layout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jDialog1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jDialog1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(61, 61, 61))
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(285, 285, 285)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel7)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel9))
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(4, 4, 4)
                .addComponent(jButton4)
                .addGap(14, 14, 14))
        );

        jDialog2.setUndecorated(true);

        jLabel11.setFont(new java.awt.Font("Palatino Linotype", 0, 18)); // NOI18N
        jLabel11.setText("Digite o nome do arquivo:");

        jTextField5.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setAlignmentY(0.2F);
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField5KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("URW Palladio L", 1, 18)); // NOI18N
        jLabel1.setText("Registradores");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        jList2.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList2);

        jList3.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList3);

        jLabel3.setFont(new java.awt.Font("URW Palladio L", 1, 16)); // NOI18N
        jLabel3.setText("   Cache Instruções: Via");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jButton1.setText("Rodar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("URW Palladio L", 1, 36)); // NOI18N
        jLabel5.setText("MIPS");

        jButton2.setText("Passo a Passo");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jList4.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList4);

        jLabel4.setFont(new java.awt.Font("URW Palladio L", 1, 16)); // NOI18N
        jLabel4.setText("   Cache Dados: Via");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel2.setFont(new java.awt.Font("URW Palladio L", 1, 18)); // NOI18N
        jLabel2.setText("Memória Principal I");
        jLabel2.setToolTipText("");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jTextField2.setToolTipText("");
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane5.setViewportView(jTextArea1);

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jButton3.setText("Inserir instruções");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("URW Palladio L", 1, 24)); // NOI18N
        jLabel6.setText("PC: 0");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        jSpinner1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jSpinner2.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jMenu1.setText("Opções");

        jMenuItem3.setText("Salvar Estado");
        jMenuItem3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem3MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Carregar Estado");
        jMenuItem4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem4MousePressed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem1.setText("Configurar Simulador");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Zerar Simulador");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                                                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGap(47, 47, 47)
                                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(258, 258, 258)
                                .addComponent(jButton3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                            .addComponent(jTextField2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(276, 276, 276)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jSpinner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jButton1)
                                .addGap(5, 5, 5)
                                .addComponent(jButton2)
                                .addGap(47, 47, 47)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        passo_a_passo();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        if (jButton1.getText() == "Rodar" || jButton1.getText() == "Desbloquear") {
            executar();
        } else {
            if (jButton1.getText() == "Parar") {
                PC.Contador = 0;
                jList1.setEnabled(true);
                jTextField1.setEnabled(true);
                jTextField2.setEnabled(true);
                jTextArea1.setEditable(true);
                jTextArea1.setFocusable(true);
                jTextArea1.setText("");
                jButton1.setText("Rodar");
                jButton2.setText("Passo a Passo");
                
            jButton3.setText("Inserir Instruções");
                this.instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
                this.dadosMem = new CacheDados(Mips.tamCache, Mips.vias);
            }
        }
        inserirInterface();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        if (!"Memória Principal D".equals(jLabel2.getText()) && PC.Contador < (Mips.tamPrincipal / 2) && jButton3.getText() == "Inserir Instruções") {
            int instNum = jTextArea1.getText().split("\n").length;
            jTextArea1.setText(MemoriaPrincipal.setMemoriaMult(PC.Contador, jTextArea1.getText()));
            if ("".equals(jTextArea1.getText())) {
                PC.Contador += instNum;
            }
            inserirInterface();
        }else{
            if(jButton3.getText() == "Salvar Log"){
                this.salvarDados();
            }
        }
    }//GEN-LAST:event_jButton3MouseClicked

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        if (evt.getClickCount() == 2) {
            jTextField2.requestFocusInWindow();
        }
        String temp = jList1.getSelectedValue();
        PC.Contador = (jList1.getSelectedIndex());
        atualizarPC();
        if (temp.split(" ").length != 1) {
            temp = temp.split(":", 2)[1].trim();
            jTextField2.setText(temp);
        } else {
            jTextField2.setText("");
        }

    }//GEN-LAST:event_jList1MouseClicked

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        boolean isInst = true;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ("".equals(jTextField2.getText())) {
                if ("Memória Principal D".equals(jLabel2.getText()) || (PC.Contador > (Mips.tamPrincipal / 2))) {
                    MemoriaPrincipal.memoria[PC.Contador] = "0";
                    jTextField2.setText("0");
                } else {
                    MemoriaPrincipal.memoria[PC.Contador] = "";
                    jTextField2.setText("");
                }
                PC.Contador++;
                inserirInterface();
            }
            if ("0".equals(jTextField2.getText())) {
                MemoriaPrincipal.memoria[PC.Contador + (Mips.tamPrincipal / 2)] = "";
                jTextField2.setText("0");
                PC.Contador++;
                inserirInterface();
            }
            if ("Memória Principal D".equals(jLabel2.getText()) || PC.Contador > (Mips.tamPrincipal / 2)) {
                isInst = false;
            }
            if (MemoriaPrincipal.setMemoria(isInst, PC.Contador, jTextField2.getText())) {
                jTextField2.setText("");
                PC.Contador++;
                inserirInterface();
            }
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jList2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList2MouseClicked
        if (evt.getClickCount() == 2) {
            jTextField1.requestFocusInWindow();
        }
        String temp = jList2.getSelectedValue();
        String temp1[] = temp.split(" ");
        temp = temp1[temp1.length - 1];
        jTextField1.setText(temp);
    }//GEN-LAST:event_jList2MouseClicked

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            switch (jList2.getSelectedIndex()) {
                case 0:
                case 1:
                case 26:
                case 27:
                    return;
                default:
                    try {
                        BancoRegs.Registradores[jList2.getSelectedIndex()] = Integer.parseInt(jTextField1.getText());
                        jTextField1.setText("");
                        inserirInterface();

                    } catch (NumberFormatException e) {
                        jTextField2.setText("");
                    }
                    break;
            }

        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        zerarSimulador();
        inserirInterface();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        mostrarConfig();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            if ("".equals(jTextField3.getText()) || "".equals(jTextField4.getText()) || Integer.parseInt(jTextField3.getText()) < 1 || ((Math.log(Integer.parseInt(jTextField4.getText())) / Math.log(2))) % 1 != 0) {
                jLabel10.setVisible(true);
            } else {
                Mips.vias = Integer.parseInt(jTextField3.getText());
                Mips.tamCache = Integer.parseInt(jTextField4.getText());
                Mips.indiceTam = (int) (Math.log(Integer.parseInt(jTextField4.getText())) / Math.log(2));
                if (this.isVisible() == false) {
                    this.instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
                    this.dadosMem = new CacheDados(Mips.tamCache, Mips.vias);
                    inserirInterface();
                    this.setVisible(true);
                }
                zerarSimulador();
                inserirInterface();
                jDialog1.setVisible(false);
            }
        } catch (NumberFormatException e) {
            jLabel10.setVisible(true);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jSlider1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseDragged
        jTextField3.setText(String.valueOf(jSlider1.getValue()));
    }//GEN-LAST:event_jSlider1MouseDragged

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyPressed
        try {
            jSlider1.setValue(Integer.parseInt(jTextField3.getText() + evt.getKeyChar()));
        } catch (NumberFormatException e) {

        }
    }//GEN-LAST:event_jTextField3KeyPressed

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        try {
            Integer.parseInt(jTextField3.getText());
        } catch (NumberFormatException e) {
            jTextField3.setText("");
        }
    }//GEN-LAST:event_jTextField3FocusLost

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked

    }//GEN-LAST:event_jLabel6MouseClicked

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        try {
            jSpinner1.commitEdit();
        } catch (java.text.ParseException e) {
        }
        if ((Integer) jSpinner1.getValue() >= Mips.vias) {
            jSpinner1.setValue(Mips.vias - 1);
        } else {
            if ((Integer) jSpinner1.getValue() < 0) {
                jSpinner1.setValue(0);
            } else {
                atualizarCaches();
            }
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        try {
            jSpinner2.commitEdit();
        } catch (java.text.ParseException e) {
        }
        if ((Integer) jSpinner2.getValue() >= Mips.vias) {
            jSpinner2.setValue(Mips.vias - 1);
        } else {
            if ((Integer) jSpinner2.getValue() < 0) {
                jSpinner2.setValue(0);
            } else {
                atualizarCaches();
            }
        }
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jDialog1ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jDialog1ComponentHidden
        if (!this.isVisible()) {
            System.exit(0);
        }
    }//GEN-LAST:event_jDialog1ComponentHidden

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        if (evt.getClickCount() == 3 && jButton1.getText() == "Rodar") {
            inicializarMemoria();
        }
        if (evt.getClickCount() == 2 && jButton1.getText() == "Rodar") {
            jLabel2.setText(" Memória Principal");
        }
        if ((evt.getClickCount() == 1 && jButton1.getText() == "Rodar")) {
            if (jLabel2.getText() == "Memória Principal I" || jLabel2.getText() == " Memória Principal") {
                jLabel2.setText("Memória Principal D");
            } else {
                if (jLabel2.getText() == "Memória Principal D" || jLabel2.getText() == " Memória Principal") {
                    jLabel2.setText("Memória Principal I");
                }
            }
        }
        inserirInterface();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        if (evt.getClickCount() == 2 && jButton1.getText() == "Rodar") {
            inicializarRegistradores();
            inserirInterface();
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        if (evt.getClickCount() == 2 && jButton1.getText() == "Rodar") {
            instrucMem = new CacheInstrucoes(Mips.tamCache, Mips.vias);
            inserirInterface();
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        if (evt.getClickCount() == 2 && jButton1.getText() == "Rodar") {
            dadosMem = new CacheDados(Mips.tamCache, Mips.vias);
            inserirInterface();
        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jDialog1WindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_jDialog1WindowOpened
        ImageIcon img = new ImageIcon("./src/Img/web_hi_res_512.png");
        jDialog1.setIconImage(img.getImage());
    }//GEN-LAST:event_jDialog1WindowOpened

    private void jTextField5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {
                //String[] split = new String[(dadoArq.length() / 1024) + 1];
                for (int i = 0; i < (dadoArq.length() / 1024); i++) {
                    //    split[i] = dadoArq.substring(1024 * i, (1024 * (i + 1)) - 1);
                    FileWriter write = new FileWriter(file.getAbsolutePath() + "/" + jTextField5.getText() + ".txt", true);
                    write.write(dadoArq.substring(1024 * i, (1024 * (i + 1))));
                    write.close();

                }
                FileWriter write = new FileWriter(file.getAbsolutePath() + "/" + jTextField5.getText() + ".txt", true);
                write.write(dadoArq.substring((dadoArq.length() / 1024) * 1024));
                write.close();
                jTextField5.setText("");
                jDialog2.setVisible(false);
            } catch (IOException ex) {
                System.out.println(ex.getCause());
            }

        }

        if ((evt.getKeyCode() == KeyEvent.VK_ESCAPE)) {
            jDialog2.setVisible(false);
        }
    }//GEN-LAST:event_jTextField5KeyPressed

    private void jMenuItem3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem3MousePressed
        salvarEstado();
    }//GEN-LAST:event_jMenuItem3MousePressed

    private void jMenuItem4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem4MousePressed
        try {
            carregarEstado();
        } catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4MousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JList<String> jList4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}

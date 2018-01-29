package com.minimalsoft.smsmx.desktop;

import com.minimalsoft.smsmx.desktop.models.SmsListResponse;
import java.awt.Color;
import java.awt.Dimension;
import static java.awt.Frame.ICONIFIED;
import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Frame.NORMAL;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/* 
 * @author David Morales (Xdave)
 */
public class MainView extends javax.swing.JFrame {

    //Crea la instancia unica de interfaz      
    private static MainView instance;
    public static final String BASE_URL = "http://sms.minimalsoft.com";

    //Declara tModel que sirve para dar formato y agrar filas a la tabla
    DefaultTableModel tModel = new DefaultTableModel(12, 6);

    //Inicializa las variables para esconder el icono en la barra de notificaciones
    TrayIcon trayIcon;
    SystemTray tray;

    String currentDate;

    Timer t = new Timer(25000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            refreshList();
            setDate();
            CheckAndSend.getInstance().readConfigs();
            CheckAndSend.getInstance().run();
        }
    });

    private void refreshList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WServices apiService = retrofit.create(WServices.class);
        Call<SmsListResponse> call = apiService.getSmsList("100", "0", currentDate);
        call.enqueue(new Callback<SmsListResponse>() {
            @Override
            public void onResponse(Call<SmsListResponse> call, Response<SmsListResponse> rspns) {
                if (rspns.isSuccessful()) {
                    if (rspns.body().getCode().equals("200")) {
                        updateTable(rspns.body());
                    } else {
                        showIndoDialog("Error!", "Error al recibir la respuesta", 0);
                    }
                } else {
                    showIndoDialog("Error!", "Error en el servidor", 0);
                }
            }

            @Override
            public void onFailure(Call<SmsListResponse> call, Throwable thrwbl) {
                showIndoDialog("Error!", "Favor de verificar la conexión a internet y vovler a intentar.", 0);
            }
        });
    }

    protected MainView() throws IOException {
        initComponents();
        IniciarFrame();
        IniciarTabla();
        setDate();
        //Minimize();        
        t.setInitialDelay(1000);
        t.start();
    }

    public final void IniciarTabla() {
        String header[] = new String[]{"Numero", "Mensaje", "Estado", "Hora"};
        tModel.setColumnIdentifiers(header);
        C_Table.setModel(tModel);
        C_Table.setShowGrid(true);
        C_Table.getColumnModel().getColumn(0).setPreferredWidth(28);
        C_Table.getColumnModel().getColumn(1).setPreferredWidth(200);
        C_Table.getColumnModel().getColumn(2).setPreferredWidth(50);
        C_Table.getColumnModel().getColumn(3).setPreferredWidth(50);
        C_Table.setBackground(Color.white);
    }

    public final void setDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate = dateFormat.format(date);
        C_Date.setText(dateFormat.format(date));
    }

    public final void IniciarFrame() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    public final void Minimize() {

        if (SystemTray.isSupported()) {            
            tray = SystemTray.getSystemTray();
            Image image = new ImageIcon(getClass().getResource("main_icon.png")).getImage();
            ActionListener exitListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    LoginView confirmar;
                    try {
                        confirmar = new LoginView();
                        confirmar.setVisible(true);
                    } catch (Throwable ex) {
                        Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Cerrar");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem = new MenuItem("Expandir");
            defaultItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            //trayIcon = new TrayIcon(image, "Cliente", popup);
            //trayIcon.setImageAutoSize(true);
        } else {
            //System.out.println("system tray no soportado");
            JOptionPane.showMessageDialog(null, "system tray no soportado", "Algo salio mal!!", 0);
        }
        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == ICONIFIED) {
                    try {
                        tray.remove(trayIcon);
                        tray.add(trayIcon);
                        setVisible(false);
                        //System.out.println("Agregado al tray");  
                        trayIcon.displayMessage("Atención!", "Se ha minimizado el Cliente SMS", TrayIcon.MessageType.INFO);
                    } catch (Throwable ex) {
                        //System.out.println("unable to add to tray");                    
                        JOptionPane.showMessageDialog(null, "Ocurrio un problema al minimizar", "Algo salio mal!!", 0);
                    }
                }
                if (e.getNewState() == 7) {
                    try {
                        tray.remove(trayIcon);
                        tray.add(trayIcon);
                        setVisible(false);
                        //System.out.println("added to SystemTray");
                        trayIcon.displayMessage("Atención!", "Se ha minimizado el Cliente SMS", TrayIcon.MessageType.INFO);
                    } catch (Throwable ex) {
                        //System.out.println("unable to add to system tray");
                        JOptionPane.showMessageDialog(null, "Ocurrio un problema al minimizar", "Algo salio mal!!", 0);
                    }
                }
                if (e.getNewState() == MAXIMIZED_BOTH) {
                    //tray.remove(interfaz.trayIcon);
                    setVisible(true);
                    //System.out.println("Tray icon removed");
                }

                if (e.getNewState() == NORMAL) {
                    //tray.remove(interfaz.trayIcon);
                    setVisible(true);
                    //System.out.println("Tray icon removed");
                }

            }
        });

        try {
            tray.add(trayIcon);
            this.setIconImage(ImageIO.read(new File("/main_icon.png"))); 
            setIconImage(new ImageIcon(getClass().getResource("main_icon.png")).getImage());
        } catch (Throwable ex) {
            FileWriter fw;
            try {
                ex.printStackTrace();                
                Date actual = new Date();
                fw = new FileWriter("C:\\Ruta\\Cliente\\res\\ErrorLog.txt", true);
                fw.append(actual + "   " + ex + "\t Error en Interfaz  ****Seccion Poner Icono\n");
                fw.append(System.getProperty("line.separator"));
                fw.append(System.getProperty("line.separator"));
                fw.flush();
                fw.close();

            } catch (Throwable ex1) {

            }
        }
    }

    public void showIndoDialog(String title, String message, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }

    private void updateTable(SmsListResponse body) {
        txt_alert.setText(body.getAlertSent().toString());
        txt_diffusion.setText(body.getDiffusionSent().toString());
        txt_daily.setText(body.getTotalDaily().toString());
        txt_failed.setText(body.getFailed().toString());

        for (int i = tModel.getRowCount() - 1; i >= 0; i--) {
            tModel.removeRow(i);
        }

        for (SmsListResponse.Data row : body.getData()) {
            tModel.addRow(new Object[]{
                row.getNumber(),
                row.getMessage(),
                row.getStatus(),
                row.getTimestamp()});

            for (int i = 0; i <= body.getData().size(); i++) {
                C_Table.setDefaultRenderer(Object.class, new Cliente_Renderer());
            }

            C_Table.repaint();
        }
    }

    public static MainView getInstance() throws IOException {
        if (instance == null) {
            instance = new MainView();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        C_Date = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        C_Table = new javax.swing.JTable();
        C_ShowN = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        txt_daily = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_failed = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_diffusion = new javax.swing.JLabel();
        btn_refresh = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txt_alert = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente");

        C_Date.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        C_Date.setText("Fecha");

        C_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Hora", "Eco", "Chofer", "Ramal", "Hr. Envio", "Estado"
            }
        ));
        C_Table.setGridColor(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(C_Table);

        C_ShowN.setSelected(true);
        C_ShowN.setText("Mostrar notificaciones");

        jLabel1.setText("Mensajes Enviados");

        txt_daily.setText("0");

        jLabel3.setText("Errores");

        txt_failed.setForeground(new java.awt.Color(255, 0, 0));
        txt_failed.setText("0");

        jLabel2.setText("Mensajes Difusion");

        txt_diffusion.setText("0");

        btn_refresh.setText("Refrescar");

        jLabel4.setText("Mensajes Alerta");

        txt_alert.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(C_ShowN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_refresh)
                        .addGap(6, 6, 6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(C_Date, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(74, 74, 74)
                                .addComponent(txt_failed, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txt_daily, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_alert, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_diffusion, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(C_Date)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txt_alert))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(txt_daily)))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_diffusion)
                    .addComponent(jLabel3)
                    .addComponent(txt_failed))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(C_ShowN)
                    .addComponent(btn_refresh))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Throwable ex) {
            FileWriter fw;
            try {
                //ex.printStackTrace();                
                Date actual = new Date();
                fw = new FileWriter("C:\\Ruta\\Cliente\\res\\ErrorLog.txt", true);
                fw.append(actual + "   " + ex + "\t Error en Interfaz  ****Seccion Main\n");
                fw.append(System.getProperty("line.separator"));
                fw.append(System.getProperty("line.separator"));
                fw.flush();
                fw.close();

            } catch (Throwable ex1) {

            }
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    instance = new MainView();
                } catch (Throwable ex) {
                    System.err.println(ex);
                }
                instance.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel C_Date;
    protected javax.swing.JCheckBox C_ShowN;
    protected javax.swing.JTable C_Table;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JLabel txt_alert;
    protected javax.swing.JLabel txt_daily;
    protected javax.swing.JLabel txt_diffusion;
    protected javax.swing.JLabel txt_failed;
    // End of variables declaration//GEN-END:variables
}

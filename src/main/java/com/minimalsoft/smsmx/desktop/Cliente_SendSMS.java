

package com.minimalsoft.smsmx.desktop;

import com.minimalsoft.smsmx.desktop.CheckAndSend;
import java.awt.TrayIcon;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import org.ini4j.Wini;

/*
 * @author David
 */
public class Cliente_SendSMS extends Thread {
    
   private CheckAndSend SendDifusion = null;
   private MainView interfaz = null;
   //SendDifusion = CheckAndSend.getInstance();
   
   
   
   private static Cliente_SendSMS instance = null;
   protected Cliente_SendSMS() {
      // Exists only to defeat instantiation.
   }
   public static Cliente_SendSMS getInstance() {
      if(instance == null) {
         instance = new Cliente_SendSMS();
      }
      return instance;
   }
  
        
    //Inicializa las config
    String config = "C:\\Ruta\\Cliente\\res\\Config.ini";    
    String ruta,bits, Sbits;    
    int tolerancia;     
        
    //Declara lo necesario para Pushbullet
//    String token,IDEN,pass,user;    
//    
//    public final void LeerConfig ()
//    {
//        try
//        {
//            Wini ini = new Wini(new File(config));
//            ruta = ini.get("iniciales", "rutaDB");        
//            tolerancia =ini.get("iniciales", "tolerancia",int.class);
//
//            token = ini.get("servidor1", "token");
//            IDEN = ini.get("servidor1", "IDEN");
//
//            bits = ini.get("RunTime", "Seleccionado"); 
//            if(bits.equals("32"))
//            Sbits = ini.get("RunTime", "32"); 
//            else
//                if(bits.equals("64"))
//                    Sbits = ini.get("RunTime", "64"); 
//                else
//                    Sbits = ini.get("RunTime", "32");
//            
//        }
//        catch (Throwable ex)
//        {
//            FileWriter fw;
//            try {
//                //ex.printStackTrace();                
//                Date actual = new Date ();
//                fw = new FileWriter("C:\\Ruta\\Cliente\\res\\ErrorLog.txt", true);
//                fw.append(actual+"   "+ex+"\t Error leyendo el config.ini  ****Seccion LeerConfig()\n");
//                fw.append(System.getProperty("line.separator"));
//                fw.append(System.getProperty("line.separator"));
//                fw.flush();
//                fw.close();
//                
//            } catch (Throwable ex1) {
//                JOptionPane.showMessageDialog(interfaz, "Error" ,"No se encuentre el archivo ErrorLog.txt", 0);
//            }
//        }
//    }
//    
//    
//    public void run ()
//    {
//        try
//        {     
//            interfaz = MainView.getInstance();
//            interfaz.t.stop();
//            //Se crea la coneccion y el Statement
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            String filename = ruta;            
//            String database = "jdbc:odbc:Driver={Microsoft Access Driver "+Sbits+"};DBQ=";
//            database+= filename.trim() + ";DriverID=22;READONLY=true}";
//            
//            Connection conn = DriverManager.getConnection(database, "", "");
//            Connection conn1 = DriverManager.getConnection(database, "", "");                     
//           
//            Statement s = conn.createStatement();
//            Statement s1 = conn1.createStatement();
//            
//                                    
//            //Se consulta la tabla       
//            s.execute("SELECT * FROM MessageOut");
//            ResultSet rs = s.getResultSet();
//            
//            interfaz.enviados= 0;
//            
//            
//            if (rs.next())
//            {
//                s.execute("SELECT * FROM MessageOut");
//                rs = s.getResultSet();
//                // Lo primero que hace si hay mensajes es detener el timer de 1 minuto
//                
//                
//                //Si y solo si hay algo en la tabla se inicializan todas las variables
//                //client = new PushbulletClient(token);
//                boolean hayInternet = true,outOfTime=false;  
//                
//                
//                
//                int timeA=0,timeS=0,diaS=0,diaA=0,
//                    Id, horasA= 0, horasS = 0,minutosA= 0,minutosS= 0, IdTipo;
//                                                                             
//                String MessageTo, MessageText, Eco, Operador, Ramal, Status, Tipo;
//                
//                //Necesarios para MessageLog
//                String MessageFrom, UserInfo, Obs, PC;
//                int UserId, IdSalida, IdEco, IdOperador, IdRamal, Vuelta, IdCorte, IdDia, IdStatus,IdDifusion;                           
//            
//                
//                while(rs.next())
//                {
//                    Calendar SendTime = Calendar.getInstance();
//                    SimpleDateFormat  dateFormat = new SimpleDateFormat("HH:mm:ss");
//                    SimpleDateFormat  dateFormat1 = new SimpleDateFormat("HH:mm");
//                    Date Hora, HoraMov, FechaV, Fecha,FechaMov;
//                    Date actual = new Date ();   
//                    //Necesarios para el mensaje
//                    Id = rs.getInt("Id");
//                    Hora= rs.getTime("Hora");                    
//                    String hora=dateFormat1.format(Hora);
//                    MessageTo = rs.getString("MessageTo");
//                    MessageText = rs.getString("MessageText");
//                    Eco = rs.getString("Eco");
//                    Operador = rs.getString("Operador");
//                    Ramal = rs.getString("Ramal");                    
//
//                    //Necesarios para MessageLog                    
//                    HoraMov = rs.getTime("HoraMov");
//                    FechaV = rs.getDate("FechaV");
//                    Fecha = rs.getDate("Fecha");
//                    FechaMov = rs.getDate("FechaMov");
//                    UserId = rs.getInt("UserId");
//                    UserInfo  = rs.getString("UserInfo");
//                    IdSalida  = rs.getInt("IdSalida");
//                    IdEco  = rs.getInt("IdEco");
//                    IdOperador  = rs.getInt("IdOperador");
//                    PC = rs.getString("PC");
//                    IdRamal = rs.getInt("IdRamal");                
//                    Vuelta = rs.getInt("Vuelta");
//                    Obs = rs.getString("Obs");                
//                    IdCorte = rs.getInt("IdCorte");
//                    IdDia = rs.getInt("IdDia");
//                    Status=rs.getString("Status"); 
//                    IdStatus=rs.getInt("IdStatus");
//                    IdTipo=rs.getInt("IdTipo");
//                    Tipo=rs.getString("Tipo");
//                    IdDifusion=rs.getInt("IdDifusion");
//                    
//                    //Se ponen los que no obtengo                    
//                    String STime= dateFormat.format(SendTime.getTime());
//                    MessageFrom="Servidor1";
//                    
//                    
//                    //System.out.println("Id: " + Id+ "Hora: "+hora + " eco: "+Eco + " Numero: "+MessageTo + " Operador: "+ Operador);
//                    
//                    
//                    //Se hace la validacion de hora y dia
//                    horasA=actual.getHours();
//                    minutosA=actual.getMinutes();
//                    diaA=actual.getDate();
//                    
//                    horasA=horasA*60;
//                    timeA=horasA+minutosA;
//                    
//                    diaS=FechaV.getDate();                    
//                    horasS=Hora.getHours()*60;
//                    timeS=horasS+Hora.getMinutes();
//                    
//                    //Si pasa la validacion se intenta enviar el SMS
//                    if(diaS>=diaA && timeS>=(timeA-tolerancia))
//                    {
//                        outOfTime=false;                        
//                        //ya que paso se hace el intento de coneccion al servidor para ver si hay internet
//                         try 
//                        {                            
//                            client.getDevices();
//                            hayInternet=true;
//                        } 
//                        catch (UnknownHostException e) 
//                        {
//                            interfaz.trayIcon.displayMessage("Error!", "No hay conexion con el servidor", TrayIcon.MessageType.ERROR);
//                            hayInternet=false;
//                            interfaz.tErrores++;
//                        }
//                        //Si hay internet se envia el mensaje
//                        if(hayInternet==true)
//                        {
//                            client.sendNote(true, IDEN, "Salida", MessageTo+" "+MessageText);
//                            interfaz.tEnviados++;
//                            interfaz.enviados++;
//                            String cadena=Eco+" - "+hora+" - "+Ramal;
//                            
//                            for(int i=4;i>0;i--)
//                            {
//                                String temp=interfaz.mHistory[i-1];
//                                interfaz.mHistory[i]=temp;
//                            }
//                            interfaz.mHistory[0]=cadena;
//
//                            if(interfaz.C_ShowN.isSelected())                            
//                            interfaz.trayIcon.displayMessage("Ultimos mensajes enviados",
//                            interfaz.mHistory[0]+"\n"+interfaz.mHistory[1]+"\n"+interfaz.mHistory[2]+"\n"
//                            +interfaz.mHistory[3]+"\n"+interfaz.mHistory[4]+"\n", TrayIcon.MessageType.INFO);                                                     
//
//                        }
//                    }
//                    else
//                    {
//                        outOfTime=true;
//                        interfaz.tErrores++;
//                    }
//                    
//                    //Se determina su estado
//                    if (hayInternet==true && outOfTime==false )
//                    {
//                        IdStatus=3;
//                        Status="Enviado";
//                    }
//                    if (outOfTime==true)
//                    { 
//                        IdStatus=20;
//                        Status="Out Time";
//                    }
//                    if (hayInternet==false)
//                    {                        
//                        IdStatus=20;
//                        Status="NT Error";
//                    } 
//                    
//                    //Se agrega una linea a la tabla
//                    interfaz.tModel.addRow(new Object[] { "", "", "","", "", "" });  
//                            
//                    //Se ordena la tabla   
//                    for (int i =interfaz.fila;i>0;i--)
//                    {
//                        String temp1= interfaz.C_Table.getValueAt(i-1, 0)+"";
//                        String temp2= interfaz.C_Table.getValueAt(i-1, 1)+"";
//                        String temp3= interfaz.C_Table.getValueAt(i-1, 2)+"";
//                        String temp4= interfaz.C_Table.getValueAt(i-1, 3)+"";
//                        String temp5= interfaz.C_Table.getValueAt(i-1, 4)+"";
//                        String temp6= interfaz.C_Table.getValueAt(i-1, 5)+"";
//                        interfaz.C_Table.setValueAt(temp1, i, 0);
//                        interfaz.C_Table.setValueAt(temp2, i, 1);
//                        interfaz.C_Table.setValueAt(temp3, i, 2);
//                        interfaz.C_Table.setValueAt(temp4, i, 3);                
//                        interfaz.C_Table.setValueAt(temp5, i, 4);                
//                        interfaz.C_Table.setValueAt(temp6, i, 5);                
//                    }                   
//                    
//                    
//                    interfaz.C_Table.setValueAt(hora+"", 0, 0);
//                    interfaz.C_Table.setValueAt(Eco+"", 0, 1);
//                    interfaz.C_Table.setValueAt(Operador+"", 0, 2);
//                    interfaz.C_Table.setValueAt(Ramal+"", 0, 3);
//                    interfaz.C_Table.setValueAt(STime+"", 0, 4);                                       
//                    interfaz.C_Table.setValueAt(Status+"", 0, 5);
//                                       
//                    for(int i=0;i<=interfaz.fila;i++)
//                    {
//                        interfaz.C_Table.setDefaultRenderer(Object.class, new Cliente_Renderer());
//                    }
//                    interfaz.C_Table.repaint();
//                    //interfaz.C_Table.setSelectionBackground(Color.yellow);
//                                                            
//                    //Se escribe en Log
//                    String addRow = "INSERT INTO MessageLog (SendTime,MessageTo,MessageFrom,MessageText,"
//                            + "UserId,UserInfo,IdSalida,IdEco,Eco,IdOperador,Operador,IdRamal,Ramal,Hora,"
//                            + "HoraMov,PC,FechaV,Fecha,Vuelta,Obs,FechaMov,IdCorte,IdDia,IdStatus,Status, IdTipo, Tipo, IdDifusion) "
//                            
//                            + "VALUES (#"+STime+"#,'"+MessageTo+"','"+MessageFrom+"','"+MessageText
//                            +"','"+UserId+"','"+UserInfo+"','"+IdSalida+"','"+IdEco+"','"+Eco+"','"+IdOperador
//                            +"','"+Operador+"','"+IdRamal+"','"+Ramal+"',#"+Hora+"#,#"+HoraMov+"#,'"+PC
//                            +"',#"+FechaV+"#,#"+Fecha+"#,'"+Vuelta+"','"+Obs+"',#"+FechaMov+"#,'"+IdCorte
//                            +"','"+IdDia+"','"+IdStatus+"','"+Status+"','"+IdTipo+"','"+Tipo+"','"+IdDifusion+"')";
//                                                                                                  
//                    s1.execute(addRow);
//                    
//                    
//                    //Se borra la linea de Out                    
//                    s1.execute("DELETE FROM MessageOut WHERE Id in ("+Id+")");
//                    
//                    interfaz.fila++;  
//                    interfaz.C_Errores.setText(interfaz.tErrores+"");
//                    interfaz.C_Enviados.setText(interfaz.tEnviados+"");
//                    
//                    if(interfaz.enviados>0)
//                     {                                
//                           synchronized(this){
//                            
//                                //System.out.println("esperando..");
//                                this.wait(17000);
//                            
//                           }
//                     //System.out.println("Enviando el siguiente");
//                     }
//                                        
//                 
//                }
//            }
//            else
//            {
//               //System.out.println("No hay mensajes"); 
//            }
//               
//            // close and cleanup
//            rs.close();
//            s.close();
//            conn.close();
//            s1.close();
//            conn1.close();
//            
//            
//            
//        }        
//        catch( Throwable ex)
//        {
//            FileWriter fw;
//            try {
//                //ex.printStackTrace();                
//                Date actual = new Date ();
//                fw = new FileWriter("C:\\Ruta\\Cliente\\res\\ErrorLog.txt", true);
//                fw.append(actual+"   "+ex+"\t Error en SendSMS  ****Seccion principal\n");
//                fw.append(System.getProperty("line.separator"));
//                fw.append(System.getProperty("line.separator"));
//                fw.flush();
//                fw.close();
//                
//            } catch (Throwable ex1) {
//                JOptionPane.showMessageDialog(interfaz, "Error" ,"No se encuentre el archivo ErrorLog.txt", 0);
//            }
//            
//        } 
//        if(interfaz.enviados>1)
//        {
//            
////            System.out.println("Envio mas de dos");
//            interfaz.t.start();
//            
//        }
//        else
//        {
//            interfaz.t.stop();
////            System.out.println("Envio solo uno o ninguno");            
//            //Codigo para enviar difusion
//            SendDifusion = new CheckAndSend();
//            SendDifusion.LeerConfig();
//            SendDifusion.start();
//            //interfaz.t.restart();
//        }                                                                                                      
//    } 
}

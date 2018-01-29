package com.minimalsoft.smsmx.desktop;

import static com.minimalsoft.smsmx.desktop.MainView.BASE_URL;
import com.minimalsoft.smsmx.desktop.models.BaseResponse;
import com.minimalsoft.smsmx.desktop.models.SmsListResponse;
import com.minimalsoft.smsmx.desktop.utils.Logger;
import java.awt.TrayIcon;
import java.io.File;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import org.ini4j.Wini;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * @author David
 */
public class CheckAndSend extends Thread {

    private MainView interfaz = null;
    private static CheckAndSend instance = null;
    public static final String BASE_URL = "http://sms.minimalsoft.com";

    protected CheckAndSend() {
        // Exists only to defeat instantiation.
    }

    public static CheckAndSend getInstance() {
        if (instance == null) {
            instance = new CheckAndSend();
        }
        return instance;
    }

    //Inicializa las config
    String config = "C:\\Ruta\\smsmx\\res\\config.ini";
    String dbPath, bits, Sbits;
    int tolerancia;

    public final void readConfigs() {
        try {
            Wini ini = new Wini(new File(config));
            dbPath = ini.get("iniciales", "rutaDB");
        } catch (Exception ex) {
            Logger.error("readConfigs", ex);
        }
    }

    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WServices apiService = retrofit.create(WServices.class);

        try {
            interfaz = MainView.getInstance();
            interfaz.t.stop();
        } catch (Exception ex) {
            Logger.error("run", ex);
        }
        try {
            //Se crea la conexion y el Statement
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbPath + ";memory=true");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM SMSMx WHERE idEstatus <> 3;");

            while (rs.next()) {
                int idSMS = rs.getInt("Id");
                String jsonString = rs.getString("jsonString");

                Call<BaseResponse> call = apiService.sendSMS(jsonString);
                call.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> rspns) {
                        if (rspns.isSuccessful()) {
                            if (rspns.body().getCode().equals("200")) {
                                try {
                                    st.execute("UPDATE SMSMx SET SMSMx.IdEstatus = 3, SMSMx.Estatus = \"Enviado\", SMSMx.FechaProcesado = Now() WHERE (((SMSMx.Id)="+idSMS+"));");
                                } catch (SQLException ex) {
                                    Logger.error("OnResponse: updateMessageStatus", ex);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable thrwbl) {
                        //Avisar 
                    }
                });
            }

            // close and cleanup
            rs.close();
            st.close();
            conn.close();

        } catch (Exception ex) {
            Logger.error("sendMessage", ex);
        }
    }

}

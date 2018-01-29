/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.minimalsoft.smsmx.desktop.utils;

import java.io.FileWriter;
import java.util.Date;

/**
 *
 * @author DaveMorales
 */
public class Logger {

    private static boolean shouldPrint = true;

    public static void error(String title, Exception ex) {
        if (shouldPrint) {
            ex.printStackTrace();
        }
        try {            
            FileWriter fw;
            Date date = new Date();
            fw = new FileWriter("C:\\Ruta\\Cliente\\res\\log.txt", true);
            fw.append(date + "\t" + title+ ": " + ex.toString());
            fw.append(System.getProperty("line.separator"));
            fw.append(System.getProperty("line.separator"));
            fw.flush();
            fw.close();
        } catch (Exception ex1) {
            
        }
    }
}

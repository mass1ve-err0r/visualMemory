/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.2021
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


public class Preferences {

    private static Preferences INSTANCE;
    private static Properties defaults = new Properties();

    private Preferences() {
        readPreferencesFile();
    }

    public static synchronized Preferences sharedInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Preferences();
        }
        return INSTANCE;
    }

    public synchronized String getValueForKey(String key) { return defaults.getProperty(key); }

    public synchronized void setValueForKey(String key, int value) {
        defaults.setProperty(key, String.valueOf(value));
    }

    private synchronized void readPreferencesFile() {
        try (FileInputStream fis = new FileInputStream("visualMemory.prefs")) {
            defaults.load(fis);
        } catch (IOException e) {
            System.out.println("[visualMemory][E]: Preference file not found! Exiting...");
            System.exit(1);
        }
    }

    public synchronized boolean synchronize() {
        try (FileOutputStream fos = new FileOutputStream("visualMemory.prefs")) {
            defaults.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        readPreferencesFile();
        return true;
    }


    /* End */
}

package main.java.gui.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserDefaults {

    // Properties

    private String filePath;
    private Properties properties;

    // Constructors

    public static UserDefaults sharedUserDefaults = new UserDefaults(Constants.USER_DEFAULTS_PATH);

    public UserDefaults(String filePath) {
        this.filePath = filePath;
        this.properties = new Properties();
        this.loadFromFile();
    }

    // Read / write file

    private boolean loadFromFile() {
        FileInputStream input = null;
        try {
            input = new FileInputStream(this.filePath);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: User defaults file was not found");
            System.out.println("User defaults file => " + this.filePath);
            return false;
        }
        try {
            this.properties.load(input);
        } catch (IOException e) {
            System.out.println("ERROR : User defaults cannot be loaded");
            System.out.println("User defaults file => " + this.filePath);
            return false;
        }
        return true;
    }

    private boolean saveToFile() {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(this.filePath);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: User defaults file cannot be created");
            System.out.println("User defaults file => " + this.filePath);
            return false;
        }
        try {
            this.properties.store(output, "");
        } catch (IOException e) {
            System.out.println("ERROR : User defaults cannot be stored");
            System.out.println("User defaults file => " + this.filePath);
            return false;
        }
        return true;
    }

    // Access defaults

    public int getInt(String key) {
        String value = this.properties.getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NullPointerException e) {
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double getDouble(String key) {
        String value = this.properties.getProperty(key);
        try {
            return Double.parseDouble(value);
        } catch (NullPointerException e) {
            return 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public boolean getBool(String key) {
        String value = this.properties.getProperty(key);
        return Boolean.parseBoolean(value);
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    // Registers defaults

    public void setInt(String key, int value) {
        String string = Integer.toString(value);
        this.setString(key, string);
    }

    public void setDouble(String key, double value) {
        String string = Double.toString(value);
        this.setString(key, string);
    }

    public void setBool(String key, boolean value) {
        String string = Boolean.toString(value);
        this.setString(key, string);
    }

    public void setString(String key, String value) {
        this.properties.setProperty(key, value);
    }

    public void setIntIfNoValue(String key, int value) {
        if (!this.properties.containsKey(key)) {
            this.setInt(key, value);
        }
    }

    public void setDoubleIfNoValue(String key, double value) {
        if (!this.properties.containsKey(key)) {
            this.setDouble(key, value);
        }
    }

    public void setBoolIfNoValue(String key, boolean value) {
        if (!this.properties.containsKey(key)) {
            this.setBool(key, value);
        }
    }

    public void setStringIfNoValue(String key, String value) {
        if (!this.properties.containsKey(key)) {
            this.setString(key, value);
        }
    }

    // Synchronizing

    public boolean synchronize() {
        return this.saveToFile();
    }

}

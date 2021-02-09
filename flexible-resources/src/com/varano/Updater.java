//Thomas Varano
//Mar 15, 2018

package com.varano;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * A modular updater for applications running on java. Please look through code to make sure the application is 
 * up to specification.
 * <br>Specifications are as follows: Must be exported with the src jar in the same package with the name "src.jar",
 * <br><strong>Arguments ideally run as such</strong>, however are not necessary. This can be run without arguments. 
 * <ul><li><code>-v</code> or <code>--verbose</code> allows for the logs to print. Put this argument first.</li>
 * <li>Location of the application. This can come next and should be the entire path of the application. Spaces are 
 * accounted for.
 * @author Thomas Varano
 *
 */
public class Updater {
   
   /**
    * Name of the app.
    */
   private static final String APP_NAME = "Agenda";
  
   /**
    * version of the app.
    */
   private static final String version = "1.8.2";

   /**
    * the main class with all package references. If using the default package, just put the class name.
    * Replace dots with "_"
    */
   private static final String MAIN_CLASS = "com_varano_managers_Agenda";
   
   
   //--------------------DO NOT TOUCH THESE VARIABLES--------------------
   private static String home = System.getProperty("user.home");   
   private static final String CFG_VERSION_KEY = "app.version=";
   private static final String CFG_PREFID_KEY = "app.preferences.id="; 
   private static final String CFG_APPID_KEY = "app.identifier="; 
   private static final String CFG_MAIN_CLASS_KEY = "app.mainclass="; 
   private static final String CFG_JAR_KEY = "app.mainjar="; 
   private static final String INFO_VERSION_KEY_A = "CFBundleShortVersionString";
   private static final String INFO_VERSION_KEY_B = "CFBundleVersion";
   private static final String THIS_LOCATION = System.getProperty("java.class.path");
   private static final char sep = File.separatorChar;
   
   private static boolean verbose;
   //--------------------------------------------------------------------
   
   private static String askHome() {
      log("ask home");
      JLabel userHome = new JLabel(Updater.home + "/");
      JLabel suffix = new JLabel("/"+APP_NAME + ".app");
      JTextField f = new JTextField();
      JPanel p = new JPanel();
      f.setPreferredSize(new java.awt.Dimension(350, 25));
      p.add(userHome); p.add(f); p.add(suffix);
      JOptionPane.showMessageDialog(null, p, "Where is the Application Kept?", JOptionPane.INFORMATION_MESSAGE, null);
      return Updater.home + "/" + f.getText() + suffix.getText();
   }
   
   /**
    * @param args if using internal update software, give the location of the app as an argument
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            update(args);
         }  
      });
   }
   
   /**
    * complete update method. 
    * @param args specified in the javadoc of the class.
    */
   public static void update(String[] args) {
      verbose = args.length == 0 ? false : args[0].equals("-v") || args[0].equals("--verbose");
      //Account for spaces in args
      int start = (verbose) ? 1 : 0;
      String argConcat = "";
      for (int i = start; i < args.length; i++) {
         if (i == start) argConcat += args[i];
         else argConcat += " " + args[i];
      }

      // Quit Application
      String newJarName = APP_NAME + ".jar";
      boolean appWasRunning;
      try {
         Process quit = new ProcessBuilder("killall", APP_NAME).start();
         appWasRunning = quit.waitFor() == 0;
      } catch (IOException | InterruptedException e1) {
         e1.printStackTrace();
         appWasRunning = false;
      }
      log("running: "+appWasRunning);
      
      //find the location of the application
      String home;
      if (argConcat == "") {
         home = "/Applications/" + APP_NAME + ".app";
      } else home = argConcat;
      log("home given: "+home);
      if (!new File(home).canExecute()) {
         if (!new File(home).canExecute()) 
            home = askHome();
      }
      home = Updater.home+ "/Desktop/" + APP_NAME + ".app";
      log("home accepted as: "+home);
      String javaHome = home + "/Contents/Java/";
      String dest = javaHome + APP_NAME + ".jar";
      
      //delete old jar
       if (!new File(dest).delete()) {
          showFailure("Error removing old jar at\n"+dest);
               System.exit(1);
       }
    }
      
      //trying to copy
      try {
         transfer(dest);
      } catch (IOException e) {
         log("attempt to copy failed");
         showFailure("Error Copying Files to "+dest);
         System.exit(1);
      }
      
      //change references in cfg
      try {
         String cfg = "";
         Scanner in = new Scanner(new File(javaHome + APP_NAME + ".cfg"));
         while (in.hasNextLine())
            cfg += in.nextLine() + "\n";
         in.close();
         BufferedWriter bw = new BufferedWriter(new FileWriter(new File(javaHome + APP_NAME + ".cfg")));
         in = new Scanner(cfg);
         while (in.hasNextLine()) {
            String ln = in.nextLine();
            if (ln.contains(CFG_VERSION_KEY))
               ln = CFG_VERSION_KEY + version;
            else if (ln.contains(CFG_JAR_KEY))
               ln = CFG_JAR_KEY + newJarName;
            else if (ln.contains(CFG_PREFID_KEY)) 
               ln = CFG_PREFID_KEY + MAIN_CLASS.replace('_', sep);
            else if (ln.contains(CFG_APPID_KEY))
               ln = CFG_APPID_KEY + MAIN_CLASS.replace('_', '.');
            else if (ln.contains(CFG_MAIN_CLASS_KEY))
               ln = CFG_MAIN_CLASS_KEY + MAIN_CLASS.replace('_', sep);
            bw.write(ln + "\n");
         }
         bw.close();
         in.close();
      } catch (IOException e1) {
         e1.printStackTrace();
         showFailure("Cannot update configuration references.");
      }
      
      //change references in info.plist
      try {
         String info = ""; 
         Scanner in = new Scanner(new File(home + "/Contents/Info.plist"));
         while (in.hasNextLine())
            info += in.nextLine() + "\n";
         in.close();
         BufferedWriter bw = new BufferedWriter(new FileWriter(new File(home + "/Contents/Info.plist")));
         in = new Scanner(info);
         while (in.hasNextLine()) {
            String ln = in.nextLine();
            if (ln.contains(INFO_VERSION_KEY_A) || ln.contains(INFO_VERSION_KEY_B)) {
               bw.write(ln + "\n");
               bw.write("<string>" + version + "</string>\n");
               in.nextLine();
            } else bw.write(ln + "\n");
         }
         bw.close();
         in.close();       
      } catch (Exception e1) {
         e1.printStackTrace();
         showFailure("Cannot update info.plist references.");
      }
      
      //if app was running, open a new instance
      if (appWasRunning) {
         try {
            //wait for processes to complete. Sometimes it doesn't work
            Thread.sleep(500);
            Process restart = new ProcessBuilder("open", home).start();
            log("opening..." + home);
            log("restart: " + restart.waitFor());
         } catch (IOException | InterruptedException e) {
            e.printStackTrace();
         }
      }
      
      //self destruct
      if (THIS_LOCATION.contains(".jar")) {
         try {
            Process delete = new ProcessBuilder("rm", THIS_LOCATION).start();
            log("delete: " + delete.waitFor());
         } catch (IOException | InterruptedException e) {
            e.printStackTrace();
         }
      }
      System.out.println("Process Complete.");
      System.exit(0);
   }
   
   public static ImageIcon getIcon(String localPath) {
      try {
         return new ImageIcon(Updater.class.getResource(localPath));
      } catch (NullPointerException e) {
         e.printStackTrace();
         return null;
      }
   }
   
   private static void showFailure(String reason) {
      JOptionPane.showMessageDialog(null, "Unable to update " + APP_NAME + ".\n"
            + reason + "\nPlease try again.", APP_NAME + "Updater", JOptionPane.ERROR_MESSAGE, null);
   }
   private static void transfer(String dest) throws IOException {
      copyFileUsingStream(Updater.class.getResourceAsStream("src.jar"), new File(dest));
   }
   
   private static void copyFileUsingStream(InputStream source, File dest) throws IOException {
      OutputStream os = null;
      log("copying from: " + source);
      log("to: " + dest);
      try {
          os = new FileOutputStream(dest);
          byte[] buffer = new byte[1024];
          int length;
          while ((length = source.read(buffer)) > 0) {
              os.write(buffer, 0, length);
          }
      } finally {
          source.close();
          os.close();
      }
  }
   
   private static void log(String s) {
      if (verbose) System.out.println(s);
   }
}

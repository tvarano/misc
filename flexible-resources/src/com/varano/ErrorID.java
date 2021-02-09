package com.varano;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StreamCorruptedException;

import javax.swing.JOptionPane;

//Thomas Varano
//Oct 24, 2017

/**
 * Shows errors through JOptionPanes, and handles everything. if there is an error, simply show it here and everything 
 * will be taken care of.
 * <ul>
 * <li>Go through the enums and change the messages as you see fit. 
 * They will be used in messages, so you can add as many errors as you want.
 * Account for them in the {@link #getType(Throwable)} method
 * <li>change the location for <code>Agenda.FileHandler.LOG_ROUTE</code> so it will work. As of now it is <code>null</code>
 * <li>change the method for <code>Agenda.FileHandler.sendEmail()</code> so the users can contact you 
 * </li>
 * </ul> 
 * <strong>Notes on this class.</strong> 
 * <br>Agenda is a main class, just has the capability to log data (you can do whatever you want) 
 * You use the method showError() to show a fatal error and showUserError() to use a  
 */
public enum ErrorID {
   IO_EXCEPTION("Internal Input / Output Error"),
   NULL_POINT("Internal Null Pointer Error"),
   FILE_NOT_FOUND("File Not Found"),
   INITIALIZER("Exception in Initializer"),
   SERIALIZE("Data Corruption in Reading / Writing Process"),
   
   FILE_TAMPER("There was an error with reading your schedule.\n"
               + "It has been reset to the default"),
   INPUT_ERROR("Input Error. Make sure all fields are filled correctly"), 
   WRONG_HALF_SELECTED("You selected a half day rotation that does not exist.\n"
         + "The rotation has been set to a half day R1."),
   WRONG_DELAY_SELECTED("You selected a delayed opening that doesn't exist\n"
         + "The rotation has been set to a delayed opening R1."),
   OTHER();

   public static final String ERROR_NAME = "ERROR";
   private final String ID;
   private final String message;
   private static boolean debug = false;

   private ErrorID(String message) {
      this.ID = Integer.toHexString((this.ordinal() + 1) * 10000);
      this.message = message;
   }

   private ErrorID() {
      this("Internal Error");
   }

   public String getID() {
      return ID;
   }

   public static class UserError extends Exception {
      private static final long serialVersionUID = 1L;
      public UserError(String message) {
        super(message);
      }
   }
   public static void showUserError(ErrorID error) {
      java.awt.Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog(null,
                  "User Error.\nDetails:\n" + error.message + "\nErrorID: "
                        + error.getID(),
                  ERROR_NAME, JOptionPane.WARNING_MESSAGE);
   }
   
   private static int showInitialMessage(String message, int messageType, boolean recoverable) {
      String fatality = (recoverable) ? "recoverable" : "fatal";
      String defMessage = "A " + fatality + " error has occurred.\nClick \"Info\" for more information.";
      String usrMessage = "A user "+ defMessage.substring(3);
      String givenMessage = (messageType == JOptionPane.ERROR_MESSAGE) ? 
            defMessage : usrMessage;
      return JOptionPane.showOptionDialog(null,
            givenMessage + "\n" + message,
            ERROR_NAME, JOptionPane.OK_CANCEL_OPTION, messageType,
            null, new String[]{"Info", "Close"}, "Close");
   }

   public static void showGeneral(Throwable e, String message, String ID, boolean copy, boolean recoverable) {
      java.awt.Toolkit.getDefaultToolkit().beep();
      e.printStackTrace();
      String newLn = "\n";
      int choice = showInitialMessage(message, JOptionPane.ERROR_MESSAGE, recoverable);
      if (choice == 0) {
         String throwMessage = getType(e).message;
         String internalMessage = (e.getMessage() == null) ? "" : e.getMessage() + newLn;
         String causeMessage = (e.getCause() == null) ? "" : "Caused by: " + getID(e.getCause());
         String importantText = "ErrorID: " + ID + newLn + causeMessage + newLn + internalMessage;
         String prompt = "Go to" + newLn + /* XXX ENTER LOG ROUTE DATA */"<log route>" + "\nFor your log data.";
         String text = "Details:\n" + throwMessage + newLn + importantText + prompt;
         int choice2 = JOptionPane.showOptionDialog(null,
               text,
               ERROR_NAME, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE, 
               null, (copy) ? new String[]{"Copy & Close", "Copy & Contact", "Close"} : new String[] {"Close", "Contact"}, 
                     "Close");
         if (choice2 == 0 && copy) {
            ErrorCopier.copy(ID, e);
         } else if (choice2 == 1) {
            ErrorCopier.copy(ID, e);
            // XXX custom email / contact sending abilities
         }
      }
   }

   public static void showError(Throwable e, boolean recover, String message) {
      String ID = getID(e);
      showGeneral(e, ID, message, true, recover);
      if (!recover)
         System.exit(0);
   }
   
   public static void showError(Throwable e, boolean recover) {
      showError(e, recover, "");
   }
   
   public static String getID(Throwable e) {
      return e.getStackTrace()[0].getClassName() + ":" + e.getStackTrace()[0].getLineNumber() + 
            ">" + getType(e).getID();
   }
   
   public static void showPrintingError(Throwable e) {
      showGeneral(e, getID(e), "Error Printing Data.", false , false);
   }
   
   /**
    * returns the proper {@linkplain ErrorID} when given e, a throwable 
    * @param e a throwable to retrieve a type from
    * @return the <code>ErrorID</code> that correlates with the given throwable 
    */
   private static ErrorID getType(Throwable e) {
      if (e instanceof StreamCorruptedException || e instanceof ClassNotFoundException)
         return SERIALIZE;
      if (e instanceof NullPointerException)
         return NULL_POINT;
      if (e instanceof FileNotFoundException)
         return FILE_NOT_FOUND;
      if (e instanceof ExceptionInInitializerError)
         return INITIALIZER;
      if (e instanceof IOException)
         return IO_EXCEPTION;
      return OTHER;
   }
   
   public static ErrorID getError(String ID) {
      for (ErrorID e : values())
         if (e.getID().equals(ID))
            return e;
      return null;
   }
   
   /**
    * 
    * 
    * @author Thomas Varano
    */
   public static class ErrorCopier implements Transferable {
      private Throwable e;
      private String str;
      
      public ErrorCopier(String str, Throwable e) {
         this.str = str;
         this.e = e;
      }
      public ErrorCopier(String str) {
         this(str, null);
      }
      
      public static String getStackTrace(Throwable e) {
         if (e == null)
            return "";
         OutputStream outAnon = new OutputStream() {
            private StringBuilder sb = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
               sb.append((char)b);
            } 
            public String toString() {
               return sb.toString();
            }
         };
         e.printStackTrace(new PrintStream(outAnon));
         return outAnon.toString();
      }
      
      public String getData() {
         StringBuilder b = new StringBuilder();
         b.append(str);
         b.append("\n"+getStackTrace(e));
         if (debug) System.out.println("copier getting data: "+b.toString());
         return b.toString();
      }
      
      public static void copy(String str, Throwable e) {
         if (debug) System.out.println("copying "+str+" : "+e);
         Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         systemClipboard.setContents(new ErrorCopier(str, e), null);
         if (debug)
            try {
               System.out.println("copied: "+systemClipboard.getData(DataFlavor.stringFlavor));
            } catch (UnsupportedFlavorException | IOException e1) {
               e1.printStackTrace();
            }
      }
      
      public static void copy(String str) {
         copy(str, null);
      }
      
      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[] {DataFlavor.stringFlavor};
      }
      
      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
         return flavor.equals(DataFlavor.stringFlavor);
      }
      
      @Override
      public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
         if (isDataFlavorSupported(flavor)) {
            return getData();
         } else {
            return null;
         }
      }
   }
   
   public static void main(String[] args) {
//      System.out.println(ErrorID.getError("7530"));
//      ErrorID.showPrintingError(new IOException());
      ErrorID.showError(new NullPointerException(), false);
//      constants.test.DayTypeTest.main(args);
   }
}

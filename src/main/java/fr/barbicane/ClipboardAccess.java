package fr.barbicane;

import com.sun.jna.platform.win32.User32;

import javax.tools.Tool;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ClipboardAccess implements Runnable{
    Robot robot;
//    WinHook winHook;
    boolean ready;
    public ClipboardAccess() throws AWTException {
        robot = new Robot();
    };
    public void convert(){
        /*
        Utilitaire copiant le texte dans le clipboard du systeme et le retournant en majuscule
         */
        //TODO ajouter item au menu contextuel de windows
        //TODO corriger bug une seule conversion
        try {
//            winHook = new WinHook();
            // on recupere le clipboard du Systeme
            new Thread(){
                @Override
                public void run() {
                    try {
                        robot.keyPress(KeyEvent.VK_C);
                        robot.keyRelease(KeyEvent.VK_C);
                        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                        Thread.sleep(30);
                        Transferable cts = clip.getContents(null);
                        String txt = (String) cts.getTransferData(DataFlavor.stringFlavor);
                        System.out.println(txt);
                        StringSelection selection = null;
                        // si le texte est deja en majuscule
                        if (((String) cts.getTransferData(DataFlavor.stringFlavor)).equals((String) ((String) cts.getTransferData(DataFlavor.stringFlavor)).toUpperCase())){
                            selection = new StringSelection(((String) cts.getTransferData(DataFlavor.stringFlavor)).toLowerCase());
                        }else{
                            selection = new StringSelection(((String) cts.getTransferData(DataFlavor.stringFlavor)).toUpperCase());
                        }
                        clip.setContents(selection, selection);
                        Thread.sleep(30);
                        robot.keyPress(KeyEvent.VK_V);
                        robot.keyRelease(KeyEvent.VK_V);
//                        System.out.println("colle");
                    } catch (UnsupportedFlavorException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                // temporisation necessaire pour laisser le temps de recuperer le clipboard
                // on tranfere le formatage au clipboard

            }.start();
//            Thread.sleep(10);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        convert();
    }
}


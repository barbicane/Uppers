package fr.barbicane;


import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ClipboardAccess implements Runnable{
    Robot robot;
    public ClipboardAccess() throws AWTException {
        robot = new Robot();
    }
    public void convert(){
        /*
        Utilitaire copiant le texte dans le clipboard du systeme et le retournant en majuscule
         */
        //TODO ajouter item au menu contextuel de windows
        try {
            // on recupere le clipboard du Systeme
                        robot.keyPress(KeyEvent.VK_C);
                        robot.keyRelease(KeyEvent.VK_C);
                        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                        Thread.sleep(30);
                        Transferable cts = clip.getContents(null);
                        String txt = (String) cts.getTransferData(DataFlavor.stringFlavor);
                        System.out.println(txt);
                        StringSelection selection;
                        // si le texte est deja en majuscule
                        if (cts.getTransferData(DataFlavor.stringFlavor).equals(((String) cts.getTransferData(DataFlavor.stringFlavor)).toUpperCase())){
                            selection = new StringSelection(((String) cts.getTransferData(DataFlavor.stringFlavor)).toLowerCase());
                        }else{
                            selection = new StringSelection(((String) cts.getTransferData(DataFlavor.stringFlavor)).toUpperCase());
                        }
                        clip.setContents(selection, selection);
                        Thread.sleep(30);
                        robot.keyPress(KeyEvent.VK_V);
                        robot.keyRelease(KeyEvent.VK_V);
                    } catch (UnsupportedFlavorException | IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
    }


                // temporisation necessaire pour laisser le temps de recuperer le clipboard
                // on tranfere le formatage au clipboard
    @Override
    public void run() {
        convert();
    }
}


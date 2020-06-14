package fr.barbicane;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;

public class ClipboardAccess {
    public static void convert(){

        /*
        Utilitaire copiant le texte dans le clipboard du systeme et le retournant en majuscule
         */
        //TODO ajouter item au menu contextuel de windows
        //TODO corriger bug une seule conversion
        try {
            Robot robot = new Robot();
            Thread.sleep(10);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_C);
            // on recupere le clipboard du Systeme
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            // on extrait les données texte
            String str = clip.getData(DataFlavor.stringFlavor).toString();
            System.out.println(str);
            // creation d'un objet transferable
            StringSelection selection = new StringSelection(str.toUpperCase());
            // temporisation necessaire pour laisser le temps de recuperer le clipboard
            Thread.sleep(10);
            // on tranfere le formatage au clipboard
            clip.setContents(selection, null);
//            System.out.println(str);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


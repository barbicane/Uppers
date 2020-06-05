package fr.barbicane;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class ClipboardAccess {
    public static void convert(){

        /*
        Utilitaire copiant le texte dans le clipboard du systeme et le retournant en majuscule
         */
        //TODO ajouter raccourci clavier (CTRL+U)?
        //TODO ajouter item au menu contextuel de windows
        try {
            // on recupere le clipboard du Systeme
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            // on extrait les données texte
            String str = clip.getData(DataFlavor.stringFlavor).toString();
            // creation d'un objet transferable
            StringSelection selection = new StringSelection(str.toUpperCase());
            // on tranfere le formatage au clipboard
            clip.setContents(selection, null);
//            System.out.println(str);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


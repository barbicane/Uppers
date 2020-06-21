package fr.barbicane;

import com.sun.jna.platform.win32.*;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws AWTException {
        WinHook winH = new WinHook();
        winH.keyboardHook();
        User32 winLib = WinHook.getLib();
        WinUser.HHOOK hhk = WinHook.getHhk();
        // permet de lancer un thread a l'arret de la JVM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("System shutdown");
            WinHook.getLib().UnhookWindowsHookEx(WinHook.getHhk());
        }));


    }
}

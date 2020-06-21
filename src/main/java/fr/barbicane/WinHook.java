package fr.barbicane;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;
import java.awt.*;
import java.awt.event.KeyEvent;

public class WinHook {
    private volatile boolean quit;
    private ClipboardAccess clip;

    public WinHook() throws AWTException {
         clip = new ClipboardAccess();
    };
    public static HHOOK getHhk() {
        return hhk;
    }

    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;
    private static User32 lib;

    public static User32 getLib() {
        return lib;
    }


    public void keyboardHook() {
        // recuperer l'instance de User32
        final User32 lib = User32.INSTANCE;

        // on recupere le module (le conteneur dans laquelle tourne l'instance)
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);

        // on cree un nouvel ecouteur d'event de la forme appartenant a la librairie Win32
        keyboardHook = (nCode, wParam, info) -> {
            if (nCode >= 0) {
                switch (wParam.intValue()) {
                    case WinUser.WM_KEYUP:
                    case WinUser.WM_SYSKEYUP:
                    case WinUser.WM_SYSKEYDOWN:
                        break;
                    case WinUser.WM_KEYDOWN:
//                        ctrl+<
                        if (info.vkCode == 0xE2 && (lib.GetAsyncKeyState(0x11) != 0)) {
//                                quit = true;
                            //lancement d'un nouveau Thread pour eviter comportement inatendu
                            new Thread(clip).start();
                        } else if (info.vkCode == 0x51 && lib.GetAsyncKeyState(0x11) != 0 && lib.GetAsyncKeyState(0x12) != 0) {
                            Runtime.getRuntime().exit(0);
                        }
                }
            }
            Pointer ptr = info.getPointer();
            long peer = Pointer.nativeValue(ptr);
            return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
        };
        // on parametre un nouveau crochet dans le flux clavier windows et on garde la reference pur annuler plus tard
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);

        System.out.println("Keyboard hook installed, type Ctrl+'<' to execute action");

        int result;
        MSG msg = new MSG();
        System.out.println(1);
        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
            if (result == -1) {
                break;
            }
            else {
                lib.TranslateMessage(msg);
                lib.DispatchMessage(msg);
            }
        }
        lib.UnhookWindowsHookEx(hhk);
    }
}


package fr.barbicane;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import com.sun.jna.platform.win32.WinUser.MSG;

public class WinHook {
    private static volatile boolean quit;

    public static HHOOK getHhk() {
        return hhk;
    }

    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;

    public static User32 getLib() {
        return lib;
    }

    private static User32 lib;

    public static void keyboardHook() {
        // recuperer l'instance de User32
        final User32 lib = User32.INSTANCE;

        // on recupere le module (le conteneur dans laquelle tourne l'instance)
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);

        // on cree un nouvel ecouteur d'event de la forme appartenant a la librairie Win32
        keyboardHook = (nCode, wParam, info) -> {
            if (nCode >= 0) {
                switch (wParam.intValue()) {
                    case WinUser.WM_KEYUP:
                    case WinUser.WM_KEYDOWN:
                    case WinUser.WM_SYSKEYUP:
                    case WinUser.WM_SYSKEYDOWN:
//                        ctrl+<
                        if (info.vkCode == 0xE2 && (lib.GetAsyncKeyState(0x11) != 0)) {
//                                quit = true;
                            ClipboardAccess.convert();
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
        // on lance un nouveau thread qui attend tant que la valeur quit est false
//        new Thread(() -> {
//            while (!quit) {
//                try {
//                    //                    Thread.sleep(10);
//                }
//                catch(Exception e) {
//                    e.printStackTrace(); }
//            }
//            System.err.println("unhook and exit");
//            lib.UnhookWindowsHookEx(hhk);
//            System.exit(0);
//        }).start();

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


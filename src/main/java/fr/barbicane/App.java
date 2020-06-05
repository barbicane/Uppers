package fr.barbicane;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ){
        WinHook.keyboardHook();
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.println("System shutdown");
            }
        });
    }
}

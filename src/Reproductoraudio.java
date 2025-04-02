import javazoom.jl.player.*;
import java.io.*;
import javax.swing.*;
import javax.swing.JOptionPane;

public class Reproductoraudio {

    private static Player Reproductor;

    public static void detener() {

        if(Reproductor != null) {
            Reproductor.close();
            Reproductor = null;
        }
    }

    public static void Reproduccion(String archivoaudio) {

        try {

            FileInputStream fis = new FileInputStream(archivoaudio);
            BufferedInputStream bis = new BufferedInputStream(fis);
            Reproductor = new Player(bis);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex);
        }

        new Thread() {


            public void run() {

                try {
                    Reproductor.play();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex);
                }
            }
        }.start();


    }
    
}

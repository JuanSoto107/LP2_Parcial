import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import entidades.Frase;
import entidades.Traduccion;
import entidades.FraseData;

public class FrmTraductor extends JFrame {

    JComboBox cmbIdiomas, cmbFrases;
    JTextField txttraduccion;
    String[] idiomas = new String[] {"", "", "", "", "", ""};
    String[] oraciones = new String[] {"", "", "", "", "", "", ""};
    List<String> IdiomasUnicos;
    List<String> Textos;
    ObjectMapper objectmapper;
    String nombreArchivo;

    public FrmTraductor() {

        setSize(600, 400);
        setTitle("Traductor de idiomas");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblfrase = new JLabel("Frase");
        lblfrase.setBounds(70, 40, 100, 20);
        getContentPane().add(lblfrase);

        JLabel lblidioma = new JLabel("Idioma");
        lblidioma.setBounds(70, 80, 100, 20);
        getContentPane().add(lblidioma);

        JButton btntraducir = new JButton("Traducir");
        btntraducir.setBounds(50, 130, 100, 30);
        getContentPane().add(btntraducir);
        btntraducir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Traducido();
            }

        });

        
        JButton btnvoz = new JButton();
        btnvoz.setBounds(410, 180, 100, 100);
        btnvoz.setIcon(new ImageIcon(getClass().getResource("/iconos/imagen.png")));
        getContentPane().add(btnvoz);
        btnvoz.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                reproducirAudio();
            }

        });

        txttraduccion = new JTextField();
        txttraduccion.setBounds(40, 180, 350, 100);
        getContentPane().add(txttraduccion);
        txttraduccion.setEnabled(false);

        nombreArchivo = System.getProperty("user.dir") + "/src/datos/FrasesTraducidas.json";


        try {
            
            objectmapper = new ObjectMapper();
            FraseData data = objectmapper.readValue(new File(nombreArchivo), FraseData.class);

            IdiomasUnicos = data.getFrases().stream()
            .flatMap(frase -> frase.getTraducciones().stream()) // Descomponer las traducciones
            .map(Traduccion::getIdioma) // Obtener solo los idiomas
            .distinct() // Eliminar duplicados
            .sorted() // Ordenar alfabéticamente (opcional)
            .collect(Collectors.toList());

            Textos = data.getFrases().stream()
            .map(frase -> frase.getTexto())
            .collect(Collectors.toList());

            int i = 0;
            int j = 0;

            for(String Idiomas : IdiomasUnicos) {
                idiomas[i] = Idiomas;

                i++;
            }

            for(String Textitos : Textos) {
                oraciones[j] = Textitos;

                j++;
            }

            cmbIdiomas = new JComboBox();
            cmbIdiomas.setBounds(170, 80, 150, 20);
            DefaultComboBoxModel mdlIdiomas = new DefaultComboBoxModel(idiomas);
            cmbIdiomas.setModel(mdlIdiomas);
            getContentPane().add(cmbIdiomas);

            cmbFrases = new JComboBox();
            cmbFrases.setBounds(170, 40, 250, 20);
            DefaultComboBoxModel mdlFrases = new DefaultComboBoxModel(oraciones);
            cmbFrases.setModel(mdlFrases);
            getContentPane().add(cmbFrases);


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "No");

        }

    }

    public void Traducido() {

        String mensaje = "";

        objectmapper = new ObjectMapper();
        nombreArchivo = System.getProperty("user.dir") + "/src/datos/FrasesTraducidas.json";

        try {
            
            FraseData data2 = objectmapper.readValue(new File(nombreArchivo), FraseData.class);

            int ind = cmbFrases.getSelectedIndex();
            int ind2 = cmbIdiomas.getSelectedIndex();

            String textofrase = Textos.get(ind);
            String textoidioma = IdiomasUnicos.get(ind2);

            try {

                for(Frase fr : data2.getFrases()) {
                    fr.getTexto();
                    if(fr.getTexto().equals(textofrase)) {
                        fr.getTraducciones();
                        boolean existe = fr.getTraducciones().contains(textoidioma);
                        for(Traduccion td : fr.getTraducciones()) {
                            if(td.getIdioma().equals(textoidioma)) {
                                mensaje = td.getTextotraducido();
                                existe = true;
                            }
                        }
                        if(existe == false) {
                            JOptionPane.showMessageDialog(null, "No hay traduccion disponible");
                        }

                    }
                }

                txttraduccion.setText(mensaje);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
   
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Algo salio mal");
        }
    }

    private boolean reproduciendo = false;


    public void reproducirAudio() {

        try {

            if(!reproduciendo) {

                int ind = cmbFrases.getSelectedIndex();
                int ind2 = cmbIdiomas.getSelectedIndex();
                String txt1 = Textos.get(ind);
                String txt2 = IdiomasUnicos.get(ind2);
    
                txt1 = txt1.replace(" ", "")
                .replace("ì", "i")
                .replace("ò", "o")
                .replace("d", "D")
                .replace("e", "E")
                .replace("l", "L")
                .replace("?", "")
                .replace("t", "T")
                .replace("v", "V");

                String NombreArchivo = System.getProperty("user.dir") + "/src/audios/"+ txt1 +"-"+ txt2 +".mp3";

                File archivoAudio = new File(NombreArchivo);

                if(archivoAudio.exists()) {
                    reproduciendo = true;

                    Reproductoraudio.Reproduccion(NombreArchivo);
                }
            } else {

                Reproductoraudio.detener();
                reproduciendo = false;
                JOptionPane.showMessageDialog(null, "No hay audio disponible");
            }


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Fallo al reproducir");
        }

    }

}
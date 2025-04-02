package entidades;

import java.util.List;

public class Frase {

    private String texto;
    private List<Traduccion> traducciones;



    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<Traduccion> getTraducciones() {
        return traducciones;
    }

    public void setTraducciones(List<Traduccion> traducciones) {
        this.traducciones = traducciones;
    }
    
}

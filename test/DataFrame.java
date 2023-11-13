package test;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class DataFrame {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Columna> data;
    private Set<String> etiquetas;
    private Map<String, String> tiposColumna;

    public DataFrame(){
        this.data = new HashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Columna> data, Map<String, String> tiposColumna){
        this.data = data;
        this.tiposColumna = tiposColumna;
        this.etiquetas = data.keySet();
    }

    public Set<String> nombreColumnas(){
        return this.etiquetas;
    }

    public int cantidadColumnas(){
        return this.etiquetas.size();
    }

    public Map<String, String> tiposColumna(){
        return this.tiposColumna;
    }

    public String toString(){
        return this.data.toString();
    }

    public DataFrame head(int cantidadFilas){
        throw new UnsupportedOperationException("Metodo no implementado 'head'");
    }

    public DataFrame head(){
        throw new UnsupportedOperationException("Metodo no implementado 'head'");
    }

    public DataFrame tail(int cantidadFilas){
        throw new UnsupportedOperationException("Metodo no implementado 'tail'");
    }

    public DataFrame tail(){
        throw new UnsupportedOperationException("Metodo no implementado 'tail'");
    }

    public <T> void setCelda(String etiqueta, int indice, T valor){
        throw new UnsupportedOperationException("Metodo no implementado 'setCelda'");
    }

    public Object getCelda(String etiqueta, int indice){
        throw new UnsupportedOperationException("Metodo no implementado 'getCelda'");
    }

    public <T> T getCelda(String etiqueta, int indice, Class<T> tipoDato){
        throw new UnsupportedOperationException("Metodo no implementado 'getCelda'");
    }

    public DataFrame clone(){
        throw new UnsupportedOperationException("Metodo no implementado 'clone'");
    }

}

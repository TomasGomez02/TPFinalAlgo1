package test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class DataFrame {
    
    private Map<String, Columna> data;
    private List<String> etiquetas;
    private Map<String, String> tiposColumna;

    public DataFrame(){
        this.data = new HashMap<>();
    }

    public List<String> nombreColumnas(){
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
        return; 
    }

    public DataFrame head(){
        return; 
    }

    public DataFrame tail(int cantidadFilas){
        return; 
    }

    public DataFrame tail(){
        return; 
    }

    public <T> void setCelda(String etiqueta, int indice, T valor){
        //TODO
    }

    public Object getCelda(String etiqueta, int indice){
        return; //TODO
    }

    public <T> T getCelda(String etiqueta, int indice, Class<T> tipoDato){
        return; //TODO
    }

    public DataFrame clone(){
        return; //TODO
    }

    






























}

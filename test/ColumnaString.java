package test;

import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;

public class ColumnaString extends Columna<String> {
    private Map<Integer, String> data;

    public ColumnaString(){
        this.data = new HashMap<>();
    }

    public ColumnaString(List<String> datos){
        this();
        for (int i=0; i < datos.size(); i++) {
            this.data.put(i, datos.get(i));
        }
    }

    public ColumnaString(String[] datos){
        this();
        for (int i=0; i < datos.length; i++) {
            this.añadirCelda(datos[i]);
        }
    }

    @Override
    public String getCelda(int indice) {
        return this.data.get(indice);
    }

    @Override
    public void setCelda(int indice, String valor) {
        this.data.put(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, String valor) {
        for (int i=0; i < this.length() - indice; i++) {
            this.data.put(this.length() - i, this.getCelda(this.length() - i - 1));
        }
        this.setCelda(indice, valor);
    }

    @Override
    public void añadirCelda(String valor) {
        int indice = this.length();
        this.data.put(indice, valor);
    }

    @Override
    public void eliminarCelda(int indice) {
        for (int i=0; i < this.length() - indice - 1; i++){
            this.setCelda(indice + i, this.getCelda(indice + i + 1));
        }
        this.data.remove(this.length() -1);
    }

    @Override
    public void borrarValorCelda(int indice) {
        this.data.put(indice, null);
    }

    @Override
    public ColumnaString recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaString recorte = new ColumnaString();
        for (int i=0; i < this.length(); i++) {
            if (i >= indiceInicio && i <= indiceFinal){
                recorte.añadirCelda(this.getCelda(i));
            }
        }
        return recorte;
    }

    @Override
    public void concatenarColumna(Columna<String> otraColumna) {
        for (int i=0; i < otraColumna.length(); i++) {
            this.añadirCelda(otraColumna.getCelda(i));
        }
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public void ordenar(boolean creciente) {
       throw new UnsupportedOperationException("metodo no implementado 'ordenar'");
    }

    @Override
    public ColumnaString clone(){
        ColumnaString copia = new ColumnaString();
        for (int i=0; i < this.length(); i++){
            copia.añadirCelda(this.getCelda(i));
        }
        return copia;
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    @Override
    public Columna<String> filtrar(String elemento, Filtro<String> filtro) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filtrar'");
    }
    
    // @Override
    public ColumnaString filtarPorIndice(List<Integer> indices){
        // Es muy importante que los indices esten ordenados
        Comparator<Integer> c;
        c = (a, b) -> (a.compareTo(b));
        indices.sort(c);
        ColumnaString filtrada = new ColumnaString();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }
}

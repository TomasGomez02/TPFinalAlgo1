package test;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnaString extends Columna<String> {
    private List<String> data;

    public ColumnaString(){
        this.data = new ArrayList<>();
    }

    public ColumnaString(List<String> datos){
        this.data = new ArrayList<>(datos);
    }

    public ColumnaString(String[] datos){
        this();
        for (String elemento : datos) {
            this.añadirCelda(elemento);
        }
    }

    @Override
    public String getCelda(int indice) {
        return this.data.get(indice);
    }

    @Override
    public void setCelda(int indice, String valor) {
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, String valor) {
        this.data.add(indice, valor);
    }

    @Override
    public void añadirCelda(String valor) {
        this.data.add(valor);
    }

    @Override
    public void eliminarCelda(int indice) {
        this.data.remove(indice);
    }

    @Override
    public void borrarValorCelda(int indice) {
        this.setCelda(indice, null);
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
    
    @Override
    public ColumnaString filtrarPorIndice(List<Integer> indices) {
        ColumnaString filtrada = new ColumnaString();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }
    
    public int getFirstIndex() throws IndexOutOfBoundsException{
        // Lo uso para obtener el indice del primer elemento no null
        for (int i=0; i < this.length(); i++){
            if (this.getCelda(i) != null){
                return i;
            }
        }
        throw new IndexOutOfBoundsException("La columna esta vacia o solo tiene elementos nulos");
    }
    
    @Override
    public Map<Integer, Integer> ordenar(boolean creciente) {
        // Crear lista de indices para trasladar los valores
        Map<Integer, Integer> trasladar = new HashMap<>();

        // Crea una copia para poder eliminar elementos sin problemas
        ColumnaString copia = this.clone();

        for (int i=0; i < this.length(); i++){
            Integer idxMinimo = copia.getFirstIndex();
            for (int j=0; j < this.length(); j++){
                if (copia.getCelda(j) != null &&
                this.getCelda(j).compareTo(this.getCelda(idxMinimo)) < 0){
                    idxMinimo = j;
                }
            }
            if (creciente){
                trasladar.put(idxMinimo, i);
            }
            else{
                trasladar.put(idxMinimo, this.length() - i - 1);
            }
            copia.borrarValorCelda(idxMinimo);
        }
        return trasladar;
    }
}

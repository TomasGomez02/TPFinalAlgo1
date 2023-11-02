package test;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnaBool extends Columna<Boolean>{
    private List<Boolean> data;

    public ColumnaBool(){
        this.data = new ArrayList<>();
    }

    public ColumnaBool(Boolean[] data){
        this();
        for (Boolean element : data) {
            this.añadirCelda(element);
        }
    }

    public ColumnaBool(List<Boolean> otro){
        this.data = new ArrayList<>(otro);
    }

    @Override
    public Boolean getCelda(int indice) {
        return this.data.get(indice);  
    }

    @Override
    public void setCelda(int indice, Boolean valor) {
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Boolean valor) {
        this.data.add(indice, valor);
    }

    @Override
    public void añadirCelda(Boolean valor) {
        this.data.add(valor); 
    }

    @Override
    public void eliminarCelda(int indice) {
        this.data.remove(indice);
    }

    @Override
    public void borrarValorCelda(int indice) {
        this.data.set(indice, null);
    }

    @Override
    public Columna<Boolean> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaBool recorte = new ColumnaBool();
        for (int i = 0; i < this.length(); i++) {
            if (i >= indiceInicio && i <= indiceFinal){
                recorte.añadirCelda(this.getCelda(i));
            }
        }
        return recorte;
    }

    @Override
    public void concatenarColumna(Columna<Boolean> otraColumna) {
        for (int i = 0; i < otraColumna.length(); i++) {
            this.añadirCelda(otraColumna.getCelda(i));
        }
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public Map<Integer, Integer> ordenar(boolean creciente) {
        // Crear lista de indices para trasladar los valores
        Map<Integer, Integer> trasladar = new HashMap<>();

        // Crea una copia para poder eliminar elementos sin problemas
        ColumnaBool copia = this.clone();

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

    private Integer getFirstIndex() {
        for (int i=0; i < this.length(); i++){
            if (this.getCelda(i) != null){
                return i;
            }
        }
        throw new IndexOutOfBoundsException("La columna esta vacia o solo tiene elementos nulos");
    }

    @Override
    public ColumnaBool clone(){
        ColumnaBool copia = new ColumnaBool();
        for (int i=0; i < this.length(); i++) {
            copia.añadirCelda(this.getCelda(i));
        }
        return copia;
    }

    public int sumaAcumulada(){
        int suma = 0;
        for (Boolean valor : data) {
            if (valor){
                suma++;
            }
        }
        return suma;
    }

    @Override
    public String toString(){
        return this.data.toString();
    }

    @Override
    public Columna<Boolean> filtrar(Boolean elemento, Filtro<Boolean> filtro) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filtrar'");
    }

    @Override
    public ColumnaBool filtrarPorIndice(List<Integer> indices){
        ColumnaBool filtrada = new ColumnaBool();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }
}

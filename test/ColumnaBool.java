package test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ColumnaBool extends Columna<Boolean>{
    private Map<Integer, Boolean> data;

    public ColumnaBool(){
        this.data = new HashMap<>();
    }
    
    public ColumnaBool(List<Boolean> datos){
        this();
        for (int i=0; i < datos.size(); i++) {
            this.data.put(i, datos.get(i));
        }
    }
    
    public ColumnaBool(Boolean[] datos){
        this();
        for (int i=0; i < datos.length; i++) {
            this.data.put(i, datos[i]);
        }
    }

    public ColumnaBool(boolean[] datos){
        this();
        for (int i=0; i < datos.length; i++) {
            this.data.put(i, datos[i]);
        }
    }

    @Override
    public Boolean getCelda(int indice) {
        return this.data.get(indice);  
    }

    @Override
    public void setCelda(int indice, Boolean valor) {
        this.data.put(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Boolean valor) {
        for (int i=0; i < this.length() - indice; i++) {
            this.data.put(this.length() - i, this.getCelda(this.length() - i - 1));
        }
        this.setCelda(indice, valor);
    }

    @Override
    public void añadirCelda(Boolean valor) {
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
    public ColumnaBool recortarColumna(int indiceInicio, int indiceFinal) {
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
    public void ordenar(boolean creciente) {
        // Usa el algoritmo de selection sort
        // Aviso: no ordena los nulos, solo los deja en el mismo lugar donde estaban
        // Tengo que reparar esto. Necesito que devuelva un Map<Integer, Integer>
        int n = this.length();
        for (int i = 0; i < n - 1; i++) {
            int idxMinimo = i;
            System.out.println(idxMinimo +" "+ this.getCelda(idxMinimo));
            if (this.getCelda(i) == null){
                continue;
            }
            for (int j = i + 1; j < n; j++) {
                if (this.getCelda(j) != null && this.getCelda(j).compareTo(this.getCelda(idxMinimo)) > 0) {
                    idxMinimo = j;
                }
            }
            // Intercambia el mínimo encontrado con el elemento en la posición i
            Boolean temp = this.getCelda(i);
            this.setCelda(i, this.getCelda(idxMinimo));
            this.setCelda(idxMinimo, temp);
        }
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
        for (Boolean valor : data.values()) {
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

    // @Override
    public ColumnaBool filtarPorIndice(List<Integer> indices){
        // Es muy importante que los indices esten ordenados
        Comparator<Integer> c;
        c = (a, b) -> (a.compareTo(b));
        indices.sort(c);
        ColumnaBool filtrada = new ColumnaBool();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }
}

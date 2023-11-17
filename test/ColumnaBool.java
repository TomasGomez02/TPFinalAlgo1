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
        if (contieneIndice(indice)){
            return this.data.get(indice);
        }
        throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
    }

    @Override
    public void setCelda(int indice, Boolean valor) {
        if (contieneIndice(indice)){
            this.data.set(indice, valor);
        }
        throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
    }

    @Override
    public void añadirCelda(int indice, Boolean valor) {
        if (!contieneIndice(indice) && indice != 0){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.add(indice, valor);
    }

    @Override
    public void añadirCelda(Boolean valor) {
        this.data.add(valor); 
    }

    @Override
    public void eliminarCelda(int indice) {
        if (contieneIndice(indice)){
            this.data.remove(indice);
        }
        throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
    }

    @Override
    public void borrarValorCelda(int indice) {
        if (contieneIndice(indice)){
            this.data.set(indice, null);
        }
        throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
    }

    @Override
    public Columna<Boolean> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaBool recorte = new ColumnaBool();
        for (int i=indiceInicio; i <= indiceFinal; i++) {
            recorte.añadirCelda(this.getCelda(i));
        }
        return recorte;
    }

    @Override
    public int length() {
        return this.data.size();
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
    public ColumnaBool filtrarPorIndice(List<Integer> indices){
        ColumnaBool filtrada = new ColumnaBool();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }

    @Override
    public Map<Integer, Integer> ordenar(boolean creciente) {
        // Crear lista de indices para trasladar los valores
        Map<Integer, Integer> trasladar = new HashMap<>();

        // Crea un array con todos sus elementos en false, para saber si ya se ordenaron los elementos
        // dentro de la columna. Si el elemento en la posicion x en la Columna ya esta ordenado, entonces
        // el valor en la posicion x del array va a ser true.
        boolean[] yaSeOrdeno = new boolean[this.length()];

        for (int i=0; i < this.length(); i++){
            Integer idxMinimo = -1;
            // Uso esto para tomar el primer indice no nulo y no ordenado
            for (int k=0; k < this.length(); k++){
                if (this.getCelda(k) != null && !yaSeOrdeno[k]){
                    idxMinimo = k;
                    break;
                }
            }
            if (idxMinimo < 0){
                continue;
            }
            for (int j=0; j < this.length(); j++){
                if (!yaSeOrdeno[j] && this.getCelda(j) != null &&
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
            yaSeOrdeno[idxMinimo] = true;
        }
        // Esta parte manda los null al final de la lista
        Integer distanciaDesdeUltimo = 0;
        for (int i=0; i < this.length(); i++){
            if (this.getCelda(i) == null){
                if (creciente){
                    trasladar.put(i, this.length() - distanciaDesdeUltimo -1);
                }
                else{
                    trasladar.put(i, distanciaDesdeUltimo);
                }
                distanciaDesdeUltimo += 1;
            }
        }
        return trasladar;
    }
}

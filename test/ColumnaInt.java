package test;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class ColumnaInt extends ColumnaNum<Integer> {
        
    private List<Integer> data;

    public ColumnaInt() {
        this.data = new ArrayList<>();
    }

    public ColumnaInt(List<Integer> data) {
        this.data = data;
    }

    public ColumnaInt(Integer[] data){
        this();
        for (Integer num : data) {
            this.añadirCelda(num);
        }
    }

    @Override
    public Double media() {
        Double suma = 0.0;
        for (int i=0; i<data.size(); i++) {
            suma += data.get(i);
        }
        return suma / data.size();
    }

    @Override
    public Integer mediana() {
        throw new UnsupportedOperationException("Unimplemented method 'mediana'");
    }

    @Override
    public Integer maximo() {
        int max = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (data.get(i) > max) {
                max = data.get(i);
            }
        }
        return max;
    }

    @Override
    public Integer minimo() {
        int min = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (data.get(i) < min) {
                min = data.get(i);
            }
        }
        return min;
    }

    @Override
    public Double desvioEstandar() {
        Double suma = 0.0;
        for (int i=0; i<data.size(); i++) {
            suma = suma + ((media() - data.get(i)) * (media() - data.get(i)));
        }
        Double desvio = suma / data.size();
        return Math.sqrt(desvio);
    }

    @Override
    public Integer sumaAcumulada() {
        int suma = 0;
        for (Integer valor : data) {
            suma += valor;
        }
        return suma;
    }

    @Override
    public Integer getCelda(int indice) {
        return this.data.get(indice); 
    }

    @Override
    public void setCelda(int indice, Integer valor) {
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Integer valor) {
        this.data.add(indice, valor);
    }

    @Override
    public void añadirCelda(Integer valor) {
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
    public Columna<Integer> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaInt recorte = new ColumnaInt();
        for (int i = 0; i < this.length(); i++) {
            if (i >= indiceInicio && i <= indiceFinal){
                recorte.añadirCelda(this.getCelda(i));
            }
        }
        return recorte;
    }

    @Override
    public void concatenarColumna(Columna<Integer> otraColumna) {
        for (int i = 0; i < otraColumna.length(); i++) {
            this.añadirCelda(otraColumna.getCelda(i));
        }
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public Columna<Integer> filtrar(Integer elemento, Filtro<Integer> filtro) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filtrar'");
    }

    @Override
    public ColumnaInt filtrarPorIndice(List<Integer> indices) {
        ColumnaInt filtrada = new ColumnaInt();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }

    public ColumnaInt clone(){
        ColumnaInt copia = new ColumnaInt();
        for (Integer num : data) {
            copia.añadirCelda(num);
        }
        return copia;
    }

    @Override
    public String toString() {
        return this.data.toString();
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
        ColumnaInt copia = this.clone();

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

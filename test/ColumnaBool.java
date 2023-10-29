package test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnaBool extends Columna<Boolean>{
    private List<Boolean> data;

    public ColumnaBool(){
        this.data = new ArrayList<>();
    }

    public ColumnaBool(Boolean[] data){
        this.data = Arrays.asList(data);
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
    public void ordenar(boolean creciente) {
        // Usa el algoritmo de selection sort
        // Aviso: no ordena los nulos, solo los deja en el mismo lugar donde estaban
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
        ColumnaBool copia = new ColumnaBool(this.data);
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
}

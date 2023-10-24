package test;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColumnaBool extends Columna<Boolean>{
    private List<Boolean> data;

    public ColumnaBool(){
        this.data = new ArrayList<>();
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
    //Tomasss chequea esto
    public void ordenar(boolean creciente) {
        Comparator<Boolean> c;
        if (creciente){
            c = (a, b) -> a.compareTo(b);
        }
        else{
            c = (a, b) -> b.compareTo(a);
        }
        this.data.sort(c);
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
}

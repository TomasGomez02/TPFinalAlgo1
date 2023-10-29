package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColumnaString extends Columna<String> {
    private List<String> data;

    public ColumnaString(){
        this.data = new ArrayList<>();
    }

    public ColumnaString(List<String> otro){
        this.data = new ArrayList<>(otro);
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
        this.data.set(indice, null);
    }

    @Override
    public ColumnaString recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaString recorte = new ColumnaString();
        for (int i = 0; i < this.length(); i++) {
            if (i >= indiceInicio && i <= indiceFinal){
                recorte.añadirCelda(this.getCelda(i));
            }
        }
        return recorte;
    }

    @Override
    public void concatenarColumna(Columna<String> otraColumna) {
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
        // Funciona bien. Pero, si algun valor es null, tira NullPointerException
        Comparator<String> c;
        if (creciente){
            c = (a, b) -> a.compareTo(b);
        }
        else{
            c = (a, b) -> b.compareTo(a);
        }
        this.data.sort(c);
    }

    @Override
    public ColumnaString clone(){
        ColumnaString copia = new ColumnaString(this.data);
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
    
}

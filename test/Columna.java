package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;

public abstract class Columna<T> implements Cloneable {
    public abstract T getCelda(int indice);
    public abstract void setCelda(int indice, T valor);
    public abstract void añadirCelda(int indice, T valor);
    public abstract void añadirCelda(T valor);
    public abstract void eliminarCelda(int indice);
    public abstract void borrarValorCelda(int indice);
    public abstract Columna<T> recortarColumna(int indiceInicio, int indiceFinal);
    public void concatenarColumna(Columna<T> otraColumna){
        for (int i=0; i < otraColumna.length(); i++){
            this.añadirCelda(otraColumna.getCelda(i));
        }
    }
    public abstract int length();
    public abstract Map<Integer, Integer> ordenar(boolean creciente);
    public void ordenarPorIndice(Map<Integer, Integer> orden){
        List<T> copia = new ArrayList<>();
        for (int i=0; i < this.length(); i++){
            copia.add(this.getCelda(i));
        }
        for (int i=0; i < this.length(); i++){
            Integer moveTo = orden.get(i);
            this.setCelda(moveTo, copia.get(i));
        }
    }
    public List<Integer> filtrar(Predicate<T> filtro){
        List<Integer> indices = new ArrayList<>();
        for (int i=0; i < this.length(); i++){
            if (this.getCelda(i) != null && filtro.test(this.getCelda(i))){
                indices.add(i);
            }
        }
        return indices;
    }
    public abstract Columna<T> filtrarPorIndice(List<Integer> indices);
    public void transformar(UnaryOperator<T> transformacion){
        for (int i=0; i < length(); i++){
            if (getCelda(i) != null){
                setCelda(i, transformacion.apply(getCelda(i)));
            }
        }
    }
    public abstract Columna<T> clone();
}

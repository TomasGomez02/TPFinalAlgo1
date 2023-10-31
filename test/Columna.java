package test;

public abstract class Columna<T> implements Cloneable, Filtrable<T> {
    public abstract T getCelda(int indice);
    public abstract void setCelda(int indice, T valor);
    public abstract void añadirCelda(int indice, T valor);
    public abstract void añadirCelda(T valor);
    public abstract void eliminarCelda(int indice);
    public abstract void borrarValorCelda(int indice);
    public abstract Columna<T> recortarColumna(int indiceInicio, int indiceFinal);
    public abstract void concatenarColumna(Columna<T> otraColumna);
    public abstract int length();
    public abstract void ordenar(boolean creciente);
    // public abstract Map<Integer, Integer> ordenar(boolean creciente);
    // public abstract void ordenarPorIndice(Map<Integer, Integer> orden);
    // public abstract List<Integer> filtrar(T elemento, Filtro<T> filtro);
    // public abstract void filtrarPorIndice(List<Integer> indices);
}
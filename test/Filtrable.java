package test;

public interface Filtrable<T> 
{
    public Columna<T> filtrar(T elemento, Filtro<T> filtro);
}
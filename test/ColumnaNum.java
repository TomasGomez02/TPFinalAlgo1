package test;

public abstract class ColumnaNum<T extends Number> extends Columna<T> {

    public abstract Double media();
    public abstract Number mediana();
    public abstract Number maximo();
    public abstract Number minimo();
    public abstract Double desvioEstandar();
    public abstract Number sumaAcumulada();
}
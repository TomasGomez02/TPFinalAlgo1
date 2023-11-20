package test;

public abstract class ColumnaNum<T extends Number> extends Columna<T> {

    /**
     * Calcula y devuelve la media de los elementos de la columna.
     * 
     * @return la media de los elementos de la columna 
     */
    public abstract Double media();

    /**
     * Calcula y devuelve la mediana de los elementos de la columna.
     * 
     * @return la mediana de los elementos de la columna 
     */
    public abstract Double mediana();

    /**
     * Busca y devuelve el valor maximo de la columna.
     * 
     * @return el valor maximo de la columna
     */
    public abstract Number maximo();

    /**
     * Busca y devuelve el valor minimo de la columna.
     * 
     * @return el valor minimo de la columna
     */
    public abstract Number minimo();

    /**
     * Calcula y devuelve el desvio estandar de los elementos de la columna.
     * 
     * @return el desvio estandar de los elementos de la columna
     */
    public abstract Double desvioEstandar();

    /**
     * Calcula y devuelve la suma acumulada de los elementos de la columna.
     * 
     * @return la suma acumulada de los elementos de la columna
     */
    public abstract Number sumaAcumulada();

    @Override
    public abstract ColumnaNum<T> clone();
}

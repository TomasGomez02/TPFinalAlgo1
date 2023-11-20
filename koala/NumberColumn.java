package koala;


public abstract class NumberColumn<T extends Number> extends Column<T> {

    /**
     * Calcula y devuelve la media de los elementos de la columna.
     * 
     * @return la media de los elementos de la columna 
     */
    public abstract Double mean();

    /**
     * Calcula y devuelve la mediana de los elementos de la columna.
     * 
     * @return la mediana de los elementos de la columna 
     */
    public abstract Double median();

    /**
     * Busca y devuelve el valor maximo de la columna.
     * 
     * @return el valor maximo de la columna
     */
    public abstract Number max();

    /**
     * Busca y devuelve el valor minimo de la columna.
     * 
     * @return el valor minimo de la columna
     */
    public abstract Number min();

    /**
     * Calcula y devuelve el desvio estandar de los elementos de la columna.
     * 
     * @return el desvio estandar de los elementos de la columna
     */
    public abstract Double std();

    /**
     * Calcula y devuelve la suma acumulada de los elementos de la columna.
     * 
     * @return la suma acumulada de los elementos de la columna
     */
    public abstract Number sum();

    @Override
    public abstract NumberColumn<T> clone();
}

package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import utils.DataType;

import java.util.function.Predicate;

public abstract class Column<T> implements Cloneable {
    /**
     * Obtiene el valor que se encuentra en la celda indicada.
     * 
     * @param indice indice de la celda
     * @return       valor encontrado en la celda 
     */
    public abstract T get(int indice);

    /**
     * Establece el valor de la celda.
     * 
     * @param index indice de la celda 
     * @param value valor que se desea establecer
     */
    public abstract void set(int index, T value);

    /**
     * Añade el valor especificado en el indice indicado. 
     * Desplaza el elemento actualmente en esa posicion (si lo hay) y cualquier elemento posterior hacia la derecha (agrega uno a sus indices). 
     * 
     * @param index indice donde se inserta el valor
     * @param value valor a insertar
     */
    public abstract void add(int index, T value);
    /**
     * Añade el valor especificado al final de la columna.
     * 
     * @param value valor a insertar
     */
    public abstract void add(T value);
    /**
     * Verifica si el indice esta dentro del rango de la Columna.
     * 
     * @param index indice a verificar
     * @return verdadero si el indice se encuentra en el rango
     */
    public boolean containsIndex(int index){
        return 0 <= index && index <= length()-1;
    }
    /**
     * Elimina el elemento encontrado en la posicion indicada.
     * Desplaza cualquier elemento posterior hacia la izquierda (resta uno de sus indices).
     * 
     * @param index indice del elemento a eliminar
     */
    public abstract void remove(int index);
    /**
     * Elimina el valor de la celda indicada. La celda pasa a tener valor nulo.
     * 
     * @param index indice de la celda a vaciar 
     */
    public abstract void erase(int index);

    /**
     * Toma la columna desde un indice inicial hasta un indice final especificado y devuelve una sub Columna formada por estas celdas.
     * 
     * @param startIndex indice donde se comienza a recortar (primer elemento de la sub Columna)
     * @param endIndex indice hasta el cual se recorta (ultimo elemento de la sub Columna)
     * @return Columna recortada
     */

    public abstract Column<T> slice(int startIndex, int endIndex);

    /**
     * Concatena los elementos de la columna especificada a la columna actual.
     * 
     * @param other columna de tipo Columna que se va a concatenar a la columna actual 
     */
    
    public void concat(Column<T> other){
        for (int i=0; i < other.length(); i++){
            this.add(other.get(i));
        }
    }

    /**
     * Devuelve la cantidad de elementos en la columna.
     * 
     * @return cantidad de elementos en la columna
     */
    public abstract int length();

    /**
     * Ordena la columna de manera ascendente o descendente segun lo indicado y devuelve un mapa que asocia los indices originales con los nuevos indices despues del ordenamiento.
     * 
     * @param ascending indica si la ordenamiento debe ser ascendente (true) o descendente (false)
     * @return mapa que asocia los indices originales con los nuevos indices obtenidos despues del ordenamiento 
     */
    public abstract Map<Integer, Integer> sort(boolean ascending);

    /**
     * Devuelve la cantidad de nulos en la columna
     * @return cantidad de nulos
     */
    public int nNull(){
        int count = 0;
        for (int i=0; i < length(); i++){
            if (get(i) == null){
                count++;
            }
        }
        return count;
    }

    /**
     * Filtra los elementos de la columna segun el predicado proporcionado y devuelve una lista de los indices de los elementos que cumplen con el criterio de filtrado.
     * 
     * @param filter predicado que define el criterio de filtrado
     * @return lista de los indices de los elementos que cumplen con el criterio de filtrado
     */
    public List<Integer> filter(Predicate<T> filter){
        List<Integer> indices = new ArrayList<>();
        for (int i=0; i < this.length(); i++){
            if (this.get(i) != null && filter.test(this.get(i))){
                indices.add(i);
            }
        }
        return indices;
    }

     /**
     * Reorganiza los elementos de la columna segun un mapa de orden que contiene los indices originales.  
     *  
     * @param order mapa que contiene como claves los indices originales y como valores los nuevos indices obtenidos despues del ordenamiento 
     */
    public abstract Column<T> sortByIndex(Map<Integer, Integer> order);

    /**
     * Toma los elementos correspondientes a los indices de la lista indices y crea una nueva columna con estos elementos.
     * 
     * @param indexes lista de indices que especifica que elementos se incluiran en la nueva columna
     * @return nueva columna con los elementos correspondientes a los indices proporcionados
     */
    public abstract Column<T> filterByIndex(List<Integer> indexes);

    /**
     * Aplica una transformacion a cada elemento no nulo de la columna utilizando el operador proporcionado.
     * 
     * @param transformer operador que define la transformacion a aplicar a cada elemento no nulo de la columna
     */
    public abstract Column<T> transform(UnaryOperator<T> transformer);

    /**
     * Crea y devuelve una copia profunda de la columna actual, incluyendo todos sus elementos
     */
    public abstract Column<T> clone();

    /**
     * Obtiene y devuelve el tipo de dato de la columna.
     * 
     * @return tipo de dato de la columna
     */
    public DataType getColumnType()
    {
        if(this instanceof IntegerColumn)
        {
            return DataType.INT;
        }
        else if(this instanceof BooleanColumn)
        {
            return DataType.BOOL;
        }
        else if(this instanceof DoubleColumn)
        {
            return DataType.DOUBLE;
        }
        
        return DataType.STRING;
    }

    /**
     * Obtiene los valores unicos de la columna. 
     */
    public abstract Column<T> unique();

    /**
     * Verifica si una columna contiene un elemento especifico.
     * 
     * @param element valor que se quiere verificar si está contenido en la columna
     * @return
     */
    public boolean contains(T element)
    {
        if(element == null)
        {
            return (nNull() > 0);
        }

        for(int i = 0; i < length(); i++)
            if(element.equals(get(i)))
                return true;

        return false;
    }

    /**
     * Convierte la columna actual a una lista.
     * 
     * @return una lista que contiene todos los elementos de la columna convertida
     */
    public List<T> toList()
    {
        List<T> lista = new ArrayList<>();
        for(int i = 0; i < length(); i++)
        {
            lista.add(get(i));
        }

        return lista;
    }
}

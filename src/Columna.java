package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import utils.DataType;

import java.util.function.Predicate;

public abstract class Columna<T> implements Cloneable {
    /**
     * Obtiene el valor que se encuentra en la celda indicada.
     * 
     * @param indice indice de la celda
     * @return       valor encontrado en la celda 
     */
    public abstract T getCelda(int indice);

    /**
     * Establece el valor de la celda.
     * 
     * @param indice indice de la celda 
     * @param valor  valor que se desea establecer
     */
    public abstract void setCelda(int indice, T valor);

    /**
     * Añade el valor especificado en el indice indicado. 
     * Desplaza el elemento actualmente en esa posicion (si lo hay) y cualquier elemento posterior hacia la derecha (agrega uno a sus indices). 
     * 
     * @param indice indice donde se inserta el valor
     * @param valor  valor a insertar
     */
    public abstract void añadirCelda(int indice, T valor);
    /**
     * Añade el valor especificado al final de la columna.
     * 
     * @param valor valor a insertar
     */
    public abstract void añadirCelda(T valor);
    /**
     * Verifica si el indice esta dentro del rango de la Columna.
     * 
     * @param indice indice a verificar
     * @return       verdadero si el indice se encuentra en el rango
     */
    public boolean contieneIndice(int indice){
        return 0 <= indice && indice <= length()-1;
    }
    /**
     * Elimina el elemento encontrado en la posicion indicada.
     * Desplaza cualquier elemento posterior hacia la izquierda (resta uno de sus indices).
     * 
     * @param indice indice del elemento a eliminar
     */
    public abstract void eliminarCelda(int indice);
    /**
     * Elimina el valor de la celda indicada. La celda pasa a tener valor nulo.
     * 
     * @param indice indice de la celda a vaciar 
     */
    public abstract void borrarValorCelda(int indice);

    /**
     * Toma la columna desde un indice inicial hasta un indice final especificado y devuelve una sub Columna formada por estas celdas.
     * 
     * @param indiceInicio indice donde se comienza a recortar (primer elemento de la sub Columna)
     * @param indiceFinal  indice hasta el cual se recorta (ultimo elemento de la sub Columna)
     * @return             Columna recortada
     */

    public abstract Columna<T> recortarColumna(int indiceInicio, int indiceFinal);

    /**
     * Concatena los elementos de la columna especificada a la columna actual.
     * 
     * @param otraColumna columna de tipo Columna que se va a concatenar a la columna actual 
     */
    
    public void concatenarColumna(Columna<T> otraColumna){
        for (int i=0; i < otraColumna.length(); i++){
            this.añadirCelda(otraColumna.getCelda(i));
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
     * @param creciente indica si la ordenamiento debe ser ascendente (true) o descendente (false)
     * @return          mapa que asocia los indices originales con los nuevos indices obtenidos despues del ordenamiento 
     */
    public abstract Map<Integer, Integer> ordenar(boolean creciente);

    /**
     * Devuelve la cantidad de nulos en la columna
     * @return cantidad de nulos
     */
    public int cantNull(){
        int count = 0;
        for (int i=0; i < length(); i++){
            if (getCelda(i) == null){
                count++;
            }
        }
        return count;
    }

    /**
     * Filtra los elementos de la columna segun el predicado proporcionado y devuelve una lista de los indices de los elementos que cumplen con el criterio de filtrado.
     * 
     * @param filtro predicado que define el criterio de filtrado
     * @return       lista de los indices de los elementos que cumplen con el criterio de filtrado
     */
    public List<Integer> filtrar(Predicate<T> filtro){
        List<Integer> indices = new ArrayList<>();
        for (int i=0; i < this.length(); i++){
            if (this.getCelda(i) != null && filtro.test(this.getCelda(i))){
                indices.add(i);
            }
        }
        return indices;
    }

     /**
     * Reorganiza los elementos de la columna segun un mapa de orden que contiene los indices originales.  
     *  
     * @param orden mapa que contiene como claves los indices originales y como valores los nuevos indices obtenidos despues del ordenamiento 
     */
    public abstract Columna<T> ordenarPorIndice(Map<Integer, Integer> orden);

    /**
     * Toma los elementos correspondientes a los indices de la lista indices y crea una nueva columna con estos elementos.
     * 
     * @param indices lista de indices que especifica que elementos se incluiran en la nueva columna
     * @return        nueva columna con los elementos correspondientes a los indices proporcionados
     */
    public abstract Columna<T> filtrarPorIndice(List<Integer> indices);

    /**
     * Aplica una transformacion a cada elemento no nulo de la columna utilizando el operador proporcionado.
     * 
     * @param transformacion operador que define la transformacion a aplicar a cada elemento no nulo de la columna
     */
    public abstract Columna<T> transformar(UnaryOperator<T> transformacion);

    /**
     * Crea y devuelve una copia profunda de la columna actual, incluyendo todos sus elementos
     */
    public abstract Columna<T> clone();

    /**
     * Obtiene y devuelve el tipo de dato de la columna.
     * 
     * @return tipo de dato de la columna
     */
    public DataType getColumnType()
    {
        if(this instanceof ColumnaInt)
        {
            return DataType.INT;
        }
        else if(this instanceof ColumnaBool)
        {
            return DataType.BOOL;
        }
        else if(this instanceof ColumnaDouble)
        {
            return DataType.DOUBLE;
        }
        
        return DataType.STRING;
    }

    /**
     * Obtiene los valores unicos de la columna. 
     */
    public abstract Columna<T> unique();

    /**
     * Verifica si una columna contiene un elemento especifico.
     * 
     * @param elemento valor que se quiere verificar si está contenido en la columna
     * @return
     */
    public boolean contiene(T elemento)
    {
        if(elemento == null)
        {
            return (cantNull() > 0);
        }

        for(int i = 0; i < length(); i++)
            if(elemento.equals(getCelda(i)))
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
            lista.add(getCelda(i));
        }

        return lista;
    }
}

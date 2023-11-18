package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;

public abstract class Columna<T> implements Cloneable {
    /**
     * Obtiene el valor que se encuentra en la celda indicada
     
     * @param indice indice de la celda
     * @return valor encontrado en la celda 
     */
    public abstract T getCelda(int indice);

    /**
     * Establece el valor de la celda
      
     * @param indice indice de la celda 
     * @param valor valor que se desea establecer
     */
    public abstract void setCelda(int indice, T valor);

    /**
     * Añade el valor especificado en el indice indicado. 
     * Desplaza el elemento actualmente en esa posicion (si lo hay) y cualquier elemento posterior hacia la derecha (agrega uno a sus indices). 
     
     * @param indice indice donde se inserta el valor
     * @param valor valor a insertar
     */
    public abstract void añadirCelda(int indice, T valor);
    /**
     * Añade el valor especificado al final de la columna
     
     * @param valor valor a insertar
     */
    public abstract void añadirCelda(T valor);
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
     * Toma la columna desde un indice inicial hasta un indice final especificado y devuelve una sub Columna formada por estas celdas
     * 
     * @param indiceInicio indice donde se comienza a recortar (primer elemento de la sub Columna)
     * @param indiceFinal indice hasta el cual se recorta (ultimo elemento de la sub Columna)
     * @return Columna recortada
     */

    public abstract Columna<T> recortarColumna(int indiceInicio, int indiceFinal);

    /**
     * Concatena los elementos de la columna especificada a la columna actual
     * 
     * @param otraColumna columna de tipo Columna que se va a concatenar a la columna actual 
     */
    public void concatenarColumna(Columna<T> otraColumna){
        for (int i=0; i < otraColumna.length(); i++){
            this.añadirCelda(otraColumna.getCelda(i));
        }
    }

    /**
     * Devuelve la cantidad de elementos en la columna
     
     * @return cantidad de elementos en la columna
     */
    public abstract int length();

    /**
     * Ordena la columna de manera ascendente o descendente segun lo indicado y devuelve un mapa que asocia los indices originales con los nuevos indices despues del ordenamiento
     
     * @param creciente indica si la ordenamiento debe ser ascendente (true) o descendente (false)
     * @return mapa que asocia los indices originales con los nuevos indices obtenidos despues del ordenamiento 
     */
    public abstract Map<Integer, Integer> ordenar(boolean creciente);

    /**
     * Reorganiza los elementos de la columna segun un mapa de orden que contiene los indices originales   
     
     * @param orden mapa que contiene como claves los indices originales y como valores los nuevos indices obtenidos despues del ordenamiento 
     */
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

    /**
     * Filtra los elementos de la columna segun el predicado proporcionado y devuelve una lista de los indices de los elementos que cumplen con el criterio de filtrado.
     
     * @param filtro predicado que define el criterio de filtrado
     * @return lista de los indices de los elementos que cumplen con el criterio de filtrado
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
     * Toma los elementos correspondientes a los indices de la lista indices y crea una nueva columna con estos elementos
 
     * @param indices lista de indices que especifica que elementos se incluiran en la nueva columna
     * @return nueva columna con los elementos correspondientes a los indices proporcionados
     */
    public abstract Columna<T> filtrarPorIndice(List<Integer> indices);

    /**
     * Aplica una transformacion a cada elemento no nulo de la columna utilizando el operador proporcionado
     * @param transformacion operador que define la transformacion a aplicar a cada elemento no nulo de la columna
     */
    public void transformar(UnaryOperator<T> transformacion){
        for (int i=0; i < length(); i++){
            if (getCelda(i) != null){
                setCelda(i, transformacion.apply(getCelda(i)));
            }
        }
    }

    /**
     * Crea y devuelve una copia profunda de la columna actual, incluyendo todos sus elementos
     */
    public abstract Columna<T> clone();
}

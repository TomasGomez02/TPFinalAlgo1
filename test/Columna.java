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
    public boolean contieneIndice(int indice){
        return 0 <= indice && indice <= length()-1;
    }
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

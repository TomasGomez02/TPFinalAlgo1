package koala;


import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import koala.utils.DataType;
import koala.utils.IllegalCastException;
import koala.utils.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class IntegerColumn extends NumberColumn<Integer> {
        
    private List<Integer> data;

    public IntegerColumn() {
        this.data = new ArrayList<>();
    }

    public IntegerColumn(List<Integer> data) {
        this.data = data;
    }

    public IntegerColumn(Integer[] data){
        this();
        for (Integer num : data) {
            this.add(num);
        }
    }

    public IntegerColumn(int[] data){
        this();
        for (int elem : data) {
            this.add(elem);
        }
    }

    public IntegerColumn(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            add(null);
        }
    }

    @Override
    public Double mean() {
        Double suma = 0.0;
        for (int i=0; i<data.size(); i++) {
            if(data.get(i) != null)
                suma += data.get(i);
        }
        return suma / (data.size() - nNull());
    }

    @Override
    public Double median() {
        IntegerColumn copia = this.clone();
        copia.sortByIndex(copia.sort(true));
        
        Double mediana;
        if (length() % 2 == 0){
            int mitad = copia.length()/2 - 1;
            mediana = (copia.get(mitad) + copia.get(mitad-1)) / 2.0;
        }
        else{
            int mitad = copia.length() / 2;
            mediana = copia.get(mitad) * 1.0;
        }
        return mediana;
    }

    @Override
    public Integer max() {
        int max = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (data.get(i) != null && data.get(i) > max) {
                max = data.get(i);
            }
        }
        return max;
    }

    @Override
    public Integer min() {
        int min = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (get(i) != null && get(i) < min) {
                min = get(i);
            }
        }
        return min;
    }

    @Override
    public Double std() {
        Double suma = 0.0;
        Double media = this.mean();
        for (int i=0; i<data.size(); i++) {
            Integer elem = get(i);
            if (elem != null){
                suma = suma + ((media - elem) * (media - elem));
            }
        }
        Double desvio = suma / (length() - nNull());
        return Math.sqrt(desvio);
    }

    @Override
    public Integer sum() {
        int suma = 0;
        for (Integer valor : data) {
            suma += valor;
        }
        return suma;
    }

    @Override
    public Integer get(int indice) {
        if (!containsIndex(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice); 
    }

    @Override
    public void set(int index, Integer value) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, value);
    }

    @Override
    public void add(int index, Integer value) {
        if (!containsIndex(index) && index != 0){
            this.data.add(index, value);
        }
        throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
    }

    @Override
    public void add(Integer value) {
        this.data.add(value);
    }

    @Override
    public void remove(int index) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.remove(index);
    }

    @Override
    public void erase(int index) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, null);
    }

    @Override
    public Column<Integer> slice(int startIndex, int endIndex) {
        IntegerColumn recorte = new IntegerColumn();
        for (int i=startIndex; i <= endIndex; i++) {
            recorte.add(this.get(i));
        }
        return recorte;
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public IntegerColumn filterByIndex(List<Integer> indexes) {
        indexes.sort(null);
        IntegerColumn filtrada = new IntegerColumn();
        for (Integer indice : indexes) {
            filtrada.add(this.get(indice));
        }
        return filtrada;
    }

    public IntegerColumn clone(){
        IntegerColumn copia = new IntegerColumn();
        for (Integer num : data) {
            copia.add(num);
        }
        return copia;
    }

    @Override
    public String toString() {
        return this.data.toString();
    }

    @Override
    public Map<Integer, Integer> sort(boolean ascending) {
        // Crear lista de indices para trasladar los valores
        Map<Integer, Integer> trasladar = new HashMap<>();

        // Crea un array con todos sus elementos en false, para saber si ya se ordenaron los elementos
        // dentro de la columna. Si el elemento en la posicion x en la Columna ya esta ordenado, entonces
        // el valor en la posicion x del array va a ser true.
        boolean[] yaSeOrdeno = new boolean[this.length()];

        for (int i=0; i < this.length(); i++){
            Integer idxMinimo = -1;
            // Uso esto para tomar el primer indice no nulo y no ordenado
            for (int k=0; k < this.length(); k++){
                if (this.get(k) != null && !yaSeOrdeno[k]){
                    idxMinimo = k;
                    break;
                }
            }
            if (idxMinimo < 0){
                continue;
            }
            for (int j=0; j < this.length(); j++){
                if (!yaSeOrdeno[j] && this.get(j) != null &&
                this.get(j).compareTo(this.get(idxMinimo)) < 0){
                    idxMinimo = j;
                }
            }
            if (ascending){
                trasladar.put(idxMinimo, i);
            }
            else{
                trasladar.put(idxMinimo, this.length() - i - 1);
            }
            yaSeOrdeno[idxMinimo] = true;
        }
        // Esta parte manda los null al final de la lista
        Integer distanciaDesdeUltimo = 0;
        for (int i=0; i < this.length(); i++){
            if (this.get(i) == null){
                if (ascending){
                    trasladar.put(i, this.length() - distanciaDesdeUltimo -1);
                }
                else{
                    trasladar.put(i, distanciaDesdeUltimo);
                }
                distanciaDesdeUltimo += 1;
            }
        }
        return trasladar;
    }

    @Override
    public IntegerColumn sortByIndex(Map<Integer, Integer> order){
        IntegerColumn copia = this.clone();

        for (int i=0; i < copia.length(); i++){
            Integer newIdx = order.get(i);
            copia.set(newIdx, get(i));
        }
        return copia;
    }

    /**
     * Convierte una columna generica en una columna de tipo entero.
     * 
     * @param col columna la cual se quiere convertir
     * @return    una columna de tipo entero
     * @throws IllegalCastException si la conversion no es posible.
     */
    public static IntegerColumn toIntegerColumn(Column col) throws IllegalCastException
    {
        return toIntegerColumn(col, false);
    }

    /**
     * Convierte una columna generica en una columna de tipo entero, con la opcion de forzar la conversion.
     * 
     * @param col   columna que se debe convertir a tipo entero
     * @param force indica si se debe forzar la conversion
     * @return      una columna de tipo entero
     * @throws IllegalCastException si la conversion no es posible o si se fuerza y hay perdida de informacion.
     */
    public static IntegerColumn toIntegerColumn(Column col, boolean force) throws IllegalCastException
    {
        switch (col.getColumnType()) 
        {
            case BOOL:
                return fromBooleanColumn(col);
            case DOUBLE:
                return fromDoubleColumn(col);
            case STRING:
                return fromStringColumn(col, force);
            default:
                return (IntegerColumn) col.clone();
        }
    }

    private static IntegerColumn fromStringColumn (Column<String> col, boolean force) throws IllegalCastException
    {
        List<Integer> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            String elemento = col.get(i);
            try
            {
                if(elemento != null && !elemento.equals("")  && !elemento.toLowerCase().strip().equals("na"))
                {
                    datos.add(Integer.parseInt(elemento));
                }
                else
                {
                    datos.add(null);
                }
            }
            catch(NumberFormatException e)
            {
                if(!force)
                    throw new IllegalCastException(elemento, String.class.toString(), Integer.class.toString());
                else
                    datos.add(null);
            }
        }

        return new IntegerColumn(datos);
    }

    private static IntegerColumn fromDoubleColumn(Column<Double> col)
    {
        List<Integer> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Double elemento = col.get(i);
            if(elemento != null)
            {
                datos.add(elemento.intValue());
            }
            else
            {
                datos.add(null);
            }
        }

        return new IntegerColumn(datos);
    }

    private static IntegerColumn fromBooleanColumn(Column<Boolean> col)
    {
        List<Integer> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Boolean elemento = col.get(i);
            if(elemento != null)
            {
                datos.add(Types.castBoolToInt(elemento));
            }
            else
            {
                datos.add(null);
            }
        }

        return new IntegerColumn(datos);
    }

    @Override
    public IntegerColumn transform(UnaryOperator<Integer> transformer) {
        IntegerColumn copia = new IntegerColumn();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.add(transformer.apply(get(i)));
            }
        }
        return copia;
    }

    @Override
    public Column<Integer> unique() {
        IntegerColumn unica = new IntegerColumn();
        for(Integer e: data)
        {
            if(!unica.contains(e))
                unica.add(e);
        }
        return unica;
    }
}

package koala;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import koala.utils.IllegalCastException;
import koala.utils.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class BooleanColumn extends Column<Boolean>{
    private List<Boolean> data;

    public BooleanColumn(){
        this.data = new ArrayList<>();
    }

    public BooleanColumn(Boolean[] data){
        this();
        for (Boolean element : data) {
            this.add(element);
        }
    }

    public BooleanColumn(List<Boolean> list){
        this.data = new ArrayList<>(list);
    }

    public BooleanColumn(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            add(null);
        }
    }

    @Override
    public Boolean get(int index) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        return this.data.get(index);
    }

    @Override
    public void set(int index, Boolean value) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, value);
    }

    @Override
    public void add(int index, Boolean value) {
        if (!containsIndex(index) && index != 0){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.add(index, value);
    }

    @Override
    public void add(Boolean value) {
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
    public Column<Boolean> slice(int startIndex, int endIndex) {
        BooleanColumn recorte = new BooleanColumn();
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
    public BooleanColumn clone(){
        BooleanColumn copia = new BooleanColumn();
        for (int i=0; i < this.length(); i++) {
            copia.add(this.get(i));
        }
        return copia;
    }

    public int sum(){
        int suma = 0;
        for (Boolean valor : data) {
            if (valor){
                suma++;
            }
        }
        return suma;
    }

    @Override
    public String toString(){
        return this.data.toString();
    }

    @Override
    public BooleanColumn filterByIndex(List<Integer> indexes){
        indexes.sort(null);
        BooleanColumn filtrada = new BooleanColumn();
        for (Integer indice : indexes) {
            filtrada.add(this.get(indice));
        }
        return filtrada;
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
    public BooleanColumn sortByIndex(Map<Integer, Integer> order){
        BooleanColumn copia = this.clone();
        for (int i=0; i < copia.length(); i++){
            Integer newIdx = order.get(i);
            copia.set(newIdx, get(i));
        }
        return copia;
    }

    public static BooleanColumn toBooleanColumn(Column col) throws IllegalCastException
    {
        return toBooleanColumn(col, false);
    }

    public static BooleanColumn toBooleanColumn(Column col, boolean force) throws IllegalCastException
    {
        switch (col.getColumnType()) 
        {
            case DOUBLE:
                return fromDoubleColumn(col, force);
            case INT:
                return fromIntegerColumn(col, force);
            case STRING:
                return fromStringColumn(col, force);
            default:
                return (BooleanColumn) col.clone();
        }
    }

    private static BooleanColumn fromStringColumn (Column<String> col, boolean force) throws IllegalCastException
    {
        List<Boolean> datos = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            String elemento = col.get(i);
            try
            {
                if(elemento != null && !elemento.equals("") && !elemento.toLowerCase().strip().equals("na"))
                {
                    datos.add(Types.parseBoolean(col.get(i)));
                }
                else
                {
                    datos.add(null);
                }
            }
            catch(NumberFormatException e)
            {
                if(!force)
                    throw new IllegalCastException(col.get(i), String.class.toString(), Boolean.class.toString());
                else
                    datos.add(null);
            }
        }

        return new BooleanColumn(datos);
    }

    private static BooleanColumn fromIntegerColumn(Column<Integer> col, boolean force) throws IllegalCastException
    {
        List<Boolean> datos = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            Integer elemento = col.get(i);
            try
            {
                if(elemento != null)
                {
                    datos.add(Types.numberToBool(col.get(i)));
                }
                else
                {
                    datos.add(null);
                }
            }
            catch(IllegalCastException e)
            {
                if(!force)
                    throw new IllegalCastException(col.get(i).toString(), Integer.class.toString(), Boolean.class.toString());
                else
                    datos.add(null);
            }
        }

        return new BooleanColumn(datos);
    }

    private static BooleanColumn fromDoubleColumn(Column<Double> col, boolean force) throws IllegalCastException
    {
        List<Boolean> datos = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            Double elemento = col.get(i);
            try
            {
                if(elemento != null)
                {
                    datos.add(Types.numberToBool(elemento));
                }
                else
                {
                    datos.add(null);
                }
            }
            catch(IllegalCastException e)
            {
                if(!force )
                    throw new IllegalCastException(col.get(i).toString(), Double.class.toString(), Boolean.class.toString());
                else
                    datos.add(null);
            }
        }

        return new BooleanColumn(datos);
    }

    @Override
    public BooleanColumn transform(UnaryOperator<Boolean> transformer) {
        BooleanColumn copia = new BooleanColumn();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.add(transformer.apply(get(i)));
            }
        }
        return copia;
    }
    @Override
    public Column<Boolean> unique() {
        BooleanColumn unica = new BooleanColumn();
        for(Boolean e: data)
        {
            if(!unica.contains(e))
                unica.add(e);
        }
        return unica;
    }
}

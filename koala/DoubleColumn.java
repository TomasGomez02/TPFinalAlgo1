package koala;


import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import koala.utils.IllegalCastException;
import koala.utils.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class DoubleColumn extends NumberColumn<Double>
{
    private List<Double> data; 

    public DoubleColumn()
    {
        data = new ArrayList<>();
    }

    public DoubleColumn(List<Double> list)
    {
        this();
        for(Double num: list)
            data.add(num);
    }

    public DoubleColumn(double[] array)
    {
        this();
        for(double num: array)
            data.add(num);
    }

    public DoubleColumn(Double[] array){
        this();
        for (Double num : array) {
            this.add(num);
        }
    }

    public DoubleColumn(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            add(null);
        }
    }

    @Override
    public Double mean() {
        Double sum = 0.0;
        for(Double num: data)
        {
            if(num != null)
                sum += num;
        }

        return sum / (length() - nNull());
    }

    @Override
    public Double median() {
        DoubleColumn copia = clone();
        copia.sortByIndex(copia.sort(true));

        Double mediana;
        int indiceMitad;
        if (length() % 2 == 0){
            indiceMitad = copia.length() / 2;
            mediana = (copia.get(indiceMitad) + copia.get(indiceMitad-1))/ 2.0;
        }
        else{
            indiceMitad = copia.length() / 2;
            mediana = copia.get(indiceMitad);
        }
        return mediana;
    }

    @Override
    public Double max() {
        Double max = get(0);
        for (int i=0; i < length(); i++){
            if (get(i) != null && get(i) > max){
                max = get(i);
            }
        }
        return max;
    }

    @Override
    public Double min() {
        Double min = get(0);
        for (int i=0; i < length(); i++){
            if (get(i) != null && get(i) < min){
                min = get(i);
            }
        }
        return min;
    }

    @Override
    public Double std() {
        double sumatoria = 0.0;
        final double media = this.mean();
        for (int i=0; i < this.length(); i++){
            if(get(i) != null)
                sumatoria += Math.pow(get(i) - media, 2);
        }
        return Math.sqrt(sumatoria / (this.length() - nNull()));
    }

    @Override
    public Double sum() {
        Double suma = 0.0;
        for (int i=0; i < length(); i++){
            if(get(i) != null)
                suma += get(i);
        }
        return suma;
    }

    @Override
    public Double get(int indice) {
        if (!containsIndex(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice);
    }

    @Override
    public void set(int index, Double value) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, value);
    }

    @Override
    public void add(int index, Double value) {
        if (!containsIndex(index) && index != 0){
            this.data.add(index, value);
        }
        throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
    }

    @Override
    public void add(Double value) {
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
        this.set(index, null);
    }

    @Override
    public Column<Double> slice(int startIndex, int endIndex) {
        DoubleColumn recorte = new DoubleColumn();
        for (int i=startIndex; i <= endIndex; i++){
            recorte.add(get(i));
        }
        return recorte;
    }

    @Override
    public int length() {
        return data.size();
    }

    @Override
    public String toString(){
        return this.data.toString();
    }

    @Override
    public DoubleColumn clone(){
        DoubleColumn copia = new DoubleColumn();
        for (Double elem : data) {
            copia.add(elem);
        }
        return copia;
    }

    @Override
    public DoubleColumn filterByIndex(List<Integer> indexes) {
        indexes.sort(null);
        DoubleColumn filtrada = new DoubleColumn();
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
    public DoubleColumn sortByIndex(Map<Integer, Integer> order){
        DoubleColumn copia = this.clone();

        for (int i=0; i < copia.length(); i++){
            Integer newIdx = order.get(i);
            copia.set(newIdx, get(i));
        }
        return copia;
    }

    public static DoubleColumn toDoubleColumn(Column col) throws IllegalCastException
    {
        return toDoubleColumn(col, false);
    }

    public static DoubleColumn toDoubleColumn(Column col, boolean force) throws IllegalCastException
    {
        switch (col.getColumnType()) 
        {
            case BOOL:
                return fromBooleanColumn(col);
            case INT:
                return fromIntegerColumn(col);
            case STRING:
                return fromStringColumn(col, force);
            default:
                return (DoubleColumn) col.clone();
        }
    }
    
    private static DoubleColumn fromStringColumn (Column<String> col, boolean force) throws IllegalCastException
    {
        List<Double> datos = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            String elemento = col.get(i);
            try
            {
                if(elemento != null && !elemento.equals("") && !elemento.toLowerCase().strip().equals("na"))
                {
                    datos.add(Double.parseDouble(col.get(i)));
                }
                else
                {
                    datos.add(null);
                }
            }
            catch(NumberFormatException e)
            {
                if(!force)
                    throw new IllegalCastException(col.get(i), String.class.toString(), Double.class.toString());
                else
                    datos.add(null);
            }
        }

        return new DoubleColumn(datos);
    }

    private static DoubleColumn fromIntegerColumn(Column<Integer> col)
    {
        List<Double> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Integer elemento = col.get(i);
            if(elemento != null)
            {
                datos.add(Double.valueOf(elemento));
            }
            else
            {
                datos.add(null);
            }
        }

        return new DoubleColumn(datos);
    }

    private static DoubleColumn fromBooleanColumn(Column<Boolean> col)
    {
        List<Double> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Boolean elemento = col.get(i);
            if(elemento != null)
            {
                datos.add(Types.castBoolToDoule(elemento));
            }
            else
            {
                datos.add(null);
            }
        }

        return new DoubleColumn(datos);
    }

    @Override
    public DoubleColumn transform(UnaryOperator<Double> transformer) {
        DoubleColumn copia = new DoubleColumn();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.add(transformer.apply(get(i)));
            }
        }
        return copia;
    }

    @Override
    public Column<Double> unique() {
        DoubleColumn unica = new DoubleColumn();
        for(Double e: data)
        {
            if(!unica.contains(e))
                unica.add(e);
        }
        return unica;
    }
}

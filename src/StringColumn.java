package src;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.ArrayList;
import java.util.HashMap;

public class StringColumn extends Column<String> {
    private List<String> data;
    
    public StringColumn(){
        this.data = new ArrayList<>();
    }

    public StringColumn(List<String> datos){
        this.data = new ArrayList<>(datos);
    }

    public StringColumn(String[] datos){
        this();
        for (String elemento : datos) {
            this.add(elemento);
        }
    }

    public StringColumn(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            add(null);
        }
    }

    @Override
    public String get(int indice) {
        if (!containsIndex(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice);
    }

    @Override
    public void set(int index, String value) {
        if (!containsIndex(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, value);
    }

    @Override
    public void add(int index, String value) {
        if (!containsIndex(index)){    
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.add(index, value);
    }

    @Override
    public void add(String value) {
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
    public StringColumn slice(int startIndex, int endIndex) {
        StringColumn recorte = new StringColumn();
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
    public StringColumn clone(){
        StringColumn copia = new StringColumn();
        for (int i=0; i < this.length(); i++){
            copia.add(this.get(i));
        }
        return copia;
    }
    
    @Override
    public String toString() {
        return this.data.toString();
    }
    
    @Override
    public StringColumn filterByIndex(List<Integer> indexes) {
        indexes.sort(null);
        StringColumn filtrada = new StringColumn();
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
    public StringColumn sortByIndex(Map<Integer, Integer> order){
        StringColumn copia = this.clone();

        for (int i=0; i < copia.length(); i++){
            Integer newIdx = order.get(i);
            copia.set(newIdx, get(i));
        }
        return copia;
    }

    @Override
    public StringColumn transform(UnaryOperator<String> transformer) {
        StringColumn copia = new StringColumn();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.add(transformer.apply(get(i)));
            }
        }
        return copia;
    }
        
    public static <T> StringColumn toStringColumn(Column<T> col)
    {
        if(col instanceof StringColumn)
            return (StringColumn) col;
        
        List<String> datos = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            T elemento = col.get(i);
            if(elemento != null)
            {
                datos.add(elemento.toString());
            }
            else
            {
                datos.add(null);
            }
        }

        return new StringColumn(datos);
    }

    @Override
    public Column<String> unique() {
        StringColumn unica = new StringColumn();
        for(String e: data)
        {
            if(!unica.contains(e))
                unica.add(e);
        }
        return unica;
    }
}
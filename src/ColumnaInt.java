package src;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import utils.CasteoIlegalException;
import utils.Types;
import utils.DataType;

import java.util.ArrayList;
import java.util.HashMap;

public class ColumnaInt extends ColumnaNum<Integer> {
        
    private List<Integer> data;

    public ColumnaInt() {
        this.data = new ArrayList<>();
    }

    public ColumnaInt(List<Integer> data) {
        this.data = data;
    }

    public ColumnaInt(Integer[] data){
        this();
        for (Integer num : data) {
            this.add(num);
        }
    }

    public ColumnaInt(int[] data){
        this();
        for (int elem : data) {
            this.add(elem);
        }
    }

    public ColumnaInt(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            add(null);
        }
    }

    @Override
    public Double media() {
        Double suma = 0.0;
        for (int i=0; i<data.size(); i++) {
            if(data.get(i) != null)
                suma += data.get(i);
        }
        return suma / (data.size() - cantNull());
    }

    @Override
    public Double mediana() {
        ColumnaInt copia = this.clone();
        copia.ordenarPorIndice(copia.ordenar(true));
        
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
    public Integer maximo() {
        int max = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (data.get(i) != null && data.get(i) > max) {
                max = data.get(i);
            }
        }
        return max;
    }

    @Override
    public Integer minimo() {
        int min = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (get(i) != null && get(i) < min) {
                min = get(i);
            }
        }
        return min;
    }

    @Override
    public Double desvioEstandar() {
        Double suma = 0.0;
        Double media = this.media();
        for (int i=0; i<data.size(); i++) {
            Integer elem = get(i);
            if (elem != null){
                suma = suma + ((media - elem) * (media - elem));
            }
        }
        Double desvio = suma / (length() - cantNull());
        return Math.sqrt(desvio);
    }

    @Override
    public Integer sumaAcumulada() {
        int suma = 0;
        for (Integer valor : data) {
            suma += valor;
        }
        return suma;
    }

    @Override
    public Integer get(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice); 
    }

    @Override
    public void set(int index, Integer value) {
        if (!contieneIndice(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, value);
    }

    @Override
    public void add(int index, Integer value) {
        if (!contieneIndice(index) && index != 0){
            this.data.add(index, value);
        }
        throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
    }

    @Override
    public void add(Integer valor) {
        this.data.add(valor);
    }

    @Override
    public void eliminarCelda(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.remove(indice);
    }

    @Override
    public void borrarValorCelda(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.set(indice, null);
    }

    @Override
    public Columna<Integer> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaInt recorte = new ColumnaInt();
        for (int i=indiceInicio; i <= indiceFinal; i++) {
            recorte.add(this.get(i));
        }
        return recorte;
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public ColumnaInt filtrarPorIndice(List<Integer> indices) {
        indices.sort(null);
        ColumnaInt filtrada = new ColumnaInt();
        for (Integer indice : indices) {
            filtrada.add(this.get(indice));
        }
        return filtrada;
    }

    public ColumnaInt clone(){
        ColumnaInt copia = new ColumnaInt();
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
    public Map<Integer, Integer> ordenar(boolean creciente) {
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
            if (creciente){
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
                if (creciente){
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
    public ColumnaInt ordenarPorIndice(Map<Integer, Integer> orden){
        ColumnaInt copia = this.clone();

        for (int i=0; i < copia.length(); i++){
            Integer newIdx = orden.get(i);
            copia.set(newIdx, get(i));
        }
        return copia;
    }

    public static ColumnaInt toColumnaInt(Columna col) throws CasteoIlegalException
    {
        return toColumnaInt(col, false);
    }

    public static ColumnaInt toColumnaInt(Columna col, boolean forzar) throws CasteoIlegalException
    {
        switch (col.getColumnType()) 
        {
            case BOOL:
                return fromColumnaBool(col);
            case DOUBLE:
                return fromColumnaDouble(col);
            case STRING:
                return fromColumnaString(col, forzar);
            default:
                return (ColumnaInt) col.clone();
        }
    }

    private static ColumnaInt fromColumnaString (Columna<String> col, boolean forzar) throws CasteoIlegalException
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
                if(!forzar)
                    throw new CasteoIlegalException(elemento, String.class.toString(), Integer.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaInt(datos);
    }

    private static ColumnaInt fromColumnaDouble(Columna<Double> col)
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

        return new ColumnaInt(datos);
    }

    private static ColumnaInt fromColumnaBool(Columna<Boolean> col)
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

        return new ColumnaInt(datos);
    }

    @Override
    public ColumnaInt transformar(UnaryOperator<Integer> transformacion) {
        ColumnaInt copia = new ColumnaInt();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.add(transformacion.apply(get(i)));
            }
        }
        return copia;
    }

    @Override
    public Columna<Integer> unique() {
        ColumnaInt unica = new ColumnaInt();
        for(Integer e: data)
        {
            if(!unica.contiene(e))
                unica.add(e);
        }
        return unica;
    }
}

package src;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import utils.CasteoIlegalException;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class ColumnaBool extends Columna<Boolean>{
    private List<Boolean> data;

    public ColumnaBool(){
        this.data = new ArrayList<>();
    }

    public ColumnaBool(Boolean[] data){
        this();
        for (Boolean element : data) {
            this.añadirCelda(element);
        }
    }

    public ColumnaBool(List<Boolean> otro){
        this.data = new ArrayList<>(otro);
    }

    public ColumnaBool(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            añadirCelda(null);
        }
    }

    @Override
    public Boolean get(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice);
    }

    @Override
    public void setCelda(int indice, Boolean valor) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Boolean valor) {
        if (!contieneIndice(indice) && indice != 0){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.add(indice, valor);
    }

    @Override
    public void añadirCelda(Boolean valor) {
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
    public Columna<Boolean> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaBool recorte = new ColumnaBool();
        for (int i=indiceInicio; i <= indiceFinal; i++) {
            recorte.añadirCelda(this.get(i));
        }
        return recorte;
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public ColumnaBool clone(){
        ColumnaBool copia = new ColumnaBool();
        for (int i=0; i < this.length(); i++) {
            copia.añadirCelda(this.get(i));
        }
        return copia;
    }

    public int sumaAcumulada(){
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
    public ColumnaBool filtrarPorIndice(List<Integer> indices){
        indices.sort(null);
        ColumnaBool filtrada = new ColumnaBool();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.get(indice));
        }
        return filtrada;
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
    public ColumnaBool ordenarPorIndice(Map<Integer, Integer> orden){
        ColumnaBool copia = this.clone();
        for (int i=0; i < copia.length(); i++){
            Integer newIdx = orden.get(i);
            copia.setCelda(newIdx, get(i));
        }
        return copia;
    }

    public static ColumnaBool toBoolColumn(Columna col) throws CasteoIlegalException
    {
        return toBoolColumn(col, false);
    }

    public static ColumnaBool toBoolColumn(Columna col, boolean forzar) throws CasteoIlegalException
    {
        switch (col.getColumnType()) 
        {
            case DOUBLE:
                return fromColumnaDouble(col, forzar);
            case INT:
                return fromColumnaInt(col, forzar);
            case STRING:
                return fromColumnaString(col, forzar);
            default:
                return (ColumnaBool) col.clone();
        }
    }

    private static ColumnaBool fromColumnaString (Columna<String> col, boolean forzar) throws CasteoIlegalException
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
                if(!forzar)
                    throw new CasteoIlegalException(col.get(i), String.class.toString(), Boolean.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaBool(datos);
    }

    private static ColumnaBool fromColumnaInt(Columna<Integer> col, boolean forzar) throws CasteoIlegalException
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
            catch(CasteoIlegalException e)
            {
                if(!forzar)
                    throw new CasteoIlegalException(col.get(i).toString(), Integer.class.toString(), Boolean.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaBool(datos);
    }

    private static ColumnaBool fromColumnaDouble(Columna<Double> col, boolean forzar) throws CasteoIlegalException
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
            catch(CasteoIlegalException e)
            {
                if(!forzar)
                    throw new CasteoIlegalException(col.get(i).toString(), Double.class.toString(), Boolean.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaBool(datos);
    }

    @Override
    public ColumnaBool transformar(UnaryOperator<Boolean> transformacion) {
        ColumnaBool copia = new ColumnaBool();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.añadirCelda(transformacion.apply(get(i)));
            }
        }
        return copia;
    }
    @Override
    public Columna<Boolean> unique() {
        ColumnaBool unica = new ColumnaBool();
        for(Boolean e: data)
        {
            if(!unica.contiene(e))
                unica.añadirCelda(e);
        }
        return unica;
    }
}

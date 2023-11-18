package test;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import utils.CasteoIlegal;
import utils.Types;

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
            this.añadirCelda(num);
        }
    }

    public ColumnaInt(int[] data){
        this();
        for (int elem : data) {
            this.añadirCelda(elem);
        }
    }

    @Override
    public Double media() {
        Double suma = 0.0;
        for (int i=0; i<data.size(); i++) {
            suma += data.get(i);
        }
        return suma / data.size();
    }

    @Override
    public Double mediana() {
        ColumnaInt copia = this.clone();
        copia.ordenarPorIndice(copia.ordenar(true));
        
        Double mediana;
        if (length() % 2 == 0){
            int mitad = copia.length()/2 - 1;
            mediana = (copia.getCelda(mitad) + copia.getCelda(mitad-1)) / 2.0;
        }
        else{
            int mitad = copia.length() / 2;
            mediana = copia.getCelda(mitad) * 1.0;
        }
        return mediana;
    }

    @Override
    public Integer maximo() {
        int max = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (data.get(i) > max) {
                max = data.get(i);
            }
        }
        return max;
    }

    @Override
    public Integer minimo() {
        int min = data.get(0);
        for (int i=1; i<data.size(); i++) {
            if (getCelda(i) != null && getCelda(i) < min) {
                min = getCelda(i);
            }
        }
        return min;
    }

    @Override
    public Double desvioEstandar() {
        Double suma = 0.0;
        Double media = this.media();
        for (int i=0; i<data.size(); i++) {
            Integer elem = getCelda(i);
            if (elem != null){
                suma = suma + ((media - elem) * (media - elem));
            }
        }
        Double desvio = suma / data.size();
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
    public Integer getCelda(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice); 
    }

    @Override
    public void setCelda(int indice, Integer valor) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Integer valor) {
        if (!contieneIndice(indice) && indice != 0){
            this.data.add(indice, valor);
        }
        throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
    }

    @Override
    public void añadirCelda(Integer valor) {
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
            recorte.añadirCelda(this.getCelda(i));
        }
        return recorte;
    }

    @Override
    public int length() {
        return this.data.size();
    }

    @Override
    public ColumnaInt filtrarPorIndice(List<Integer> indices) {
        ColumnaInt filtrada = new ColumnaInt();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }

    public ColumnaInt clone(){
        ColumnaInt copia = new ColumnaInt();
        for (Integer num : data) {
            copia.añadirCelda(num);
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
                if (this.getCelda(k) != null && !yaSeOrdeno[k]){
                    idxMinimo = k;
                    break;
                }
            }
            if (idxMinimo < 0){
                continue;
            }
            for (int j=0; j < this.length(); j++){
                if (!yaSeOrdeno[j] && this.getCelda(j) != null &&
                this.getCelda(j).compareTo(this.getCelda(idxMinimo)) < 0){
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
            if (this.getCelda(i) == null){
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
            copia.setCelda(newIdx, getCelda(i));
        }
        return copia;
    }

    public static ColumnaInt fromColumnaString(Columna<String> col) throws CasteoIlegal
    {
        return fromColumnaString(col, false);
    }

    public static ColumnaInt fromColumnaString (Columna<String> col, boolean forzar) throws CasteoIlegal
    {
        List<Integer> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            String elemento = col.getCelda(i);
            try
            {
                if(elemento != null && !elemento.equals("") && !elemento.toLowerCase().strip().equals("na"))
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
                    throw new CasteoIlegal(elemento, String.class.toString(), Integer.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaInt(datos);
    }

    public static ColumnaInt fromColumnaDouble(Columna<Double> col)
    {
        List<Integer> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Double elemento = col.getCelda(i);
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

    public static ColumnaInt fromColumnaBool(Columna<Boolean> col)
    {
        List<Integer> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Boolean elemento = col.getCelda(i);
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
            if (getCelda(i) != null){
                copia.añadirCelda(transformacion.apply(getCelda(i)));
            }
        }
        return copia;
    }
}

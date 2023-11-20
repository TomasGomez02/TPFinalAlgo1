package src;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import utils.CasteoIlegalException;
import utils.Types;

import java.util.ArrayList;
import java.util.HashMap;

public class ColumnaDouble extends ColumnaNum<Double>
{
    private List<Double> data; 

    public ColumnaDouble()
    {
        data = new ArrayList<>();
    }

    public ColumnaDouble(List<Double> lista)
    {
        this();
        for(Double num: lista)
            data.add(num);
    }

    public ColumnaDouble(double[] array)
    {
        this();
        for(double num: array)
            data.add(num);
    }

    public ColumnaDouble(Double[] array){
        this();
        for (Double num : array) {
            this.añadirCelda(num);
        }
    }

    public ColumnaDouble(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            añadirCelda(null);
        }
    }

    @Override
    public Double media() {
        Double sum = 0.0;
        for(Double num: data)
        {
            if(num != null)
                sum += num;
        }

        return sum / (length() - cantNull());
    }

    @Override
    public Double mediana() {
        ColumnaDouble copia = clone();
        copia.ordenarPorIndice(copia.ordenar(true));

        Double mediana;
        int indiceMitad;
        if (length() % 2 == 0){
            indiceMitad = copia.length() / 2;
            mediana = (copia.getCelda(indiceMitad) + copia.getCelda(indiceMitad-1))/ 2.0;
        }
        else{
            indiceMitad = copia.length() / 2;
            mediana = copia.getCelda(indiceMitad);
        }
        return mediana;
    }

    @Override
    public Double maximo() {
        Double max = getCelda(0);
        for (int i=0; i < length(); i++){
            if (getCelda(i) != null && getCelda(i) > max){
                max = getCelda(i);
            }
        }
        return max;
    }

    @Override
    public Double minimo() {
        Double min = getCelda(0);
        for (int i=0; i < length(); i++){
            if (getCelda(i) != null && getCelda(i) < min){
                min = getCelda(i);
            }
        }
        return min;
    }

    @Override
    public Double desvioEstandar() {
        double sumatoria = 0.0;
        final double media = this.media();
        for (int i=0; i < this.length(); i++){
            if(getCelda(i) != null)
                sumatoria += Math.pow(getCelda(i) - media, 2);
        }
        return Math.sqrt(sumatoria / (this.length() - cantNull()));
    }

    @Override
    public Double sumaAcumulada() {
        Double suma = 0.0;
        for (int i=0; i < length(); i++){
            if(getCelda(i) != null)
                suma += getCelda(i);
        }
        return suma;
    }

    @Override
    public Double getCelda(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice);
    }

    @Override
    public void setCelda(int indice, Double valor) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Double valor) {
        if (!contieneIndice(indice) && indice != 0){
            this.data.add(indice, valor);
        }
        throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
    }

    @Override
    public void añadirCelda(Double valor) {
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
        this.setCelda(indice, null);
    }

    @Override
    public Columna<Double> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaDouble recorte = new ColumnaDouble();
        for (int i=indiceInicio; i <= indiceFinal; i++){
            recorte.añadirCelda(getCelda(i));
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
    public ColumnaDouble clone(){
        ColumnaDouble copia = new ColumnaDouble();
        for (Double elem : data) {
            copia.añadirCelda(elem);
        }
        return copia;
    }

    @Override
    public ColumnaDouble filtrarPorIndice(List<Integer> indices) {
        ColumnaDouble filtrada = new ColumnaDouble();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
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
    public ColumnaDouble ordenarPorIndice(Map<Integer, Integer> orden){
        ColumnaDouble copia = this.clone();

        for (int i=0; i < copia.length(); i++){
            Integer newIdx = orden.get(i);
            copia.setCelda(newIdx, getCelda(i));
        }
        return copia;
    }

    public static ColumnaDouble fromColumnaString (Columna<String> col) throws CasteoIlegalException
    {
        return fromColumnaString(col, false);
    }
    
    public static ColumnaDouble fromColumnaString (Columna<String> col, boolean forzar) throws CasteoIlegalException
    {
        List<Double> datos = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            String elemento = col.getCelda(i);
            try
            {
                if(elemento != null && !elemento.equals("") && !elemento.toLowerCase().strip().equals("na"))
                {
                    datos.add(Double.parseDouble(col.getCelda(i)));
                }
                else
                {
                    datos.add(null);
                }
            }
            catch(NumberFormatException e)
            {
                if(!forzar)
                    throw new CasteoIlegalException(col.getCelda(i), String.class.toString(), Double.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaDouble(datos);
    }

    public static ColumnaDouble fromColumnaInt(Columna<Integer> col)
    {
        List<Double> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Integer elemento = col.getCelda(i);
            if(elemento != null)
            {
                datos.add(Double.valueOf(elemento));
            }
            else
            {
                datos.add(null);
            }
        }

        return new ColumnaDouble(datos);
    }

    public static ColumnaDouble fromColumnaBool(Columna<Boolean> col)
    {
        List<Double> datos = new ArrayList<>();
        
        for(int i = 0; i < col.length(); i++)
        {
            Boolean elemento = col.getCelda(i);
            if(elemento != null)
            {
                datos.add(Types.castBoolToDoule(elemento));
            }
            else
            {
                datos.add(null);
            }
        }

        return new ColumnaDouble(datos);
    }

    @Override
    public ColumnaDouble transformar(UnaryOperator<Double> transformacion) {
        ColumnaDouble copia = new ColumnaDouble();
        for (int i=0; i < length(); i++){
            if (getCelda(i) != null){
                copia.añadirCelda(transformacion.apply(getCelda(i)));
            }
        }
        return copia;
    }

    @Override
    public Columna<Double> unique() {
        ColumnaDouble unica = new ColumnaDouble();
        for(Double e: data)
        {
            if(!unica.contiene(e))
                unica.añadirCelda(e);
        }
        return unica;
    }
}

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
            this.add(num);
        }
    }

    public ColumnaDouble(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            add(null);
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
            mediana = (copia.get(indiceMitad) + copia.get(indiceMitad-1))/ 2.0;
        }
        else{
            indiceMitad = copia.length() / 2;
            mediana = copia.get(indiceMitad);
        }
        return mediana;
    }

    @Override
    public Double maximo() {
        Double max = get(0);
        for (int i=0; i < length(); i++){
            if (get(i) != null && get(i) > max){
                max = get(i);
            }
        }
        return max;
    }

    @Override
    public Double minimo() {
        Double min = get(0);
        for (int i=0; i < length(); i++){
            if (get(i) != null && get(i) < min){
                min = get(i);
            }
        }
        return min;
    }

    @Override
    public Double desvioEstandar() {
        double sumatoria = 0.0;
        final double media = this.media();
        for (int i=0; i < this.length(); i++){
            if(get(i) != null)
                sumatoria += Math.pow(get(i) - media, 2);
        }
        return Math.sqrt(sumatoria / (this.length() - cantNull()));
    }

    @Override
    public Double sumaAcumulada() {
        Double suma = 0.0;
        for (int i=0; i < length(); i++){
            if(get(i) != null)
                suma += get(i);
        }
        return suma;
    }

    @Override
    public Double get(int indice) {
        if (!contieneIndice(indice)){
            throw new IndexOutOfBoundsException("Indice "+indice+" fuera de rango para longitud "+length());
        }
        return this.data.get(indice);
    }

    @Override
    public void set(int index, Double value) {
        if (!contieneIndice(index)){
            throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
        }
        this.data.set(index, value);
    }

    @Override
    public void add(int index, Double value) {
        if (!contieneIndice(index) && index != 0){
            this.data.add(index, value);
        }
        throw new IndexOutOfBoundsException("Indice "+index+" fuera de rango para longitud "+length());
    }

    @Override
    public void add(Double valor) {
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
        this.set(indice, null);
    }

    @Override
    public Columna<Double> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaDouble recorte = new ColumnaDouble();
        for (int i=indiceInicio; i <= indiceFinal; i++){
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
    public ColumnaDouble clone(){
        ColumnaDouble copia = new ColumnaDouble();
        for (Double elem : data) {
            copia.add(elem);
        }
        return copia;
    }

    @Override
    public ColumnaDouble filtrarPorIndice(List<Integer> indices) {
        indices.sort(null);
        ColumnaDouble filtrada = new ColumnaDouble();
        for (Integer indice : indices) {
            filtrada.add(this.get(indice));
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
    public ColumnaDouble ordenarPorIndice(Map<Integer, Integer> orden){
        ColumnaDouble copia = this.clone();

        for (int i=0; i < copia.length(); i++){
            Integer newIdx = orden.get(i);
            copia.set(newIdx, get(i));
        }
        return copia;
    }

    public static ColumnaDouble toDoubleColumn(Columna col) throws CasteoIlegalException
    {
        return toDoubleColumn(col, false);
    }

    public static ColumnaDouble toDoubleColumn(Columna col, boolean forzar) throws CasteoIlegalException
    {
        switch (col.getColumnType()) 
        {
            case BOOL:
                return fromColumnaBool(col);
            case INT:
                return fromColumnaInt(col);
            case STRING:
                return fromColumnaString(col, forzar);
            default:
                return (ColumnaDouble) col.clone();
        }
    }
    
    private static ColumnaDouble fromColumnaString (Columna<String> col, boolean forzar) throws CasteoIlegalException
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
                if(!forzar)
                    throw new CasteoIlegalException(col.get(i), String.class.toString(), Double.class.toString());
                else
                    datos.add(null);
            }
        }

        return new ColumnaDouble(datos);
    }

    private static ColumnaDouble fromColumnaInt(Columna<Integer> col)
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

        return new ColumnaDouble(datos);
    }

    private static ColumnaDouble fromColumnaBool(Columna<Boolean> col)
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

        return new ColumnaDouble(datos);
    }

    @Override
    public ColumnaDouble transformar(UnaryOperator<Double> transformacion) {
        ColumnaDouble copia = new ColumnaDouble();
        for (int i=0; i < length(); i++){
            if (get(i) != null){
                copia.add(transformacion.apply(get(i)));
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
                unica.add(e);
        }
        return unica;
    }
}

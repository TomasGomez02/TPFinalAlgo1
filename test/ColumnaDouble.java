package test;

import java.util.List;
import java.util.Map;
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

    @Override
    public Double media() {
        double sum = 0;
        for(double num: data)
        {
            sum += num;
        }

        return sum / length();
    }

    @Override
    public Double mediana() {
        ColumnaDouble copia = clone();
        copia.ordenarPorIndice(copia.ordenar(true));

        Double mediana;
        if (length() % 2 == 0){
            int mitad = copia.length() / 2;
            mediana = copia.getCelda(mitad) + copia.getCelda(mitad+1) / 2.0;
        }
        else{
            int mitad = copia.length() / 2;
            mediana = copia.getCelda(mitad);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desvioEstandar'");
    }

    @Override
    public Double sumaAcumulada() {
        Double suma = 0.0;
        for (int i=0; i < length(); i++){
            suma += getCelda(i);
        }
        return suma;
    }

    @Override
    public Double getCelda(int indice) {
        return this.data.get(indice);
    }

    @Override
    public void setCelda(int indice, Double valor) {
        this.data.set(indice, valor);
    }

    @Override
    public void añadirCelda(int indice, Double valor) {
        this.data.add(indice, valor);
    }

    @Override
    public void añadirCelda(Double valor) {
        this.data.add(valor);
    }

    @Override
    public void eliminarCelda(int indice) {
        this.data.remove(indice);
    }

    @Override
    public void borrarValorCelda(int indice) {
        this.setCelda(indice, null);
    }

    @Override
    public Columna<Double> recortarColumna(int indiceInicio, int indiceFinal) {
        ColumnaDouble recorte = new ColumnaDouble();
        for (int i=0; i < length(); i++){
            if (i >= indiceInicio && i <= indiceFinal){
                recorte.añadirCelda(getCelda(i));
            }
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
    
}

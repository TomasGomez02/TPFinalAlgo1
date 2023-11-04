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
        data = new ArrayList<>();

        for(Double num: lista)
            data.add(num);
    }

    public ColumnaDouble(double[] array)
    {
        data = new ArrayList<>();

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
    public Columna<Double> filtrar(Double elemento, Filtro<Double> filtro) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'filtrar'");
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mediana'");
    }

    @Override
    public Double maximo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maximo'");
    }

    @Override
    public Double minimo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'minimo'");
    }

    @Override
    public Double desvioEstandar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desvioEstandar'");
    }

    @Override
    public Double sumaAcumulada() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sumaAcumulada'");
    }

    @Override
    public Double getCelda(int indice) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCelda'");
    }

    @Override
    public void setCelda(int indice, Double valor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCelda'");
    }

    @Override
    public void añadirCelda(int indice, Double valor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'añadirCelda'");
    }

    @Override
    public void añadirCelda(Double valor) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'añadirCelda'");
    }

    @Override
    public void eliminarCelda(int indice) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarCelda'");
    }

    @Override
    public void borrarValorCelda(int indice) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarValorCelda'");
    }

    @Override
    public Columna<Double> recortarColumna(int indiceInicio, int indiceFinal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recortarColumna'");
    }

    @Override
    public int length() {
        return data.size();
    }

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

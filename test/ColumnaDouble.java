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
    public void concatenarColumna(Columna<Double> otraColumna) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'concatenarColumna'");
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
    public Map<Integer, Integer> ordenar(boolean creciente) {
        // Crear lista de indices para trasladar los valores
        Map<Integer, Integer> trasladar = new HashMap<>();

        // Crea una copia para poder eliminar elementos sin problemas
        ColumnaDouble copia = this.clone();

        for (int i=0; i < this.length(); i++){
            Integer idxMinimo = copia.getFirstIndex();
            for (int j=0; j < this.length(); j++){
                if (copia.getCelda(j) != null &&
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
            copia.borrarValorCelda(idxMinimo);
        }
        return trasladar;
    }

    public int getFirstIndex() throws IndexOutOfBoundsException{
        // Lo uso para obtener el indice del primer elemento no null
        for (int i=0; i < this.length(); i++){
            if (this.getCelda(i) != null){
                return i;
            }
        }
        throw new IndexOutOfBoundsException("La columna esta vacia o solo tiene elementos nulos");
    }

    @Override
    public ColumnaDouble filtrarPorIndice(List<Integer> indices) {
        ColumnaDouble filtrada = new ColumnaDouble();
        for (Integer indice : indices) {
            filtrada.añadirCelda(this.getCelda(indice));
        }
        return filtrada;
    }
    
}

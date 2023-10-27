package test;

import java.util.List;
import java.util.ArrayList;

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

    @Override
    public void ordenar(boolean creciente) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ordenar'");
    }
    
}

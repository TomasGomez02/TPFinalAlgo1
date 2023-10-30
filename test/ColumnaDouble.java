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
        double max = this.getCelda(0);
        for (int i=1; i < this.length(); i++) {
            if (this.getCelda(i) > max) {
                max = this.getCelda(i);
            }
        }
        return max;
    }

    @Override
    public Double minimo() {
        double min = this.getCelda(0);
        for (int i=1; i < this.length();i++){
            if (this.getCelda(0) < min){
                min = this.getCelda(1);
            }
        }
        return min;
    }

    @Override
    public Double desvioEstandar() {
        double desvio = 0.0;
        for (int i=0; i < this.length(); i++){
            double diferencia = data.get(i) - this.media(); 
            double cuadrado = Math.pow(diferencia, 2); 
            double sumatoria =+ cuadrado; 
            double division = sumatoria/ this.length();
            desvio = Math.sqrt(division);
        }
        return desvio;
    }

    @Override
    public Double sumaAcumulada() {
        double suma = 0/.;
        for (int i=0; i < this.length(); i++){
            suma =+ this.getCelda(i);
        }
        return suma;
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

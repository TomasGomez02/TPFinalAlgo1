package test;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataFrame {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Columna> data;
    private List<String> etiquetas;
    private Map<String, String> tiposColumna;

    public DataFrame(){
        this.data = new HashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Columna> data, Map<String, String> tiposColumna){
        this.data = data;
        this.tiposColumna = tiposColumna;
        // this.etiquetas = data.keySet().stream().toList();
        // Esto es porque el .toList() no funciona en la compu de la unsam
        this.etiquetas = new ArrayList<>();
        for (String etiqueta : data.keySet()) {
            this.etiquetas.add(etiqueta);
        }
    }

    public List<String> nombreColumnas(){
        return this.etiquetas;
    }

    public int cantidadColumnas(){
        return this.etiquetas.size();
    }

    public int cantidadFilas(){
        return this.data.get(etiquetas.get(0)).length();
    }

    public Map<String, String> tiposColumna(){
        return this.tiposColumna;
    }

    public String toString(){
        return this.data.toString();
    }

    public void printDesdeHasta(int indiceInicio, int indiceFinal){
        if (indiceInicio < -1 || indiceFinal > cantidadFilas()){
            indiceInicio = 0;
            indiceFinal = cantidadFilas();
        }

        final int COLUMNASMAX = 10;
        final int OFFSETMINIMO = 6;
        final String OVERFLOWSTRING = "...|";

        final boolean COLUMNASOVERFLOW = (cantidadColumnas() > COLUMNASMAX);
        int cantColumnas = COLUMNASOVERFLOW ? COLUMNASMAX : cantidadColumnas();

        String fila = "";
        String sep = "|";
        int[] tamaño = new int[cantColumnas];

        int espacioIzq;
        int espacioDer;

        // Esta parte ajusta el center por si la etiqueta es muy larga
        for (int i = 0; i < cantColumnas; i++){
            tamaño[i] = OFFSETMINIMO * 2;
            if (etiquetas.get(i).length() + OFFSETMINIMO > tamaño[i]){
                tamaño[i] = etiquetas.get(i).length() + OFFSETMINIMO;
            }
        }

        // Esta parte es para escribir los nombres de las columnas
        for (int i = 0; i < cantColumnas; i++){
            int diff = tamaño[i] - etiquetas.get(i).length();
            espacioIzq = diff / 2;
            if (diff % 2 == 0){
                espacioDer = espacioIzq;
            } else {
                espacioDer = espacioIzq+1;
            }
            fila += " ".repeat(espacioIzq)+etiquetas.get(i)+" ".repeat(espacioDer)+sep;
        }
        if(COLUMNASOVERFLOW)
            fila += OVERFLOWSTRING;
        System.out.println(fila);
        int cantidadSepHeader = 0;
        for(int tam: tamaño)
            cantidadSepHeader += tam + 1;
        System.out.println("-".repeat(cantidadSepHeader));
        fila = "";
        // Esta parte es para escribir los valores
        for (int row=indiceInicio; row < indiceFinal; row++){
            for (int i = 0; i < cantColumnas; i++) {
                String elem;
                if (getCelda(etiquetas.get(i), row) != null){
                    elem = this.getCelda(etiquetas.get(i), row).toString();
                } else {
                    elem = "null";
                }

                if(elem.length() > tamaño[i])
                {
                    elem = stringRecortado(elem, tamaño[i]);
                }

                int diff = tamaño[i] - elem.length();
                espacioIzq = diff / 2;
                if (diff % 2 == 0){
                    espacioDer = espacioIzq;
                } else{
                    espacioDer = espacioIzq+1;
                }
                
                //System.out.println(elem.length() + " - " + espacioIzq);

                fila += " ".repeat(espacioIzq)+elem+" ".repeat(espacioDer)+sep;
            }
            if(COLUMNASOVERFLOW)
                fila += OVERFLOWSTRING;
            System.out.println(fila);
            fila = "";
        }
    }

    private String stringRecortado(String stringOriginal, int tamaño)
    {
        String stringNuevo = "";
        char[] charArr = stringOriginal.toCharArray();
        for(int i = 0; i < tamaño - 3; i++)
        {
            stringNuevo += charArr[i];
        }

        return stringNuevo + "...";
    }

    public void head(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        printDesdeHasta(0, cantidadFilas);
    }

    public void head(){
        this.head(5);
    }

    public void tail(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        printDesdeHasta(this.cantidadFilas() - cantidadFilas, this.cantidadFilas());
    }

    public void tail(){
        this.tail(5);
    }

    public <T> void setCelda(String etiqueta, int indice, T valor){
        this.data.get(etiqueta).setCelda(indice, valor);
    }

    public Object getCelda(String etiqueta, int indice){
        return this.data.get(etiqueta).getCelda(indice);
    }

    public <T> T getCelda(String etiqueta, int indice, Class<T> tipoDato){
        return tipoDato.cast(data.get(etiqueta).getCelda(indice));
    }

    public <T> void añadirCelda(String etiqueta, T valor){
        this.data.get(etiqueta).añadirCelda(valor);
    }

    public DataFrame clone(){
        Map<String, Columna> columnas = new HashMap<>();
        Map<String, String> dataTypes = new HashMap<>();
        for (String colName : this.etiquetas){
            columnas.put(colName, this.getColumna(colName).clone());
            dataTypes.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, dataTypes);
    }

    public <T> void añadirColumna(String etiqueta, Columna<T> columna){
        this.data.put(etiqueta, columna);
    }

    public Columna getColumna(String etiqueta){
        return this.data.get(etiqueta);
    }

    public DataFrame getColumna(String[] etiqueta){
        Map<String, Columna> columnas = new HashMap<>();
        Map<String, String> tiposCol = new HashMap<>();
        for (String colName : etiqueta){
            columnas.put(colName, this.getColumna(colName));
            tiposCol.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, tiposCol);
    }

    public DataFrame getColumna(List<String> etiqueta){
        Map<String, Columna> columnas = new HashMap<>();
        Map<String, String> tiposCol = new HashMap<>();
        for (String colName : etiqueta){
            columnas.put(colName, this.getColumna(colName));
            tiposCol.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, tiposCol);
    }

    public void añadirFila(DataFrame fila){
        for (String colName : this.etiquetas){
            switch (this.tiposColumna.get(colName)) {
                case "String":
                    this.añadirCelda(colName, fila.getCelda(colName, 0, String.class));
                    break;
                case "Integer":
                    this.añadirCelda(colName, fila.getCelda(colName, 0, Integer.class));
                    break;
                case "Double":
                    this.añadirCelda(colName, fila.getCelda(colName, 0, Double.class));
                    break;
                case "Boolean":
                    this.añadirCelda(colName, fila.getCelda(colName, 0, Boolean.class));
                    break;
                default:
                    System.out.println("Si esto se ejecuta, paso algo raro");
                    break;
            }
        }
    }

    public void concatDataFrame(DataFrame otro){
        for (int i=0; i < cantidadFilas(); i++){
            this.añadirFila(otro.getFila(i));
        }
    }

    public DataFrame getFila(int fila){
        // TODO: Hay que resolver esto
        Map<String, Columna> otro = new HashMap<>();
        for (String colName : this.etiquetas){
            switch (this.tiposColumna.get(colName)){
                case "String":
                    otro.put(colName, new ColumnaString());
                    otro.get(colName).añadirCelda(getCelda(colName, fila, String.class));
                    break;
                case "Integer":
                    otro.put(colName, new ColumnaInt());
                    otro.get(colName).añadirCelda(getCelda(colName, fila, Integer.class));
                    break;
                case "Double":
                    otro.put(colName, new ColumnaDouble());
                    otro.get(colName).añadirCelda(getCelda(colName, fila, Double.class));
                    break;
                case "Boolean":
                    otro.put(colName, new ColumnaBool());
                    otro.get(colName).añadirCelda(getCelda(colName, fila, Boolean.class));
                    break;
            }
        }
        return new DataFrame(otro, this.tiposColumna);
    }
    
    public DataFrame getFila(int[] fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getFila(Integer[] fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getFila(List<Integer>[] fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void eliminarCol(String etiqueta){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void eliminarFila(String fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame recortar(int indiceInicio, int indiceFinal){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void ordenar(String etiqueta, boolean creciente){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public <T> DataFrame filtrar(String etiqueta, Predicate<T> filtro){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public <T> void transformCol(String etiqueta, UnaryOperator<T> filtro){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

}

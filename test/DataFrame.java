package test;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataFrame implements Cloneable {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Columna> data;
    private List<String> etiquetas;
    private Map<String, String> tiposColumna;

    public DataFrame(){
        this.data = new HashMap<>();
        this.etiquetas = new ArrayList<>();
        this.tiposColumna = new HashMap<>();
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
        if (this.data.isEmpty()){
            return 0;
        }
        return this.data.get(etiquetas.get(0)).length();
    }

    public Map<String, String> tiposColumna(){
        return this.tiposColumna;
    }

    public String toString(){
        this.head();
        return "";
    }

    public void printDesdeHasta(int indiceInicio, int indiceFinal){
        if (indiceInicio < -1){
            indiceInicio = 0;
        }
        if (indiceFinal > cantidadFilas()){
            indiceFinal = cantidadFilas();
        }
        String fila = "";
        String sep = "|";
        int center = 6;

        int espacioIzq;
        int espacioDer;

        // Esta parte ajusta el center por si la etiqueta es muy larga
        for (String colName : etiquetas){
            if (colName.length()-2 > center){
                center = colName.length();
            }
        }

        // Esta parte es para escribir los nombres de las columnas
        for (String colName : etiquetas){
            espacioIzq = ((center - colName.length()) / 2) + 1;
            if (colName.length()%2==0){
                espacioDer = espacioIzq;
            } else {
                espacioDer = espacioIzq+1;
            }
            fila += " ".repeat(espacioIzq)+colName+" ".repeat(espacioDer)+sep;
        }
        System.out.println(fila);
        System.out.println("-".repeat(etiquetas.size() * (center + 2) + etiquetas.size()));
        fila = "";
        // Esta parte es para escribir los valores
        for (int row=indiceInicio; row < indiceFinal; row++){
            for (String colName : etiquetas) {
                String elem;
                if (getCelda(colName, row) != null){
                    elem = this.getCelda(colName, row).toString();
                } else {
                    elem = "null";
                }
                espacioIzq = ((center - elem.length()) / 2) + 1;
                if (elem.length()%2==0){
                    espacioDer = espacioIzq;
                } else{
                    espacioDer = espacioIzq+1;
                }
                
                fila += " ".repeat(espacioIzq)+elem+" ".repeat(espacioDer)+sep;
            }
            System.out.println(fila);
            fila = "";
        }
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

    private <T> void añadirCelda(String etiqueta, T valor){
        this.data.get(etiqueta).añadirCelda(valor);
    }
    @Override
    public DataFrame clone(){
        Map<String, Columna> columnas = new HashMap<>();
        Map<String, String> dataTypes = new HashMap<>();
        for (String colName : this.etiquetas){
            columnas.put(colName, this.getColumna(colName).clone());
            dataTypes.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, dataTypes);
    }

    public <T> void addColumna(String etiqueta, Columna<T> columna){
        this.data.put(etiqueta, columna);
        if (!this.etiquetas.contains(etiqueta)){
            this.etiquetas.add(etiqueta);
            String dataType;
            if (columna.getClass() == ColumnaString.class){
                dataType = "String";
            } else if (columna.getClass() == ColumnaInt.class){
                dataType = "Integer";
            } else if (columna.getClass() == ColumnaDouble.class){
                dataType = "Double";
            } else if (columna.getClass() == ColumnaBool.class){
                dataType = "Integer";
            } else {
                throw new RuntimeException("Error en addColumna. Esto no deberia ocurrir");
            }
            this.tiposColumna.put(etiqueta, dataType);
        }
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

    public void addFila(DataFrame fila){
        for (String colName : this.etiquetas){
            // this.añadirCelda(colName, fila.getCelda(colName, 0));
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
                    System.out.println("Error en DataFrame.añadirFila");
                    System.out.println("Falta un tipo de dato para la columna: "+colName);
                    break;
            }
        }
    }

    public void addFila(Map<String, Object> fila){
        for (String colName : fila.keySet()){
            this.data.get(colName).añadirCelda(fila.get(colName));
        }
    }

    public void concatDataFrame(DataFrame otro){
        for (int i=0; i < otro.cantidadFilas(); i++){
            this.addFila(otro.getFila(i));
        }
    }

    public DataFrame getFila(int fila){
        Map<String, Columna> otro = new HashMap<>();
        DataFrame otro2 = new DataFrame();
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
        return otro2;
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

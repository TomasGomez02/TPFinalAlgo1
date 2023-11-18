package test;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import utils.ColumnaInexistenteException;

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

    public boolean contieneEtiqueta(String etiqueta){
        return this.etiquetas.contains(etiqueta);
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

    public void printDesdeHasta(int indiceInicio, int indiceFinal)
    {
        printDesdeHasta(indiceInicio, indiceFinal, 4);
    }

    public void printDesdeHasta(int indiceInicio, int indiceFinal, final int OFFSETMINIMO){
        if (indiceInicio < -1 || indiceFinal > cantidadFilas()){
            indiceInicio = 0;
        }
        if (indiceFinal > cantidadFilas()){
            indiceFinal = cantidadFilas();
        }

        final int COLUMNASMAX = 10;
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
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        this.data.get(etiqueta).setCelda(indice, valor);
    }

    public Object getCelda(String etiqueta, int indice){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        return this.data.get(etiqueta).getCelda(indice);
    }

    public <T> T getCelda(String etiqueta, int indice, Class<T> tipoDato){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        return tipoDato.cast(data.get(etiqueta).getCelda(indice));
    }

    private <T> void añadirCelda(String etiqueta, T valor){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException();
        }
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
        if (!this.contieneEtiqueta(etiqueta)){
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
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
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
        if (0 <= fila && fila <= this.cantidadFilas()-1){
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
            return new DataFrame(otro, tiposColumna);
        }
        throw new RuntimeException("El DataFrame no contiene la fila: "+fila);
    }
    
    public DataFrame getFila(int[] fila){
        DataFrame copia = this.getFila(fila[0]);
        for (int indice : fila){
            if (indice == fila[0]){ continue; }
            copia.addFila(this.getFila(indice));
        }
        return copia;
    }

    public DataFrame getFila(Integer[] fila){
        DataFrame copia = this.getFila(fila[0]);
        for (Integer indice : fila){
            if (indice == fila[0]){ continue; }
            copia.addFila(this.getFila(indice));
        }
        return copia;
    }

    public DataFrame getFila(List<Integer> fila){
        DataFrame copia = this.getFila(fila.get(0));
        for (Integer indice : fila){
            if (indice == fila.get(0)){ continue; }
            copia.addFila(this.getFila(indice));
        }
        return copia;
    }

    public void eliminarCol(String etiqueta){
        if (this.contieneEtiqueta(etiqueta)){
            this.data.remove(etiqueta);
            this.etiquetas.remove(this.etiquetas.indexOf(etiqueta));
            return;
        }
        throw new ColumnaInexistenteException(etiqueta);
    }

    public void eliminarFila(int fila){
        if (0 <= fila && fila <= this.cantidadFilas()-1){
            for (String colName : this.etiquetas){
                this.getColumna(colName).eliminarCelda(fila);
            }
        }
        throw new RuntimeException("La fila debe estar en el rango [0, n)");
    }

    public DataFrame recortar(int indiceInicio, int indiceFinal){
        DataFrame copia = this.getFila(indiceInicio);
        for (int i=indiceInicio+1; i <= indiceFinal; i++){
            copia.addFila(this.getFila(i));
        }
        return copia;
    }

    public void ordenar(String etiqueta, boolean creciente){
        Map<Integer, Integer> orden = getColumna(etiqueta).ordenar(creciente);
        
        for (Map.Entry<String, Columna> entry : this.data.entrySet()) {
            entry.getValue().ordenarPorIndice(orden);
        }
    }

    public <T> DataFrame filtrar(String etiqueta, Predicate<T> filtro){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public <T> void transformCol(String etiqueta, UnaryOperator<T> transformacion){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        this.getColumna(etiqueta).transformar(transformacion);
    }

}

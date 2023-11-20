package src;

import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import utils.ColumnaInexistenteException;
import utils.DataType;
import utils.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class DataFrame implements Cloneable {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Columna> data;
    private List<String> etiquetas;
    private Map<String, DataType> tiposColumna;

    public DataFrame(){
        this.data = new LinkedHashMap<>();
        this.etiquetas = new ArrayList<>();
        this.tiposColumna = new LinkedHashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Columna> data, Map<String, DataType> tiposColumna){
        this.data = new LinkedHashMap<>(data);
        this.tiposColumna = new LinkedHashMap<>(tiposColumna);
        this.etiquetas = new ArrayList<>(data.keySet());
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(List<Columna> data){
        this();
        for (int i=0; i < data.size(); i++){
            this.data.put(String.valueOf(i), data.get(i).clone());
            this.etiquetas.add(String.valueOf(i));
            this.tiposColumna.put(String.valueOf(i), data.get(i).getColumnType());
        }
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Columna[] data){
        this();
        for (int i=0; i < data.length; i++){
            this.data.put(String.valueOf(i), data[i].clone());
            this.etiquetas.add(String.valueOf(i));
            this.tiposColumna.put(String.valueOf(i), data[i].getColumnType());
        }
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Columna> data){
        this();
        for (String colName : data.keySet()){
            this.data.put(colName, data.get(colName).clone());
            this.etiquetas.add(colName);
            this.tiposColumna.put(colName, data.get(colName).getColumnType());
        }
    }

    /**
     * Verifica si la lista de etiquetas contiene la etiqueta especificada.
     * 
     * @param etiqueta la etiqueta que se busca en la lista
     * @return <code>true</code> si la etiqueta se encuentra en la lista, <code>false</code> si la etiqueta no se encuentra en la lista
     */
    public boolean contieneEtiqueta(String etiqueta){
        return this.etiquetas.contains(etiqueta);
    }

    /**
     * Obtiene una lista con todas las columnas del dataframe. 
     * 
     * @return una lista que contiene todas las etiquetas 
     */
    public List<String> nombreColumnas(){
        return new ArrayList<>(this.etiquetas);
    }

    /**
     * Obtiene la cantidad de columnas que conforman el dataframe.
     * 
     * @return la cantidad de columnas 
     */
    public int cantidadColumnas(){
        return this.etiquetas.size();
    }

    /**
     * Obtiene la cantidad de filas que conforman el dataframe.
     * 
     * @return si no se encuentran datos devuelve 0. De lo contrario, devuelve la cantidad de filas
     */
    public int cantidadFilas(){
        if (this.data.isEmpty()){
            return 0;
        }
        return this.data.get(etiquetas.get(0)).length();
    }

    /**
     * Obtiene un mapa que asocia cada columna con el tipo de datos que contiene la misma.
     * 
     * @return un mapa que representa la asociacion entre los nombres de las columnas y sus tipos de datos.
     */
    public Map<String, DataType> tiposColumna(){
        return new LinkedHashMap<>(this.tiposColumna);
    }

    public String toString(){
        this.head();
        return "";
    }

    /**
     * Imprime una porcion del dataframe delimitado por un indice de inicio y un indice final.
     * @param indiceInicio primer indice de fila a imprimir
     * @param indiceFinal ultimo indice de fila a imprimir 
     */
    public void printDesdeHasta(int indiceInicio, int indiceFinal){
        printDesdeHasta(indiceInicio, indiceFinal, 4);
    }

    /**
     * Imprime una porcion del dataframe delimitado por un indice de inicio y un indice final.
     * 
     * @param indiceInicio primer indice de fila a imprimir
     * @param indiceFinal ultimo indice de fila a imprimir 
     * @param OFFSETMINIMO valor minimo de desplazamiento utilizado para ajustar el centrado de las etiquetas y el contenido de las celdas
     */
    public void printDesdeHasta(int indiceInicio, int indiceFinal, final int OFFSETMINIMO){
        if (indiceInicio < -1){
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
        int tamañoIndice = String.valueOf(indiceFinal).length();

        int espacioIzq;
        int espacioDer;

        for (int i = 0; i < cantColumnas; i++){
            tamaño[i] = OFFSETMINIMO * 2;
            if (etiquetas.get(i).length() + OFFSETMINIMO > tamaño[i]){
                tamaño[i] = etiquetas.get(i).length() + OFFSETMINIMO;
            }
        }

        fila += " ".repeat(tamañoIndice) + sep;
        // Esta parte es para escribir los nombres de las columnas
        
        for (int i = 0; i < cantColumnas; i++)
        {
            int diff = tamaño[i] - etiquetas.get(i).length();
            espacioIzq = diff / 2;
            if (diff % 2 == 0)
            {
                espacioDer = espacioIzq;
            } 
            else 
            {
                espacioDer = espacioIzq+1;
            }
            fila += " ".repeat(espacioIzq)+etiquetas.get(i)+" ".repeat(espacioDer)+sep;
        }

        int cantidadSepHeader = 0;
        if(COLUMNASOVERFLOW)
        {
            fila += OVERFLOWSTRING;
            cantidadSepHeader += OVERFLOWSTRING.length();
        }
        System.out.println(fila);
        
        for(int tam: tamaño)
            cantidadSepHeader += tam + 1;
        System.out.println("-".repeat(cantidadSepHeader + tamañoIndice + 1));

        fila = "";
        // Esta parte es para escribir los valores
        for (int row=indiceInicio; row <= indiceFinal; row++){
            int diffTamañoIndice = tamañoIndice - String.valueOf(row).length();
            fila += row + " ".repeat(diffTamañoIndice) + sep;
            for (int i = 0; i < cantColumnas; i++) {
                String elem = String.valueOf(getCelda(etiquetas.get(i), row));

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

    /**
     * Recorta una cadena dada en base a un tamaño indicado y le agrega  "..."  al final.
     *  
     * @param stringOriginal cadena a recortar
     * @param tamaño  cantidad de caracteres maximos deseados
     * @return una cadena recortada con el tamaño especificado de caracteres seguida de "..." 
     */
    private String stringRecortado(String stringOriginal, int tamaño){
        String stringNuevo = "";
        char[] charArr = stringOriginal.toCharArray();
        for(int i = 0; i < tamaño - 3; i++)
        {
            stringNuevo += charArr[i];
        }
        return stringNuevo + "...";
    }

    /**
     * Imprime desde la primera fila del dataframe hasta la cantidad especificada.
     * 
     * @param cantidadFilas el numero de filas a imprimir
     */
    public void head(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        printDesdeHasta(0, cantidadFilas - 1);
    }

    /**
     * Imprime las primeras cinco filas del dataframe. 
     */
    public void head(){
        this.head(5);
    }

    /**
     * Imprime las ultimas filas del dataframe en base a la cantidad especificada. 
     * 
     * @param cantidadFilas la cantidad de filas a imprimir
     */
    public void tail(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        printDesdeHasta(this.cantidadFilas() - cantidadFilas - 1, this.cantidadFilas() - 1);
    }
    /**
     * Imprime las ultimas cinco filas del dataframe.
     */
    public void tail(){
        this.tail(5);
    }

    public void info(){
        System.out.println("<"+this.getClass()+">");
        System.out.println("Indices: 0 hasta "+(cantidadFilas()-1));
        System.out.println("Data de columnas (total "+cantidadColumnas()+" columnas): ");
        
        int espacioColName = 12;
        for (String colname : this.etiquetas){
            if (colname.length() > espacioColName){
                espacioColName = colname.length() +2;
            }
        }
        final int ESPACIOTITULO = 12;
        String fila = "";
        fila += "Columna" + " ".repeat(espacioColName - 7);
        fila += "non-null" + " ".repeat(ESPACIOTITULO - 8);
        fila += "DataType" + " ".repeat(ESPACIOTITULO - 7);
        System.out.println(fila);
        fila = "";
        System.out.println("-".repeat(espacioColName + ESPACIOTITULO*2));

        for (String colName : this.etiquetas){
            int nonNull = getColumna(colName).length() - getColumna(colName).cantNull();
            int espacioNum = ESPACIOTITULO - String.valueOf(nonNull).length();
            fila += colName;
            fila += " ".repeat(espacioColName - colName.length());
            fila += getColumna(colName).length() - getColumna(colName).cantNull();
            fila += " ".repeat(espacioNum);
            fila += this.tiposColumna.get(colName);
            System.out.println(fila);
            fila = "";
        }
    }

    /**
     * Establece el valor para una celda indicada.
     * 
     * @param <T> tipo de dato del valor a asignar
     * @param etiqueta etiqueta de la columna a la que pertenece la celda
     * @param indice indice de fila de la celda
     * @param valor el nuevo valor que se le asignara a la celda
     * @throws ColumnaInexistenteException si la etiqueta especificada no existe en la estructura de datos
     */
    public <T> void setCelda(String etiqueta, int indice, T valor){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        this.data.get(etiqueta).set(indice, valor);
    }

    /**
     * Obtiene el valor de una celda indicada.
     * 
     * @param etiqueta etiqueta de la columna a la que pertenece la celda
     * @param indice indice de fila de la celda 
     * @return el valor contenido en la celda indicada
     * @throws ColumnaInexistenteException si la etiqueta especificada no existe en la estructura de datos
     */
    public Object getCelda(String etiqueta, int indice){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        return this.data.get(etiqueta).get(indice);
    }

    /**
     * Obtiene el valor de una celda y lo convierte al tipo de dato especificado.
     * 
     * @param <T> el tipo de dato del valor a obtener
     * @param etiqueta etiqueta de la columna a la que pertenece la celda
     * @param indice indice de fila de la celda 
     * @param tipoDato la clase que representa el tipo de dato esperado para el valor de la celda
     * @return el valor de la celda especificada convertido al tipo de dato proporcionado
     * @throws ColumnaInexistenteException si la etiqueta especificada no existe en la estructura de datos
     */
    public <T> T getCelda(String etiqueta, int indice, Class<T> tipoDato){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        return tipoDato.cast(data.get(etiqueta).get(indice));
    }

    /**
     * Añade una nueva celda al final de la columna especificada. 
     * 
     * @param <T> tipo de dato del valor que tendra la celda añadida
     * @param etiqueta etiqueta de la columna a la que pertenece la celda
     * @param valor valor que tendra la celda añadida
     * @throws ColumnaInexistenteException si la etiqueta especificada no existe en la estructura de datos
     */
    private <T> void añadirCelda(String etiqueta, T valor){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException();
        }
        this.data.get(etiqueta).add(valor);
    }
    @Override
    public DataFrame clone(){
        Map<String, Columna> columnas = new LinkedHashMap<>();
        Map<String, DataType> dataTypes = new LinkedHashMap<>();
        for (String colName : this.etiquetas){
            columnas.put(colName, this.getColumna(colName).clone());
            dataTypes.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, dataTypes);
    }

    public DataFrame switchRows(int row1, int row2)
    {
        if(row1 < 0 || row1 >= cantidadFilas())
            throw new IndexOutOfBoundsException("DataFrame no contiene la fila: " + row1);
        if(row2 < 0 || row2 >= cantidadFilas())
            throw new IndexOutOfBoundsException("DataFrame no contiene la fila: " + row2);

        DataFrame df = clone();

        for(String etiqueta: etiquetas)
        {
            df = df.setRow(this.getFila(row1), row2)
                    .setRow(this.getFila(row2), row1);
        }

        return df;
    }

    public DataFrame setRow(DataFrame fila, int index)
    {
        if(index < 0 || index >= cantidadFilas())
            throw new IndexOutOfBoundsException("DataFrame no contiene la fila: " + index);
        DataFrame df = clone();
        
        for(String etiqueta: etiquetas)
        {
            df.setCelda(etiqueta, index, fila.getCelda(etiqueta, 0, Types.evaluarTipo(tiposColumna.get(etiqueta))));
        }

        return df;
    }

    /**
     * Agrega una nueva columna de booleanos al dataframe. 
     * 
     * @param etiqueta etiqueta de la nueva columna
     * @param columna  lista de booleanos que contiene los valores que tendran las celdas de la nueva columna
     */
    public DataFrame addColumna(String etiqueta, Columna columna){
        DataFrame df = clone();
        if (df.contieneEtiqueta(etiqueta)){
            throw new RuntimeException("la columna "+etiqueta+" ya existe");
        }
        df.data.put(etiqueta, columna.clone());
        df.tiposColumna.put(etiqueta, columna.getColumnType());
        df.etiquetas.add(etiqueta);
        df.head();
        return df;
    }

    /**
     * Obtiene una columna completa a partir de su etiqueta. 
     * 
     * @param etiqueta etiqueta de la columna a obtener 
     * @return la columna completa asociada a la etiqueta especificada
     */
    public Columna getColumna(String etiqueta){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        return this.data.get(etiqueta);
    }

    /**
     * Obtiene un nuevo Dataframe que contiene las columnas proporcionadas a partir de sus etiquetas. 
     * 
     * @param etiqueta array de las etiquetas de las columnas a obtener 
     * @return Dataframe que contiene las columnas obtenidas 
     */
    public DataFrame getColumna(String[] etiqueta){
        Map<String, Columna> columnas = new LinkedHashMap<>();
        Map<String, DataType> tiposCol = new LinkedHashMap<>();
        for (String colName : etiqueta){
            columnas.put(colName, this.getColumna(colName));
            tiposCol.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, tiposCol);
    }

    /**
     * Obtiene un nuevo Dataframe que contiene las columnas proporcionadas a partir de sus etiquetas. 
     * 
     * @param etiqueta lista de las etiquetas de las columnas a obtener
     * @return Dataframe que contiene las columnas obtenidas 
     */
    public DataFrame getColumna(List<String> etiqueta){
        Map<String, Columna> columnas = new LinkedHashMap<>();
        Map<String, DataType> tiposCol = new LinkedHashMap<>();
        for (String colName : etiqueta){
            columnas.put(colName, this.getColumna(colName));
            tiposCol.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, tiposCol);
    }

    /**
     * Añade una nueva fila al DataFrame.
     * 
     * @param fila DataFrame que representa la nueva fila a añadir
     */
    public void addFila(DataFrame fila){
        for (String colName : this.etiquetas){
            switch (this.tiposColumna.get(colName)) {
                case STRING:
                    this.añadirCelda(colName, fila.getCelda(colName, 0, String.class));
                    break;
                case INT:
                    this.añadirCelda(colName, fila.getCelda(colName, 0, Integer.class));
                    break;
                case DOUBLE:
                    this.añadirCelda(colName, fila.getCelda(colName, 0, Double.class));
                    break;
                case BOOL:
                    this.añadirCelda(colName, fila.getCelda(colName, 0, Boolean.class));
                    break;
                default:
                    System.out.println("Error en DataFrame.añadirFila");
                    System.out.println("Falta un tipo de dato para la columna: "+colName);
                    break;
            }
        }
    }

    /**
     * Añade una nueva fila al DataFrame.
     *  
     * @param fila mapa que contiene los nombres de las columnas y sus valores asociados 
     * @throws ColumnaInexistenteException Si alguna de las columnas en el mapa no existe en el DataFrame actual
     */
    public void addFila(Map<String, Object> fila){
        for (String colName : fila.keySet()){
            this.data.get(colName).add(fila.get(colName));
        }
    }

    /**
     * Concatena dos Dataframe.
     * 
     * @param otro dataframe de tipo Dataframe
     */
    public void concatDataFrame(DataFrame otro){
        for (int i=0; i < otro.cantidadFilas(); i++){
            this.addFila(otro.getFila(i));
        }
    }

    /**
     * Obtiene y devuelve los valores de la fila indicada.
     * 
     * @param fila indice de la fila a obtener
     * @return la fila obtenida en formato Dataframe
     */
    public DataFrame getFila(int fila){
        if (0 <= fila && fila <= this.cantidadFilas()-1){
            Map<String, Columna> otro = new LinkedHashMap<>();
            for (String colName : this.etiquetas){
                switch (this.tiposColumna.get(colName)){
                    case STRING:
                        otro.put(colName, new ColumnaString());
                        otro.get(colName).add(getCelda(colName, fila, String.class));
                        break;
                    case INT:
                        otro.put(colName, new ColumnaInt());
                        otro.get(colName).add(getCelda(colName, fila, Integer.class));
                        break;
                    case DOUBLE:
                        otro.put(colName, new ColumnaDouble());
                        otro.get(colName).add(getCelda(colName, fila, Double.class));
                        break;
                    case BOOL:
                        otro.put(colName, new ColumnaBool());
                        otro.get(colName).add(getCelda(colName, fila, Boolean.class));
                        break;
                }
            }
            return new DataFrame(otro, tiposColumna);
        }
        throw new RuntimeException("El DataFrame no contiene la fila: "+fila);
    }
    
    /**
     * Obtiene y devuelve un Dataframe formado solo por las filas correspondientes a los indices proporcionados.
     * 
     * @param fila array de enteros que representan los indices de las filas a obtener
     * @return un nuevo Dataframe que contiene las filas correspondientes a los indices proporcionados
     */
    public DataFrame getFila(int[] fila){
        Arrays.sort(fila);
        System.out.println(fila);
        DataFrame copia = this.getFila(fila[0]);
        for (int i=1; i < fila.length; i++){
            copia.addFila(this.getFila(fila[i]));
        }
        return copia;
    }

    /**
     * Obtiene y devuelve un Dataframe formado solo por las filas correspondientes a los indices proporcionados.
     * @param fila lista de Integer que representan los indices de las filas a obtener
     * @return un nuevo Dataframe que contiene las filas correspondientes a los indices proporcionados
     */
    public DataFrame getFila(Integer[] fila){
        Arrays.sort(fila);
        // System.out.println(fila);
        DataFrame copia = this.getFila(fila[0]);
        for (int i=1; i < fila.length; i++){
            copia.addFila(this.getFila(fila[i]));
        }
        return copia;
    }

    /**
     * Obtiene y devuelve un Dataframe formado solo por las filas correspondientes a los indices proporcionados.
     * 
     * @param fila lista de Integer que representan los indices de las filas a obtener
     * @return un nuevo Dataframe que contiene las filas correspondientes a los indices proporcionados
     */
    public DataFrame getFila(List<Integer> fila){
        fila.sort(null);
        // System.out.println(fila);
        DataFrame copia = this.getFila(fila.get(0));
        for (int i=1; i < fila.size(); i++){
            copia.addFila(this.getFila(fila.get(i)));
        }
        return copia;
    }

    /**
     * Elimina una columna del dataframe a partir de su etiqueta. 
     * 
     * @param etiqueta la etiqueta de la columna a eliminar
     * @throws ColumnaInexistenteException Si la columna con la etiqueta especificada no existe en el DataFrame
     */
    public DataFrame eliminarCol(String etiqueta){
        if (!contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        DataFrame df = clone();
        df.data.remove(etiqueta);
        df.tiposColumna.remove(etiqueta);
        df.etiquetas.remove(df.etiquetas.indexOf(etiqueta));
        return df;
    }

    public DataFrame eliminarCol(String[] etiquetas){
        DataFrame df = eliminarCol(etiquetas[0]);
        for (int i=1; i < etiquetas.length; i++){
            df = df.eliminarCol(etiquetas[i]);
        }
        return df;
    }

    /**
     * Elimina una fila del dataframe a partir de su indice. 
     * 
     * @param etiqueta el indice de la fila a eliminar
     */
    public DataFrame eliminarFila(int fila){
        // if (0 <= fila && fila <= this.cantidadFilas()-1){
        //     for (String colName : this.etiquetas){
        //         this.getColumna(colName).eliminarCelda(fila);
        //     }
        //     return;
        // }
        // throw new RuntimeException("La fila debe estar en el rango [0, "+cantidadFilas()+")");
        if (fila < 0 || fila > this.cantidadFilas()-1){
            throw new RuntimeException("La fila debe estar en el rango [0, "+cantidadFilas()+")");
        }
        for (String colName : this.etiquetas){
            this.getColumna(colName).eliminarCelda(fila);
        }
    }

    /**
     * Crea un Dataframe que contiene las filas correspondientes a los indices de inicio y fin indicados.
     * 
     * @param indiceInicio indice de la primera fila a incluir en el nuevo DataFrame
     * @param indiceFinal indice de la ultima fila a incluir en el nuevo DataFrame
     * @return un nuevo Dataframe formado por las filas indicadas
     */
    public DataFrame recortar(int indiceInicio, int indiceFinal){
        DataFrame copia = this.getFila(indiceInicio);
        for (int i=indiceInicio+1; i <= indiceFinal; i++){
            copia.addFila(this.getFila(i));
        }
        return copia;
    }

    /**
     * Ordena una columna del dataframe de manera creciente o decreciente.
     * 
     * @param etiqueta etiqueta de la columna a ordenar
     * @param creciente indica si la ordenamiento debe ser ascendente (true) o descendente (false)
     */
    public DataFrame ordenar(String etiqueta, boolean creciente){
        if (!contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }

        Map<Integer, Integer> orden = getColumna(etiqueta).ordenar(creciente);
        DataFrame copia = this.clone();
        
        for (String colName : this.etiquetas){
            copia.data.put(colName, getColumna(colName).ordenarPorIndice(orden));
        }
        return copia;
    }

    public DataFrame sort(String[] tags, boolean increasing)
    {
        for(String tag: tags)
        {
            if(!contieneEtiqueta(tag))
                throw new ColumnaInexistenteException(tag);
        }

        DataFrame df = clone();
        List<String> groupedTags = new ArrayList<>();
        df = df.ordenar(tags[0], increasing);

        for(int i = 1; i < tags.length; i++)
        {
            groupedTags.add(tags[i - 1]);
            df = df.groupBy(groupedTags).order(tags[i], increasing).unGroup();
        }
        return df;
    }

    public DataFrame orderByIndex(Map<Integer, Integer> indexes)
    {
        if(!indexes.keySet().equals(new HashSet<Integer>(indexes.values())))
        {
            throw new IllegalArgumentException("Se requiere tener una posición para cada indice dado.");
        }
        DataFrame df = clone();

        for(Map.Entry<Integer, Integer> entry: indexes.entrySet())
        {
            df = df.setRow(getFila(entry.getKey()), entry.getValue());
        }

        return df;
    }

    public DataFrame sample(double fraction)
    {
        if(fraction < 0 || fraction > 1)
        {
            throw new IllegalArgumentException("La fracción debe estar entre 0.0 y 1.0.");
        }
        long cant = Math.round(fraction * cantidadFilas());
        return sample((int) cant);
    }

    public DataFrame sample(int n)
    {
        List<Integer> indexes = new ArrayList<>();
        Random rand = new Random();

        while(indexes.size() < n)
        {
            int indice = rand.nextInt(cantidadFilas());
            if(!indexes.contains(indice))
            {
                indexes.add(indice);
            }
        }

        return getFila(indexes);
    }

    /**
     * Filtra la filas de una columna del dataframe segun la conidicion proporcionada.
     * 
     * @param <T> tipo de datos de la columna a filtrar
     * @param etiqueta etiqueta de la columna a filtrar
     * @param filtro el predicado que define las condiciones de filtrado
     * @return nuevo DataFrame que contiene solo las filas que cumplen con las condiciones de filtrado
     * @throws UnsupportedOperationException si el metodo no fue implementado
     */
    public <T> DataFrame filtrar(String etiqueta, Predicate<T> filtro){
        if (!contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }

        List<Integer> indices = this.getColumna(etiqueta).filtrar(filtro);
        DataFrame filtrado = this.clone();

        for (String colName : this.etiquetas){
            filtrado.data.put(colName, getColumna(colName).filtrarPorIndice(indices));
        }
        return filtrado;
    }

    /**
     * Aplica una transformacion a los valores de la columna utilizando el operador proporcionado.
     * 
     * @param <T> tipo de datos de la columna a transformar
     * @param etiqueta etiqueta de la columna a transformar
     * @param transformacion operador que define la transformacion a aplicar 
     * @return devuelve un nuevo DataFrame con la transformacion aplicada
     * @throws ColumnaInexistenteException Si la columna con la etiqueta especificada no existe en el DataFrame
     */
    public <T> DataFrame transformCol(String etiqueta, UnaryOperator<T> transformacion){
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        DataFrame copia = this.clone();
        copia.data.put(etiqueta, this.getColumna(etiqueta).transformar(transformacion));
        return copia;
    }

    public GroupBy groupBy(String etiqueta)
    {
        
        return groupBy(new String[]{etiqueta});
    }

    public GroupBy groupBy(List<String> etiquetas)
    {
        
        return groupBy(etiquetas.toArray(String[] ::new));
    }

    public GroupBy groupBy(String[] etiquetas)
    {
        
        return new GroupBy(this, etiquetas);
    }
}

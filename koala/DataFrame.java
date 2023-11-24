package koala;

import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import koala.utils.DataType;
import koala.utils.Types;
import koala.utils.UnexistentColumnException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

public class DataFrame implements Cloneable {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Column> data;
    private List<String> tags;
    private Map<String, DataType> columnTypes;

    public DataFrame(){
        this.data = new LinkedHashMap<>();
        this.tags = new ArrayList<>();
        this.columnTypes = new LinkedHashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Column> data, Map<String, DataType> columnTypes){
        this.data = new LinkedHashMap<>(data);
        this.columnTypes = new LinkedHashMap<>(columnTypes);
        this.tags = new ArrayList<>(data.keySet());
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(List<Column> data){
        this();
        for (int i=0; i < data.size(); i++){
            this.data.put(String.valueOf(i), data.get(i).clone());
            this.tags.add(String.valueOf(i));
            this.columnTypes.put(String.valueOf(i), data.get(i).getColumnType());
        }
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Column[] data){
        this();
        for (int i=0; i < data.length; i++){
            this.data.put(String.valueOf(i), data[i].clone());
            this.tags.add(String.valueOf(i));
            this.columnTypes.put(String.valueOf(i), data[i].getColumnType());
        }
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Column> data){
        this();
        for (String colName : data.keySet()){
            this.data.put(colName, data.get(colName).clone());
            this.tags.add(colName);
            this.columnTypes.put(colName, data.get(colName).getColumnType());
        }
    }

    /**
     * Verifica si la lista de etiquetas contiene la etiqueta especificada.
     * 
     * @param tag la etiqueta que se busca en la lista
     * @return         <code>true</code> si la etiqueta se encuentra en la lista, <code>false</code> si la etiqueta no se encuentra en la lista
     */
    public boolean containsCol(String tag){
        return this.tags.contains(tag);
    }

    /**
     * Obtiene una lista con todas las columnas del dataframe. 
     * 
     * @return una lista que contiene todas las etiquetas 
     */
    public List<String> colNames(){
        return new ArrayList<>(this.tags);
    }

    /**
     * Obtiene la cantidad de columnas que conforman el dataframe.
     * 
     * @return la cantidad de columnas 
     */
    public int nCol(){
        return this.tags.size();
    }

    /**
     * Obtiene la cantidad de filas que conforman el dataframe.
     * 
     * @return si no se encuentran datos devuelve 0. De lo contrario, devuelve la cantidad de filas
     */
    public int nRow(){
        if (this.data.isEmpty()){
            return 0;
        }
        return this.data.get(tags.get(0)).length();
    }

    /**
     * Obtiene un mapa que asocia cada columna con el tipo de datos que contiene la misma.
     * 
     * @return un mapa que representa la asociacion entre los nombres de las columnas y sus tipos de datos.
     */
    public Map<String, DataType> colTypes(){
        return new LinkedHashMap<>(this.columnTypes);
    }

    private String getTableString(int startIndex, int finalIndex, final int MINIMUMOFFSET)
    {
        if (startIndex < -1)
        {
            startIndex = 0;
        }
        if (finalIndex >= nRow())
        {
            finalIndex = nRow() - 1;
        }

        final int COLUMNASMAX = 10;
        final String OVERFLOWSTRING = "...|";

        final boolean COLUMNASOVERFLOW = (nCol() > COLUMNASMAX);
        int cantColumnas = COLUMNASOVERFLOW ? COLUMNASMAX : nCol();

        String out = "";
        String sep = "|";
        int[] tamaño = new int[cantColumnas];
        int tamañoIndice = String.valueOf(finalIndex).length();

        int espacioIzq;
        int espacioDer;

        for (int i = 0; i < cantColumnas; i++){
            tamaño[i] = MINIMUMOFFSET * 2;
            if (tags.get(i).length() + MINIMUMOFFSET > tamaño[i]){
                tamaño[i] = tags.get(i).length() + MINIMUMOFFSET;
            }
        }

        out += " ".repeat(tamañoIndice) + sep;
        // Esta parte es para escribir los nombres de las columnas
        
        for (int i = 0; i < cantColumnas; i++)
        {
            int diff = tamaño[i] - tags.get(i).length();
            espacioIzq = diff / 2;
            if (diff % 2 == 0)
            {
                espacioDer = espacioIzq;
            } 
            else 
            {
                espacioDer = espacioIzq+1;
            }
            out += " ".repeat(espacioIzq)+tags.get(i)+" ".repeat(espacioDer)+sep;
        }

        int cantidadSepHeader = 0;
        if(COLUMNASOVERFLOW)
        {
            out += OVERFLOWSTRING;
            cantidadSepHeader += OVERFLOWSTRING.length();
        }
        out += '\n';
        
        for(int tam: tamaño)
            cantidadSepHeader += tam + 1;
        out += "-".repeat(cantidadSepHeader + tamañoIndice + 1) + '\n';

        // Esta parte es para escribir los valores
        for (int row=startIndex; row <= finalIndex; row++){
            int diffTamañoIndice = tamañoIndice - String.valueOf(row).length();
            out += row + " ".repeat(diffTamañoIndice) + sep;
            for (int i = 0; i < cantColumnas; i++) {
                String elem = String.valueOf(getValue(tags.get(i), row));

                if(elem.length() > tamaño[i])
                {
                    elem = shortenedString(elem, tamaño[i]);
                }

                int diff = tamaño[i] - elem.length();
                espacioIzq = diff / 2;
                if (diff % 2 == 0){
                    espacioDer = espacioIzq;
                } else{
                    espacioDer = espacioIzq+1;
                }
                

                out += " ".repeat(espacioIzq)+elem+" ".repeat(espacioDer)+sep;
            }
            if(COLUMNASOVERFLOW)
                out += OVERFLOWSTRING;
            out += '\n';
        }
        return out;
    }

    @Override
    public String toString()
    {
        return getTableString(0, 10, 4);
    }

    /**
     * Imprime una porcion del dataframe delimitado por un indice de inicio y un indice final.
     * @param startindex primer indice de fila a imprimir
     * @param finalIndex ultimo indice de fila a imprimir 
     */
    public void printRange(int startindex, int finalIndex){
        printRange(startindex, finalIndex, 4);
    }

    /**
     * Imprime una porcion del dataframe delimitado por un indice de inicio y un indice final.
     * 
     * @param startIndex primer indice de fila a imprimir
     * @param finalIndex  ultimo indice de fila a imprimir 
     * @param MINIMUMOFFSET valor minimo de desplazamiento utilizado para ajustar el centrado de las etiquetas y el contenido de las celdas
     */
    public void printRange(int startIndex, int finalIndex, final int MINIMUMOFFSET)
    {
        System.out.println(getTableString(startIndex, finalIndex, MINIMUMOFFSET));    
    }

    /**
     * Recorta una cadena dada en base a un tamaño indicado y le agrega  "..."  al final.
     *  
     * @param originalString cadena a recortar
     * @param size         cantidad de caracteres maximos deseados
     * @return               una cadena recortada con el tamaño especificado de caracteres seguida de "..." 
     */
    private String shortenedString(String originalString, int size)
    {
        String stringNuevo = "";
        char[] charArr = originalString.toCharArray();
        for(int i = 0; i < size - 3; i++)
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
        if (cantidadFilas > this.nRow()){
            cantidadFilas = this.nRow();
        }
        printRange(0, cantidadFilas - 1);
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
        if (cantidadFilas > this.nRow()){
            cantidadFilas = this.nRow();
        }
        printRange(this.nRow() - cantidadFilas - 1, this.nRow() - 1);
    }
    /**
     * Imprime las ultimas cinco filas del dataframe.
     */
    public void tail(){
        this.tail(5);
    }

    /**
     * Obtiene los nombres de las columnas del Dataframe, la cantidad de elementos no nulos que tienen y el DataType de las mismas.
     */
    public void info(){
        System.out.println("<"+this.getClass()+">");
        System.out.println("Indices: 0 hasta "+(nRow()-1));
        System.out.println("Data de columnas (total "+nCol()+" columnas): ");
        
        int espacioColName = 12;
        for (String colname : this.tags){
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

        for (String colName : this.tags){
            int nonNull = getCol(colName).length() - getCol(colName).nNull();
            int espacioNum = ESPACIOTITULO - String.valueOf(nonNull).length();
            fila += colName;
            fila += " ".repeat(espacioColName - colName.length());
            fila += getCol(colName).length() - getCol(colName).nNull();
            fila += " ".repeat(espacioNum);
            fila += this.columnTypes.get(colName);
            System.out.println(fila);
            fila = "";
        }
    }

    /**
     * Establece el valor para una celda indicada.
     * 
     * @param <T>      tipo de dato del valor a asignar
     * @param tag etiqueta de la columna a la que pertenece la celda
     * @param index   indice de fila de la celda
     * @param value    el nuevo valor que se le asignara a la celda
     * @throws UnexistentColumnException si la etiqueta especificada no existe en la estructura de datos
     */
    public <T> void setValue(String tag, int index, T value){
        if (!this.containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }
        this.data.get(tag).set(index, value);
    }

    /**
     * Obtiene el valor de una celda indicada.
     * 
     * @param tag etiqueta de la columna a la que pertenece la celda
     * @param index   indice de fila de la celda 
     * @return         el valor contenido en la celda indicada
     * @throws UnexistentColumnException si la etiqueta especificada no existe en la estructura de datos
     */
    public Object getValue(String tag, int index){
        if (!this.containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }
        return this.data.get(tag).get(index);
    }

    /**
     * Obtiene el valor de una celda y lo convierte al tipo de dato especificado.
     * 
     * @param <T>      el tipo de dato del valor a obtener
     * @param tag etiqueta de la columna a la que pertenece la celda
     * @param index   indice de fila de la celda 
     * @param dataType la clase que representa el tipo de dato esperado para el valor de la celda
     * @return         el valor de la celda especificada convertido al tipo de dato proporcionado
     * @throws UnexistentColumnException si la etiqueta especificada no existe en la estructura de datos
     */
    public <T> T getValue(String tag, int index, Class<T> dataType){
        if (!this.containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }
        return dataType.cast(data.get(tag).get(index));
    }

    /**
     * Añade una nueva celda al final de la columna especificada. 
     * 
     * @param <T>      tipo de dato del valor que tendra la celda añadida
     * @param tag etiqueta de la columna a la que pertenece la celda
     * @param value    valor que tendra la celda añadida
     * @throws UnexistentColumnException si la etiqueta especificada no existe en la estructura de datos
     */
    private <T> void addValue(String tag, T value){
        if (!this.containsCol(tag)){
            throw new UnexistentColumnException();
        }
        this.data.get(tag).add(value);
    }
    @Override
    public DataFrame clone(){
        Map<String, Column> columnas = new LinkedHashMap<>();
        Map<String, DataType> dataTypes = new LinkedHashMap<>();
        for (String colName : this.tags){
            columnas.put(colName, this.getCol(colName).clone());
            dataTypes.put(colName, this.columnTypes.get(colName));
        }
        return new DataFrame(columnas, dataTypes);
    }

    /**
     * Intercambia dos filas del Dataframe de lugar a partir de sus indices.
     * 
     * @param row1 fila a intercambiar
     * @param row2 fila a intercambiar
     * @return     un nuevo Dataframe con las filas intercambiadas
     */
    public DataFrame switchRows(int row1, int row2)
    {
        if(row1 < 0 || row1 >= nRow())
            throw new IndexOutOfBoundsException("DataFrame no contiene la fila: " + row1);
        if(row2 < 0 || row2 >= nRow())
            throw new IndexOutOfBoundsException("DataFrame no contiene la fila: " + row2);

        DataFrame df = clone();

        for(String etiqueta: tags)
        {
            df = df.setRow(this.getRow(row1), row2)
                    .setRow(this.getRow(row2), row1);
        }

        return df;
    }

    /**
     * Establece los valores de una fila del Dataframe indicada por su indice.
     * 
     * @param fila  DataFrame que representa la fila cuyos valores se deben establecer
     * @param index indice de la fila en el DataFrame donde se deben establecer los valores
     * @return      nuevo DataFrame con los valores de la fila actualizados en el indice especificado
     */
    public DataFrame setRow(DataFrame fila, int index)
    {
        if(index < 0 || index >= nRow())
            throw new IndexOutOfBoundsException("DataFrame no contiene la fila: " + index);
        DataFrame df = clone();
        
        for(String etiqueta: tags)
        {
            df.setValue(etiqueta, index, fila.getValue(etiqueta, 0, Types.evaluateType(columnTypes.get(etiqueta))));
        }

        return df;
    }

    /**
     * Agrega una nueva columna de booleanos al dataframe. 
     * 
     * @param tag etiqueta de la nueva columna
     * @param col lista de booleanos que contiene los valores que tendran las celdas de la nueva columna
     */
    public DataFrame addCol(String tag, Column col){
        DataFrame df = clone();
        if (df.containsCol(tag)){
            throw new RuntimeException("la columna "+tag+" ya existe");
        }
        df.data.put(tag, col.clone());
        df.columnTypes.put(tag, col.getColumnType());
        df.tags.add(tag); 
        return df;
    }

    /**
     * Obtiene una columna completa a partir de su etiqueta. 
     * 
     * @param tag etiqueta de la columna a obtener 
     * @return    la columna completa asociada a la etiqueta especificada
     */
    public Column getCol(String tag){
        if (!this.containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }
        return this.data.get(tag);
    }

    /**
     * Obtiene un nuevo Dataframe que contiene las columnas proporcionadas a partir de sus etiquetas. 
     * 
     * @param tags array de las etiquetas de las columnas a obtener 
     * @return     Dataframe que contiene las columnas obtenidas 
     */
    public DataFrame getCol(String[] tags){
        Map<String, Column> columnas = new LinkedHashMap<>();
        Map<String, DataType> tiposCol = new LinkedHashMap<>();
        for (String colName : tags){
            columnas.put(colName, this.getCol(colName));
            tiposCol.put(colName, this.columnTypes.get(colName));
        }
        return new DataFrame(columnas, tiposCol);
    }

    /**
     * Obtiene un nuevo Dataframe que contiene las columnas proporcionadas a partir de sus etiquetas. 
     * 
     * @param tags lista de las etiquetas de las columnas a obtener
     * @return     Dataframe que contiene las columnas obtenidas 
     */
    public DataFrame getCol(List<String> tags){
        Map<String, Column> columnas = new LinkedHashMap<>();
        Map<String, DataType> tiposCol = new LinkedHashMap<>();
        for (String colName : tags){
            columnas.put(colName, this.getCol(colName));
            tiposCol.put(colName, this.columnTypes.get(colName));
        }
        return new DataFrame(columnas, tiposCol);
    }

    /**
     * Añade una nueva fila al DataFrame.
     *  
     * @param row mapa que contiene los nombres de las columnas y sus valores asociados 
     * @throws UnexistentColumnException Si alguna de las columnas en el mapa no existe en el DataFrame actual
     */
    public DataFrame addRow(Map<String, Object> row)
    {
        DataFrame df = clone();
        for (String colName : row.keySet()){
            df.data.get(colName).add(row.get(colName));
        }

        return df;
    }

    /**
     * Concatena dos Dataframe.
     * 
     * @param other dataframe de tipo Dataframe
     */
    public DataFrame concat(DataFrame other) throws IllegalArgumentException
    {
        if(other.nCol() != nCol())
        {
            throw new IllegalArgumentException("other debe tener " + nCol() + " columnas, pero tiene " + other.nCol());
        }
        for(String tag: tags)
        {
            if(!other.containsCol(tag))
                throw new IllegalArgumentException("otro no contiene la columna " + tag);
            if(!other.columnTypes.get(tag).equals(columnTypes.get(tag)))
                throw new IllegalArgumentException("Columna " + tag + " debe ser de tipo " + columnTypes.get(tag) + ", no de tipo " + other.columnTypes.get(tag));
        }

        DataFrame df = clone();
        for (int i=0; i < other.nRow(); i++){
            for (String colName : df.tags){
                switch (df.columnTypes.get(colName)) {
                    case STRING:
                        df.addValue(colName, other.getValue(colName, 0, String.class));
                        break;
                    case INT:
                        df.addValue(colName, other.getValue(colName, 0, Integer.class));
                        break;
                    case DOUBLE:
                        df.addValue(colName, other.getValue(colName, 0, Double.class));
                        break;
                    case BOOL:
                        df.addValue(colName, other.getValue(colName, 0, Boolean.class));
                        break;
                    default:
                        System.out.println("Error en DataFrame.añadirFila");
                        System.out.println("Falta un tipo de dato para la columna: "+colName);
                        break;
                }
            }
        }

        return df;
    }

    /**
     * Obtiene y devuelve los valores de la fila indicada.
     * 
     * @param row indice de la fila a obtener
     * @return    la fila obtenida en formato Dataframe
     */
    public DataFrame getRow(int row){
        if (0 <= row && row <= this.nRow()-1){
            Map<String, Column> otro = new LinkedHashMap<>();
            for (String colName : this.tags){
                switch (this.columnTypes.get(colName)){
                    case STRING:
                        otro.put(colName, new StringColumn());
                        otro.get(colName).add(getValue(colName, row, String.class));
                        break;
                    case INT:
                        otro.put(colName, new IntegerColumn());
                        otro.get(colName).add(getValue(colName, row, Integer.class));
                        break;
                    case DOUBLE:
                        otro.put(colName, new DoubleColumn());
                        otro.get(colName).add(getValue(colName, row, Double.class));
                        break;
                    case BOOL:
                        otro.put(colName, new BooleanColumn());
                        otro.get(colName).add(getValue(colName, row, Boolean.class));
                        break;
                }
            }
            return new DataFrame(otro, columnTypes);
        }
        throw new RuntimeException("El DataFrame no contiene la fila: "+row);
    }
    
    /**
     * Obtiene y devuelve un Dataframe formado solo por las filas correspondientes a los indices proporcionados.
     * 
     * @param rows array de enteros que representan los indices de las filas a obtener
     * @return     un nuevo Dataframe que contiene las filas correspondientes a los indices proporcionados
     */
    public DataFrame getRow(int[] rows){
        Arrays.sort(rows);
        System.out.println(rows);
        DataFrame copia = this.getRow(rows[0]);
        for (int i=1; i < rows.length; i++){
            copia = copia.concat(this.getRow(rows[i]));
        }
        return copia;
    }

    /**
     * Obtiene y devuelve un Dataframe formado solo por las filas correspondientes a los indices proporcionados.
     * @param rows lista de Integer que representan los indices de las filas a obtener
     * @return un nuevo Dataframe que contiene las filas correspondientes a los indices proporcionados
     */
    public DataFrame getRow(Integer[] rows){
        Arrays.sort(rows);
        // System.out.println(fila);
        DataFrame copia = this.getRow(rows[0]);
        for (int i=1; i < rows.length; i++){
            copia = copia.concat(this.getRow(rows[i]));
        }
        return copia;
    }

    /**
     * Obtiene y devuelve un Dataframe formado solo por las filas correspondientes a los indices proporcionados.
     * 
     * @param rows lista de Integer que representan los indices de las filas a obtener
     * @return     un nuevo Dataframe que contiene las filas correspondientes a los indices proporcionados
     */
    public DataFrame getRow(List<Integer> rows){
        rows.sort(null);
        // System.out.println(fila);
        DataFrame copia = this.getRow(rows.get(0));
        for (int i=1; i < rows.size(); i++){
            copia = copia.concat(this.getRow(rows.get(i)));
        }
        return copia;
    }

    /**
     * Elimina una columna del dataframe a partir de su etiqueta. 
     * 
     * @param tag la etiqueta de la columna a eliminar
     * @throws UnexistentColumnException Si la columna con la etiqueta especificada no existe en el DataFrame
     */
    public DataFrame removeCol(String tag){
        if (!containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }
        DataFrame df = clone();
        df.data.remove(tag);
        df.columnTypes.remove(tag);
        df.tags.remove(df.tags.indexOf(tag));
        return df;
    }

    /**
     * Elimina columnas del Dataframe a partir de sus etiquetas.
     * 
     * @param tag arreglo de etiquetas de las columnas a eliminar
     * @return    un nuevo Dataframe sin las columnas eliminadas
     */
    public DataFrame removeCol(String[] tag){
        DataFrame df = removeCol(tag[0]);
        for (int i=1; i < tag.length; i++){
            df = df.removeCol(tag[i]);
        }
        return df;
    }

    /**
     * Elimina una fila del dataframe a partir de su indice. 
     * 
     * @param row el indice de la fila a eliminar
     */
    public DataFrame removeRow(int row){
        DataFrame df = clone();
        if (row < 0 || row >= df.nRow())
        {
            throw new IndexOutOfBoundsException("Indice fuera de rango. Debe estar entre 0 y " + (df.nRow() - 1));
        }
        for (String colName : df.tags)
        {
            df.getCol(colName).remove(row);
        }

        return df; 
    }

    /**
     * Crea un Dataframe que contiene las filas correspondientes a los indices de inicio y fin indicados.
     * 
     * @param startIndex indice de la primera fila a incluir en el nuevo DataFrame
     * @param endIndex  indice de la ultima fila a incluir en el nuevo DataFrame
     * @return             un nuevo Dataframe formado por las filas indicadas
     */
    public DataFrame slice(int startIndex, int endIndex){
        DataFrame copia = this.getRow(startIndex);
        for (int i=startIndex+1; i <= endIndex; i++){
            copia = copia.concat(this.getRow(i));
        }
        return copia;
    }

    /**
     * Ordena una columna del dataframe de manera creciente o decreciente.
     * 
     * @param tag  etiqueta de la columna a ordenar
     * @param ascending indica si la ordenamiento debe ser ascendente (true) o descendente (false)
     */
    public DataFrame sort(String tag, boolean ascending){
        if (!containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }

        Map<Integer, Integer> orden = getCol(tag).sort(ascending);
        DataFrame copia = this.clone();
        
        for (String colName : this.tags){
            copia.data.put(colName, getCol(colName).sortByIndex(orden));
        }
        return copia;
    }

    /**
     * Ordena las etiquetas del Dataframe de manera ascendente o descendente.
     * 
     * @param tags       Arreglo de nombres de columnas a ordenar
     * @param ascending indica la forma de ordenamiento (true para ascendente, false para descendente).
     * @return           un nuevo Dataframe ordenado segun las columnas y el orden indicado
     */
    public DataFrame sort(String[] tags, boolean ascending)
    {
        for(String tag: tags)
        {
            if(!containsCol(tag))
                throw new UnexistentColumnException(tag);
        }

        DataFrame df = clone();
        List<String> groupedTags = new ArrayList<>();
        df = df.sort(tags[0], ascending);

        for(int i = 1; i < tags.length; i++)
        {
            groupedTags.add(tags[i - 1]);
            df = df.groupBy(groupedTags).sort(tags[i], ascending).unGroup();
        }
        return df;
    }

    /**
     * Ordena el DataFrame en funcion de los indices proporcionados. 
     * 
     * @param indexes un mapa que asocia indices originales con sus nuevas posiciones en el DataFrame
     * @return        nuevo DataFrame ordenado segun las nuevas posiciones especificadas
     */
    public DataFrame sortByIndex(Map<Integer, Integer> indexes)
    {
        if(!indexes.keySet().equals(new HashSet<Integer>(indexes.values())))
        {
            throw new IllegalArgumentException("Se requiere tener una posición para cada indice dado.");
        }
        DataFrame df = clone();

        for(Map.Entry<Integer, Integer> entry: indexes.entrySet())
        {
            df = df.setRow(getRow(entry.getKey()), entry.getValue());
        }

        return df;
    }

    /**
     * Obtiene una muestra aleatoria de filas del DataFrame basada en una fraccion del total de filas.
     * 
     * @param fraction fraccion de filas que se deben incluir en la muestra (debe estar entre 0.0 y 1.0)
     * @return         un nuevo DataFrame que contiene una muestra aleatoria basada en la fraccion especificada.
     */
    public DataFrame sample(double fraction)
    {
        if(fraction < 0 || fraction > 1)
        {
            throw new IllegalArgumentException("La fracción debe estar entre 0.0 y 1.0.");
        }
        long cant = Math.round(fraction * nRow());
        return sample((int) cant);
    }

    /**
     * Obtiene una muestra aleatoria de filas del DataFrame.
     * 
     * @param n cantidad de filas que se deben incluir en la muestra
     * @return  nuevo DataFrame que contiene una muestra aleatoria de 'n' filas
     */
    public DataFrame sample(int n)
    {
        List<Integer> indexes = new ArrayList<>();
        Random rand = new Random();

        while(indexes.size() < n)
        {
            int indice = rand.nextInt(nRow());
            if(!indexes.contains(indice))
            {
                indexes.add(indice);
            }
        }
        return getRow(indexes);
    }

    /**
     * Filtra la filas de una columna del dataframe segun la conidicion proporcionada.
     * 
     * @param <T>      tipo de datos de la columna a filtrar
     * @param tag      etiqueta de la columna a filtrar
     * @param filter   el predicado que define las condiciones de filtrado
     * @return         nuevo DataFrame que contiene solo las filas que cumplen con las condiciones de filtrado
     * @throws UnsupportedOperationException si el metodo no fue implementado
     */
    public <T> DataFrame filter(String tag, Predicate<T> filter){
        if (!containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }

        List<Integer> indices = this.getCol(tag).filter(filter);
        DataFrame filtrado = this.clone();

        for (String colName : this.tags){
            filtrado.data.put(colName, getCol(colName).filterByIndex(indices));
        }
        return filtrado;
    }

    /**
     * Aplica una transformacion a los valores de la columna utilizando el operador proporcionado.
     * 
     * @param <T>            tipo de datos de la columna a transformar
     * @param tag       etiqueta de la columna a transformar
     * @param transformer operador que define la transformacion a aplicar 
     * @return               devuelve un nuevo DataFrame con la transformacion aplicada
     * @throws UnexistentColumnException Si la columna con la etiqueta especificada no existe en el DataFrame
     */
    public <T> DataFrame transformCol(String tag, UnaryOperator<T> transformer){
        if (!this.containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }
        DataFrame copia = this.clone();
        copia.data.put(tag, this.getCol(tag).transform(transformer));
        return copia;
    }

    /**
     * Agrupa la columna del Dataframe indicada por su etiqueta.
     *   
     * @param etiqueta nombre de la columna a agrupar
     * @return
     */
    public GroupBy groupBy(String etiqueta)
    {
        
        return groupBy(new String[]{etiqueta});
    }

    /**
     * Agrupa las columnas del Dataframe indicadas por sus etiquetas.
     * 
     * @param etiquetas lista de etiquetas de las columnas a agrupar
     * @return
     */
    public GroupBy groupBy(List<String> etiquetas)
    {
        
        return groupBy(etiquetas.toArray(String[] ::new));
    }

    /**
     * Agrupa las columnas del Dataframe indicadas por sus etiquetas.
     * 
     * @param etiquetas arreglo de etiquetas de las columnas a agrupar
     * @return
     */
    public GroupBy groupBy(String[] etiquetas)
    {
        
        return new GroupBy(this, etiquetas);
    }

    /**
     * Rellena celdas NA de la columna dada con el valor dado.
     * 
     * @param <T>
     * @param tag nombre de la columna a llenar.
     * @param value valor para rellenar los NA.
     * @return
     */
    public <T> DataFrame fillNA(String tag, T value)
    {
        DataFrame df = clone();
        if (!df.containsCol(tag)){
            throw new UnexistentColumnException(tag);
        }

        df.data.put(tag, df.getCol(tag).fillNA(value));

        return df;
    }
}
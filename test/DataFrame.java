package test;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import utils.ColumnaInexistenteException;
import utils.DataTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataFrame implements Cloneable {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Columna> data;
    private List<String> etiquetas;
    private Map<String, DataTypes> tiposColumna;

    public DataFrame(){
        this.data = new HashMap<>();
        this.etiquetas = new ArrayList<>();
        this.tiposColumna = new HashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Columna> data, Map<String, DataTypes> tiposColumna){
        this.data = data;
        this.tiposColumna = tiposColumna;
        // this.etiquetas = data.keySet().stream().toList();
        // Esto es porque el .toList() no funciona en la compu de la unsam
        this.etiquetas = new ArrayList<>();
        for (String etiqueta : data.keySet()) {
            this.etiquetas.add(etiqueta);
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
        return this.etiquetas;
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
    public Map<String, DataTypes> tiposColumna(){
        return this.tiposColumna;
    }

    /**
     * HAY QUE DOCUMENTAR ESTE?
     */
    public String toString(){
        this.head();
        return "";
    }

    /**
     * 
     * @param indiceInicio
     * @param indiceFinal
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
        printDesdeHasta(0, cantidadFilas);
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
        printDesdeHasta(this.cantidadFilas() - cantidadFilas, this.cantidadFilas());
    }
    /**
     * Imprime las ultimas cinco filas del dataframe.
     */
    public void tail(){
        this.tail(5);
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
        this.data.get(etiqueta).setCelda(indice, valor);
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
        return this.data.get(etiqueta).getCelda(indice);
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
        return tipoDato.cast(data.get(etiqueta).getCelda(indice));
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
        this.data.get(etiqueta).añadirCelda(valor);
    }
    @Override
    public DataFrame clone(){
        Map<String, Columna> columnas = new LinkedHashMap<>();
        Map<String, DataTypes> dataTypes = new LinkedHashMap<>();
        for (String colName : this.etiquetas){
            columnas.put(colName, this.getColumna(colName).clone());
            dataTypes.put(colName, this.tiposColumna.get(colName));
        }
        return new DataFrame(columnas, dataTypes);
    }

    /**
     * Agrega una nueva columna de cadenas al dataframe. 
     * 
     * @param etiqueta etiqueta de la nueva columna
     * @param columna lista de cadenas que contiene los valores que tendran las celdas de la nueva columna
     */
    public void addColumna(String etiqueta, ColumnaString columna){
        if (contieneEtiqueta(etiqueta)){
            throw new RuntimeException("la columna "+etiqueta+" ya existe");
        }
        this.data.put(etiqueta, columna);
        this.tiposColumna.put(etiqueta, DataTypes.STRING);
        this.etiquetas.add(etiqueta);
    }

    /**
     * Agrega una nueva columna de enteros al dataframe. 
     * 
     * @param etiqueta etiqueta de la nueva columna
     * @param columna  lista de enteros que contiene los valores que tendran las celdas de la nueva columna
     */
    public void addColumna(String etiqueta, ColumnaInt columna){
        if (contieneEtiqueta(etiqueta)){
            throw new RuntimeException("la columna "+etiqueta+" ya existe");
        }
        this.data.put(etiqueta, columna);
        this.tiposColumna.put(etiqueta, DataTypes.INT);
        this.etiquetas.add(etiqueta);
    }

    /**
     * Agrega una nueva columna de Double al dataframe. 
     * 
     * @param etiqueta etiqueta de la nueva columna
     * @param columna  lista de Double que contiene los valores que tendran las celdas de la nueva columna
     */
    public void addColumna(String etiqueta, ColumnaDouble columna){
        if (contieneEtiqueta(etiqueta)){
            throw new RuntimeException("la columna "+etiqueta+" ya existe");
        }
        this.data.put(etiqueta, columna);
        this.tiposColumna.put(etiqueta, DataTypes.DOUBLE);
        this.etiquetas.add(etiqueta);
    }

    /**
     * Agrega una nueva columna de booleanos al dataframe. 
     * 
     * @param etiqueta etiqueta de la nueva columna
     * @param columna  lista de booleanos que contiene los valores que tendran las celdas de la nueva columna
     */
    public void addColumna(String etiqueta, ColumnaBool columna){
        if (contieneEtiqueta(etiqueta)){
            throw new RuntimeException("la columna "+etiqueta+" ya existe");
        }
        this.data.put(etiqueta, columna);
        this.tiposColumna.put(etiqueta, DataTypes.BOOL);
        this.etiquetas.add(etiqueta);
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
        Map<String, Columna> columnas = new HashMap<>();
        Map<String, DataTypes> tiposCol = new HashMap<>();
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
        Map<String, DataTypes> tiposCol = new LinkedHashMap<>();
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
            this.data.get(colName).añadirCelda(fila.get(colName));
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
                        otro.get(colName).añadirCelda(getCelda(colName, fila, String.class));
                        break;
                    case INT:
                        otro.put(colName, new ColumnaInt());
                        otro.get(colName).añadirCelda(getCelda(colName, fila, Integer.class));
                        break;
                    case DOUBLE:
                        otro.put(colName, new ColumnaDouble());
                        otro.get(colName).añadirCelda(getCelda(colName, fila, Double.class));
                        break;
                    case BOOL:
                        otro.put(colName, new ColumnaBool());
                        otro.get(colName).añadirCelda(getCelda(colName, fila, Boolean.class));
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
        DataFrame copia = this.getFila(fila[0]);
        for (int i=1; i < fila.length; i++){
            copia.addFila(this.getFila(fila[i]));
        }
        return copia;
    }

    /**
     * 
     * @param fila
     * @return
     */
    public DataFrame getFila(Integer[] fila){
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
    public void eliminarCol(String etiqueta){
        if (this.contieneEtiqueta(etiqueta)){
            this.data.remove(etiqueta);
            this.etiquetas.remove(this.etiquetas.indexOf(etiqueta));
            return;
        }
        throw new ColumnaInexistenteException(etiqueta);
    }

    /**
     * Elimina una fila del dataframe a partir de su indice. 
     * 
     * @param etiqueta el indice de la fila a eliminar
     */
    public void eliminarFila(int fila){
        if (0 <= fila && fila <= this.cantidadFilas()-1){
            for (String colName : this.etiquetas){
                this.getColumna(colName).eliminarCelda(fila);
            }
        }
        throw new RuntimeException("La fila debe estar en el rango [0, n)");
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
    public void ordenar(String etiqueta, boolean creciente){
        Map<Integer, Integer> orden = getColumna(etiqueta).ordenar(creciente);
        
        for (Map.Entry<String, Columna> entry : this.data.entrySet()) {
            this.data.put(entry.getKey(), entry.getValue().ordenarPorIndice(orden));
        }
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
        DataFrame filtrado = new DataFrame();

        for (String colName : this.etiquetas){
            DataTypes type = this.tiposColumna.get(colName);
            switch (tiposColumna.get(colName)) {
                case BOOL:
                    filtrado.addColumna(colName, (ColumnaBool) getColumna(colName).filtrarPorIndice(indices));
                    break;
                case INT:
                    filtrado.addColumna(colName, (ColumnaInt) getColumna(colName).filtrarPorIndice(indices));
                    break;
                case DOUBLE:
                    filtrado.addColumna(colName, (ColumnaDouble) getColumna(colName).filtrarPorIndice(indices));
                    break;
                default:
                    // STRING
                    filtrado.addColumna(colName, (ColumnaString) getColumna(colName).filtrarPorIndice(indices));
                    break;
            }
        }
        return filtrado;
    }

    public <T> DataFrame transformCol(String etiqueta, UnaryOperator<T> transformacion){
    /**
     * Aplica una transformacion a los valores de la columna utilizando el operador proporcionado.
     * 
     * @param <T> tipo de datos de la columna a transformar
     * @param etiqueta etiqueta de la columna a transformar
     * @param transformacion operador que define la transformacion a aplicar 
     * @throws ColumnaInexistenteException Si la columna con la etiqueta especificada no existe en el DataFrame
     */
        if (!this.contieneEtiqueta(etiqueta)){
            throw new ColumnaInexistenteException(etiqueta);
        }
        DataFrame copia = this.clone();
        copia.data.put(etiqueta, this.getColumna(etiqueta).transformar(transformacion));
        return copia;
    }

}

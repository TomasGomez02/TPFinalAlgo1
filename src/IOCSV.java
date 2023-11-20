package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import utils.CasteoIlegalException;
import utils.DataType;

public final class IOCSV 
{
    static char delim;
    static final char defaultDelim = ',';
    static final boolean defaultHasHeaders = true;
    static final DataType[] defaultDataTypes = null;

    private IOCSV(){}

    /**
     * Crea un DataFrame a partir de un archivo CSV.
     * 
     * @param path ruta del archivo csv
     * @return DataFrame creado a partir del archivo csv
     */
    public static DataFrame fromCSV(String path)
    {
        return fromCSV(path, defaultHasHeaders);
    }

    public static DataFrame fromCSV(String path, boolean hasHeaders)
    {
        return fromCSV(path, hasHeaders, defaultDelim);
    }

    public static DataFrame fromCSV(String path, boolean hasHeaders, char delim)
    {
        return fromCSV(new File(path), hasHeaders, delim, defaultDataTypes);
    }

    public static DataFrame fromCSV(String path, char delim)
    {
        return fromCSV(path, defaultHasHeaders, delim);
    }

    /**
     * Crea un Dataframe a partir de un archivo csv.
     * 
     * @param path      ruta del archivo csv
     * @param dataTypes tipos de datos de las columnas del Dataframe 
     * @return          DataFrame creado a partir del archivo csv
     */
    public static DataFrame fromCSV(String path, DataType[] dataTypes) {
        return fromCSV(new File(path), defaultHasHeaders, defaultDelim, dataTypes);
    }

    /**
     * Crea un Dataframe a partir de un archivo File.
     * 
     * @param file archivo csv representado como un objeto File
     * @return     Dataframe creado a partir del archivo csv
     */
    public static DataFrame fromCSV(File file)
    {
        return fromCSV(file, defaultHasHeaders, defaultDelim, defaultDataTypes);
    }

    /**
     * Crea un Dataframe a partir de un archivo File.
     * 
     * @param file      archivo csv representado como un objeto File
     * @param dataTypes tipos de datos de las columnas del Dataframe
     * @return          Dataframe creado a partir del archivo csv
     */
    public static DataFrame fromCSV(File file, boolean hasHeaders, char delim, DataType[] dataTypes){
        IOCSV.delim = delim;
        Map<String, DataType> tiposEtiqueta = new LinkedHashMap<>();
        String[] header;
        @SuppressWarnings("rawtypes")
        Map<String, Columna> columnas = new LinkedHashMap<>();

        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            linea = br.readLine();
            
            // Genero las columnas para cada etiqueta
            // Y el tipo de dato para cada etiqueta
            
            header = procesarFila(linea);

            if(!hasHeaders)
            {
                for(int i = 0; i < header.length; i++)
                {
                    header[i] = String.valueOf(i);
                }
            }
            //System.out.println(header.length);
            columnas = crearColumnas(header);

            for (int i=0; i < header.length; i++){
                if(dataTypes != null)
                    tiposEtiqueta.put(header[i], dataTypes[i]);
                else
                    tiposEtiqueta.put(header[i], DataType.STRING);
            }  

            if(hasHeaders)
                linea = br.readLine();

            // Asigno los valores a cada columna
            while (linea != null) {
                String[] valores = procesarFila(linea, header.length);
                //System.out.println(valores.length);
                for (int i=0; i < valores.length; i++) {
                    columnas.get(header[i]).add(valores[i]);
                }
                linea = br.readLine();
            }
            
        } catch (FileNotFoundException e) {
            System.out.println(e.getClass());
            System.out.println("Error: Archivo no encontrado. Path: "+file.getPath());
        } catch (IOException e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        } catch (Exception e){
            System.out.println(e.getClass());
            System.out.println(e.getMessage());
        }

        if(dataTypes != null)
        {
            return crearDataFrame(columnas, tiposEtiqueta, dataTypes);
        }

        return autogenerarDataFrame(columnas, tiposEtiqueta);
    }

    /**
     * Crea las columnas del Dataframe a partir de las etiquetas proporcionadas.
     * 
     * @param headers    array de etiquetas de las columnas
     * @return           mapa que asocia cada etiqueta con su respectiva Columna
     * @throws Exception si ocurre un error al crear las columnas
     */
    @SuppressWarnings("rawtypes")
    private static Map<String, Columna> crearColumnas(String[] headers) throws Exception {
        Map<String, Columna> columnas = new LinkedHashMap<>();

        for (int i=0; i < headers.length; i++){
                columnas.put(headers[i], new ColumnaString());
        }
        return columnas;
    }

    /**
     * Procesa las cadenas proporcionadas como entrada. 
     * 
     * @param fila cadena a procesar
     * @return     array de String que contiene las filas procesadas
     */
    public static String[] procesarFila(String fila)
    {
        return procesarFila(fila, 0);
    }

    private static String[] procesarFila(String fila, int cantidadCol)
    {
        List<String> filaProcesada = new ArrayList<>();

        String elemento = "";
        boolean enComillas = false;

        char[] filaChar = fila.toCharArray();
        for(int i = 0; i < filaChar.length; i++)    
        {
            if(filaChar[i] == IOCSV.delim)
            {
                if(!enComillas)
                {
                    filaProcesada.add(elemento);
                    elemento = "";
                }
            }
            else if(filaChar[i] == '"')
            {
                enComillas = !enComillas;
            }
            else
            {
                elemento += filaChar[i];
            }
        }

        if(elemento != "")
            filaProcesada.add(elemento);

        while(cantidadCol > 0 && filaProcesada.size() < cantidadCol)
            filaProcesada.add("");

        return filaProcesada.toArray(String[] ::new);
    }

    /**
     * Crea y devuelve un nuevo DataFrame con columnas modificadas según los tipos de datos proporcionados.
     * 
     * @param columnas      mapa que asocia etiqueta con objetos Columna
     * @param tiposEtiqueta mapa que asocia etiqueta de columna y el tipo de dato de la columna
     * @param datatypes     array con los tipos de datos a los que se deben convertir las columnas.
     * @return              nuevo DataFrame con las columnas modificadas según los tipos de datos proporcionados
     */
    private static DataFrame crearDataFrame(Map<String, Columna> columnas, Map<String, DataType> tiposEtiqueta, DataType[] datatypes)
    {
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());

        for(int i = 0; i < keys.size(); i++)
        {
            Columna col = columnas.get(keys.get(i));
            columnas.put(keys.get(i), castearColumna(col, datatypes[i]));
            tiposEtiqueta.put(keys.get(i), datatypes[i]);
        }

        return new DataFrame(columnas, tiposEtiqueta);
    }

    /**
     * Convierte una columna al tipo de dato especificado como entrada.
     * 
     * @param col      columna a convertir
     * @param dataType tipo dato al cual se va a convertir la columna
     * @return         columna convertida al tipo de dato especificado
     */
    private static Columna castearColumna(Columna col, DataType dataType)
    {
        switch (dataType) {
            case INT:
                return ColumnaInt.toColumnaInt(col, true);
            case DOUBLE:
                return ColumnaDouble.toDoubleColumn(col, true);
            case BOOL:
                return ColumnaBool.toBoolColumn(col, true);
            default:
                return col;
        }
    }

    /**
     * Autogenera un nuevo DataFrame ajustando automaticamente las columnas segun los tipos de datos proporcionados.
     *
     * @param columnas      mapa que asocia etiqueta con objetos Columna
     * @param tiposEtiqueta mapa que asocia etiqueta de columna y el tipo de dato de la columna
     * @return              Dataframe con las columnas ajustadas 
     */
    private static DataFrame autogenerarDataFrame(Map<String, Columna> columnas, Map<String, DataType> tiposEtiqueta){
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());

        for(int i = 0; i < keys.size(); i++){
            Columna col = columnas.get(keys.get(i));
            columnas.put(keys.get(i), autodetectarColumna(col, tiposEtiqueta, i));
        }
        return new DataFrame(columnas, tiposEtiqueta);
    }

    /**
     * Realiza la deteccion automatica del tipo de datos de una columna.
     * 
     * @param col           Columna a autodetectar
     * @param tiposEtiqueta mapa que asocia etiqueta de columna y el tipo de dato de la columna
     * @param index         indice de la columna
     * @return              nueva columna ajustada segun el tipo de datos detectado
     */
    private static Columna autodetectarColumna(Columna col, Map<String, DataType> tiposEtiqueta, int index){
        return autodetectarColumna(col, tiposEtiqueta, index, 0);
    }

    /**
     * Realiza la deteccion automatica del tipo de datos de una columna, intentando diferentes castings.
     * 
     * @param col           Columna a autodetectar
     * @param tiposEtiqueta mapa que asocia etiqueta de columna y el tipo de dato de la columna
     * @param index         indice de la columna
     * @param count         contador utilizado para realizar diferentes castings
     * @return              nueva columna ajustada segun el tipo de datos detectado
     */
    private static Columna autodetectarColumna(Columna col, Map<String, DataType> tiposEtiqueta, int index, int count)
    {
        Columna colCasteada;
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());
        try
        {
            switch (count) {
                case 0:
                    colCasteada = ColumnaBool.toBoolColumn(col);
                    tiposEtiqueta.put(keys.get(index), DataType.BOOL);
                    break;
                case 1:
                    colCasteada = ColumnaInt.toColumnaInt(col);
                    tiposEtiqueta.put(keys.get(index), DataType.INT);
                    break;
                case 2:
                    colCasteada = ColumnaDouble.toDoubleColumn(col);
                    tiposEtiqueta.put(keys.get(index), DataType.DOUBLE);
                    break;
                default:
                    colCasteada = col;
            }
        }
        catch(CasteoIlegalException e)
        {
            colCasteada = autodetectarColumna(col, tiposEtiqueta, index, count + 1);
        }
        return colCasteada;
    }

    /**
     * Obtiene una linea de encabezado con los nombres de las columnas del DataFrame.
     *
     * @param df DataFrame del cual se obtendran los nombres de las columnas
     * @return   cadena que contiene los nombres de las columnas, separando los valores por comas
     */
    private static String getHeaderLine(DataFrame df)
    {
        String linea = "";
        for(String etiqueta: df.nombreColumnas()) {
            linea += etiqueta + ",";
        }
        return linea;
    }

    /**
     * Obtiene la primera fila del Dataframe.
     * 
     * @param df Dataframe del cual se obtendra la fila
     * @return   cadena que contiene la primera fila. separando los valores por comas
     */
    private static String getRowLine(DataFrame df)
    {
        String linea = "";
        List<String> headers = df.nombreColumnas();
        for(String header: headers)
        {
            String celda = String.valueOf(df.getCelda(header, 0));
            if(celda.contains(","))
                celda = "\"" + celda + "\"";
            else if(celda.equals("null"))
                celda = "";
            linea += celda + ",";
        }

        return linea;
    }

    /**
     * Guarda un DataFrame en formato CSV en la ruta especificada.
     * 
     * @param data Dataframe a guardar
     * @param path ruta donde se guarda el archivo
     */
    public static void toCSV(DataFrame data, String path)
    {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path)))
        {
            bw.write(getHeaderLine(data));
            bw.newLine();

            for(int i = 0; i < data.cantidadFilas(); i++)
            {
                bw.write(getRowLine(data.getFila(i)));
                bw.newLine();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

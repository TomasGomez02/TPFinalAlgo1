package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import utils.CasteoIlegalException;
import utils.DataType;

public final class IOCSV 
{
    private IOCSV(){}

    public static DataFrame fromCSV(String path)
    {
        return fromCSV(path, null);
    }

    public static DataFrame fromCSV(String path, DataType[] dataTypes) {
        return fromCSV(new File(path), dataTypes);
    }

    public static DataFrame fromCSV(File file)
    {
        return fromCSV(file, null);
    }

    public static DataFrame fromCSV(File file, DataType[] dataTypes){
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
            //System.out.println(header.length);
            columnas = crearColumnas(header);

            for (int i=0; i < header.length; i++){
                if(dataTypes != null)
                    tiposEtiqueta.put(header[i], dataTypes[i]);
                else
                    tiposEtiqueta.put(header[i], DataType.STRING);
            }  

            // Asigno los valores a cada columna
            while ((linea = br.readLine()) != null) {
                String[] valores = procesarFila(linea, header.length);
                //System.out.println(valores.length);
                for (int i=0; i < valores.length; i++) {
                    columnas.get(header[i]).añadirCelda(valores[i]);
                }
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

    @SuppressWarnings("rawtypes")
    private static Map<String, Columna> crearColumnas(String[] headers) throws Exception {
        Map<String, Columna> columnas = new LinkedHashMap<>();

        for (int i=0; i < headers.length; i++){
                columnas.put(headers[i], new ColumnaString());
        }
        return columnas;
    }

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
            switch (filaChar[i]) 
            {
                case ',':
                    if(!enComillas)
                    {
                        filaProcesada.add(elemento);
                        elemento = "";
                    }
                    break;
                case '"':
                    enComillas = !enComillas;
                    break;
                default:
                    elemento += filaChar[i];
                    break;
            }
        }

        if(elemento != "")
            filaProcesada.add(elemento);

        while(cantidadCol > 0 && filaProcesada.size() < cantidadCol)
            filaProcesada.add("");

        return filaProcesada.toArray(String[] ::new);
    }

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

    private static Columna castearColumna(Columna col, DataType dataType)
    {
        switch (dataType) {
            case INT:
                return ColumnaInt.fromColumnaString(col, true);
            case DOUBLE:
                return ColumnaDouble.fromColumnaString(col, true);
            case BOOL:
                return ColumnaBool.fromColumnaString(col, true);
            default:
                return col;
        }
    }

    private static DataFrame autogenerarDataFrame(Map<String, Columna> columnas, Map<String, DataType> tiposEtiqueta)
    {
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());

        for(int i = 0; i < keys.size(); i++)
        {
            Columna col = columnas.get(keys.get(i));
            columnas.put(keys.get(i), autodetectarColumna(col, tiposEtiqueta, i));
        }

        return new DataFrame(columnas, tiposEtiqueta);
    }

    private static Columna autodetectarColumna(Columna col, Map<String, DataType> tiposEtiqueta, int index)
    {
        return autodetectarColumna(col, tiposEtiqueta, index, 0);
    }

    private static Columna autodetectarColumna(Columna col, Map<String, DataType> tiposEtiqueta, int index, int count)
    {
        Columna colCasteada;
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());
        try
        {
            switch (count) {
                case 0:
                    colCasteada = ColumnaBool.fromColumnaString(col);
                    tiposEtiqueta.put(keys.get(index), DataType.BOOL);
                    break;
                case 1:
                    colCasteada = ColumnaInt.fromColumnaString(col);
                    tiposEtiqueta.put(keys.get(index), DataType.INT);
                    break;
                case 2:
                    colCasteada = ColumnaDouble.fromColumnaString(col);
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

    private static String getHeaderLine(DataFrame df)
    {
        String linea = "";
        for(String etiqueta: df.nombreColumnas())
        {
            linea += etiqueta + ",";
        }

        return linea;
    }

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

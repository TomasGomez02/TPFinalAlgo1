package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

import utils.CasteoIlegal;
import utils.DataTypes;

public final class IOCSV 
{
    private IOCSV(){}

    public static DataFrame fromCSV(String path)
    {
        return fromCSV(path, null);
    }

    public static DataFrame fromCSV(String path, DataTypes[] dataTypes) {
        return fromCSV(new File(path), dataTypes);
    }

    public static DataFrame fromCSV(File file)
    {
        return fromCSV(file, null);
    }

    public static DataFrame fromCSV(File file, DataTypes[] dataTypes){
        Map<String, DataTypes> tiposEtiqueta = new LinkedHashMap<>();
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
                    tiposEtiqueta.put(header[i], DataTypes.STRING);
            }  

            // Asigno los valores a cada columna
            while ((linea = br.readLine()) != null) {
                String[] valores = procesarFila(linea);
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

    private static String[] procesarFila(String fila)
    {
        String[] filaComillas = fila.split("\"");
        List<String> filaProcesada = new ArrayList<>();

        for(int i = 0; i < filaComillas.length; i++)
        {
            if(i % 2 == 0 && !filaComillas[i].equals(",") && !filaComillas[i].equals(""))
            {
                if(i > 0)
                {
                    filaComillas[i] = filaComillas[i].replaceFirst(",", "");
                }
                filaProcesada.addAll(Arrays.asList(filaComillas[i].replaceAll("\n", "")
                                            .split(",")));
            }
            else if(i % 2 == 1)
            {
                filaProcesada.add(filaComillas[i]);
            }
        }

        return filaProcesada.toArray(String[] ::new);
    }

    private static DataFrame crearDataFrame(Map<String, Columna> columnas, Map<String, DataTypes> tiposEtiqueta, DataTypes[] datatypes)
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

    private static Columna castearColumna(Columna col, DataTypes dataType)
    {
        switch (dataType) {
            case INT:
                return ColumnaInt.fromColumnaString(col);
            case DOUBLE:
                return ColumnaDouble.fromColumnaString(col);
            case BOOL:
                return ColumnaBool.fromColumnaString(col);
            default:
                return col;
        }
    }

    private static DataFrame autogenerarDataFrame(Map<String, Columna> columnas, Map<String, DataTypes> tiposEtiqueta)
    {
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());

        for(int i = 0; i < keys.size(); i++)
        {
            Columna col = columnas.get(keys.get(i));
            columnas.put(keys.get(i), autodetectarColumna(col, tiposEtiqueta, i));
        }

        return new DataFrame(columnas, tiposEtiqueta);
    }

    private static Columna autodetectarColumna(Columna col, Map<String, DataTypes> tiposEtiqueta, int index)
    {
        return autodetectarColumna(col, tiposEtiqueta, index, 0);
    }

    private static Columna autodetectarColumna(Columna col, Map<String, DataTypes> tiposEtiqueta, int index, int count)
    {
        Columna colCasteada;
        List<String> keys = new ArrayList<>(tiposEtiqueta.keySet());
        try
        {
            switch (count) {
                case 0:
                    colCasteada = ColumnaBool.fromColumnaString(col);
                    tiposEtiqueta.put(keys.get(index), DataTypes.BOOL);
                    break;
                case 1:
                    colCasteada = ColumnaInt.fromColumnaString(col);
                    tiposEtiqueta.put(keys.get(index), DataTypes.INT);
                    break;
                case 2:
                    colCasteada = ColumnaDouble.fromColumnaString(col);
                    tiposEtiqueta.put(keys.get(index), DataTypes.DOUBLE);
                default:
                    colCasteada = col;
            }
        }
        catch(CasteoIlegal e)
        {
            colCasteada = autodetectarColumna(col, tiposEtiqueta, index, count + 1);
        }
        return colCasteada;
    }

    public static void toCSV(DataFrame data)
    {
        // TODO: agregar esto algun dia
    }
}

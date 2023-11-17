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
import java.util.LinkedHashMap;

public final class IOCSV 
{
    private IOCSV(){}

    public static DataFrame fromCSV(String path, String[] dataTypes) {
        return fromCSV(new File(path),dataTypes);
    }

    public static DataFrame fromCSV(File file, String[] dataTypes){
        Map<String, String> tiposEtiqueta = new HashMap<>();
        String[] header;
        @SuppressWarnings("rawtypes")
        Map<String, Columna> columnas = new HashMap<>();

        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            linea = br.readLine();
            
            // Genero las columnas para cada etiqueta
            // Y el tipo de dato para cada etiqueta
            header = procesarFila(linea);
            //System.out.println(header.length);
            columnas = crearColumnas(header, dataTypes);
            for (int i=0; i < header.length; i++){
                tiposEtiqueta.put(header[i], dataTypes[i]);
            }

            

            // Asigno los valores a cada columna
            while ((linea = br.readLine()) != null) {
                String[] valores = procesarFila(linea);
                //System.out.println(valores.length);
                for (int i=0; i < valores.length; i++) {
                    addElemToDataFrame(columnas, header[i], valores[i], tiposEtiqueta.get(header[i]));
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
        return new DataFrame(columnas, tiposEtiqueta);
    }
    @SuppressWarnings("rawtypes")
    private static void addElemToDataFrame(Map<String, Columna> columnas, String etiqueta, 
                            String elem, String dataType) {
        try {
            switch (dataType) {
                case "String":
                    columnas.get(etiqueta).añadirCelda(elem);
                    break;
                case "Integer":
                    columnas.get(etiqueta).añadirCelda(Integer.valueOf(elem));
                    break;
                case "Double":
                    columnas.get(etiqueta).añadirCelda(Double.valueOf(elem));
                    break;
                case "Boolean":
                    columnas.get(etiqueta).añadirCelda(Boolean.valueOf(elem));
                    break;
                default:
                    System.out.println("Tipo desconocido: "+dataType);
                    break;
            }
        } catch (NumberFormatException e){
            System.out.println(e);
            System.out.println("El elemento: "+elem+" es de tipo incorrecto para la columna");
            columnas.get(etiqueta).añadirCelda(null);
        }
    }

    @SuppressWarnings("rawtypes")
    private static Map<String, Columna> crearColumnas(String[] headers, String[] dataTypes) throws Exception {
        if (headers.length > dataTypes.length){
            // TODO: Hay que crear una excepcion para eso?
            throw new Exception("Faltan especificar "+(headers.length-dataTypes.length)+" tipos de datos");
        }

        Map<String, Columna> columnas = new LinkedHashMap<>();

        for (int i=0; i < headers.length; i++){
            switch (dataTypes[i].strip()) {
                case "String":
                    columnas.put(headers[i], new ColumnaString());
                    break;
                case "Integer":
                    columnas.put(headers[i], new ColumnaInt());
                    break;
                case "Double":
                    columnas.put(headers[i], new ColumnaDouble());
                    break;
                case "Boolean":
                    columnas.put(headers[i], new ColumnaBool());
                    break;
                default:
                    System.out.println("Tipo de dato desconocido: "+dataTypes[i]);
                    // TODO: Deberia tirar un error?
                    break;
            }
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

    public static void toCSV(DataFrame data)
    {
        // TODO: agregar esto algun dia
    }
}

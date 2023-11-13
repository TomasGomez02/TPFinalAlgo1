package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

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
            header = linea.replaceAll("\n", "").split(",");
            for (int i=0; i < header.length; i++){
                switch (dataTypes[i].strip()) {
                    case "String":
                        columnas.put(header[i], new ColumnaString());
                        tiposEtiqueta.put(header[i], dataTypes[i]);
                        break;
                    case "Integer":
                        columnas.put(header[i], new ColumnaInt());
                        tiposEtiqueta.put(header[i], dataTypes[i]);
                        break;
                    case "Double":
                        columnas.put(header[i], new ColumnaDouble());
                        tiposEtiqueta.put(header[i], dataTypes[i]);
                        break;
                    case "Boolean":
                        columnas.put(header[i], new ColumnaBool());
                        tiposEtiqueta.put(header[i], dataTypes[i]);
                        break;
                    default:
                        System.out.println("Tipo de dato desconocido: "+dataTypes[i]);
                        break;
                }
            }
            
            // Asigno los valores a cada columna
            while ((linea = br.readLine()) != null) {
                String[] valores = linea.replaceAll("\n", "").split(",");
                for (int i=0; i < valores.length; i++) {
                    System.out.print(valores[i]+" ");
                    switch (tiposEtiqueta.get(header[i])) {
                        case "String":
                            columnas.get(header[i]).añadirCelda(valores[i]);
                            break;
                        case "Integer":
                            columnas.get(header[i]).añadirCelda(Integer.valueOf(valores[i]));
                            break;
                        case "Double":
                            columnas.get(header[i]).añadirCelda(Double.valueOf(valores[i]));
                            break;
                        case "Boolean":
                            columnas.get(header[i]).añadirCelda(Boolean.valueOf(valores[i]));
                            break;
                        default:
                            break;
                    }
                }
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Archivo no encontrado. Path: "+file.getName());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return new DataFrame(columnas, tiposEtiqueta);
    }

    public static void toCSV(DataFrame data)
    {
        // TODO: agregar esto algun dia
    }
}

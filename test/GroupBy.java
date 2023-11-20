package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupBy 
{
    DataFrame data;
    Map<String, List<Integer>> grupos;
    Map<String, ColumnaString> colAgrupadas;
    List<String> colNoAgrupadas;
    List<String> colAgregadas;
    List<String> valorGrupos;

    public GroupBy(DataFrame df, List<String> colAgrupadas)
    {
        this.data = df.clone();

        
        this.colAgrupadas = new LinkedHashMap<>();
        
        colNoAgrupadas = this.data.nombreColumnas();
        for(String col: colAgrupadas)
        {
            this.colAgrupadas.put(col, ColumnaString.toColumnaString(this.data.getColumna(col).unique()));
            colNoAgrupadas.remove(col);
        }
        colAgregadas = new ArrayList<>();

        grupos = generarGrupos();
    }

    public GroupBy(DataFrame df, String[] colAgrupadas)
    {
        this(df, Arrays.asList(colAgrupadas));
    }

    private List<String> getColumnasAgrupadas()
    {
        return new ArrayList<>(colAgrupadas.keySet());
    }

    private Map<String, List<Integer>> generarGrupos()
    {
        valorGrupos = new ArrayList<>();
        Map<String, List<Integer>> grupos = new LinkedHashMap<>();
        for(String grupo: getGrupos())
        {
            List<Integer> indices = generarIndicesGrupo(grupo);
            if(indices.size() > 0)
            {
                grupos.put(grupo, indices);
                valorGrupos.add(grupo);
            }
        }

        return grupos;
    }

    private List<Integer> generarIndicesGrupo(String grupo)
    {
        List<Integer> indices = new ArrayList<>();

        for(int i = 0; i < data.cantidadFilas(); i++)
        {
            if(perteneceAGrupo(data.getFila(i), grupo))
            {
                indices.add(i);
            }
        }

        return indices;
    }

    private String[] parseGrupo(String valorGrupo)
    {
        List<String> filaProcesada = new ArrayList<>();

        String elemento = "";
        boolean enComillas = false;

        char[] filaChar = valorGrupo.toCharArray();
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

        while(filaProcesada.size() < getColumnasAgrupadas().size())
            filaProcesada.add("");

        return filaProcesada.toArray(String[] ::new);
    }

    private boolean perteneceAGrupo(DataFrame fila, String grupo)
    {
        String[] valores = parseGrupo(grupo);
        List<String> columnas = getColumnasAgrupadas();
        for(int i = 0; i < columnas.size(); i++)
        {
            if(!String.valueOf(fila.getCelda(columnas.get(i), 0)).equals(String.valueOf(valores[i])))
            {
                return false;
            }
        }
        return true;
    }

    private List<String> getGrupos()
    {
        return getGrupos(0);
    }

    private List<String> getGrupos(int indiceGrupo)
    {
        List<String> grupos = new ArrayList<>();
        List<String> siguientesGrupos = null;
        if(indiceGrupo < colAgrupadas.size() - 1)
        {
            siguientesGrupos = getGrupos(indiceGrupo + 1);
        }
        
        List<String> cols = getColumnasAgrupadas();
        ColumnaString values = colAgrupadas.get(cols.get(indiceGrupo));
        for(int i = 0; i < values.length(); i++)
        {
            if(siguientesGrupos == null)
                grupos.add(String.valueOf(values.getCelda(i)));
            else
                for(String str: siguientesGrupos)
                    grupos.add(String.valueOf(values.getCelda(i)) + "," + str);
        }

        return grupos;
    }

    private String getHeaderLine(List<String> columnasPrintear)
    {
        String linea = "";
        for(String etiqueta: columnasPrintear)
        {
            linea += etiqueta + " | ";
        }

        return linea;
    }

    private String getRowLine(DataFrame fila, List<String> columnasPrintear)
    {
        String linea = "";
        List<String> headers = columnasPrintear;
        for(String header: headers)
        {
            String celda = String.valueOf(fila.getCelda(header, 0));
            linea += celda + " | ";
        }

        return linea;
    }

    public Map<String, List<Integer>> getInfoGrupos()
    {
        return grupos;
    }

    @Override
    public String toString()
    {
        String out = "";
        List<String> colPrinteables = getColumnasAgrupadas();
        colPrinteables.addAll(colAgregadas);

        out += getHeaderLine(colPrinteables) + "\n";

        for(String grupo: valorGrupos)
        {
            out += getRowLine(data.getFila(grupos.get(grupo).get(0)), colPrinteables) + "\n";
        }

        return out;
    }

    public DataFrame unGroup()
    {
        return data.clone();
    }
}

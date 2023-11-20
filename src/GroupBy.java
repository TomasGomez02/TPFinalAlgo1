package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import utils.ColumnaInexistenteException;
import utils.ColumnaNoOperableException;

public class GroupBy implements Cloneable
{
    private DataFrame data;
    private Map<String, List<Integer>> grupos;
    private Map<String, StringColumn> colAgrupadas;
    private List<String> colNoAgrupadas;
    private List<String> colAgregadas;
    private List<String> valorGrupos;

    public GroupBy(DataFrame df, List<String> colAgrupadas)
    {
        this.data = df.clone();
        this.colAgrupadas = new LinkedHashMap<>();
        
        colNoAgrupadas = this.data.nombreColumnas();
        for(String col: colAgrupadas)
        {
            this.colAgrupadas.put(col, StringColumn.toStringColumn(this.data.getColumna(col).unique()));
            colNoAgrupadas.remove(col);
        }
        colAgregadas = new ArrayList<>();

        grupos = generateGroups();
    }

    public GroupBy(DataFrame df, String[] colAgrupadas)
    {
        this(df, Arrays.asList(colAgrupadas));
    }

    public GroupBy(DataFrame df, List<String> colAgrupadas, List<String> colAgregadas)
    {
        this.data = df.clone();

        
        this.colAgrupadas = new LinkedHashMap<>();
        
        colNoAgrupadas = this.data.nombreColumnas();
        for(String col: colAgrupadas)
        {
            this.colAgrupadas.put(col, StringColumn.toStringColumn(this.data.getColumna(col).unique()));
            colNoAgrupadas.remove(col);
        }
        this.colAgregadas = new ArrayList<>(colAgregadas);

        grupos = generateGroups();
    }

    private List<String> getGroupedColumns()
    {
        return new ArrayList<>(colAgrupadas.keySet());
    }

    private Map<String, List<Integer>> generateGroups()
    {
        valorGrupos = new ArrayList<>();
        Map<String, List<Integer>> grupos = new LinkedHashMap<>();
        for(String grupo: getGroups())
        {
            List<Integer> indices = generateGroupIndexes(grupo);
            if(indices.size() > 0)
            {
                grupos.put(grupo, indices);
                valorGrupos.add(grupo);
            }
        }

        return grupos;
    }

    private List<Integer> generateGroupIndexes(String group)
    {
        List<Integer> indices = new ArrayList<>();

        for(int i = 0; i < data.cantidadFilas(); i++)
        {
            if(belongsToGroup(data.getFila(i), group))
            {
                indices.add(i);
            }
        }
        return indices;
    }

    private String[] parseGroup(String groupValue)
    {
        List<String> filaProcesada = new ArrayList<>();

        String elemento = "";
        boolean enComillas = false;

        char[] filaChar = groupValue.toCharArray();
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

        while(filaProcesada.size() < getGroupedColumns().size())
            filaProcesada.add("");

        return filaProcesada.toArray(String[] ::new);
    }

    private boolean belongsToGroup(DataFrame row, String group)
    {
        String[] valores = parseGroup(group);
        List<String> columnas = getGroupedColumns();
        for(int i = 0; i < columnas.size(); i++)
        {
            if(!String.valueOf(row.getCelda(columnas.get(i), 0)).equals(String.valueOf(valores[i])))
            {
                return false;
            }
        }
        return true;
    }

    private List<String> getGroups()
    {
        return getGroups(0);
    }

    private List<String> getGroups(int groupIndex)
    {
        List<String> grupos = new ArrayList<>();
        List<String> siguientesGrupos = null;
        if(groupIndex < colAgrupadas.size() - 1)
        {
            siguientesGrupos = getGroups(groupIndex + 1);
        }
        
        List<String> cols = getGroupedColumns();
        StringColumn values = colAgrupadas.get(cols.get(groupIndex));
        for(int i = 0; i < values.length(); i++)
        {
            if(siguientesGrupos == null)
                grupos.add(String.valueOf(values.get(i)));
            else
                for(String str: siguientesGrupos)
                    grupos.add(String.valueOf(values.get(i)) + "," + str);
        }

        return grupos;
    }

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

    private String getHeaderLine(List<String> printableColumns, int[] size)
    {
        String linea = "";
        final String sep = "|";
        int espacioIzq;
        int espacioDer;

        for (int i = 0; i < printableColumns.size(); i++)
        {
            int diff = size[i] - printableColumns.get(i).length();
            espacioIzq = diff / 2;
            if (diff % 2 == 0)
            {
                espacioDer = espacioIzq;
            } 
            else 
            {
                espacioDer = espacioIzq+1;
            }
            linea += " ".repeat(espacioIzq)+printableColumns.get(i)+" ".repeat(espacioDer)+sep;
        }

        return linea;
    }

    private String getRowLine(DataFrame fila, List<String> columnasPrintear, int[] tamaño)
    {
        String linea = "";
        final String sep = "|";
        int espacioIzq;
        int espacioDer;
        for (int i = 0; i < columnasPrintear.size(); i++)
        {
            String celda = String.valueOf(fila.getCelda(columnasPrintear.get(i), 0));
            
            if(celda.length() > tamaño[i])
            {
                celda = shortenedString(celda, tamaño[i]);
            }

            int diff = tamaño[i] - celda.length();
                espacioIzq = diff / 2;
                if (diff % 2 == 0)
                {
                    espacioDer = espacioIzq;
                } 
                else
                {
                    espacioDer = espacioIzq+1;
                }

            linea += " ".repeat(espacioIzq) + celda + " ".repeat(espacioDer) + sep;
        }

        return linea;
    }

    /**
     * Imprime en la consola una representacion de cadena del objeto actual utilizando el metodo toString().
     */
    public void print()
    {
        System.out.println(toString());
    }

    @Override
    public String toString()
    {
        final List<String> colPrinteables = getGroupedColumns();
        colPrinteables.addAll(colAgregadas);
        int[] tamaño = new int[colPrinteables.size()];
        final int OFFSETMINIMO = 4;

        for (int i = 0; i < colPrinteables.size(); i++)
        {
            tamaño[i] = OFFSETMINIMO * 2;
            if (colPrinteables.get(i).length() + OFFSETMINIMO > tamaño[i])
            {
                tamaño[i] = colPrinteables.get(i).length() + OFFSETMINIMO;
            }
        }

        String out = "";

        out += getHeaderLine(colPrinteables, tamaño) + "\n";

        int cantidadSepHeader = 0;
        for(int tam: tamaño)
            cantidadSepHeader += tam + 1;

        out += "-".repeat(cantidadSepHeader) + "\n";

        for(String grupo: valorGrupos)
        {
            out += getRowLine(data.getFila(grupos.get(grupo).get(0)), colPrinteables, tamaño) + "\n";
        }

        return out;
    }

    /**
     * Crea y devuelve el DataFrame con las nuevas columnas para las operaciones de agrupacion.  
     * 
     * @return Dataframe original sumando las columnas que fueron resultado de la agrupacion 
     */
    public DataFrame unGroup()
    {
        return data.clone();
    }

    @Override
    public GroupBy clone(){
        DataFrame df = data.clone();
        List<String> etiquetas = new ArrayList<>(getGroupedColumns());

        return new GroupBy(df, etiquetas, colAgregadas);
    }

    private NumberColumn getColumnaOperable(String etiqueta)
    {
        if(!data.contieneEtiqueta(etiqueta))
        {
            throw new ColumnaInexistenteException(etiqueta);
        }

        Column col = data.getColumna(etiqueta);
        if(!colNoAgrupadas.contains(etiqueta) || !(col instanceof NumberColumn))
        {
            throw new ColumnaNoOperableException(etiqueta);
        }

        return (NumberColumn) col;
    }

    private NumberColumn<Double> insertarValoresGrupo(NumberColumn<Double> col, List<Integer> indices, Double valor)
    {
        NumberColumn<Double> colInsertada = col.clone();
        for(Integer indice: indices)
        {
            colInsertada.set(indice, valor);
        }

        return colInsertada;
    }

    private <T extends Number> NumberColumn<Double> operacionDeAgregacion(String etiqueta, Function<NumberColumn, T> op)
    {
        NumberColumn col = getColumnaOperable(etiqueta);
        NumberColumn<Double> colOperada = new DoubleColumn(data.cantidadFilas());

        for(String grupo: valorGrupos)
        {
            List<Integer> indicesGrupo = grupos.get(grupo);
            Double val = op.apply((NumberColumn) col.filterByIndex(indicesGrupo)).doubleValue();
            colOperada = insertarValoresGrupo(colOperada, indicesGrupo, val);
        }

        return colOperada;
    }

    public GroupBy mean(String tag)
    {
        Function<NumberColumn, Double> op = (col) -> (col.mean());
        NumberColumn<Double> col = operacionDeAgregacion(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Media: " + tag;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb; 
    }

    public GroupBy median(String tags)
    {
        Function<NumberColumn, Double> op = (col) -> (col.median());
        NumberColumn<Double> col = operacionDeAgregacion(tags, op);
        GroupBy gb = clone();

        String nuevaCol = "Mediana: " + tags;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy max(String tag)
    {
        Function<NumberColumn, Number> op = (col) -> (col.max());
        NumberColumn<Double> col = operacionDeAgregacion(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Maximo: " + tag;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy min(String tag)
    {
        Function<NumberColumn, Number> op = (col) -> (col.min());
        NumberColumn<Double> col = operacionDeAgregacion(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Minimo: " + tag;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy std(String tag)
    {
        Function<NumberColumn, Double> op = (col) -> (col.std());
        NumberColumn<Double> col = operacionDeAgregacion(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "SD: " + tag;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy sum(String tag)
    {
        Function<NumberColumn, Number> op = (col) -> (col.sum());
        NumberColumn<Double> col = operacionDeAgregacion(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Suma: " + tag;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy n()
    {
        NumberColumn<Double> col = new DoubleColumn(data.cantidadFilas());

        for(String grupo: valorGrupos)
        {
            List<Integer> indicesGrupo = grupos.get(grupo);
            Double val = (double) grupos.get(grupo).size();
            col = insertarValoresGrupo(col, indicesGrupo, val);
        }

        IntegerColumn nuevaCol = IntegerColumn.toIntegerColumn(col);

        GroupBy gb = clone();

        String nuevaColName = "n";
        gb.data = gb.data.addColumna(nuevaColName, nuevaCol);
        gb.colAgregadas.add(nuevaColName);
        return gb;
    }

    public GroupBy sort(String tag, boolean ascending)
    {
        GroupBy gb = clone();

        for(String grupo: valorGrupos)
        {
            List<Integer> indexGroup = grupos.get(grupo);
            indexGroup.sort(null);
            
            DataFrame slice = gb.data.getFila(indexGroup);
            
            Map<Integer, Integer> mappedIndexes = new HashMap<>();
            for(int i = 0; i < indexGroup.size(); i++)
            {
                mappedIndexes.put(i, indexGroup.get(i));
            }
            
            Map<Integer, Integer> orderIndex = slice.getColumna(tag).sort(ascending);
            Map<Integer, Integer> newOrder = new HashMap<>();

            for(Map.Entry<Integer, Integer> entry: orderIndex.entrySet())
            {
                newOrder.put(mappedIndexes.get(entry.getKey()), mappedIndexes.get(entry.getValue()));
            }
            gb.data = gb.data.orderByIndex(newOrder);
        }

        return gb;
    }
}

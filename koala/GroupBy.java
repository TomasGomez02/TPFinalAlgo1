package koala;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import koala.utils.InoperableColumnException;
import koala.utils.UnexistentColumnException;

public class GroupBy implements Cloneable
{
    private DataFrame data;
    private Map<String, List<Integer>> groups;
    private Map<String, StringColumn> groupedCols;
    private List<String> ungroupedCols;
    private List<String> aggregatedCols;
    private List<String> groupValues;

    public GroupBy(DataFrame df, List<String> groupedCols)
    {
        this.data = df.clone();
        this.groupedCols = new LinkedHashMap<>();
        
        ungroupedCols = this.data.colNames();
        for(String col: groupedCols)
        {
            this.groupedCols.put(col, StringColumn.toStringColumn(this.data.getCol(col).unique()));
            ungroupedCols.remove(col);
        }
        aggregatedCols = new ArrayList<>();

        groups = generateGroups();
    }

    public GroupBy(DataFrame df, String[] groupedCols)
    {
        this(df, Arrays.asList(groupedCols));
    }

    public GroupBy(DataFrame df, List<String> groupedCols, List<String> aggregatedCols)
    {
        this.data = df.clone();

        
        this.groupedCols = new LinkedHashMap<>();
        
        ungroupedCols = this.data.colNames();
        for(String col: groupedCols)
        {
            this.groupedCols.put(col, StringColumn.toStringColumn(this.data.getCol(col).unique()));
            ungroupedCols.remove(col);
        }
        this.aggregatedCols = new ArrayList<>(aggregatedCols);

        groups = generateGroups();
    }

    private List<String> getGroupedColumns()
    {
        return new ArrayList<>(groupedCols.keySet());
    }

    private Map<String, List<Integer>> generateGroups()
    {
        groupValues = new ArrayList<>();
        Map<String, List<Integer>> grupos = new LinkedHashMap<>();
        for(String grupo: getGroups())
        {
            List<Integer> indices = generateGroupIndexes(grupo);
            if(indices.size() > 0)
            {
                grupos.put(grupo, indices);
                groupValues.add(grupo);
            }
        }

        return grupos;
    }

    private List<Integer> generateGroupIndexes(String group)
    {
        List<Integer> indices = new ArrayList<>();

        for(int i = 0; i < data.nRow(); i++)
        {
            if(belongsToGroup(data.getRow(i), group))
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
            if(!String.valueOf(row.getValue(columnas.get(i), 0)).equals(String.valueOf(valores[i])))
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
        if(groupIndex < groupedCols.size() - 1)
        {
            siguientesGrupos = getGroups(groupIndex + 1);
        }
        
        List<String> cols = getGroupedColumns();
        StringColumn values = groupedCols.get(cols.get(groupIndex));
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
            String celda = String.valueOf(fila.getValue(columnasPrintear.get(i), 0));
            
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
        colPrinteables.addAll(aggregatedCols);
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

        for(String grupo: groupValues)
        {
            out += getRowLine(data.getRow(groups.get(grupo).get(0)), colPrinteables, tamaño) + "\n";
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

        return new GroupBy(df, etiquetas, aggregatedCols);
    }

    private NumberColumn getOperableColumn(String tag)
    {
        if(!data.containsCol(tag))
        {
            throw new UnexistentColumnException(tag);
        }

        Column col = data.getCol(tag);
        if(!ungroupedCols.contains(tag) || !(col instanceof NumberColumn))
        {
            throw new InoperableColumnException(tag);
        }

        return (NumberColumn) col;
    }

    private NumberColumn<Double> insertGroupValues(NumberColumn<Double> col, List<Integer> indexes, Double value)
    {
        NumberColumn<Double> colInsertada = col.clone();
        for(Integer indice: indexes)
        {
            colInsertada.set(indice, value);
        }

        return colInsertada;
    }

    private <T extends Number> NumberColumn<Double> aggregationOperation(String tag, Function<NumberColumn, T> op)
    {
        NumberColumn col = getOperableColumn(tag);
        NumberColumn<Double> colOperada = new DoubleColumn(data.nRow());

        for(String grupo: groupValues)
        {
            List<Integer> indicesGrupo = groups.get(grupo);
            Double val = op.apply((NumberColumn) col.filterByIndex(indicesGrupo)).doubleValue();
            colOperada = insertGroupValues(colOperada, indicesGrupo, val);
        }

        return colOperada;
    }

    /**
     * Calcula y devuelve la media para cada grupo que forma la columna correspondiente a la etiqueta proporcionada.
     * 
     * @param tag columna para la cual se calcula la media
     * @return         nuevo objeto GroupBy que incluye la media calculada para cada grupo como una columna adicional
     */
    public GroupBy mean(String tag)
    {
        Function<NumberColumn, Double> op = (col) -> (col.mean());
        NumberColumn<Double> col = aggregationOperation(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Mean: " + tag;
        gb.data = gb.data.addCol(nuevaCol, col);
        gb.aggregatedCols.add(nuevaCol);
        return gb; 
    }

    /**
     * Calcula y devuelve la mediana para cada grupo que forma la columna correspondiente a la etiqueta proporcionada.
     * 
     * @param tag columna para la cual se calcula la mediana
     * @return         nuevo objeto GroupBy que incluye la mediana calculada para cada grupo como una columna adicional
     */
    public GroupBy median(String tag)
    {
        Function<NumberColumn, Double> op = (col) -> (col.median());
        NumberColumn<Double> col = aggregationOperation(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Median: " + tag;
        gb.data = gb.data.addCol(nuevaCol, col);
        gb.aggregatedCols.add(nuevaCol);
        return gb;
    }

    /**
     * Obtiene y devuelve el valor maximo para cada grupo que forma la columna correspondiente a la etiqueta proporcionada.
     * 
     * @param tag columna para la cual se obtiene el valor maximo
     * @return         nuevo objeto GroupBy que incluye el valor maximo obtenido para cada grupo como una columna adicional
     */
    public GroupBy max(String tag)
    {
        Function<NumberColumn, Number> op = (col) -> (col.max());
        NumberColumn<Double> col = aggregationOperation(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Max: " + tag;
        gb.data = gb.data.addCol(nuevaCol, col);
        gb.aggregatedCols.add(nuevaCol);
        return gb;
    }

    /**
     * Obtiene y devuelve el valor minimo para cada grupo que forma la columna correspondiente a la etiqueta proporcionada.
     * 
     * @param tag columna para la cual se obtiene el valor minimo
     * @return         nuevo objeto GroupBy que incluye el valor minimo obtenido para cada grupo como una columna adicional
     */
    public GroupBy min(String tag)
    {
        Function<NumberColumn, Number> op = (col) -> (col.min());
        NumberColumn<Double> col = aggregationOperation(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Min: " + tag;
        gb.data = gb.data.addCol(nuevaCol, col);
        gb.aggregatedCols.add(nuevaCol);
        return gb;
    }

    /**
     * Calcula y devuelve el desvio estandar para cada grupo que forma la columna correspondiente a la etiqueta proporcionada.
     * 
     * @param tag columna para la cual se calcula el desvio estandar
     * @return         nuevo objeto GroupBy que incluye el desvio estandar calculado para cada grupo como una columna adicional
     */
    public GroupBy std(String tag)
    {
        Function<NumberColumn, Double> op = (col) -> (col.std());
        NumberColumn<Double> col = aggregationOperation(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "STD: " + tag;
        gb.data = gb.data.addCol(nuevaCol, col);
        gb.aggregatedCols.add(nuevaCol);
        return gb;
    }

    /**
     * Calcula y devuelve la suma acumulada para cada grupo que forma la columna correspondiente a la etiqueta proporcionada.
     * 
     * @param tag columna para la cual se calcula la suma acumulada
     * @return         nuevo objeto GroupBy que incluye la suma acumulada calculada para cada grupo como una columna adicional
     */
    public GroupBy sum(String tag)
    {
        Function<NumberColumn, Number> op = (col) -> (col.sum());
        NumberColumn<Double> col = aggregationOperation(tag, op);
        GroupBy gb = clone();

        String nuevaCol = "Sum: " + tag;
        gb.data = gb.data.addCol(nuevaCol, col);
        gb.aggregatedCols.add(nuevaCol);
        return gb;
    }

    /**
     * Devuelve la cantidad de elementos que contiene cada grupo.
     * 
     * @return nuevo objeto GroupBy que incluye la cantidad de elementos para cada grupo como una columna adicional
     */
    public GroupBy n()
    {
        NumberColumn<Double> col = new DoubleColumn(data.nRow());

        for(String grupo: groupValues)
        {
            List<Integer> indicesGrupo = groups.get(grupo);
            Double val = (double) groups.get(grupo).size();
            col = insertGroupValues(col, indicesGrupo, val);
        }

        IntegerColumn nuevaCol = IntegerColumn.toIntegerColumn(col);

        GroupBy gb = clone();

        String nuevaColName = "n";
        gb.data = gb.data.addCol(nuevaColName, nuevaCol);
        gb.aggregatedCols.add(nuevaColName);
        return gb;
    }

    /**
     * Ordena los grupos de las columnas del Dataframe de manera ascendente o descendente segun lo indicado.
     * 
     * @param tag etiqueta de la columna a ordenar
     * @param ascending determina si el ordenamiento sera ascendente o descendente 
     * @return un objeto GroupBy con los grupos ordenados
     */
    public GroupBy sort(String tag, boolean ascending)
    {
        GroupBy gb = clone();

        for(String grupo: groupValues)
        {
            List<Integer> indexGroup = groups.get(grupo);
            indexGroup.sort(null);
            
            DataFrame slice = gb.data.getRow(indexGroup);
            
            Map<Integer, Integer> mappedIndexes = new HashMap<>();
            for(int i = 0; i < indexGroup.size(); i++)
            {
                mappedIndexes.put(i, indexGroup.get(i));
            }
            
            Map<Integer, Integer> orderIndex = slice.getCol(tag).sort(ascending);
            Map<Integer, Integer> newOrder = new HashMap<>();

            for(Map.Entry<Integer, Integer> entry: orderIndex.entrySet())
            {
                newOrder.put(mappedIndexes.get(entry.getKey()), mappedIndexes.get(entry.getValue()));
            }
            gb.data = gb.data.sortByIndex(newOrder);
        }

        return gb;
    }
}

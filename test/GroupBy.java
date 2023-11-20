package test;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Map<String, ColumnaString> colAgrupadas;
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

    public GroupBy(DataFrame df, List<String> colAgrupadas, List<String> colAgregadas)
    {
        this.data = df.clone();

        
        this.colAgrupadas = new LinkedHashMap<>();
        
        colNoAgrupadas = this.data.nombreColumnas();
        for(String col: colAgrupadas)
        {
            this.colAgrupadas.put(col, ColumnaString.toColumnaString(this.data.getColumna(col).unique()));
            colNoAgrupadas.remove(col);
        }
        this.colAgregadas = new ArrayList<>(colAgregadas);

        grupos = generarGrupos();
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

    private String stringRecortado(String stringOriginal, int tamaño)
    {
        String stringNuevo = "";
        char[] charArr = stringOriginal.toCharArray();
        for(int i = 0; i < tamaño - 3; i++)
        {
            stringNuevo += charArr[i];
        }
        return stringNuevo + "...";
    }

    private String getHeaderLine(List<String> columnasPrintear, int[] tamaño)
    {
        String linea = "";
        final String sep = "|";
        int espacioIzq;
        int espacioDer;

        for (int i = 0; i < columnasPrintear.size(); i++)
        {
            int diff = tamaño[i] - columnasPrintear.get(i).length();
            espacioIzq = diff / 2;
            if (diff % 2 == 0)
            {
                espacioDer = espacioIzq;
            } 
            else 
            {
                espacioDer = espacioIzq+1;
            }
            linea += " ".repeat(espacioIzq)+columnasPrintear.get(i)+" ".repeat(espacioDer)+sep;
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
                celda = stringRecortado(celda, tamaño[i]);
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

    public void print()
    {
        System.out.println(toString());
    }

    @Override
    public String toString()
    {
        final String fila = "";
        final List<String> colPrinteables = getColumnasAgrupadas();
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

    public DataFrame unGroup()
    {
        return data.clone();
    }

    @Override
    public GroupBy clone(){
        DataFrame df = data.clone();
        List<String> etiquetas = new ArrayList<>(getColumnasAgrupadas());

        return new GroupBy(df, etiquetas, colAgregadas);
    }

    private ColumnaNum getColumnaOperable(String etiqueta)
    {
        if(!data.contieneEtiqueta(etiqueta))
        {
            throw new ColumnaInexistenteException(etiqueta);
        }

        Columna col = data.getColumna(etiqueta);
        if(!colNoAgrupadas.contains(etiqueta) || !(col instanceof ColumnaNum))
        {
            throw new ColumnaNoOperableException(etiqueta);
        }

        return (ColumnaNum) col;
    }

    private ColumnaNum<Double> insertarValoresGrupo(ColumnaNum<Double> col, List<Integer> indices, Double valor)
    {
        ColumnaNum<Double> colInsertada = col.clone();
        for(Integer indice: indices)
        {
            colInsertada.setCelda(indice, valor);
        }

        return colInsertada;
    }

    private <T extends Number> ColumnaNum<Double> operacionDeAgregacion(String etiqueta, Function<ColumnaNum, T> op)
    {
        ColumnaNum col = getColumnaOperable(etiqueta);
        ColumnaNum<Double> colOperada = new ColumnaDouble(data.cantidadFilas());

        for(String grupo: valorGrupos)
        {
            List<Integer> indicesGrupo = grupos.get(grupo);
            Double val = op.apply((ColumnaNum) col.filtrarPorIndice(indicesGrupo)).doubleValue();
            colOperada = insertarValoresGrupo(colOperada, indicesGrupo, val);
        }

        return colOperada;
    }

    public GroupBy media(String etiqueta)
    {
        Function<ColumnaNum, Double> op = (col) -> (col.media());
        ColumnaNum<Double> col = operacionDeAgregacion(etiqueta, op);
        GroupBy gb = clone();

        String nuevaCol = "Media: " + etiqueta;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy mediana(String etiqueta)
    {
        Function<ColumnaNum, Double> op = (col) -> (col.mediana());
        ColumnaNum<Double> col = operacionDeAgregacion(etiqueta, op);
        GroupBy gb = clone();

        String nuevaCol = "Mediana: " + etiqueta;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy maximo(String etiqueta)
    {
        Function<ColumnaNum, Number> op = (col) -> (col.maximo());
        ColumnaNum<Double> col = operacionDeAgregacion(etiqueta, op);
        GroupBy gb = clone();

        String nuevaCol = "Maximo: " + etiqueta;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy minimo(String etiqueta)
    {
        Function<ColumnaNum, Number> op = (col) -> (col.minimo());
        ColumnaNum<Double> col = operacionDeAgregacion(etiqueta, op);
        GroupBy gb = clone();

        String nuevaCol = "Minimo: " + etiqueta;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy desvioEstandar(String etiqueta)
    {
        Function<ColumnaNum, Double> op = (col) -> (col.desvioEstandar());
        ColumnaNum<Double> col = operacionDeAgregacion(etiqueta, op);
        GroupBy gb = clone();

        String nuevaCol = "SD: " + etiqueta;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy sumaAcumulada(String etiqueta)
    {
        Function<ColumnaNum, Number> op = (col) -> (col.sumaAcumulada());
        ColumnaNum<Double> col = operacionDeAgregacion(etiqueta, op);
        GroupBy gb = clone();

        String nuevaCol = "Suma: " + etiqueta;
        gb.data = gb.data.addColumna(nuevaCol, col);
        gb.colAgregadas.add(nuevaCol);
        return gb;
    }

    public GroupBy cant()
    {
        ColumnaNum<Double> col = new ColumnaDouble(data.cantidadFilas());

        for(String grupo: valorGrupos)
        {
            List<Integer> indicesGrupo = grupos.get(grupo);
            Double val = (double) grupos.get(grupo).size();
            col = insertarValoresGrupo(col, indicesGrupo, val);
        }

        ColumnaInt nuevaCol = ColumnaInt.fromColumnaDouble(col);

        GroupBy gb = clone();

        String nuevaColName = "n";
        gb.data = gb.data.addColumna(nuevaColName, nuevaCol);
        gb.colAgregadas.add(nuevaColName);
        return gb;
    }
}

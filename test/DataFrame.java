package test;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.HashMap;
import java.util.List;

public class DataFrame {
    
    @SuppressWarnings("rawtypes")   // Literalmente es lo que dice
    private Map<String, Columna> data;
    private List<String> etiquetas;
    private Map<String, String> tiposColumna;

    public DataFrame(){
        this.data = new HashMap<>();
    }

    @SuppressWarnings("rawtypes")
    public DataFrame(Map<String, Columna> data, Map<String, String> tiposColumna){
        this.data = data;
        this.tiposColumna = tiposColumna;
        this.etiquetas = data.keySet().stream().toList();
    }

    public List<String> nombreColumnas(){
        return this.etiquetas;
    }

    public int cantidadColumnas(){
        return this.etiquetas.size();
    }

    public int cantidadFilas(){
        return this.data.get(etiquetas.get(0)).length();
    }

    public Map<String, String> tiposColumna(){
        return this.tiposColumna;
    }

    public String toString(){
        return this.data.toString();
    }

    private void printDesde(int indiceInicio, int indiceFinal){
        String fila = "";
        String sep = "|";
        int center = 6;

        int espacioIzq;
        int espacioDer;

        // Esta parte ajusta el center por si la etiqueta es muy larga
        for (String colName : etiquetas){
            if (colName.length()-2 > center){
                center = colName.length();
            }
        }

        // Esta parte es para escribir los nombres de las columnas
        for (String colName : etiquetas){
            espacioIzq = ((center - colName.length()) / 2) + 1;
            if (colName.length()%2==0){
                espacioDer = espacioIzq;
            } else {
                espacioDer = espacioIzq+1;
            }
            fila += " ".repeat(espacioIzq)+colName+" ".repeat(espacioDer)+sep;
        }
        System.out.println(fila);
        System.out.println("-".repeat(etiquetas.size() * (center + 2) + etiquetas.size()));
        fila = "";
        // Esta parte es para escribir los valores
        for (int row=indiceInicio; row < indiceFinal; row++){
            for (String colName : etiquetas) {
                String elem;
                if (getCelda(colName, row) != null){
                    elem = this.getCelda(colName, row).toString();
                } else {
                    elem = "null";
                }
                espacioIzq = ((center - elem.length()) / 2) + 1;
                if (elem.length()%2==0){
                    espacioDer = espacioIzq;
                } else{
                    espacioDer = espacioIzq+1;
                }
                
                fila += " ".repeat(espacioIzq)+elem+" ".repeat(espacioDer)+sep;
            }
            System.out.println(fila);
            fila = "";
        }
    }

    public void head(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        printDesde(0, cantidadFilas);
    }

    public void head(){
        this.head(5);
    }

    public void tail(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        printDesde(this.cantidadFilas() - cantidadFilas, this.cantidadFilas());
    }

    public void tail(){
        this.tail(5);
    }

    public <T> void setCelda(String etiqueta, int indice, T valor){
        this.data.get(etiqueta).setCelda(indice, valor);
    }

    public Object getCelda(String etiqueta, int indice){
        return this.data.get(etiqueta).getCelda(indice);
    }

    public <T> T getCelda(String etiqueta, int indice, Class<T> tipoDato){
        return tipoDato.cast(data.get(etiqueta).getCelda(indice));
    }

    public DataFrame clone(){
        throw new UnsupportedOperationException("Metodo no implementado 'clone'");
    }

    public <T> void añadirColumna(Columna<T> columna){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public Columna getColumna(String etiqueta){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getColumna(String[] etiqueta){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getColumna(List<Integer> etiqueta){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void añadirFila(DataFrame fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getFila(int fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }
    
    public DataFrame getFila(int[] fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getFila(Integer[] fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame getFila(List<Integer>[] fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void eliminarCol(String etiqueta){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void eliminarFila(String fila){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public DataFrame recortar(int indiceInicio, int indiceFinal){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public void ordenar(String etiqueta, boolean creciente){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public <T> DataFrame filtrar(String etiqueta, Predicate<T> filtro){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    public <T> void transformCol(String etiqueta, UnaryOperator<T> filtro){
        throw new UnsupportedOperationException("Metodo no implementado");
    }

}

package test;

import java.util.Map;
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

    public void head(int cantidadFilas){
        if (cantidadFilas > this.cantidadFilas()){
            cantidadFilas = this.cantidadFilas();
        }
        String fila = "";
        String sep = "|";
        int center = 6;

        int espacioIzq;
        int espacioDer;
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
        for (int row=0; row < cantidadFilas; row++){
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

    public void head(){
        this.head(5);
    }

    public void tail(int cantidadFilas){
        throw new UnsupportedOperationException("Metodo no implementado 'tail'");
    }

    public void tail(){
        throw new UnsupportedOperationException("Metodo no implementado 'tail'");
    }

    public <T> void setCelda(String etiqueta, int indice, T valor){
        throw new UnsupportedOperationException("Metodo no implementado 'setCelda'");
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

}

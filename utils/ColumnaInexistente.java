package utils;

public class ColumnaInexistente extends RuntimeException {
    public ColumnaInexistente(){
        super("No existe la Columna");
    }
    public ColumnaInexistente(String colName){
        super("No existe la columna: "+colName);
    }
}

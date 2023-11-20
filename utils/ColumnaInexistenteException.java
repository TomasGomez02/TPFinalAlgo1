package utils;

public class ColumnaInexistenteException extends RuntimeException {
    public ColumnaInexistenteException(){
        super("No existe la Columna");
    }
    public ColumnaInexistenteException(String colName){
        super("No existe la columna: |"+colName + "|");
    }
}

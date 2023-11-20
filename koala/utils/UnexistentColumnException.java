package koala.utils;

public class UnexistentColumnException extends RuntimeException {
    public UnexistentColumnException(){
        super("No existe la Columna");
    }
    public UnexistentColumnException(String colName){
        super("No existe la columna: |"+colName + "|");
    }
}

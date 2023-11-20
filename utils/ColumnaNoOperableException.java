package utils;

public class ColumnaNoOperableException extends RuntimeException 
{
    public ColumnaNoOperableException(){
        super("No se puede operar.");
    }
    public ColumnaNoOperableException(String colName){
        super("No se puede operar la columna: |" + colName + "|");
    }
}

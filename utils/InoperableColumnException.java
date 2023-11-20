package utils;

public class InoperableColumnException extends RuntimeException 
{
    public InoperableColumnException(){
        super("No se puede operar.");
    }
    public InoperableColumnException(String colName){
        super("No se puede operar la columna: |" + colName + "|");
    }
}

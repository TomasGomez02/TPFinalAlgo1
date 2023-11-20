package koala.utils;

public class IllegalCastException extends RuntimeException 
{
    public IllegalCastException(){
        super("No se pudo realizar el casteo.");
    }
    public IllegalCastException(String val, String claseOrigen, String claseDestino){
        super("No se puede castear \"" + val + "\" de " +  claseOrigen + " a " + claseDestino);
    }
}

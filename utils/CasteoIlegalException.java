package utils;

public class CasteoIlegalException extends RuntimeException 
{
    public CasteoIlegalException(){
        super("No se pudo realizar el casteo.");
    }
    public CasteoIlegalException(String val, String claseOrigen, String claseDestino){
        super("No se puede castear \"" + val + "\" de " +  claseOrigen + " a " + claseDestino);
    }
}

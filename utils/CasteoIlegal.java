package utils;

public class CasteoIlegal extends RuntimeException 
{
    public CasteoIlegal(){
        super("No se pudo realizar el casteo.");
    }
    public CasteoIlegal(String val, String claseOrigen, String claseDestino){
        super("No se puede castear \"" + val + "\" de " +  claseOrigen + " a " + claseDestino);
    }
}

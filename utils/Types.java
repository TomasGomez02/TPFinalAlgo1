package utils;

public class Types 
{
    private Types(){}
    
    public static Class evaluarTipo(DataTypes tipo)
    {
        switch(tipo)
        {
            case BOOL:
                return Boolean.class;
            case DOUBLE:
                return Double.class;
            case INT:
                return Integer.class;
            case STRING:
                return String.class;
            default:
                return null;
        }
    }

    public static boolean parseBoolean(String texto) throws NumberFormatException
    {
        if(texto == null)
        {
            throw new NumberFormatException("No se puede convertir " + texto + " en " + Boolean.class);
        }

        switch(texto.toLowerCase())
        {
            case "true":
            case "1":
                return true;
            case "false":
            case "0":
                return false;
            default:
                throw new NumberFormatException("No se puede convertir " + texto + " en " + Boolean.class);
        }
    }
}

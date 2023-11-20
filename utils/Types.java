package utils;

public class Types 
{
    private Types(){}
    
    public static Class evaluarTipo(DataType tipo)
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

    public static int castBoolToInt(boolean bool)
    {
        return bool ? 1 : 0;
    }

    public static double castBoolToDoule(boolean bool)
    {
        return bool ? 1.0 : 0.0;
    }

    public static <T extends Number> boolean numberToBool(T n)
    {
        if(n.intValue() == 1.0)
            return true;
        if(n.intValue() == 0.0)
            return false;

        throw new CasteoIlegalException(n.toString(), Number.class.toString(), Boolean.class.toString());
    }


}

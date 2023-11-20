package utils;

public class Types 
{
    private Types(){}
    
    /**
     * Evalua el tipo de dato y devuelve la correspondiente clase de Java.
     * 
     * @param type tipo de datp a evaluar
     * @return     la clase asociada al tipo de dato. Si el tipo no tiene una correspondencia, devuelve null.
     */
    public static Class evaluateType(DataType type)
    {
        switch(type)
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

    /**
     * Convierte una cadena ingresada a boolean.
     * 
     * @param texto cadena a convertir
     * @return      valor booleano correspondiente a la cadena de texto
     * @throws NumberFormatException si el texto ingresado es nulo
     */
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

    /**
     * Convierte un valor booleano a un valor entero.
     * 
     * @param bool valor booleano a convertir
     * @return     1 si el valor booleano es true, 0 si es false.
     */
    public static int castBoolToInt(boolean bool)
    {
        return bool ? 1 : 0;
    }

    /**
     * Convierte un valor booleano a un valor Double.
     * 
     * @param bool valor booleano a convertir
     * @return      1.0 si el valor booleano es true, 0.0 si es false.
     */
    public static double castBoolToDoule(boolean bool)
    {
        return bool ? 1.0 : 0.0;
    }

    /**
     * Convierte un valor numerico a un valor booleano.
     * 
     * @param <T> tipo de dato del valor ingresado
     * @param n   valor numerico ingresado
     * @return    true si el valor numerico es 1.0, false si es 1.0
     */
    public static <T extends Number> boolean numberToBool(T n)
    {
        if(n.intValue() == 1.0)
            return true;
        if(n.intValue() == 0.0)
            return false;

        throw new IllegalCastException(n.toString(), Number.class.toString(), Boolean.class.toString());
    }


}

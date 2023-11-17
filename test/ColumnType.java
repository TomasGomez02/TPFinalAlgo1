package test;

public class ColumnType 
{
    private ColumnType(){}
    
    enum DataTypes{
        STRING,
        INT,
        DOUBLE,
        BOOL   
    }

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
}

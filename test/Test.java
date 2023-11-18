package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test 
{
    public static void main(String[] args) 
    {
        String nombreArchivo = "datasets/tiny.csv";
        Columna<String> col = new ColumnaString(new String[]{"True", "a", "true"});
        Columna<Boolean> colInt = ColumnaBool.fromColumnaString(col);
        System.out.println(colInt);
    }
}

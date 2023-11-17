package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test 
{
    public static void main(String[] args) 
    {
        String nombreArchivo = "datasets/tiny.csv";
        Columna<String> col = new ColumnaString(new String[]{"1", "2", "3"});
        Columna<Integer> colint = col.castearATipo(ColumnType.DataTypes.INT);

        Class<Integer> clase = Integer.class;

        List<Integer> lis = new ArrayList<>();

        for(int i = 0; i < col.length(); i++)
        {
            lis.add(Integer.parseInt(col.getCelda(i)));
        }

        System.out.println(new ColumnaInt(lis));
    }
}

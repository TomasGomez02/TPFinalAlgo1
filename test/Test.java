package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test 
{
    public static void main(String[] args) 
    {
        String nombreArchivo = "datasets/taylor_all_songs.csv";
        String[] datatypes = "String,".repeat(28).split(",");
        DataFrame df = IOCSV.fromCSV(nombreArchivo, datatypes);

        df.printDesdeHasta(150, 170);;
    }
}

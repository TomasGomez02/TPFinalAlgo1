package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import utils.DataTypes;
import utils.Types;

public class Test 
{
    public static void main(String[] args) 
    {
        // String path = "datasets/tiny.csv";
        // String path = "datasets/taylor_all_songs.csv";
        // String path = "datasets/taylor_albums.csv";
        // String path = "datasets/taylor_album_songs.csv";
        String path = "datasets/mucho_texto.csv";

        // DataFrame df = IOCSV.fromCSV(path, new DataTypes[]{DataTypes.BOOL, 
        //                                                     DataTypes.INT,
        //                                                     DataTypes.INT,
        //                                                     DataTypes.DOUBLE,
        //                                                     DataTypes.INT});
        DataFrame df = IOCSV.fromCSV(path);
        // DataFrame df2 = IOCSV.fromCSV(path2);
        // DataFrame df3 = IOCSV.fromCSV(path3);

        System.out.println(df.tiposColumna());
        IOCSV.toCSV(df, "datasets/test.csv");
        // System.out.println(df.tiposColumna());

        // df2.head();
        // df2.ordenar("user_score", false);
        // System.out.println(df2.tiposColumna());

        // df3.head();
        // System.out.println(df3.tiposColumna());
    }
}

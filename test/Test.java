package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import utils.DataTypes;

public class Test 
{
    public static void main(String[] args) 
    {
        String path = "datasets/taylor_all_songs.csv";
        String path2 = "datasets/taylor_albums.csv";
        String path3 = "datasets/taylor_album_songs.csv";

        DataFrame df = IOCSV.fromCSV(path);
        DataFrame df2 = IOCSV.fromCSV(path2);
        DataFrame df3 = IOCSV.fromCSV(path3);

        df.head();
        System.out.println(df.tiposColumna());

        df2.head();
        System.out.println(df2.tiposColumna());

        df3.head();
        System.out.println(df3.tiposColumna());
    }
}

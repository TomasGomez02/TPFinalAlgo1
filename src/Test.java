package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import utils.DataType;
import utils.Types;

public class Test 
{
    public static void main(String[] args) 
    {
        String path = "datasets/taylor_all_songs.csv";
        // String path = "datasets/taylor_albums.csv";
        // String path = "datasets/taylor_album_songs.csv";
        // String path = "datasets/tiny.csv";
        // String path = "datasets/mucho_texto.csv";

        DataFrame df = IOCSV.fromCSV(path);
        // DataFrame df2 = IOCSV.fromCSV(path2);
        // DataFrame df3 = IOCSV.fromCSV(path3);
        

        // df2.head();
        // df2.ordenar("user_score", false);
        // System.out.println(df2.tiposColumna());

        // df3.head();
        // System.out.println(df3.tiposColumna());

        // GroupBy gb = df.groupBy(new String[]{"ANO4", "cumple_requisito"});

        // System.out.println(gb.getInfoGrupos());
        // gb = gb.cant()
        //         .media("CH06")
        //         .media("ITF")
        //         .maximo("ITF")
        //         .maximo("CH06");

        // gb.print();

        // IOCSV.toCSV(gb.unGroup(), "datasets/ganamos.csv");

        df.removeCol("featuring");
        df.removeCol("bonus_track");
        df.removeCol("promotional_release");
        df.removeCol("single_release");
        df.removeCol("artist");
        
        df.sort(new String[]{"album_name", "key", "danceability"}, true).head(25);

        // df.groupBy(new String[]{"album_name", "track_release"}).print();



        
        // GroupBy gb = df.groupBy("album_name");
        // gb.media("energy").unGroup().getColumna(new String[]{"album_name", "energy", "Media: energy"}).head(25);
    }
}

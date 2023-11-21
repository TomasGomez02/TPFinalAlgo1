package demo;
import java.util.function.Predicate;
import java.util.Scanner;

import koala.*;

public class Demo
{
    public static void main(String[] args) {
        DataFrame df = IOCSV.fromCSV("koala/datasets/taylor_all_songs.csv");
        df.info();

        df = df.groupBy(new String[]{"album_name", "featuring", "bonus_track"}).std("duration_ms").mean("loudness").max("danceability").unGroup();

        Predicate<Double> op = (a) -> (a > 0.5);
        df = df.filter("Max: danceability", op).sort(new String[]{"STD: duration_ms", "Mean: loudness"}, true);

        df = df.getCol(new String[]{"album_name", "featuring", "bonus_track", "duration_ms", "loudness", "danceability", "Max: danceability","STD: duration_ms", "Mean: loudness"});
        df.head(100);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Presiona enter para continuar...");
        scanner.nextLine();

        IOCSV.toCSV(df, "demo.csv");
        scanner.close();
    }
}
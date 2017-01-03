package seleniumwebautomator.utils;

/**
 *
 * @author amoi
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CSVFileUtilities {

    @SuppressWarnings("rawtypes")
    public static ArrayList<String> readData(String delimiter, String filePath) throws Exception {

        String splitBy = delimiter;

        ArrayList<String> data = new ArrayList();

        Scanner scanner = new Scanner(new File(filePath));

        scanner.useDelimiter(",");

        while (scanner.hasNext()) {
            data.add(scanner.next());
        }
        scanner.close();

        return data;
    }

}

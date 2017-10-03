import java.util.Collection;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * An implementation of WordData that assumes that the data are stored in files in Google's 1-gram format (word, year, count)
 *
 * @author Kevin Becker
 */
public class WordDataImpl implements WordData{

    /**
     * Read 1-gram data from a file into an internal data structure for further processing.
     * @param fileName the name of the 1-gram data file.
     * @thows FileNotFoundException if the file cannot be opened.
     */
    public WordDataImpl (String fileName) throws FileNotFoundException
    {
        Scanner in = new Scanner(new File(fileName));
        Map<String, TreeMap<Integer, Integer>> words = new HashMap();
        while(in.hasNextLine())
        {
            String line = in.nextLine();
            String [] tokenized_line = line.split(",\\s*");
        }
    }

    public static void main(String[] args) {}
	    // write your code here
    @Override
    public void dumpData() {

    }
    @Override
    public Collection<String> words() {
        return Arrays.asList( new String[]{"hello"} );
    }
    @Override
    public long totalWords() {
        return 0;
    }
    @Override
    public int getRankFor(String word) {
        return 0;
    }
    @Override
    public int getRankFor(String word, int startYear, int endYear) {
        return 0;
    }
    @Override
    public long getCountFor(String word) {
        return 0;
    }
    @Override
    public long getCountFor(String word, int startYear, int endYear) {
        return 0;
    }
}

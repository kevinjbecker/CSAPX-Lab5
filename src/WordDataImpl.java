import java.util.Collection;
import java.util.Arrays;

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
    public WordDataImpl(String fileName)
    {

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

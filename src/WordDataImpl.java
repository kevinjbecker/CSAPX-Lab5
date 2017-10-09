import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * An implementation of WordData that assumes that the data are stored in files in Google's 1-gram format (word, year, count)
 *
 * @author Kevin Becker
 */
public class WordDataImpl implements WordData
{
    private Map<String, Word> words;


    /**
     * Read 1-gram data from a file into an internal data structure for further processing.
     * @param fileName the name of the 1-gram data file.
     * @throws FileNotFoundException if the file cannot be opened.
     */
    public WordDataImpl (String fileName) throws FileNotFoundException
    {
        Scanner in = new Scanner(new File(fileName));
        words = new HashMap<>();

        while(in.hasNextLine())
        {
            String line = in.nextLine();
            // this is my PHP brain reminding me what each index is.
            // tokenized line: 0 => word, 1 => year, 2 => number of occurrences in year
            String [] tokenized_line = line.split(",\\s*");
            addWordIfNeeded(tokenized_line);
            addYearData(tokenized_line);
        }
    }

    private void addWordIfNeeded(String [] word_line)
    {
        if(words.containsKey(word_line[0]))
            words.put(word_line[0], new Word(word_line[0]));
    }

    private void addYearData(String [] word_line)
    {
        Word word = words.get(word_line[0]);
        word.addDataForYear(Integer.parseInt(word_line[1]), Long.parseLong(word_line[2]));
    }

    public static void main(String[] args)
    {
        try
        {
            WordDataImpl words = new WordDataImpl(args[0]);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Your file cannot be found. Please try again.");
        }
    }

    @Override
    public void dumpData()
    {
        // attempting to use lambda expressions (for learning about them)
        // runs a lambda foreach over the entire words HashMap and dumps each word.
        words.forEach((key, value) -> System.out.println(value.dumpWord()));
    }
    @Override
    public Collection<String> words()
    {
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
    public long getCountFor(String word)
    {
        return 0;
    }
    @Override
    public long getCountFor(String word, int startYear, int endYear)
    {
        return 0;
    }
}

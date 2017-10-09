import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Collectors;

/**
 * An implementation of WordData that assumes that the data are stored in files in Google's 1-gram format (word, year, count)
 *
 * @author Kevin Becker
 */
public class WordDataImpl implements WordData
{
    private Map<String, Word> words;
    private long numWordsReadIn = 0;

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
            ++numWordsReadIn;
            // this is my PHP brain reminding me what each index is.
            // tokenized line: 0 => word, 1 => year, 2 => number of occurrences in year
            String [] tokenized_line = line.split(",\\s*");
            addWordIfNeeded(tokenized_line[0]);
            addYearData(tokenized_line[0], Integer.parseInt(tokenized_line[1]), Long.parseLong(tokenized_line[2]));
        }
    }

    /**
     * Add word if needed is used when the file is being read in.
     * @param word the word that is being added to the HashMap.
     */
    private void addWordIfNeeded(String word)
    {
        if(!words.containsKey(word.toLowerCase()))
            words.put(word, new Word(word));
    }

    /**
     * Adds the year data to the Word. Word adds it to its contained HashMap with the year as the key and the
     * occurrences as the value.
     * @param word the String of the word which is being modified
     * @param year the year that is being added to the Word.
     * @param occurrences the number of occurrences the word appeared in year.
     */
    private void addYearData(String word, int year, long occurrences)
    {
        // We don't have to check if the word exists because no erroneous input will be given to this method
        // It is private and therefore we know when it's going to be called.
        // toLowerCase() is used just in case the data used has variable case words.
        Word getWord = words.get(word.toLowerCase());

        if(!getWord.addDataForYear(year, occurrences))
            System.out.println("Duplicate year was found, the first value is being used.");
    }


    /**
     * Will remove this
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("File read-in begun.");
            WordDataImpl words = new WordDataImpl(args[0]);
            System.out.println("File read-in completed.");
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
        // dumpWord() ends on a new line character so this only needs to be "print" and not "println"
        words.forEach((key, value) -> System.out.print(value));
    }

    @Override
    public Collection<String> words()
    {
        List<String> wordsList = new ArrayList<>();

        words.forEach((key, value) -> wordsList.add(key));

        return wordsList;
    }

    @Override
    public long totalWords()
    {
        // There was no reason to continually have to compute this, so I saved it as a field.
        return numWordsReadIn;
    }

    @Override
    public int getRankFor(String word)
    {
        // Saves compute time if the word isn't in the list.
        // This is the only check we need to perform since we're performing the rank on the entire list.
        if(!words.containsKey(word.toLowerCase()))
            return UNRANKED;

        Map<String, Long> unsortedWordHashMap = new HashMap<>();
        words.forEach((key, value) -> unsortedWordHashMap.put(key, value.getData()));

        List<String> sortedWordKeyList = sortWordHashMap(unsortedWordHashMap);

        // The + 1 at the end is so the rank isn't the index of the item, but rather the correct rank.
        // I.E. the highest ranked word is 1 not 0.
        return sortedWordKeyList.indexOf(word.toLowerCase()) + 1;
    }

    @Override
    public int getRankFor(String word, int startYear, int endYear)
    {
        // Saves compute time if the word isn't in the list.
        // This is the only check we need to perform since we're performing the rank on the entire list.
        if(!words.containsKey(word.toLowerCase()))
            return UNRANKED;

        Map<String, Long> unsortedWordHashMap = new HashMap<>();
        words.forEach((key, value) -> unsortedWordHashMap.put(key, value.getData(startYear, endYear)));

        List<String> sortedWordKeyList = sortWordHashMap(unsortedWordHashMap);

        // The + 1 at the end is so the rank isn't the index of the item, but rather the correct rank.
        // I.E. the highest ranked word is 1 not 0.
        return sortedWordKeyList.indexOf(word.toLowerCase()) + 1;
    }

    private List<String> sortWordHashMap(Map<String, Long> unsortedWordHashMap)
    {
        /* Once the map is sorted then there is no reason to keep their values. The keys which are the words
         * are sorted so the sorted results are dumped to an ArrayList of String objects.
         * The lambda expression words as outlined below:
         *
         * 1. Explodes the unsortedHashMap into an entry set (i.e. makes a set of Map.Entry<K,V> items.
         *
         * 2. Puts the entry set into a stream.
         *
         * 3. The stream is sorted using the comparingByValue() method using Comparator.reverseOrder().
         *    As a note: reverse ordering is used over natural ordering because a word's rank
         *    is determined by its index in the ArrayList. So instead of naturally sorting the words
         *    and then reversing the list after, Comparator's reverse sorting is used.
         *
         * 4. The now sorted set is put into an ArrayList of the keys (which are the words) and returned.
        */
        return new ArrayList<>( unsortedWordHashMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (previous, current) -> previous, LinkedHashMap::new)).keySet() );
    }

    @Override
    public long getCountFor(String word)
    {
        return words.containsKey(word.toLowerCase()) ? words.get(word.toLowerCase()).getData() : 0;
    }

    @Override
    public long getCountFor(String word, int startYear, int endYear)
    {
        return words.containsKey(word) ? words.get(word).getData(startYear, endYear) : 0;
    }
}

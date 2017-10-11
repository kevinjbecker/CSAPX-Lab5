import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * An implementation of WordData that assumes that the data are stored in files in Google's 1-gram format (word, year, count)
 *
 * @author Kevin Becker
 */
public class WordDataImpl implements WordData
{

    private Map<String, Word> words = new HashMap<>();
    private List<String> overallRanks= new ArrayList<>();
    private Collection<String> wordsReadIn = new ArrayList<>();
    private long totalWords = 0;

    /**
     * Read 1-gram data from a file into an internal data structure for further processing.
     * @param fileName the name of the 1-gram data file.
     * @throws FileNotFoundException if the file cannot be opened.
     */
    WordDataImpl (String fileName) throws FileNotFoundException
    {
        // Makes a new Scanner that reads from a new File pointing to fileName
        Scanner in = new Scanner(new File(fileName));

        System.out.println("Reading in file \"" + fileName + "\"...");
        // Continues reading from the file until it has no more lines
        while(in.hasNextLine())
        {
            // This is my PHP brain reminding me what each index is.
            // tokenizedLine: 0 => word, 1 => year, 2 => number of occurrences in year
            // Reads in a line from the file
            String [] tokenizedLine = in.nextLine().split(",\\s*");

            // Adds the word to the HashMap if it is needed
            if(!words.containsKey(tokenizedLine[0].toLowerCase()))
                addWord(tokenizedLine[0].toLowerCase());

            // Add the data of the line being read in to the word
            addYearDataToWord(tokenizedLine[0].toLowerCase(), Integer.parseInt(tokenizedLine[1]), Long.parseLong(tokenizedLine[2]));
        }
        System.out.println("Completed reading in file.");
        // Gets the overall ranks of each word and then sets it to the field
        // This saves compute time when running the zipf command because the rank for the total of each word will
        // already be computed
        this.overallRanks = getOverallRanks();
    }

    /**
     * It seemed like a waste of compute time to continually have to recompute the overall rank when running zipf,
     * so this method gets the overall rank for each word and returns that in a list.
     */
    private List<String> getOverallRanks()
    {
        // Creates a new empty HashMap
        Map<String, Long> unsortedWordHashMap = new HashMap<>();

        // Goes through words and adds each word and the total number of occurrences of the word to the HashMap
        words.forEach((key, value) -> unsortedWordHashMap.put(key, value.getData()));

        // Calls sortWordMap to sort the HashMap and gets the ordered List of keys based on their occurrences
        // Assigns it to the field overallRanks
        return sortWordMap(unsortedWordHashMap);
    }

    /**
     * Add word if needed is used when the file is being read in.
     * @param word the word that is being added to the HashMap.
     */
    private void addWord(String word)
    {
        // Puts the new word into the HashMap with a key of the word and a value of a new word.
        words.put(word, new Word(word));

        // Adds the word to the readWords Collection. Again saves compute time.
        this.wordsReadIn.add(word);
    }

    /**
     * Adds the year data to the Word. Word adds it to its contained HashMap with the year as the key and the
     * occurrences as the value. Method is private so users cannot add their own data that unless in the file.
     * @param word the String of the word which is being modified.
     * @param year the year that is being added to the Word.
     * @param occurrences the number of occurrences the word appeared in year.
     */
    private void addYearDataToWord(String word, int year, long occurrences)
    {
        // We don't have to check if the word exists because no erroneous input will be given to this method
        // It is private and therefore we know when it's going to be called.
        Word getWord = words.get(word);

        // Because the totalWords never changes, we can generate this value when we're reading in the file.
        totalWords += occurrences;

        /*
         * Attempts to add the data to the year, if it fails to add it, that means a duplicate year was
         * provided which isn't allowed
         */
        if(!getWord.addDataForYear(year, occurrences))
            System.out.println("Duplicate year was found, the first value is being used.");
    }


    /**
     * Will remove this.
     * @param args the arguments
     */
    public static void main(String[] args)
    {
        try
        {
            System.out.println("File read-in begun.");
            WordDataImpl words = new WordDataImpl(args[0]);
            System.out.println("File read-in completed.");

            System.out.println(words.getRankFor("atomic"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Your file cannot be found. Please try again.");
        }
    }

    /**
     * The dump data dumps all of the data that was read in to the console.
     */
    @Override
    public void dumpData()
    {
        // The word toString return ends on a new line character so this only needs to be "print" and not "println"
        words.forEach((key, value) -> System.out.print(value));
    }

    /**
     * Collects all of the words into an iterable collection of Strings.
     * @return A Collection of String objects, in this case a List.
     */
    @Override
    public Collection<String> words()
    {
        // Since all of the words are collected already, it saves time by saving the words to a Collection when
        // the program is starting up
        return wordsReadIn;
    }

    /**
     * Gives the total number of words that were initially read in. This is better described as number of lines in the
     * source file since any duplicate words (i.e. word data for different years) are treated as different words.
     * @return A long of the number of words that were read in.
     */
    @Override
    public long totalWords()
    {
        // There was no reason to have to compute this on-the-fly, so it was saved as a field
        return totalWords;
    }

    /**
     * Computes the rank of a word for the entire data set.
     * @param word the word to be looked up.
     * @return An int representing the rank of the word for the entire data set.
     */
    @Override
    public int getRankFor(String word)
    {
        // The + 1 at the end is so the rank isn't the index of the item, but rather the correct rank
        // I.E. the highest ranked word is 1 not 0
        return overallRanks.indexOf(word.toLowerCase()) + 1;
    }

    /**
     * Computes the rank of a word for a given year period in the data set.
     * @param word the word to be looked up.
     * @param year the first year of the range of time desired.
     * @return An int representing the rank of the word for the entire data set.
     */
    @Override
    public int getRankFor(String word, int year)
    {
        // Saves compute time if the word isn't in the list or if its number of occurrences is 0 in the range
        if(!words.containsKey(word.toLowerCase()) || words.get(word.toLowerCase()).getData(year) == 0)
            return UNRANKED;

        // Creates a new empty HashMap
        Map<String, Long> unsortedWordHashMap = new HashMap<>();

        // Goes through words and adds each word and the number of occurrences in the year for each word to the HashMap
        words.forEach((key, value) -> unsortedWordHashMap.put(key, value.getData(year)));

        // Calls sortWordMap to sort the HashMap and gets the ordered List of keys based on their occurrences
        List<String> sortedWordKeyList = sortWordMap(unsortedWordHashMap);

        // The + 1 at the end is so the rank isn't the index of the item, but rather the correct rank
        // I.E. the highest ranked word is 1 not 0
        return sortedWordKeyList.indexOf(word.toLowerCase()) + 1;
    }

    /**
     * Computes the rank of a word for a given year period in the data set.
     * @param word the word to be looked up.
     * @param startYear the first year of the range of time desired.
     * @param endYear the last year of the range of time desired.
     * @return An int representing the rank of the word for the entire data set.
     */
    @Override
    public int getRankFor(String word, int startYear, int endYear)
    {
        // Saves compute time if the word isn't in the list or if its number of occurrences is 0 in the range
        if(!words.containsKey(word.toLowerCase()) || words.get(word.toLowerCase()).getData(startYear, endYear) == 0)
            return UNRANKED;

        // Creates a new empty HashMap
        Map<String, Long> unsortedWordHashMap = new HashMap<>();

        // Goes through words and adds each word and the number of occurrences in the range for each word to the HashMap
        words.forEach((key, value) -> unsortedWordHashMap.put(key, value.getData(startYear, endYear)));

        // Calls sortWordMap to sort the HashMap and gets the ordered List of keys based on their occurrences
        List<String> sortedWordKeyList = sortWordMap(unsortedWordHashMap);

        // The + 1 at the end is so the rank isn't the index of the item, but rather the correct rank
        // I.E. the highest ranked word is 1 not 0
        return sortedWordKeyList.indexOf(word.toLowerCase()) + 1;
    }

    /**
     * This sorts an unsorted HashMap of words and their occurrences into a sorted List of the words based on its
     * occurrences.
     * @param unsortedWordMap the unsorted Map of Word objects.
     * @return A List of Strings representing the sorted words by occurrences.
     */
    private List<String> sortWordMap(Map<String, Long> unsortedWordMap)
    {
        /* The keys which are the words are sorted so the sorted results are dumped to an ArrayList of String objects
         * Once the map is sorted then there is no reason to keep their values which is why a List was preferred
         * The lambda expression words as outlined below:
         *
         * 1. Explodes the unsortedHashMap into an entry set (i.e. makes a set of Map.Entry<K,V> items)
         *
         * 2. Puts the entry set into a stream
         *
         * 3. The stream is sorted using the comparingByValue() and then is reversed
         *    As a note: reverse ordering is used over natural ordering because a word's rank
         *    is determined by its index in the ArrayList. So instead of naturally sorting the words
         *    and then reversing the list after, Comparator's reverse sorting is used
         *
         * 4. The now sorted set is put into an ArrayList of the keys (which are the words) and returned
        */
        return new ArrayList<>( unsortedWordMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (collisionValue1, collisionValue2) -> collisionValue1, LinkedHashMap::new)).keySet() );
    }

    /**
     * Gets the number of times a word appeared for the entire data set.
     * @param word the word to be looked up
     * @return A long-integer representing the number of occurrences of the word.
     */
    @Override
    public long getCountFor(String word)
    {
        // Returns the total number of occurrences for the word in the data set if it exists, otherwise returns 0
        return words.containsKey(word.toLowerCase()) ? words.get(word.toLowerCase()).getData() : 0;
    }

    /**
     * Gets the count of a word for a given single year. (Slightly more efficient than the default).
     * @param word the word to be looked up.
     * @param year the year to be considered.
     * @return A long-integer representing the number of occurrences of the word in the specified year.
     */
    @Override
    public long getCountFor(String word, int year)
    {
        // Returns the total number of occurrences for the word in the year if it exists, otherwise returns 0
        return words.containsKey(word.toLowerCase()) ? words.get(word.toLowerCase()).getData(year) : 0;
    }

    /**
     * Gets the number of times a word appeared for the given year range.
     * @param word the word to be looked up
     * @param startYear the first year of the range of time desired
     * @param endYear the last year of the range of time desired
     * @return A long-integer representing the number of occurrences of the word in the specified time period.
     */
    @Override
    public long getCountFor(String word, int startYear, int endYear)
    {
        // Returns the total number of occurrence for the word in the year range if it exists, otherwise returns 0
        return words.containsKey(word) ? words.get(word).getData(startYear, endYear) : 0;
    }
}

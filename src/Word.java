import java.util.HashMap;
import java.util.Map;

/**
 * The Word class is an extension for the WordData class. It houses a word and each year's data.
 *
 * @author Kevin Becker
 */

public class Word
{
    private Map<Integer, Long> years = new HashMap<>();
    private String word;

    /**
     * Constructs a new Word object.
     * @param word A String which is the word.
     */
    Word(String word)
    {
        this.word = word;
    }

    /**
     * Adds data for a given year that the word has data for.
     * @param year The year which the word was found.
     * @param numberOfOccurrencesOfWord The number of occurrences of the word in year
     * @return true or false; true if the data was added, false if the year already has data.
     */
    boolean addDataForYear(int year, long numberOfOccurrencesOfWord)
    {
        // If there already is data for the year, return false
        if(years.containsKey(year))
            return false;

        // Otherwise we can put this and return true
        years.put(year, numberOfOccurrencesOfWord);
        return true;
    }

    /**
     * Gets the data of the word for all of the years it has data for.
     * @return A long-integer representing the number of occurrences the word had in the entire data set.
     */
    long getCount()
    {
        long totalOccurrences = 0;

        // Performs a foreach loop on each entry summing the total number of occurrences of word
        for(Map.Entry<Integer, Long> entry : years.entrySet())
            totalOccurrences += entry.getValue();

        return totalOccurrences;
    }

    /**
     * Gets the data of the word for all of the years it has data for.
     * @param year The year that should be searched for.
     * @return A long-integer representing the number of occurrences the word had in the entire data set.
     */
    long getCount(int year)
    {
        return years.containsKey(year) ? years.get(year) : 0;
    }

    /**
     * Gets the data of the word in the specified year range.
     * @param startYear The start year to get data for.
     * @param endYear The end year to get data for.
     * @return A long-integer representing the number of occurrences the word had in the year range.
     */
    long getCount(int startYear, int endYear)
    {
        long totalOccurrences = 0;

        // Loops through each year in the specified range and sums the occurrences of word
        for(int year = startYear; year <= endYear; year++)
        {
            // If there is a data-point for the year
            if(years.containsKey(year))
            {
                totalOccurrences += years.get(year);
            }
        }

        return totalOccurrences;
    }

    /**
     * Generates a String which contains each data-point for the word in the style: "word='<em>word</em>', count=<em>count</em>, year=<em>year</em>"
     * @return a String with each data-point of the word.
     */
    @Override
    public String toString()
    {
        String toString = "";

        for(Map.Entry<Integer,Long> year : years.entrySet())
        {
            toString += "word='"+ this.word + "', count=" + year.getValue() + ", year=" + year.getKey() + "\n";
        }

        return toString;
    }

    /*
     * Accessors are not needed for this class. The only operations that need to be done with the fields are completed
     * with the methods.
     */
}

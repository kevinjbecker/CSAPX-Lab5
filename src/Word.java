import java.util.HashMap;
import java.util.Map;

/**
 * The Word class is an extension for the WordData class. It houses a word and each year's data.
 *
 * @author Kevin Becker
 */

public class Word
{
    private Map<Integer, Long> years;
    private String word;

    Word(String word)
    {
        this.word = word;
        years = new HashMap<>();
    }

    boolean addDataForYear(int year, long numberOfOccurrencesOfWord)
    {
        if(years.containsKey(year))
        {
            return false;
        }
        years.put(year, numberOfOccurrencesOfWord);
        return true;
    }

    long getData()
    {
        long totalOccurrences = 0;

        for(Map.Entry<Integer, Long> entry : years.entrySet())
        {
            // System.out.println(entry.getValue());
            totalOccurrences += entry.getValue();
        }
        return totalOccurrences;
    }

    long getData(int startYear, int endYear)
    {
        long totalOccurrences = 0;

        for(int i = startYear; i <= endYear; i++)
        {
            if(years.containsKey(i))
            {
                totalOccurrences += years.get(i);
            }
        }
        return totalOccurrences;
    }

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder();

        years.forEach((key, value) -> toReturn.append("word='"+ this.word + "', count=" + value + ", year=" + key + "\n"));

        return toReturn.toString();
    }

    /* Accessors are not needed for this class. The only operations that need to be done with the fields are completed
     with the methods.*/
}

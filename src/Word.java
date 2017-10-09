import java.util.HashMap;
import java.util.Map;

public class Word
{
    private Map<Integer, Long> years;
    private String word;

    public Word(String word)
    {
        this.word = word;
        years = new HashMap<>();
    }

    public boolean addDataForYear(int year, long numberOfOccurrencesOfWord)
    {
        if(years.containsKey(year))
        {
            return false;
        }
        years.put(year, numberOfOccurrencesOfWord);
        return true;
    }

    public long getData(int year)
    {
        return years.containsKey(year) ? years.get(year) : -1;
    }

    public long getData(int startYear, int endYear)
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

    public String getWord()
    {
        return this.word;
    }

    //not going to include a
}

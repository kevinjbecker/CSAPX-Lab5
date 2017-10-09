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

    public String dumpWord()
    {
        StringBuilder toReturn = new StringBuilder();

        //"word='"+ this.word + "', count=" + value + ", year=" + key + "\n"

        for(Map.Entry<Integer, Long> entry : years.entrySet())
        {
            toReturn.append("word='"+ this.word + "', count=" + entry.getValue() + ", year=" + entry.getKey() + "\n");
        }

        return toReturn.toString();
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

    public long getData()
    {
        long totalOccurrences = 0;

        for(Map.Entry<Integer, Long> entry : years.entrySet())
        {
            System.out.println(entry.getValue());
            totalOccurrences += entry.getValue();
        }
        return totalOccurrences;
    }

    public String getWord()
    {
        return this.word;
    }

    //not going to include a
}


import java.util.Collection;

/**
 * An interface of useful functions for handling data about word use
 * frequency. Each implementing class must declare the source of its
 * data and that source's format, as well as implement the methods
 * herein that process those data once read into memory.
 *
 * @author James Heliotis
 */
public interface WordData {

    /**
     * A constant used by methods that return a ranking when requested
     * to rank a word that is not in the data
     */
    public static final int UNRANKED = 0;

    /**
     * The string to be returned when no string matches the criteria of
     * a method call
     */
    public static final String NO_SUCH_WORD = "**ERROR**";

    /**
     * Print to standard output all of the data that were read in from
     * the source. Each item is printed on a separate line. The format
     * is as follows.
     * <pre>
     *     word='<i>string</i>', count=<i>long-int</i>, year=<i>int</i>
     * </pre>
     * <br>
     * This method is provided mainly for debugging purposes.
     */
    public void dumpData();

    /**
     * What words are in the data?
     * @return an iterable collection of word strings
     */
    public Collection< String > words();

    /**
     * How many words were read in to make the original data?
     * This number is important for determining the frequency
     * of a word.
     * @return the number of words in the data,
     *         <em>counting duplicates as separate words</em>
     */
    public long totalWords();

    /**
     * Get the rank of a word.
     * Most common word gets a rank of 1.
     * @param word the word to be looked up
     * @return the ordinal rank of a word over <em>all</em>
     *         the years mentioned in the data source, or
     *         {@link WordData#UNRANKED}
     *         if the word is not in the data
     */
    public int getRankFor( String word );

    /**
     * Get the rank of a word over a given time periods.
     * Most common word gets a rank of 1.
     * The default implementation just calls the 3-parameter method,
     * but classes can override this to optimize the case of a single year.
     * @param word the word to be looked up
     * @param year the year to be considered
     * @return the ordinal rank of a word during the year, or
     *         {@link WordData#UNRANKED}
     *         if the word is not in the data for that year.
     */
    public default int getRankFor( String word, int year ) {
        return this.getRankFor( word, year, year );
    }

    /**
     * Get the rank of a word over a given time periods.
     * Most common word gets a rank of 1.
     * @param word the word to be looked up
     * @param startYear the first year of the range of time desired
     * @param endYear the last year of the range of time desired
     * @return the ordinal rank of a word over the given range of
     *         years, inclusive, or
     *         {@link WordData#UNRANKED}
     *         if the word is not in the data for those years
     */
    public int getRankFor( String word, int startYear, int endYear );

    /**
     * Get the number of times a word was used considering all years
     * in the data.
     * @param word the word to be looked up
     * @return the number of times the word was used
     */
    public long getCountFor( String word );

    /**
     * Get the number of times a word was used in a given year.
     * The default implementation just calls the 3-parameter method,
     * but classes can override this to optimize the case of a single year.
     * @param word the word to be looked up
     * @param year the year to be considered
     * @return the number of times the word was used during the year
     */
    public default long getCountFor( String word, int year ) {
        return this.getCountFor( word, year, year );
    }

    /**
     * Get the number of times a word was used during a given time period.
     * @param word the word to be looked up
     * @param startYear the first year of the range of time desired
     * @param endYear the last year of the range of time desired
     * @return the number of times the word was used over the given range of
     *         years, inclusive
     */
    public long getCountFor( String word, int startYear, int endYear );

}

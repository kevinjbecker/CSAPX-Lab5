
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Consumer;

/**
 * Test the student lab concerning the application of Zipf's Law to literature.
 *
 * @author James Heliotis
 */
public class WordFreq {

    /**
     * An enumeration for the allowed commands in this test program
     * Data are stored in these objects to allow easy addition of new
     * commands and associated user help.
     */
    public static enum Command {

        zipf(
                "Compute numbers for Zipf's law; x => show table of values.",
                "[ table ]"
        ),
        count(
                "Show #occurrences of a word.",
                "word [ start-year [ end-year ] ]"
        ),
        rank(
                "Show rank for a word.",
                "word [ start-year [ end-year ] ]"
        ),
        test(
                "Run a multifaceted test program (all.csv)",
                ""
        );

        private String hint;
        private String protocol;

        private Command( String hint, String protocol ) {
            this.hint = hint;
            this.protocol = protocol;
        }

        public String getHint() {
            return this.hint;
        }

        public String getProtocol() {
            return this.protocol;
        }

    }

    /**
     * A table mapping command strings to methods
     */
    private static Map< Command, Consumer< String[] > > cmds;

    /**
     * Initialize the table with all commands.
     */
    static {
        cmds = new EnumMap<>( Command.class );
        cmds.put( Command.zipf, WordFreq::checkZipf );
        cmds.put( Command.count, WordFreq::count );
        cmds.put( Command.rank, WordFreq::rank );
        cmds.put( Command.test, WordFreq::bigTest );
    }

    /*****************************************************************/

    /**
     * Compute all the data, in log-log form, that would be useful
     * for plotting a graph to demonstrate Zipf's Law.
     * By default a measure of validity of the law is computed in the
     * mean and standard deviation of the supposed plot points.
     *
     * @param args singleton arg, if present, displays all the data.
     */
    private static void checkZipf( String[] args ) {
        Collection< String > words = allWords.words();
        int size = words.size();
        long[] ranking = new long[ size ];
        double[] diffsInLogs = new double[ size ];
        words.forEach(
                w ->
                    ranking[ allWords.getRankFor( w ) - 1 ] =
                                            allWords.getCountFor( w )
        );
        long word1Count = ranking[ 0 ]; // denominator for freq ratio
        for ( int r = 1; r <= size; ++r ) {
            long freq = ranking[ r - 1 ];
            double logRank = Math.log( (double)r );
            double logFreqRatio = Math.log( (double)freq / (double)word1Count );
            if ( args.length > 1 ) {
                System.out.println(
                        r + ". " + freq + ": " +
                        logRank + ", " + logFreqRatio
                );
            }
            diffsInLogs[ r - 1 ] = logRank + logFreqRatio;
        }
        double mean = Arrays.stream( diffsInLogs ).average().getAsDouble();
        double stdev = Math.sqrt(
                            Arrays.stream( diffsInLogs )
                                 .map( d -> Math.pow( d - mean, 2 ) )
                                 .sum()
                                 / ( size - 1 )
        );
        System.out.println(
                "\n mean difference " + mean +
                "; std. dev. " + stdev
        );
    }

    /*****************************************************************/

    /**
     * How many times was a word used?
     * @param args (optional) starting year, ending year
     */
    private static void count( String[] args ) {
        String word = ( args.length < 2 ) ? "***" : args[ 1 ];
        try {
            switch ( args.length ) {
                case 2:
                    System.out.println(
                            "Count of " + word + ": " +
                            allWords.getCountFor( word ) );
                    break;
                case 3:
                    int year = Integer.parseInt( args[ 2 ] );
                    System.out.println(
                            "Count of " + word + " in " + year + ": " +
                            allWords.getCountFor( word, year ) );
                    break;
                case 4:
                    int year1 = Integer.parseInt( args[ 2 ] );
                    int year2 = Integer.parseInt( args[ 3 ] );
                    System.out.println(
                            "Count of " + word +
                            " from " + year1 + '-' + year2 + ": " +
                            allWords.getCountFor( word, year1, year2 ) );
                    break;
                default:
                    System.out.println( "Incorrect number of arguments" );
            }
        }
        catch( NumberFormatException e ) {
            System.out.println( "Years must be integer values." );
        }
    }

    /*****************************************************************/

    /**
     * What is the frequency rank of a word. (Top rank is 1.)
     * @param args (optional) starting year, ending year
     */
    private static void rank( String[] args ) {
        String word = ( args.length < 2 ) ? "***" : args[ 1 ];
        try {
            switch ( args.length ) {
                case 2:
                    System.out.println(
                            "Rank of " + word + ": " +
                            allWords.getRankFor( word ) );
                    break;
                case 3:
                    int year = Integer.parseInt( args[ 2 ] );
                    System.out.println(
                            "Rank of " + word + " in " + year + ": " +
                            allWords.getRankFor( word, year ) );
                    break;
                case 4:
                    int year1 = Integer.parseInt( args[ 2 ] );
                    int year2 = Integer.parseInt( args[ 3 ] );
                    System.out.println(
                            "Rank of " + word +
                            " from " + year1 + '-' + year2 + ": " +
                            allWords.getRankFor( word, year1, year2 ) );
                    break;
                default:
                    System.out.println( "Incorrect number of arguments" );
            }
        }
        catch( NumberFormatException e ) {
            System.out.println( "Years must be integer values." );
        }
    }

    /*****************************************************************/

    public final static String[] TEST_WORDS = {
            "request", "wandered", "airport", "the", "good",
            "love", "supercalifragilisticexpialidocious"
    };
    public final static String[] TEST_WORDS_2 = {
            "lordship", "computer", "carriage", "steam", "atomic", "video"
    };
    public final static int FIRST_YEAR = 1600;
    public final static int YEAR_SPAN = 100;
    public static final int NEAR_FUTURE = 2050;

    /**
     * A single, long test program that calls lots of WordData methods with
     * various argument values. The values are appropriate for all.csv, and
     * a bit for short.csv, too.
     * @param args unused
     */
    public static void bigTest( String[] args ) {

        for ( int y = 2004; y < 2010; ++y ) {
            System.out.println( "Year " + y );
            for ( String w: TEST_WORDS ) {
                System.out.println(
                        "    " + w + ": " + allWords.getCountFor( w, y ) );
            }
        }
        for ( String w: TEST_WORDS ) {
            System.out.println( "Overall for " + w + ": " +
                                allWords.getCountFor( w, 0, 2100 ) );
        }

        for ( String word: TEST_WORDS ) {
            System.out.println(
                    "Rank of " + word + " is " + allWords.getRankFor( word ) );
        }

        System.out.println( "These should be the top words" );

        for (
                String word: new String[] {
                "the", "and", "a", "for", "as",
                "it", "on", "from", "he", "this"
        }
                ) {
            System.out.println( word + " is #" + allWords.getRankFor( word ) );
        }

        System.out.println();
        for ( String word : TEST_WORDS_2 ) {
            for ( int year = FIRST_YEAR;
                  year < NEAR_FUTURE;
                  year += YEAR_SPAN ) {
                int yearEnd = year + YEAR_SPAN - 1;
                System.out.println(
                        "Rank of " + word + " from " + year +
                        " to " + yearEnd + " was " +
                        allWords.getRankFor( word, year, yearEnd ) );
            }
            System.out.println();
        }
    }

    /*****************************************************************/

    /**
     * A multiline prompt for the user of this test program
     */
    private static String prompt = null;

    /**
     * Build the prompt from information stored with the enumerated
     * type values.
     */
    static {
        prompt = "\n";
        for ( Command c: Command.values() ) {
            prompt += c + " " + c.getProtocol() + '\n';
        }
        prompt += "help\nq\n\n> ";
    }

    /**
     * A loop that requests commands from the user, via the console, and
     * calls the methods that execute them.
     * Command arguments are left to the individual methods to decipher.
     * @param user the input stream (presumably standard input)
     */
    private static void commandCentral( Scanner user ) {
        System.out.print( prompt );
        String[] cmdLine = user.nextLine().split( "\\s" );
        String cmd = ( cmdLine.length == 0 ) ? "" : cmdLine[ 0 ];
        while ( !cmd.equals( "q" ) ) {
            if ( cmd.equals( "help" ) ) {
                System.out.println();
                for ( Command c : Command.values() ) {
                    System.out.printf(
                            "%8s %-35s -- %s\n",
                            c, c.getProtocol(), c.getHint()
                    );
                }
            }
            else {
                try {
                    Command cmdKey = Command.valueOf( cmd );
                    if ( cmds.containsKey( cmdKey ) ) {
                        cmds.get( cmdKey ).accept( cmdLine );
                    }
                    else {
                        throw new IllegalArgumentException();
                    }
                }
                catch( IllegalArgumentException e ) {
                    System.out.println( "No such command '" + cmd + "'." );
                }
            }
            System.out.print( prompt );
            cmdLine = user.nextLine().split( "\\s" );
            cmd = ( cmdLine.length == 0 ) ? "" : cmdLine[ 0 ];
        }
    }

    /*****************************************************************/

    private static WordData allWords = null;

    /**
     * Read in the data file to an internal structure, then process
     * user commands.
     * @param args if present, the name of the data file (User is prompted
     *             if no args.)
     * @throws FileNotFoundException if the file cannot be opened
     */
    public static void main( String[] args ) throws FileNotFoundException {
        try ( Scanner userIn = new Scanner( System.in ) ) {
            String fileName = null;
            if ( args.length == 0 ) {
                System.out.print( "Word data file: " );
                fileName = userIn.nextLine();
            }
            else {
                fileName = args[ 0 ];
            }

            allWords = new WordDataImpl( fileName );

            commandCentral( userIn );
        }
    }

}

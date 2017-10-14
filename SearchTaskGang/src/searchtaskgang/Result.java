package searchtaskgang;

/**
 * @class SearchResult
 *
 * @brief Holds one search result.
 */
public class Result {
    /**
     * The index in the search String where the word that was
     * found.
     */
    public int mIndex;

    /**
     * Create a Result object contains meta-data about a search
     * result..
     */
    public Result(int index) {
        mIndex = index;
    }
}
package mad.database.backend;

/**
 *
 */
public class PageCache {

    private static final int CACHESIZE = 100;

    private final Page[] cache = new Page[CACHESIZE];
    private int size = 0;

    /**
     * Tries finding a page in the cache.
     *
     * @param pageStartPosition the start position of the desired page.
     * @return the desired page if found, otherwise null.
     */
    public Page find(int pageStartPosition) {
        for (int i = 0; i < CACHESIZE && i < size; i++) {
            if (cache[i].getPageStartPosition() == pageStartPosition) {
                // Every time we access a Page it gets moved one step forward
                // in the cache array. This way the most used pages end up
                // first in the array and the least used is swaped out when
                // reading in a new page.
                if (i > 0) {
                    Page tmp = cache[i - 1];
                    cache[i - 1] = cache[i];
                    cache[i] = tmp;
                }
                return cache[i - 1];
            }
        }
        return null;
    }

    /**
     * Add a new Page to the cache.
     *
     * @param page
     */
    public void put(Page page) {
        if (size < CACHESIZE) {
            cache[size] = page;
            size++;
        } else {
            cache[CACHESIZE - 1] = page;
        }
    }

}

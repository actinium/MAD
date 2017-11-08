package mad.database.backend;

/**
 *
 */
public class PageCache {

    private static final int CACHESIZE = 100;
    
    private final Page[] cache = new Page[CACHESIZE];
    
    public Page find(int pageStartPosition){
        for (int i = 0; i < CACHESIZE; i++) {
            if(cache[i].getPageStartPosition() == pageStartPosition){
                if(i>0){
                    Page tmp = cache[i-1];
                    cache[i-1] = cache[i];
                    cache[i] = tmp;
                }
                return cache[i-1];
            }
        }
        return null;
    }
    
    public void put(Page page){
        cache[CACHESIZE-1] = page;
    }
}

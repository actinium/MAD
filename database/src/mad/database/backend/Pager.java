package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import mad.util.Bytes;

/**
 *
 */
public class Pager {

    private final RandomAccessFile dbFile;
    private final PageCache cache;
    private Page currentPage;

    public Pager(File file) throws FileNotFoundException {
        dbFile = new RandomAccessFile(file, "rwd");
        cache = new PageCache();
        currentPage = null;
    }
    
    public void close() throws IOException{
        dbFile.close();
    }

    /**
     * Get a new Page.
     * Returns a page from the free-list or, if the free-list is empty,
     * allocates a new page.
     * 
     * @return the startPosition of the page
     */
    public int newPage() {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    /**
     * Frees a page when it's no longer needed.
     * 
     * @param starPosition the startPosition of the page.
     */
    public void freePage(int starPosition) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    public int readInteger(int filePosition) {
        return Bytes.toInt(readBytes(filePosition, 4));
    }

    public void writeInteger(int filePosition, int number) {
        writeBytes(filePosition, Bytes.fromInt(number),4);
    }

    public float readFloat(int filePosition) {
        return Bytes.toFloat(readBytes(filePosition, 4));
    }

    public void writeFloat(int filePosition, float number) {
        writeBytes(filePosition, Bytes.fromFloat(number),4);
    }

    public boolean readBoolean(int filePosition) {
        return Bytes.toBoolean(readBytes(filePosition, 1));
    }

    public void writeBoolean(int filePosition, boolean bool) {
        writeBytes(filePosition, Bytes.fromBoolean(bool),1);
    }

    public String readString(int fileposition, int length) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    public void writeString(int fileposition, String string) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    private byte[] readBytes(int filePosition, int length) {
        // - is 0 <= filePosition < file-size?
        // - is page == currentPage?
        // - is page in PageCache?
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    private void writeBytes(int filePosition, byte[] bytes, int length) {
        // - is 0 <= filePosition < file-size?
        // - is page == currentPage?
        // - is page in PageCache?
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    /**
     * Calculate the start of the page containing the position.
     *
     * @param position a position in the file.
     * @return the page containing the position.
     */
    private int positionToPageStart(int position) {
        throw new UnsupportedOperationException("Not yet Implemented!");
    }

    /**
     *
     */
    public static class Page {

        private final byte[] data;
        private final int pageStartPosition;

        public Page(int pageStartPosition, byte[] data) {
            super();
            this.pageStartPosition = pageStartPosition;
            this.data = data;
        }

        public int getPageStartPosition() {
            return pageStartPosition;
        }

        public byte[] getBytes(int filePosition, int length) {
            byte[] ret = new byte[length];
            System.arraycopy(data, filePosition - pageStartPosition, ret, 0, length);
            return ret;
        }

        public void putBytes(int filePosition, byte[] bytes, int length) {
            System.arraycopy(bytes, 0, data, filePosition - pageStartPosition, length);
        }
    }

    /**
     *
     */
    public static class PageCache {

        private static final int CACHESIZE = 100;
        private final Pager.Page[] cache = new Pager.Page[CACHESIZE];
        private int size = 0;

        /**
         * Tries finding a page in the cache.
         *
         * @param pageStartPosition the start position of the desired page.
         * @return the desired page if found, otherwise null.
         */
        public Pager.Page find(int pageStartPosition) {
            for (int i = 0; i < CACHESIZE && i < size; i++) {
                if (cache[i].getPageStartPosition() == pageStartPosition) {
                    // Every time we access a Page it gets moved one step forward
                    // in the cache array. This way the most used pages end up
                    // first in the array and the least used is swaped out when
                    // reading in a new page.
                    if (i > 0) {
                        Pager.Page tmp = cache[i - 1];
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
        public void put(Pager.Page page) {
            if (size < CACHESIZE) {
                cache[size] = page;
                size++;
            } else {
                cache[CACHESIZE - 1] = page;
            }
        }
    }

}

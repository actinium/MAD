package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import static mad.database.Config.PAGECACHESIZE;
import static mad.database.Config.PAGESIZE;
import mad.util.Bytes;

/**
 *
 */
public class Pager implements AutoCloseable{

    private static final int firstTablePointerOffset = 0;
    private static final int lastTablePointerOffset = 4;
    private static final int freePagePointerOffset = 8;
    private static final int startOffset = 12;

    private final RandomAccessFile dbFile;

    private final PageCache cache;
    private Page currentPage;
    private int firstTablePointer;
    private int lastTablePointer;
    private int freePagePointer;

    /**
     * 
     * @param file
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public Pager(File file) throws FileNotFoundException, IOException {
        dbFile = new RandomAccessFile(file, "rwd");
        cache = new PageCache();
        currentPage = null;
        byte[] bytes = new byte[4];
        dbFile.read(bytes);
        firstTablePointer = Bytes.toInt(bytes);
        dbFile.read(bytes);
        lastTablePointer = Bytes.toInt(bytes);
        dbFile.read(bytes);
        freePagePointer = Bytes.toInt(bytes);
    }

    /**
     * 
     * @throws IOException 
     */
    @Override
    public void close() throws IOException {
        dbFile.close();
    }

    /**
     * Get a new Page. Returns a page from the free-list or, if the free-list is
     * empty, allocates a new page.
     *
     * @return the startPosition of the page
     */
    public int newPage() throws IOException {
        int firstFreePage = readInteger(freePagePointerOffset);
        if (firstFreePage == 0) {
            firstFreePage = (int) dbFile.length();
            dbFile.setLength(dbFile.length() + PAGESIZE);
        } else {
            int secondFreePage = readInteger(firstFreePage);
            writeInteger(freePagePointerOffset, secondFreePage);
        }
        byte[] data = new byte[PAGESIZE];
        dbFile.seek(firstFreePage);
        dbFile.read(data);
        cache.put(new Page(firstFreePage, data));
        return firstFreePage;
    }

    /**
     * Frees a page when it's no longer needed.
     *
     * @param startPosition the startPosition of the page.
     */
    public void freePage(int startPosition) throws IOException {
        int firstFreePage = readInteger(freePagePointerOffset);
        writeInteger(freePagePointerOffset, startPosition);
        writeInteger(startPosition, firstFreePage);
    }

    /**
     * 
     * @param filePosition
     * @return
     * @throws IOException 
     */
    public int readInteger(int filePosition) throws IOException {
        if (filePosition >= 0 && filePosition < startOffset) { // File Header
            switch (filePosition) {
                case firstTablePointerOffset:
                    return firstTablePointer;
                case lastTablePointerOffset:
                    return lastTablePointer;
                case freePagePointerOffset:
                    return freePagePointer;
            }
        }
        return Bytes.toInt(readBytes(filePosition, 4));
    }

    /**
     * 
     * @param filePosition
     * @param number
     * @throws IOException 
     */
    public void writeInteger(int filePosition, int number) throws IOException {
        writeBytes(filePosition, Bytes.fromInt(number), 4);
        if (filePosition >= 0 && filePosition < startOffset) { // File Header
            switch (filePosition) {
                case firstTablePointerOffset:
                    firstTablePointer = number;
                    break;
                case lastTablePointerOffset:
                    lastTablePointer = number;
                    break;
                case freePagePointerOffset:
                    freePagePointer = number;
                    break;
            }
        }
    }

    /**
     * 
     * @param filePosition
     * @return
     * @throws IOException 
     */
    public float readFloat(int filePosition) throws IOException {
        return Bytes.toFloat(readBytes(filePosition, 4));
    }

    /**
     * 
     * @param filePosition
     * @param number
     * @throws IOException 
     */
    public void writeFloat(int filePosition, float number) throws IOException {
        writeBytes(filePosition, Bytes.fromFloat(number), 4);
    }

    /**
     * 
     * @param filePosition
     * @return
     * @throws IOException 
     */
    public boolean readBoolean(int filePosition) throws IOException {
        return Bytes.toBoolean(readBytes(filePosition, 1));
    }

    /**
     * 
     * @param filePosition
     * @param bool
     * @throws IOException 
     */
    public void writeBoolean(int filePosition, boolean bool) throws IOException {
        writeBytes(filePosition, Bytes.fromBoolean(bool), 1);
    }

    /**
     * 
     * @param filePosition
     * @param length
     * @return
     * @throws IOException 
     */
    public String readString(int filePosition, int length) throws IOException {
        return Bytes.toString(readBytes(filePosition, length));
    }

    /**
     * 
     * @param filePosition
     * @param string
     * @param length
     * @throws IOException 
     */
    public void writeString(int filePosition, String string, int length) throws IOException {
        writeBytes(filePosition, Bytes.fromString(string, length), length);
    }

    /**
     * 
     * @param filePosition
     * @return
     * @throws IOException 
     */
    public byte readByte(int filePosition) throws IOException {
        return readBytes(filePosition, 1)[0];
    }

    /**
     * 
     * @param filePosition
     * @param b
     * @throws IOException 
     */
    public void writeByte(int filePosition, byte b) throws IOException {
        byte[] bytes = {b};
        writeBytes(filePosition, bytes, 1);
    }

    /**
     * 
     * @param filePosition
     * @param length
     * @return
     * @throws IOException 
     */
    public byte[] readBytes(int filePosition, int length) throws IOException {
        int pageStart = positionToPageStart(filePosition);
        Page page;
        if (currentPage != null && pageStart == currentPage.getPageStartPosition()) {
            return currentPage.getBytes(filePosition, length);
        } else if ((page = cache.find(pageStart)) == null) {
            byte[] data = new byte[PAGESIZE];
            dbFile.seek(pageStart);
            dbFile.read(data);
            page = new Page(pageStart, data);
            cache.put(page);
        }
        currentPage = page;
        return page.getBytes(filePosition, length);
    }

    /**
     * 
     * @param filePosition
     * @param bytes
     * @param length
     * @throws IOException 
     */
    public void writeBytes(int filePosition, byte[] bytes, int length) throws IOException {
        if (filePosition >= 0 && filePosition < startOffset) { // File Header
            dbFile.seek(filePosition);
            dbFile.write(bytes, 0, length);
            return;
        }
        int pageStart = positionToPageStart(filePosition);
        Page page;
        if (currentPage != null && pageStart == currentPage.getPageStartPosition()) {
            page = currentPage;
        } else if ((page = cache.find(pageStart)) == null) {
            byte[] data = new byte[PAGESIZE];
            dbFile.seek(pageStart);
            dbFile.read(data);
            page = new Page(pageStart, data);
            cache.put(page);
        }
        page.putBytes(filePosition, bytes, length);
        dbFile.seek(filePosition);
        dbFile.write(bytes, 0, length);
    }

    /**
     * Calculate the start of the page containing the position.
     *
     * @param position a position in the file.
     * @return the page containing the position.
     */
    public int positionToPageStart(int position) {
        return ((position - 12) / PAGESIZE) * PAGESIZE + 12;
    }

    /**
     *
     */
    private static class Page {

        private final byte[] data;
        private final int pageStartPosition;

        /**
         * 
         * @param pageStartPosition
         * @param data 
         */
        public Page(int pageStartPosition, byte[] data) {
            super();
            this.pageStartPosition = pageStartPosition;
            this.data = data;
        }

        /**
         * 
         * @return 
         */
        public int getPageStartPosition() {
            return pageStartPosition;
        }

        /**
         * 
         * @param filePosition
         * @param length
         * @return 
         */
        public byte[] getBytes(int filePosition, int length) {
            byte[] ret = new byte[length];
            System.arraycopy(data, filePosition - pageStartPosition, ret, 0, length);
            return ret;
        }

        /**
         * 
         * @param filePosition
         * @param bytes
         * @param length 
         */
        public void putBytes(int filePosition, byte[] bytes, int length) {
            System.arraycopy(bytes, 0, data, filePosition - pageStartPosition, length);
        }
    }

    /**
     *
     */
    private static class PageCache {

        private final int CACHESIZE = PAGECACHESIZE;
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
                        return cache[i - 1];
                    }
                    return cache[i];
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

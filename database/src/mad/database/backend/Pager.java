package mad.database.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import static mad.database.Config.BYTEORDER;
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

}

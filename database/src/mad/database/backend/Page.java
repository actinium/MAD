package mad.database.backend;

/**
 *
 */
public class Page {

    private final byte[] data;
    private final int pageStartPosition;

    public Page(int pageStartPosition, byte[] data) {
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

    public void putBytes(int filePosition, byte[] bytes) {
        System.arraycopy(bytes, 0, data, filePosition - pageStartPosition, bytes.length);
    }

}

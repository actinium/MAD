# Pager
Pager handles all reads and writes to the database file.

| Functions| |
|----------|-|
| [Pager(...)](#pager-1)|
| [void close()](#close)|
| [int newPage()](#newpage)|[void freePage(...)](#freepage)|
| [int positionToPageStart(...)](#positiontopagestart)|
| [int readInteger(...)](#readinteger)|[void writeInteger(...)](#writeinteger)|
| [float readFloat(...)](#readfloat)|[void writeFloat(...)](#writefloat)|
| [boolean readBoolean(...)](#readboolean)|[void writeBoolean(...)](#writeboolean)|
| [String readString(...)](#readstring)|[void writeString(...)](#writestring)
| [byte readByte(...)](#readbyte)|[void writeByte(...)](#writebyte)|
| [byte[] readBytes(...)](#readbytes)|[void writeBytes(...)](#writebytes)|

## Pager(...)
```Java
public Pager(File file) throws FileNotFoundException, IOException {
```
Constructor of Pager. Uses `file` as storage.

## close()
```Java
public void close() throws IOException {
```
Closes the file Pager use for storage.

## newPage()
```Java
public int newPage() throws IOException {
```
Get a new Page. Returns the position of the first byte of the page in the file.

## freePage(...)
```Java
public void freePage(int startPosition) throws IOException {
```
Return a page you don't need any more. `startPosition` is the position of the first
byte of page you want to return in the file.

## positionToPageStart(...)
```Java
public int positionToPageStart(int position) {
```
Takes a positon and returns the position of the first byte of the page it's in.

## readInteger(...)
```Java
public int readInteger(int filePosition) throws IOException {
```
Reads 4 bytes from `filePosition` and converts them to an integer.

## writeInteger(...)
```Java
public void writeInteger(int filePosition, int number) throws IOException {
```
Writes an integer to `filePosition`.

## readFloat(...)
```Java
public float readFloat(int filePosition) throws IOException {
```
Reads 4 bytes from `filePosition` and converts them to a float.

## writeFloat(...)
```Java
public void writeFloat(int filePosition, float number) throws IOException {
```
Writes a float to `filePosition`.

## readBoolean(...)
```Java
public boolean readBoolean(int filePosition) throws IOException {
```
Reads 1 byte from `filePosition` and converts it to a boolean.

## writeBoolean(...)
```Java
public void writeBoolean(int filePosition, boolean bool) throws IOException {
```
Writes a boolean to `filePosition`.

## readString(...)
```Java
public String readString(int filePosition, int length) throws IOException {
```
Reads `length` bytes from `filePosition` and converts them to a string.

## writeString(...)
```Java
public void writeString(int filePosition, String string, int length) throws IOException {
```
Converts a string to bytes and writes at most `length` bytes of it to `filePosition`.

## readByte(...)
```Java
public byte readByte(int filePosition) throws IOException {
```
Reads one byte from `filePosition`.

## writeByte(...)
```Java
public void writeByte(int filePosition, byte b) throws IOException {
```
Writes one byte to `filePosition`.

## readBytes(...)
```Java
public byte[] readBytes(int filePosition, int length) throws IOException {
```
Reads `length` bytes from `filePosition`.

## writeBytes(...)
```Java
public void writeBytes(int filePosition, byte[] bytes, int length) throws IOException {
```
Writes `length` bytes to `filePosition`.

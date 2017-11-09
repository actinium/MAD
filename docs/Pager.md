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

## writeInteger(...)

## readFloat(...)

## writeFloat(...)

## readBoolean(...)

## writeBoolean(...)

## readString(...)

## writeString(...)

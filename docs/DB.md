# DB
The almighty Database Class.

|Functions||
|-|-|
|[static DB open()](#open)| [void close()](#close)|

# open(...)
```Java
public static DB open(String filename) throws FileNotFoundException, IOException {
```
Opens or creates `filename` and returns a `DB` object.

# close(...)
```Java
public void close() throws IOException {
```
Close down the database.

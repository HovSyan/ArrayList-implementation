import java.util.Collection;
import java.util.Iterator;

public class HList<T> {

  /* -data-
  * Array to store data in it.
  * */
  private Object[] data;

  /* -arrayCapacity-
  * The capacity of data array. This capacity shouldn't
  * be confused with data length(see -size-).
  * */
  private int arrayCapacity;

  /* -size-
  * Count of elements in array. This shouldn't be confused
  * with array total capacity(see -arrayCapacity-)
  * */
  private int size;

  /* -DEFAULT_CAPACITY-
  * Default array capacity. This will be used in case of empty
  * constructor call(see -HList()-).
  * */
  private final int DEFAULT_CAPACITY = 10;

  /* -HList()-
  * An empty constructor. The array(see -data-) initial capacity(see -arrayCapacity-)
  * will be assigned the default value(see -DEFAULT_CAPACITY-).
  * */
  public HList() {
    arrayCapacity = DEFAULT_CAPACITY;
    size = 0;
    data = new Object[arrayCapacity];
  }

  /* -HList(int)-
  * Constructor with initial capacity argument. Throws IllegalArgumentException
  * if the passed argument is less than 0.
  * */
  public HList(int initialCapacity) {
    if(initialCapacity < 0) {
      throw new IllegalArgumentException();
    }

    arrayCapacity = initialCapacity;
    size = 0;
    data = new Object[arrayCapacity];
  }

  /* -HList(Collection<? extends T>)-
  * Constructor with collection argument. The elements of collection will be stored
  * in array(see -data-) in the order of its iterator.
  * */
  public HList(Collection<? extends T> collection) {
    arrayCapacity = collection.size();
    size = collection.size();
    data = new Object[arrayCapacity];
    collection.toArray(data);
  }

  /* -add(T)-
  * Adds specified element to array(see -data-). Note that this method
  * returns void while ArrayList's add(T method returns boolean. This method
  * can change the array's(see -data-) capacity if necessary(see -ensureCapacity()-)
  * */
  public void add(T t) {
    ensureCapacity();

    data[size] = t;
    size++;
  }

  /* -add(int, T)-
  *  Adds specified element to array(see -data-) in specified index. This method
  *  can change the array's(see -data-) capacity if necessary. Note that this method can throw
  *  ArrayOutOfBoundsException if the index argument is not valid.
  * */
  public void add(int index, T t) {
    if(index >= size) {
      throw new ArrayIndexOutOfBoundsException("Index:" + index + ", Size: " + size);
    }

    boolean enoughStorage = checkCapacity();

    if(enoughStorage) {
      Object previous = data[index];
      Object current;
      data[index] = t;
      for(int i = index + 1; i <= size; i++) {
        current = data[i];
        data[i] = previous;
        previous = current;
      }
    }
    else {
      arrayCapacity += (arrayCapacity / 2);
      Object[] newArray = new Object[arrayCapacity];
      for(int i = 0; i < index; i++) {
        newArray[i] = data[i];
      }
      newArray[index] = t;
      for(int i = index + 1; i <= size; i++) {
        newArray[i] = data[i - 1];
      }
      data = newArray;
    }
    size++;
  }

  /* -get(int)-
  * Returns the element from array(see -data-) in specified index. Note that this method throws
  * ArrayOutOfBoundsException if the index argument is not valid.
  * */
  public T get(int index) {
    if(index >= size) {
      throw new ArrayIndexOutOfBoundsException("Index:" + index + ", Size: " + size);
    }

    return (T) data[index];
  }

  /* -addAll(Collection<? extends T>)-
  * Adds all elements of collection to array(see -data-). The order of elements depend on the collection's
  * iterator.
  * */
  public void addAll(Collection<? extends T> collection) {
    Iterator<? extends T> iterator = collection.iterator();
    while (iterator.hasNext()) {
      add(iterator.next());
    }
  }

  /* -addAll(int, Collection<? extends T>)-
  *  Adds all elements of collection to array(see -data-) starting form specified index. The
  *  order of elements depends on the collection's iterator. Note that this method throws
  *  ArrayOutOfBoundsException if the index argument is not valid.
  * */
  public void addAll(int index, Collection<? extends T> collection) {
    if(index >= size) {
      throw new ArrayIndexOutOfBoundsException("Index:" + index + ", Size: " + size);
    }

    boolean enoughStorage = (size + collection.size()) < arrayCapacity;
    Iterator<? extends T> iterator = collection.iterator();
    Object[] newArray = new Object[size + collection.size()];

    if(!enoughStorage) {
      arrayCapacity = size + collection.size();
    }

    for(int i = 0; i < index; i++) {
      newArray[i] = data[i];
    }
    for(int i = index; iterator.hasNext(); i++) {
      newArray[i] = iterator.next();
    }
    for(int i = index + collection.size(); i < newArray.length; i++) {
      newArray[i] = data[i - collection.size()];
    }
    data = newArray;
    size += collection.size();
  }

  /* -clear()-
  * Clears all elements in array(see -data-).
  * */
  public void clear() {
    for(int i = 0; i < size; i++) {
      data[i] = null;
    }
    size = 0;
  }

  /* -contains(Object)-
  *  Returns true if the specified object is equal to some element in array(see -data-)
  *  and false otherwise. The equality is checked with specified equals() method.
  * */
  public boolean contains(Object o) {
    if(o == null) {
      for(int i = 0; i < size; i++) {
        if(data[i] == null) {
          return true;
        }
      }
      return false;
    }
    else {
      for(int i = 0; i < size; i++) {
        if(o.equals(data[i])) {
          return true;
        }
      }
      return false;
    }
  }

  /* -equals(Object)-
  *  See more in super.equals(Object)
  * */
  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }

    if(!(obj instanceof HList)) {
      return false;
    }

    HList<T> other = (HList<T>) obj;

    if(arrayCapacity != ((HList) obj).arrayCapacity) {
      return false;
    }

    if(size != other.size) {
      return false;
    }

    for(int i = 0; i < size; i++) {
      if(!data[i].equals(other.data[i])) {
        return false;
      }
    }

    return true;
  }

  /* -hashCode()-
  * See more in super.hashCode(). This method implementation is written
  * by the recipe in book "Effective Java, 2nd Edition/Chapter 3/Item 9: Always
  * override hashCode when you override equals".
  * */
  @Override
  public int hashCode() {
    int result = 28;

    result = 31 * result + arrayCapacity;
    result = 31 * result + size;

    for(int i = 0; i < size; i++) {
      result = 31 * result + data[i].hashCode();
    }

    return result;
  }

  /* -indexOf(Object)-
  *  Returns the index of an element in array(see -data-) if it is equal to
  *  specified object and -1 if no such an element exists. The equality is checked
  *  with equals() method.
  * */
  public int indexOf(Object o) {
    if(o == null) {
      for (int i = 0; i < size; i++) {
        if (data[i] == null) {
          return i;
        }
      }
      return -1;
    }
    else {
      for(int i = 0; i < size; i++) {
        if (o.equals(data[i])) {
          return i;
        }
      }
      return -1;
    }
  }

  /* -isEmpty()-
  * Checks if the array(see -data-) has any elements.
  * */
  public boolean isEmpty() {
    return size == 0;
  }

  /* -remove(int)-
  * Removes the element in specified index of array(see -data-). If no such an
  * element exists this method does nothing. After removing all elements that were
  * at higher index are shifted to one index below. Note that this method throws
  * ArrayOutOfBoundsException if the argument index is not valid. Note that this
  * method changes element's count(see -size-) but not affects the array's(see -data-)
   * capacity * see (arrayCapacity).
  * */
  public void remove(int index) {
    data[index] = null;
    for(int i = index; i < size - 1; i++) {
      data[i] = data[i + 1];
    }
    data[size - 1] = null;
    size--;
  }

  /* -size()-
  * Returns the element's count(see -size-) which must not be
  * confused with array's(see -data-) capacity(see -arrayCapacity-).
  * */
  public int size() {
    return size;
  }

  /* -set(int, T)-
  * Replaces the element in specified index of array(see -data-) with the
  * specified element passed to argument. The replaced element will be returned.
  * Note that this method throws ArrayOutOfBoundsException if the index argument is not
  * valid.
  * */
  public T set(int index, T newElement) {
    if(index >= size) {
      throw new ArrayIndexOutOfBoundsException("Index:" + index + ", Size: " + size);
    }

    T oldElement = (T) data[index];
    data[index] = newElement;
    return oldElement;
  }

  /* -ensureCapacity()-
  * This method increases the array(see -data-) capacity(see -arrayCapacity-)
  * if necessary. The growth is computed with formula arrayCapacity *= (3 / 2) + 1.
  * This method can be called from adding method(see -add(T)-).
  * */
  private void ensureCapacity() {
    if(size == arrayCapacity) {
      arrayCapacity = (arrayCapacity * 3) / 2 + 1;
      Object[] newArray = new Object[arrayCapacity];
      for(int i = 0; i < size; i++) {
        newArray[i] = data[i];
      }
      data = newArray;
    }
  }

  /* -checkCapacity()-
  * Checks if there is free place in array(see -data-).
  * */
  private boolean checkCapacity() {
    return size < arrayCapacity;
  }

  /* -getObjectState-
  * Prints all significant fields of object to console. This method must be
  * used only for study purposes and must not be used in everywhere else. Prints:
  * arrayCapacity, size, toString()
  * */
  public void printObjectState() {
    System.out.print("arrayCapacity: " + arrayCapacity + ", ");
    System.out.println("size " + size);

    System.out.println(toString());
  }

  /* -toString()-
  * See more in super.toString(). The syntax of returned String is
  * elements toString() method split by commas(',');
  * */
  @Override
  public String toString() {
    String s = "";
    for(int i = 0; i < size; i++) {
      s += data[i].toString() + " ";
    }

    return s;
  }
}

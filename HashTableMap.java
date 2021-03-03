// --== CS400 File Header Information ==--
// Name: Rudy Banerjee
// Email: ajbanerjee@wisc.edu
// Team: Red
// Group: KF
// TA: Keren Chen
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashTableMap <KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  LinkedList<Node> list;
  LinkedList<Node>[] array;
  int capacity;
  int size;
  
  private class Node {
    private KeyType key;
    private ValueType value;
    
    public Node(KeyType k, ValueType v) {
      key = k;
      value = v;
    }
    
    public KeyType getKey() {
      return this.key;
    }
    
    public ValueType getValue() {
      return this.value;
    }
  }
  
  
  /**
   * HashTableMap fieldless constructor: Instantiates a LinkedList<Node> array with
   * a capacity of 10 elements and a size of 0.
   */
  public HashTableMap() {
    capacity = 10;
    array = (LinkedList<Node> []) new LinkedList<?>[10];
    //list = new LinkedList<Node>();
    size = 0;
    
    // HashTableMap(10)
  }
  
  /**
   * HashTableMap capacity constructor: Instantiates a LinkedList<Node> array with a
   * given capacity.
   * @param capacity - size of initial LinkedList<Node> array
   */
  public HashTableMap(int capacity) {
    this.capacity = capacity;
    array = (LinkedList<Node> []) new LinkedList<?>[capacity];
    //list = new LinkedList<Node>();
    size = 0;
  }
  
  // Do we need two arrays, 1 for keys and 1 for values?
  // How else are we going to rehash all the keys/value pairs without knowing the keys?
  
  // hashes a key to an integer using .hashCode()
  // method to hash a key and put in proper position in array
  // get array position %capacity
  
  /**
   * Hashes a key to an integer using the .hashCode() method modulo capacity
   * to find the proper position for the key/value pair in the LinkedList<Node> array
   * 
   * @param key - specified key to be hashed
   * @return array position for given key
   */
  public int hash(KeyType key) {
    return Math.abs(key.hashCode()) % capacity;
  }
  
  
  // method to resize and rehash array
  // create new array of double capacity length
  // if load factor is greater than 0.85 (size/capacity)
  
  // How will we resize the array if the program is using LinkedLists for chaining?
  // That is an important detail
  // Apparently, it doesn't matter because only the raw number of elements put into
  // the HashTableMap determines the size of the array, and it doesn't matter if
  // all the elements hash to one specific LinkedList position because as soon as the
  // size hits the threshold of the load factor, then it will resize and rehash.
  
  // I see. Apparently, we are storing key-value pairs together in the HashMap.
  // That makes more sense. How are we doing that though?
  // Are we designing out own pair class?
  
  /**
   * Creates a new LinkedList<Node> array of double size and rehashes all of
   * the elements in the previous array to the new array.
   */
  public void rehash() {
    //LinkedList<KeyType> newKeys = new LinkedList<KeyType>();
    // ^^ Actually, I don't think this is necessary
    LinkedList<Node>[] newArray =
        (LinkedList<Node> []) new LinkedList<?>[2*capacity];

    // Is load factor even necessary for the resizing operation itself
    // or is it only necessary for figuring out when to call the resize operation?
    
    // what happens next? There has to be a for loop or something
    // loop through the 'key' list and rehash with new capacity
    int oldCapacity = capacity;
    capacity*= 2;
    for (int i = 0; i < oldCapacity; i++) {
      if (array[i] != null) {
        for (Node element : array[i]) {
          // There was an error, where form some reason, the Node class returned an
          // Object type for .getKey() or something. I fixed it by moving the Node class
          // inside the HashTableMap class and that fixed it.
          int pos = hash(element.getKey());
          if (newArray[pos] == null) newArray[pos] = new LinkedList<Node>();
          
          // Ok, I just realized I needed to implement a Node class that contains
          // both the key and value pairs. That makes everything else work.
          // Handling collisions only happens in one specific LinkedList position
          // in the array, and I don't need to try identifying the corresping keys or values
          // for doing anything.
          
          newArray[pos].add(element);
        }
      }
    }
    array = newArray;
  }
  
  // get hash code of key
  
  // Are we supposed to have 2 LinkedList arrays so that we can search up
  // duplicate key/value pairs?
  // Suppose we have a collision, and we have to check if there is a duplicate.
  // What we can do is cycle through the LinkedList and see if there are
  // duplicate 'value' entries.
  // In that case, we don't need to keep the positions of the 'key' entries/
  // What about if there are duplicate 'key' values? That is the main problem.
  // Suppose we have a collision.
  // I think the most practical solution right now is to just run through the LinkedList
  // of keys to check for duplicates, because I don't have a better way of doing
  // something faster than O(n) for handling collisions in general.
  
  /**
   * Hashes key and places it in a designated Linkedlist<Node> array position.
   * Rehashes and doubles array size if load factor is >= 0.85
   * 
   * @return Returns true if element is placed in the proper place without problems
   * and returns false if the key is null or a duplicate key
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    // TODO Auto-generated method stub
    // check for null key
    if (key == null) return false;
    Node element = new Node(key, value);
    int pos = hash(key);
    // check for open spot
    if (array[pos] == null) {
      array[pos] = new LinkedList<Node>();
    } else if (array[pos].size() != 0) {
    // check for duplicate key
      for (Node e : array[pos]) {
        if (e.getKey().equals(key)) return false;
      }
    }
    
    // no duplicate key, add element
    //list.add(element);
    array[pos].add(element);
    size++;
    
    if ((size)/(capacity*1.0) >= 0.85) {
      rehash();
    }
    
    return true;
  }

  /**
   * Hashes the given key and returns the corresponding value.
   * 
   * @param key - specified key of target key/value pair
   * @return specified value of target key/value pair
   * @throws NoSuchElementException if key is null or key is nonexistent 
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    // TODO Auto-generated method stub
    if (key == null) throw new NoSuchElementException();
    
    int pos = hash(key);
    if (array[pos] == null) {
      throw new NoSuchElementException();
    } else if (array[pos].size() == 0) {
      throw new NoSuchElementException();
    } else {
      // Either only 1 value in linked list
      if (array[pos].size() == 1) {
        return array[pos].get(0).getValue();
      } else {
        // or multiple values in linked list
        for (Node element : array[pos]) {
          if (element.getKey().equals(key)) return element.getValue();
        }
        throw new NoSuchElementException();
      }
    }
    
    //return null;
  }

  /**
   * Returns the number of elements in the HashTableMap
   * 
   * @return number of elements in the LinkedList<Node> array
   */
  @Override
  public int size() {
    // TODO Auto-generated method stub
    return size;
  }

  /**
   * Checks to see if the key/value pair of the given key exists in the HashTableMap
   * 
   * @param key - key of target key/value pair
   * @return True if key/value pair exists in the LinkedList<Node> array
   * and false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    // TODO Auto-generated method stub
    if (key == null) return false;
    if (size == 0) return false;
    
    int pos = hash(key);
    // if value is null
    if (array[pos] == null) {
      return false;
    } else if (array[pos].size() == 0) {
      return false;
    } else {
      // Either only 1 value in linked list
      // or multiple values in linked list
      for (Node element : array[pos]) {
        if (element.getKey().equals(key)) return true;
      }
      return false;
    }
    
    //return false;
  }

  /**
   * Removes target key/value pair from the HashTableMap
   * 
   * @param key of corresponding key/value pair
   * @return value of corresponding key/value pair if it is successfully removed
   * and null otherwise
   */
  @Override
  public ValueType remove(KeyType key) {
    // TODO Auto-generated method stub
    // check for null key
    if (key == null) return null;
    if (size == 0) return null;
    int pos = hash(key);
    // check for null spot
    if (array[pos] == null) {
      return null;
    } else if (array[pos].size() == 0) {
      return null;
    } else {
    // check for key and remove it
      for (int i = 0; i < array[pos].size(); i++) {
        if (array[pos].get(i).getKey().equals(key)) {
          size--;
          return array[pos].remove(i).getValue();
        }
      }
    }
    
    // Are we rehashing down? I don't think so because there is nothing in the
    // specifications that specifies that.
    /*
    if ((size+1)/(capacity*1.0) >= 0.85) {
      rehash();
    }
    */
    return null;
  }

  /**
   * Resets the HashmapTable to a blank LinkedList<Node> array of capacity 10
   */
  @Override
  public void clear() {
    // TODO Auto-generated method stub
    capacity = 10;
    array = (LinkedList<Node> []) new LinkedList<?>[10];
    //list = new LinkedList<Node>();
    size = 0;
  }
  
  public static void main(String[] args) {
    System.out.println(test1());
    System.out.println(test2());
    System.out.println(test3());
    System.out.println(test4());
    System.out.println(test5());
    
    /*
    HashTableMap map = new HashTableMap();
    
    map.put("Rudy", "First");
    map.put("Jakob", "Second");
    map.put("Arnav", "Third");
    
    map.put("Rudy", "Fourth");
    
    System.out.println(map.size());
    
    System.out.println(map.get("Rudy"));
    System.out.println(map.get("Jakob"));
    System.out.println(map.get("Arnav"));
    
    System.out.println(map.remove("Arnav"));
    System.out.println(map.remove("Jakob"));
    System.out.println(map.remove("Rudy"));
    
    System.out.println(map.remove("Rudy"));
    
    System.out.println();
    
    map.clear();
    
    System.out.println(new Integer(1).hashCode() % 10);
    System.out.println(new Integer(11).hashCode() % 10);
    
    map.put(1, "1");
    map.put(11, "11");
    
    System.out.println(map.get(1));
    System.out.println(map.get(11));
    */
  }
  
  /**
   * Tests HashTableMap default constructor and put method.
   * - Does the HashTableMap constructor work?
   * - Does the put method work?
   * - Does the rehash method work?
   * - Does the HashTableMap constructor work with a custom capacity?
   * @return True if no errors with HashTableMap default constructor or put method
   * and false if otherwise
   */
  public static boolean test1() {
    try {
      HashTableMap map = new HashTableMap();
      
      if (!map.put("Rudy", "First")) return false;
      if (!map.put("Jakob", "Second")) return false;
      if (!map.put("Arnav", "Fourth")) return false;
      
      if (map.put("Rudy", "Fourth")) return false;
      
      if (map.put(null, "Fourth")) return false;
      
      int num = 234;
      for (int i = num; i < num+10; i++) {
        if (!map.put("" + i, (i + 10) + "")) return false;
      }
      
      HashTableMap map2 = new HashTableMap(5);
      
      if (!map2.put("Rudy", "First")) return false;
      if (!map2.put("Jakob", "Second")) return false;
      if (!map2.put("Arnav", "Third")) return false;
      if (!map.put("Badger", "Fourth")) return false;
      if (!map.put("Wisconsin", "Fifth")) return false;
      if (!map.put("Hippo", "Sixth")) return false;
      
      return true;
    } catch (Exception e) {
      System.out.println(e.fillInStackTrace());
      return false;
    }
  }
  
  /**
   * Tests HashTableMap get method
   * - Does the get method work normally?
   * - Does the get(null) throw a NoSuchElementException?
   * - Does the get() method throw a NoSuchElementException if getting a key
   * that is not in the array?
   * @return True if no errors with get method and false if otherwise
   */
  public static boolean test2() {
    int correct = 0;
    
    HashTableMap map = new HashTableMap();
    
    try {
      
      
      if (!map.put("Rudy", "First")) return false;
      if (!map.put("Jakob", "Second")) return false;
      if (!map.put("Arnav", "Fourth")) return false;
      
      if (map.get("Rudy").equals("First")) correct++;
      
      map.get(null);
      
      
    } catch (NoSuchElementException no) {
      correct++;
    } catch (Exception e) {
      return false;
    }
    
    try {
      map.get("Mit");
    } catch (NoSuchElementException no) {
      correct++;
    } catch (Exception e) {
      return false;
    }
    
    if (correct == 3) return true;
    else return false;
  }
  
  /**
   * Tests HashTableMap contains method
   * - Does the contains method work normally?
   * - Does contains(null) return false?
   * - Does the contains() method return false if getting a key not in the array?
   * @return True if no errors with contains method and false if otherwise
   */
  public static boolean test3() {
    int correct = 0;
    
    HashTableMap map = new HashTableMap();
    
    try {
      
      
      if (!map.put("Rudy", "First")) return false;
      if (!map.put("Jakob", "Second")) return false;
      if (!map.put("Arnav", "Fourth")) return false;
      
      if (map.containsKey("Rudy")) correct++;
      if (!map.containsKey("Ratheon")) correct++;
      if (!map.containsKey(null)) correct++;
      
    } catch (Exception e) {
      System.out.println(e);
      return false;
    }
    
    if (correct == 3) return true;
    else return false;
  }
  
  /**
   * Tests HashTableMap remove method
   * - Does the remove method work normally?
   * - Does remove(null) return null?
   * - Does the remove() method return null if getting a key not in the array?
   * @return True if no errors with remove method and false if otherwise
   */
  public static boolean test4() {
    int correct = 0;
    
    HashTableMap map = new HashTableMap();
    
    try {
      
      
      if (!map.put("Rudy", "First")) return false;
      if (!map.put("Jakob", "Second")) return false;
      if (!map.put("Arnav", "Fourth")) return false;
      
      if (map.remove("Rudy").equals("First")) correct++;
      if (map.remove("Rudy") == null) correct++;
      if (map.remove(null) == null) correct++;
      
    } catch (Exception e) {
      //System.out.println(e);
      return false;
    }
    
    if (correct == 3) return true;
    else return false;
  }
  
  /**
   * Tests HashTableMap size and clear methods
   * - Does the size method work after adding elements?
   * - Does the size method work after removing elements?
   * - Does the clear method work normally?
   * @return True if no errors with size and clear methods and false if otherwise
   */
  public static boolean test5() {
    int correct = 0;
    
    HashTableMap map = new HashTableMap();
    
    try {
      
      
      if (!map.put("Rudy", "First")) return false;
      if (!map.put("Jakob", "Second")) return false;
      if (!map.put("Arnav", "Fourth")) return false;
      
      if (map.size() == 3) correct++;
      
      map.remove("Rudy");
      
      if (map.size() == 2) correct++;
      
      map.clear();
      
      if (map.size() == 0) correct++;
      
    } catch (Exception e) {
      return false;
    }
    
    if (correct == 3) return true;
    else return false;
  }

}

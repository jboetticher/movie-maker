// --== CS400 File Header Information ==--
// Name: <Kenneth Diao>
// Email: <kdiao2@wisc.edu>
// Team: <Red>
// Group: <KF>
// TA: <Keren Chen>
// Lecturer: <Gary Dahl>
// Notes to Grader: I created an extra class HashNode to contain the Keys and Values. 
// 					I also typecast the getKey() and getValue() from returning Object to returning KeyType and ValueType respectively
// 					because I didn't see another way of having them properly return KeyType and ValueType instead of Object

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
	private int capacity;
	private LinkedList<HashNode> hashArray[];
	private final double LOAD_FACTOR = 0.85;
	public HashTableMap(int capacity) {
		this.capacity = capacity;
		hashArray = (LinkedList<HashNode>[]) new LinkedList[this.capacity]; // with customizable capacity
	}
	
	public HashTableMap() { // with default capacity = 10 
		capacity = 10;
		hashArray = (LinkedList<HashNode>[]) new LinkedList[capacity];
	}
	
	/**
	 * method getLoad()
	 * helps to get the current load factor of hashArray
	 * @return double value representing the current load factor
	 */
	public double getLoad() {
		return 1.0*(size()/(1.0*hashArray.length)); // divide the number of objects in hashArray by its current capacity
	}
	
	/**
	 * helper method helpResize()
	 * helps to double the size of the array and input all the values into the new array
	 */
	private void helpResize() {
		LinkedList<HashNode> helper = new LinkedList<HashNode>();
		for(int i = 0; i < hashArray.length; i++) {
			while(hashArray[i] != null && hashArray[i].size() > 0) {
				helper.add(hashArray[i].remove()); // transfer all the objects from hashArray to helper
			}
		}
		hashArray = (LinkedList<HashNode>[]) new LinkedList[hashArray.length * 2];
		for(int i = 0; i < helper.size(); i++) {
			// I won't need to loop back more than once because there will be at most
			// 0.425 * hashArray.length items to add to the new hashArray
			put((KeyType) helper.get(i).getKey(), (ValueType) helper.get(i).getValue());
		}
	}
	
	/**
	 * method resize()
	 * uses helpGetLoad() and helpResize() to check if the array needs to be resized and to resize it if it needs resizing
	 * @return true if the array was resized, false otherwise
	 */
	public boolean resize() {
		// resize() will be called after every addition, but will only act if the condition below is met
		if(getLoad() >= LOAD_FACTOR) {
			helpResize();
			return true;
		}
		return false;
	}
	
	/**
	 * method put()
	 * @param key is the key part of the key-value pair to be added; it determines the placement of the value
	 * @param value is the value part of the key-value pair to be added
	 * adds the key-value pair to hashArray if the key is not null, the value is not null, and there is not currently an equivalent value in the array
	 * @return whether or not the key-value pair was added successfully
	 */
	public boolean put(KeyType key, ValueType value) {
		if(key == null) {
			return false;
		}
		int index = Math.abs(key.hashCode()) % hashArray.length;
		if(hashArray[index] == null) { // create a new LinkedList and add the first value to it
			HashNode<KeyType, ValueType> n = new HashNode<KeyType, ValueType>(key, value);
			hashArray[index] = new LinkedList<HashNode>();
			hashArray[index].add(n);
			resize(); 
			return true;
		}
		else if(hashArray[index] != null) { 
			for(int i = 0; i < hashArray[index].size(); i++) {
				if(((KeyType)hashArray[index].get(i).getKey()).equals(key)) { // check if it's already in the LinkedList
					return false;
				}
			}
			HashNode<KeyType, ValueType> n = new HashNode<KeyType, ValueType>(key, value);
			hashArray[index].add(n); // add the value to a preexisting LinkedList
			resize();
			return true;
		}
		else { //key did not map to an index
			return false; 
		}
	}
	
	/**
	 * method get()
	 * @param key gets the value at a location in the array specified by the key
	 * @throws NoSuchElementException if the key does not map to an index in the array
	 * the purpose is to get a value at the index specified by the key
	 * @return a value at the index in the array specified by the key
	 */
	public ValueType get(KeyType key) throws NoSuchElementException{
		int index = Math.abs(key.hashCode()) % hashArray.length;
		if(hashArray[index] == null) {
			throw new NoSuchElementException(); // so I don't get another exception that I'm not expecting (in the case of there being no values at this index)
		}
		for(int i = 0; i < hashArray[index].size(); i++) {
			if(((KeyType) hashArray[index].get(i).getKey()).equals(key)) { // search through the linkedList for a match
				return (ValueType) hashArray[index].get(i).getValue();
			}
		}
		throw new NoSuchElementException(); // no match was found
	}
	
	/**
	 * method size()
	 * calculates the number of objects in hashArray
	 * @return the number of objects in hashArray
	 */
	public int size() {
		int counter = 0;
		for(int i = 0; i < hashArray.length; i++) {
			if(hashArray[i] != null) { // for every linkedList in the array, check how many objects are in the list
				counter = counter + hashArray[i].size();
			}
		}
		return counter;
	}
	
	/**
	 * method containsKey()
	 * @param key is the key we are looking for in hashArray
	 * the purpose is to find whether or not hashArray contains a value with a key that is equal to or maps to the same index as the key being put in
	 * @return true if the above condition is met, false otherwise
	 */
	public boolean containsKey(KeyType key) {
		int index = Math.abs(key.hashCode()) % hashArray.length; // I did this so I wouldn't have to search through every index, only the one the key might be in
		if(hashArray[index] != null) {
			for(int j = 0; j < hashArray[index].size(); j++) { // Search through the linkedList for the key
				if(((KeyType)hashArray[index].get(j).getKey()).equals(key)) {
					return true;
				}
			}
		}
		return false; // either the param key was null or there was no equivalent key in the hashArray
	}
	
	/**
	 * method remove()
	 * @param key is the key that maps to the index at which we want to remove a value
	 * the purpose is to return and remove the first value at an index corresponding to the key that was input
	 * @return the value removed or null if no such value was found
	 */
	public ValueType remove(KeyType key) {
		if(key == null || !containsKey(key)) {
			return null;
		}
		int index = Math.abs(key.hashCode()) % hashArray.length;
		ValueType val = get(key); // first, get the value, then remove it, then return
		hashArray[index].remove();
		return val;
	}
	
	/**
	 * method clear()
	 * the purpose is to clear the hashArray and return it to its original capacity
	 */
	public void clear() {
		hashArray = (LinkedList<HashNode>[]) new LinkedList[capacity]; // returns array to original capacity and clears all contents in constant time
	}
}

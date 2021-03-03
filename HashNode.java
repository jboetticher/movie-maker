// --== CS400 File Header Information ==--
// Name: <Kenneth Diao>
// Email: <kdiao2@wisc.edu>
// Team: <Red>
// Group: <KF>
// TA: <Keren Chen>
// Lecturer: <Gary Dahl>
// Notes to Grader: <optional extra notes>

import java.util.NoSuchElementException;
import java.util.LinkedList;

public class HashNode<KeyType, ValueType>{
	private KeyType key;
	private ValueType value;
	public HashNode(KeyType k, ValueType v) {
		key = k;
		value = v;
	}
	public KeyType getKey() {
		return key;
	}
	public ValueType getValue() {
		return value;
	}
}

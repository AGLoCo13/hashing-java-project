// **********************************************************************
// ***  HAROKOPIO UNIVERSITY - INFORMATICS & TELEMATICS DPT           ***
// ***  DATA STRUCTURES - ASSIGNMENT 2022                             ***
// ***  CHRISTINA SOTIRIOU (it21780)                                  ***
// ***  11/01/2023                                                    ***
// **********************************************************************

// PROGRAM DESCRIPTION
// -------------------
// Java Program to implement Hashing Table with Open Addressing (Linear Probing) & 
// Universal Hashing using the Matrix Method
// Supports Rehashing if Hash Table capacity thresholds will be exceeded 

// An ADT Dictionary will be implemented
// Entries will not be sorted
// Hash Table size will be a Prime number as this allows for least collisions
// when choosing a good hash function
// ('Introduction to Algorithms, 3rd edition' Cormen, Leiserson, Rivest, Stein, 11.3.1. p.263)

// OPEN ADDRESSING ALGORITHM WITH LINEAR PROBING
// ---------------------------------------------
//
//  Step 1: Retrieve key k
//  Step 2: Calculate hash function h[k]= k %size T (where T = Hash table)
//  Step 3: IF T[h(k)] = NULL, which means that T is empty at the calculated hash value place,
//            then insert key at h[k] position of T
//          ELSE
//            find next empty place (linear probing) in T to insert the key

// UNIVERSAL HASHING WITH MATRIX METHOD ALGORITHM
// ----------------------------------------------
// Step 1: Define the size (S) of desired hash table T (e.x. 256)
// Step 2: Calculate the power of 2 (b) that gives T size (e.x. 8)
// Step 3: Define the size of input keys (U) in bits (e.x. 32) 
// Step 4: Produce a set of randomized (b) integers that have (U) bits length (e.x. 8 integers 32bit)
// Step 5: Create an array H(U x b) by storing the integers produced at previous step
// Step 6: Call hashCode(k) with the key (k)
// Step 7: Multiply (H x k) modulo 2, this is the final hashed key

import java.util.Random;

public class HashLinearDictionary<K, V> 
{
	// main class
	public Dictionary<K, V>[] T;                         // Hash Table definition from Dictionary
    private int numDictHits;                             // Counter of number of Items entered at Dictionary
    private int numTRowsFull;                            // Counter of number of T[] rows that are full
    private static final int DEFAULT_SIZE = 101;         // T[] rows, must be odd AND prime
    // Critical capacity % of T[] that, if exceeded, size must be doubled
    private static final double MAX_LOAD_FACTOR = 0.75;  
    // Critical capacity % of T[] that, if reached, size must be reduced to half
    private static final double MIN_LOAD_FACTOR = 0.25; 
    private int UniversalArray[];
    public static boolean blnUniversalHash;
    
    public HashLinearDictionary()
    {
        this(DEFAULT_SIZE); // call next constructor
        
    } // end default constructor
    
    public HashLinearDictionary(int tableSize)
    {
        // check that T[tableSize] is prime size
        int primeRows = getNextPrime(tableSize);
                               
        // initialization
        T = new Dictionary[primeRows];
        numDictHits = 0;
        numTRowsFull = 0;
    } // end constructor

    private int getNextPrime(int integer)
    {
    	// find the closest next prime of the integer
    	
        // if number is even, add 1 to make it odd
        if (integer % 2 == 0)
        {
            integer++;
        }
        
        // test odd integer if is prime number 
        while(!isPrime(integer))
        {
            integer = integer + 2;
        }
        
        return integer;
    } // end getNextPrime
        
    private boolean isPrime(int integer)
    {
    	// check that an integer is prime or not
    	
        boolean result;
        boolean done = false;
        
        // 1 and even numbers are not primes
        if ( (integer == 1) || (integer % 2 == 0) )
        {
            result = false;
        }
        
        // 2 and 3 are primes
        else if ( (integer == 2) || (integer == 3) )
        {
            result = true;
        }
        
        else // integer is odd and >= 5
        {
            assert (integer % 2 != 0) && (integer >= 5);
            
            // a prime number is odd and not divisible by every odd integer that (odd integer)^2 <= prime
            result = true; // assume prime
            for (int divisor = 3; !done && (divisor * divisor <= integer); divisor = divisor + 2)
            {
                if (integer % divisor == 0)
                {
                    result = false; // divisible, not prime
                    done = true;
                }
            }
        }
        
        return result;
    } // end isPrime
    
    public int getHashIndex(K key)
    {
        // returns theoretical T[index] of a given key according to the JAVA hash function
    	
    	int hashIndex;
    	
    	if (blnUniversalHash == false)
    	{
          // CLASSICAL hashCode algorithm
          hashIndex = key.hashCode() % T.length;    	
    	}
    	else
    	{
          // UNIVERSAL hashCode algorithm
          hashIndex = getUniversalHashIndex(key.hashCode()) % T.length;    		
    	}
  
        if (hashIndex < 0)
        {
            hashIndex = hashIndex + T.length;
        }
        
        return hashIndex;
    } // end getHashIndex
   
    public int getUniversalHashIndex(int tempKey)
    {
    	// returns theoretical T[index] of a given key according to the custom Universal hash function
    	
    	int u;             // record of the Matrix
    	int x;             // tempKey produced by hashCode
    	int countModulo;   // modulo operation result
    	String uBinary;    // u in binary mode
        String xBinary;    // x in binary mode
        String result[];   // result hash key in binary array mode
        int finalKey;      // result hash key as integer
        int i;             // temporary variable
        int j;             // temporary variable
        String tmp;        // temporary variable
    	
        x = tempKey;
        finalKey = tempKey; // initialization
        tmp = "";
	    xBinary = Integer.toBinaryString(x);       
	    xBinary = String.format("%32s", xBinary).replaceAll(" ", "0");
	    char[] arr_x_Binary = xBinary.toCharArray();
	    char[] arr_u_Binary;
	    
        result =  new String[UniversalArray.length];
        
        // loop for all entries of H[] and multiply with tempKey in binary mode
    	for (i = 0; i < UniversalArray.length; i++)
    	{
    		u = UniversalArray[i];    		
    		countModulo = 0;
    		
    		// convert integers to binary & make them 32-bit wide strings
    	    uBinary = Integer.toBinaryString(u);       
    	    uBinary = String.format("%32s", uBinary).replaceAll(" ", "0");

    	    // convert string to character array
    	    arr_u_Binary = uBinary.toCharArray();
                        
            for (j = 0; j < uBinary.length(); j++) 
            {
                if (arr_u_Binary[j] == '1' && arr_x_Binary[j] == '1')
                {
                  countModulo++;  // apply modulo 2 arithmetic
                }
            }
            
            if(countModulo % 2 == 0)
                result[i] = "0";                
            else
                result[i] = "1"; 
            
            tmp = tmp + result[i];    
    	}
    	    	
    	finalKey = Integer.parseInt(tmp,2);
    	    	    	    	
    	return finalKey;
    	
    } // end getUniversalHashIndex
        
    private int linearProbe(int index, K key)
    {
    	// Apply Open addressing with Linear Probing algorithm for avoiding key collision
    	// Returns T[index] that key was found OR index of the first removed entry (hole) at T
        boolean found = false;
        
        int removedStateIndex = -1; // index of first Hash Table row in removed state
        
        int i=0;
        while (!found && (T[index] != null) && i< T.length)
        {
            if (T[index].isIn())
            {
                if (key.equals(T[index].getKey()))
                found = true; // key found
                else // follow linear probe sequence
                {
                    index = (index + 1) % T.length;
                    i++;
                }
            }
            else // skip entries that were removed
            {
                // save index of first Hash Table row in removed state
                if (removedStateIndex == -1)
                  removedStateIndex = index;
                
                index = (index + 1) % T.length;
                i++;
            }
        }
        
        if (found || (removedStateIndex == -1) )
         return index;             // key was found at this index position at T
        else
         return removedStateIndex; // this is the first hole at T, a new entry will be added here
    } // end linearProbe
    
    public V add(K key, V value)
    {
    	// add entry to the Dictionary OR update value to an existing one and return old value just for display purposes
        V oldValue;
        
        if (checkTableMaxThreshold())
          rehashIncreaseTable();
        
        /* In this App, 'removing' entries from hash table T is not supported 
         * Library supports entries to be marked 'for removal' and these 'holes' could be filled with new entries
         * App, does not support (UI) entries to be 'marked for removal', only additions are supported
         *  
         * if (checkTableMinThreshold())
             rehashDecreaseTable();          
          */ 
                       
        int index = getHashIndex(key);
        index = linearProbe(index, key);
        
        assert (index >= 0) && (index < T.length);
        
        if ( (T[index] == null) || T[index].isRemoved())
        { // key not found, so insert new entry
            T[index] = new Dictionary<K, V>(key, value);
            numDictHits++;
            numTRowsFull++;
            oldValue = null;
        }
        else
        { // key found; get old value for return and then replace it
            oldValue = T[index].getValue();
            T[index].setValue(value);
        } // end if
        
        return oldValue;
    } // end add
    
    public int searchIndex(int index, K key)
    {
    	// searches for the index of a key
    	// returns either the index passed as parameter, the index found due to linear probing, or -1
        boolean found = false;
        int i=0;
        
        while (!found && (T[index] != null) && i<T.length)
        {
            if (T[index].isIn() && key.equals(T[index].getKey()) )
            found = true; // key found
            else // follow linear probe sequence
            {
                index = (index + 1) % T.length;
                i++;
            }
        }
        
        int result = -1;
        if (found)
         result = index;
        
        return result;
    } // end searchIndex
        
    public V remove(K key)
    {
    	//remove entry from the Dictionary, returned removed value just for display purposes
        V removedValue = null;
        
        int index = getHashIndex(key);     // calculate the theoretical index of the key
        index = searchIndex(index, key);   // pass theoretical index and find actual one
        
        if (index != -1)
        {
            // key found, flag entry as removed and return its value
            removedValue = T[index].getValue();
            T[index].setToRemoved();
            numDictHits--;
        }
        //
        
        return removedValue;
    } // end remove
    
    public V getValue(K key)
    {
        V result = null;
        
        int index = getHashIndex(key);        // calculate the theoretical index of the key
        index = searchIndex(index, key);      // pass theoretical index and find actual one
        
        if (index != -1)
        result = T[index].getValue(); // key found, get value
        
        // else not found, result is null
        
        return result;
    } // end getValue
    
    public boolean contains(K key)
    {
        return getValue(key) != null;
    } // end contains
    
    public boolean isEmpty()
    {
        return numDictHits == 0;
    } // end isEmpty
    
    
    public boolean isFull()
    {
        return false;
    } // end isFull
    
    
    public int getSize()
    {
        return numDictHits;
    } // end getSize
    
    
    public final void clear()
    {
        for (int index = 0; index < T.length; index++)
        T[index] = null;
        
        numDictHits = 0;
        numTRowsFull = 0;
    } // end clear
           
    private void rehashIncreaseTable()
    {
    	// Increases the size of the Hash Table to a prime > twice its old size
        Dictionary<K, V>[] oldTable = T;
        
        int oldSize = T.length;
        int newSize = getNextPrime(oldSize + oldSize);
                        
        T = new Dictionary[newSize];
        numDictHits = 0;        
        numTRowsFull = 0;
                
        // Recalculate Universal Hashing H[uxb] table size
        initUniversalHash();
                        
        // rehash dictionary entries from old array to the new and bigger array
        // skip both null locations and removed entries
        for (int index = 0; index < oldSize; index++)
        {
            if ( (oldTable[index] != null) && oldTable[index].isIn() )
              add(oldTable[index].getKey(), oldTable[index].getValue());
        }
                
    } // end rehashIncreaseTable    
    
    private void rehashDecreaseTable()
    {
    	// Decreases the size of the Hash Table to a prime < half its old size
        Dictionary<K, V>[] oldTable = T;
        
        int oldSize = T.length;
        int newSize = getNextPrime(oldSize / 2);
                                                
        T = new Dictionary[newSize];
        numDictHits = 0;        
        numTRowsFull = 0;
        
        // Recalculate Universal Hashing H[uxb] table size
        initUniversalHash();
                
        // rehash dictionary entries from old array to the new and smaller array
        // skip both null locations and removed entries
        for (int index = 0; index < oldSize; index++)
        {
            if ( (oldTable[index] != null) && oldTable[index].isIn() )
              add(oldTable[index].getKey(), oldTable[index].getValue());
        }
                        
    } // end rehashDecreaseTable
                                
    private boolean checkTableMaxThreshold()
    {    	
        return numTRowsFull > MAX_LOAD_FACTOR * T.length;
    } // end checkTableMaxThreshold
    
    private boolean checkTableMinThreshold()
    {    	    	    		 
    	return numTRowsFull < MIN_LOAD_FACTOR * T.length;
    } // end checkTableMinThreshold
           
    public void display()
    {
        // display hash table for illustration and testing            	
    	for (int index = 0; index < T.length; index++)
        {
            if (T[index] == null)
              System.out.println("Index: " + index + " null ");
            else if (T[index].isRemoved())
              System.out.println("Index: " + index + " Removed ");
            else
            System.out.println("Index: " + index + " Key: " + T[index].getKey() + " Value: " + T[index].getValue());
        }
        System.out.println();
    } // end display

    public void initUniversalHash()
    {
    	// preparing data structures for Universal Hash Matrix method
    	int tableSize = T.length;

        // if number is odd, add 1 to make it even
        if (tableSize % 2 != 0)
        {
        	tableSize++;
        }

        Random rng = new Random(17000);
        
        // find the exponent power (2) of hash table size
        int b = (int) (Math.log(tableSize) / Math.log(2));
        
        // filling Array H = [U x b]
        UniversalArray =  new int[b];
        for (int index = 0; index < b; index++)
        {
        	UniversalArray[index] = rng.nextInt(1000);        	        	
        }
                    	
    } //end initUniversalHash    
    
    // Build main Dictionary class
    class Dictionary<S, T>
    {
        private S key;
        private T value;
        private boolean inTable; // true if entry is in table
        
        private Dictionary(S searchKey, T dataValue)
        {
            key = searchKey;
            value = dataValue;
            inTable = true;
        } // end constructor
        
        public S getKey()
        {
            return key;
        } // end getKey
        
        private T getValue()
        {
            return value;
        } // end getValue
        
        private void setValue(T newValue)
        {
            value = newValue;
        } // end setValue
        
        private boolean isIn()
        {
            return inTable;
        } // end isIn
        
        private boolean isRemoved() // opposite of isIn
        {
            return !inTable;
        } // end isRemoved
        
        private void setToRemoved()
        {
            key = null;
            value = null;
            inTable = false;
        } // end setToRemoved
                        
    } // end Dictionary
    
} // end HashLinearDictionary

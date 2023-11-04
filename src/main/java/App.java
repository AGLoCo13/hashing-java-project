// **********************************************************************
// ***  HAROKOPIO UNIVERSITY - INFORMATICS & TELEMATICS DPT           ***
// ***  DATA STRUCTURES - ASSIGNMENT 2022                             ***
// ***  GEORGIOY ANTONIOS - IT21615                                   ***
// ***  11/01/2023                                                    ***
// **********************************************************************

// PROGRAM DESCRIPTION
// -------------------
// Java Program to implement Hashing Table with Open Addressing (Linear Probing) & 
// Universal Hashing using the Matrix Method
// Supports Rehashing if Hash Table capacity thresholds will be exceeded
//
// Provide file 'book.txt' with ASCII text to be parsed and indexed 
//
// Calling App with argument 1 (App 1), activates Universal Hashing!

// *************************************
// ***      This is the MAIN APP     ***
// *************************************

// Importing necessary libraries
import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class App
{
    String key;
    Integer value;
    
    public App(String key, Integer value)
    {
        this.key = key;
        this.value = value;
    } // end constructor
    
    public static void main(String[] args)
    {            	    	
    	// Initialize a Dictionary Array
    	// Assign, deliberately, a small hash table size, so rehash function will be fired 
        HashLinearDictionary<String, Integer> wordList = new HashLinearDictionary<String, Integer>(3);
        
        // check arguments for activating Universal Hashing algorithm        
        if (args.length == 1 && args[0].equals("1"))
    	{
        	wordList.blnUniversalHash = true;
        	
            // Initialize Universal Hashing H[uxb] table size
            wordList.initUniversalHash();                	
    	} 
        else
        {
        	wordList.blnUniversalHash = false;
        	System.out.println(" for executing App with Universal hashing algorithm, call App 1 " + '\n');        	
        }
    		    	                        
        try{
        	// read a text file and store words in an array
            Scanner scanner = new Scanner(new File("book.txt")).useDelimiter(" ");
            while (scanner.hasNext()) 
            {
            	String line = scanner.nextLine();
            	StringTokenizer st = new StringTokenizer(line);
            	while(st.hasMoreTokens())
            	{
            		String word  = st.nextToken();            		
            		Integer curFreq = wordList.getValue(word);
            	            	
            	    if (curFreq == null) 
            	    { 
					   curFreq = 1;
				    } else 
				    { 
					   curFreq++;
				    }            	    
            	    wordList.add(word, curFreq);
            	}            	            	            	
            }                        
           }
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
                
        // Display Hash Table
        System.out.println(" ");
        System.out.println("Dictionary - Display Results");
        System.out.println("----------------------------" + '\n');        
    	if (wordList.blnUniversalHash == false)
    	{
          // CLASSICAL hashCode algorithm
    	  System.out.println("Regular Java hash function" + '\n');
    	}
    	else
    	{
          // UNIVERSAL hashCode algorithm
    	  System.out.println("Universal Java hash function" + '\n');    		
    	}                                        
        System.out.println(" Number of unique indexed entries: " + wordList.getSize() + '\n');
        wordList.display();
    } // end of Main
    
} // end of class App

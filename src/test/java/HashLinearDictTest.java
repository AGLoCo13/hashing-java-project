import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HashLinearDictTest extends TestCase 
{	
	private static final int SIZE = 10000;
	
	public void testLinearHashTable() 
	{
	
		HashLinearDictionary<String, Integer> testList = new HashLinearDictionary<String, Integer>(101);
		
		// Initialize Universal Hashing H[uxb] table size
        testList.initUniversalHash();
				
		List<Integer> values = new ArrayList<>();
		
		Random rng = new Random(17);
		
		for (int i = 0; i < SIZE; i++) 
		{
			int n = rng.nextInt(1000);
			
			values.add(n);
			testList.add(Integer.toString(n), n + 1);
		}
	
		for (Integer v : values) 
		{
			assertTrue(testList.getValue(Integer.toString(v)) == v + 1);
		}
	}
}

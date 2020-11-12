package ssynx.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CalcTest 
{
	private CalcTest() {}
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testSum() 
    {
    	assertTrue(Calc.sum(1,2) == 3);
    }
}

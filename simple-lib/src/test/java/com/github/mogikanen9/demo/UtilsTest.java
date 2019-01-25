package com.github.mogikanen9.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for Utils.
 * @author mogikanen9
 */
public class UtilsTest 
{
    /**
     * Dummy Test :-)
     */
    @Test
    public void testSayHello()
    {
        assertEquals( Utils.sayHello("Ejik"), "Hello, Ejik !");
    }
}

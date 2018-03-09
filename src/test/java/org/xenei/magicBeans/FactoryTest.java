package org.xenei.magicBeans;

import static org.junit.Assert.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.magicBeans.test.fullMagic.FullMagic;
import org.xenei.magicBeans.test.partialMagic.PartialMagic;

public class FactoryTest
{
 /* TODO change MagicBean  to Subject( magic=trie ) annotation */
	
	/**
	 * Test case where all interfaces are annotated with magicbean annotation.
	 * @throws Exception on error
	 */
	@Test
	public void testFullyMagic() throws Exception
	{
		FullMagic magic = Factory.read( ResourceFactory.createResource( "http://example.com/Magic"), FullMagic.class);

		assertFalse( magic.hasA());
		assertFalse( magic.hasB());
		
		magic.setA(1);
		magic.setB("hello");
		
		assertTrue( magic.hasA());
		assertTrue( magic.hasB());
		
		assertEquals( 1, magic.getA());
		assertEquals( "hello", magic.getB());
		
		Resource r = ((ResourceWrapper) magic).getResource();
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("a"), 1));
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("b"), "hello"));
	}
	
	/**
	 * Test case where only base interface is annotated with magicbean annotation.
	 * @throws Exception on error
	 */
	@Test
	public void testPartialMagic() throws Exception
	{
		PartialMagic magic = Factory.read( ResourceFactory.createResource( "http://example.com/Magic2"), PartialMagic.class);

		assertFalse( magic.hasA());
		assertFalse( magic.hasB());
		
		magic.setA(1);
		magic.setB("hello");
		
		assertTrue( magic.hasA());
		assertTrue( magic.hasB());
		
		assertEquals( 1, magic.getA());
		assertEquals( "hello", magic.getB());
		
		Resource r = ((ResourceWrapper) magic).getResource();
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("a"), 1));
		assertTrue( r.hasLiteral( ResourceFactory.createProperty("b"), "hello"));
	}

}



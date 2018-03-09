package org.xenei.magicBeans;

import static org.junit.Assert.*;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.magicBeans.test.Magic;

public class FactoryTest
{

	
	@Test
	public void testFullyMagic() throws Exception
	{
		Magic magic = Factory.read( ResourceFactory.createResource( "http://example.com/Magic"), Magic.class);

		assertFalse( magic.hasA());
		assertFalse( magic.hasB());
		
		magic.setA(1);
		magic.setB("hello");
		
		assertTrue( magic.hasA());
		assertTrue( magic.hasB());
		
		assertEquals( 1, magic.getA());
		assertEquals( "hello", magic.getB());
		
		Model model = ((ResourceWrapper) magic).getResource().getModel();
		model.write( System.out, "TURTLE" );
	}
}



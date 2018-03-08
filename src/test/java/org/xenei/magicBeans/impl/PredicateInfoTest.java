package org.xenei.magicBeans.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.Test;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.magicBeans.Factory;
import org.xenei.magicBeans.test.ComponentA;

import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

public class PredicateInfoTest
{

	Factory factory = mock(Factory.class);
	Model model = ModelFactory.createDefaultModel();
	
	@Test
	public void testConstructor() throws Exception {
		
		Method method = ComponentA.class.getMethod( "setA" , int.class);
		
		EffectivePredicate predicate = new EffectivePredicate(method);
	
	PredicateInfoImpl impl = new PredicateInfoImpl( factory, predicate,
			method, int.class );
	
	Resource resource = model.createResource( "http://example.com/ComponentA" ); 
	Object[] args = {1};
	Object result = impl.exec(factory, method, resource, args);
	model.write( System.out, "TURTLE");
	}
	
}

package org.xenei.magicBeans.impl;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.junit.Test;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.jena.entities.impl.ActionType;
import org.xenei.jena.entities.impl.EffectivePredicate;
import org.xenei.jena.entities.impl.handlers.LiteralHandler;
import org.xenei.magicBeans.Factory;
import org.xenei.magicBeans.test.ComponentA;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;


public class PredicateInfoTest
{

	private Factory factory = mock(Factory.class);
	private Model model = ModelFactory.createDefaultModel();

	@Test
	public void testConstructor() throws Exception
	{

		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		Resource resource = model
				.createResource("http://example.com/ComponentA");
		Object[] args = { 1 };
		Object result = impl.exec(factory, method, resource, args);
		assertNull(result);
		assertTrue(resource.hasLiteral(
				ResourceFactory.createProperty("http://example.com/a"), 1));

	}

	@Test
	public void isPredicateTest() throws Exception
	{

		assertTrue(PredicateInfoImpl.isPredicate(X.class.getMethod("get")));
		assertFalse(PredicateInfoImpl.isPredicate(X.class.getMethod("getS")));
	}

	@Test
	public void getActionTypeTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( ActionType.SETTER, impl.getActionType() );
	}

	@Test
	public void getEffectivePredicateTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( predicate, impl.getEffectivePredicate() );
	}

	@Test
	public void getMethodNameTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( "setA", impl.getMethodName() );

	}

	@Test
	public void getNamespaceTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( "http://example.com/", impl.getNamespace() );

	}

	@Test
	public void getObjectHandlerTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( LiteralHandler.class, impl.getObjectHandler( factory ).getClass() );

	}

	@Test
	public void getPostExecTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( 0, impl.getPostExec().size() );

	}

	@Test
	public void getPropertyTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( ResourceFactory.createResource( "http://example.com/a"), impl.getProperty() );

	}

	@Test
	public void getUriStringTest() throws Exception
	{		
		Method method = ComponentA.class.getMethod("setA", int.class);

	EffectivePredicate predicate = new EffectivePredicate(method);

	PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
			method, int.class);

	assertEquals( "http://example.com/a", impl.getUriString() );

	}

	@Test
	public void getValueClassTest() throws Exception
	{
		Method method = ComponentA.class.getMethod("setA", int.class);

		EffectivePredicate predicate = new EffectivePredicate(method);

		PredicateInfoImpl impl = new PredicateInfoImpl(factory, predicate,
				method, int.class);

		assertEquals( int.class, impl.getValueClass() );

	}

	@Subject
	private static class X
	{
		@Predicate
		public int get()
		{
			return 1;
		}

		public String getS()
		{
			return "foo";
		}
	}

}

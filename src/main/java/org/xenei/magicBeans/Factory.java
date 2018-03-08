package org.xenei.magicBeans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.MissingAnnotation;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.impl.SubjectInfoImpl;

public interface Factory
{
	
	static final Factory INSTANCE = new FactoryImpl();
	
	static <T> T read( Resource r, Class<T> clazz) throws MissingAnnotation
	{
		return INSTANCE.makeInstance( r, clazz );
	}
	
	static <T> T read( ResourceWrapper r, Class<T> clazz) throws MissingAnnotation
	{
		return INSTANCE.makeInstance( r, clazz );
	}
	
	SubjectInfo getSubjectInfo( Class<?> clazz );
	
	<T> T makeInstance( Object source, Class<T> clazz) throws MissingAnnotation;
	
	/**
     * Parse the class if necessary.
     * 
     * The first time the class is seen it is parsed, after that a cached
     * version is returned.
     * 
     * @param clazz
     * @return The SubjectInfo for the class.
     * @throws MissingAnnotation
     */
    SubjectInfo parse(final Class<?> clazz) throws MissingAnnotation;
 
}

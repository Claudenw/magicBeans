package org.xenei.magicBeans.test;

import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.annotations.Subject;
import org.xenei.magicBeans.MagicBean;

@MagicBean
@Subject( namespace="http://example.com/")
public interface ComponentA
{
	
	int getA();
	@Predicate()
	void setA( int i );
	boolean hasA();
	
	
}

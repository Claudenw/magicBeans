package org.xenei.magicBeans.test.fullMagic.namespace;

import org.xenei.magicBeans.MagicBean;

@MagicBean("http://example.com/classpath/")
public interface FullMagicA
{
	public int getA();
	public void setA( int a );
	public boolean hasA();
}

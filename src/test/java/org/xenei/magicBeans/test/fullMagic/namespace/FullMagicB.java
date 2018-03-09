package org.xenei.magicBeans.test.fullMagic.namespace;

import org.xenei.magicBeans.MagicBean;

@MagicBean("http://example.com/classpath/")
public interface FullMagicB
{
	public String getB();
	public void setB( String a );
	public boolean hasB();
}

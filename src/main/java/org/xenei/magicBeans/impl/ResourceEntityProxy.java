package org.xenei.magicBeans.impl;


import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.proxy.exception.InvokerException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.xenei.jena.entities.PredicateInfo;
import org.xenei.jena.entities.ResourceWrapper;
import org.xenei.jena.entities.SubjectInfo;
import org.xenei.jena.entities.annotations.Predicate;
import org.xenei.jena.entities.impl.TypeChecker;
import org.xenei.magicBeans.Factory;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Implements the invoker that the proxy uses.
 * 
 * This method intercepts the annotated entity methods as well as the
 * ResourceWrapper.getResource() method.
 */
public class ResourceEntityProxy implements MethodInterceptor {
    private final Resource resource;
    private final SubjectInfo subjectInfo;
    private final Factory factory;

    /**
     * The constructor
     * 
     * @param factory
     *            The magic bean factory to use.
     * @param resource
     *            The resource that is being wrapped
     * @param subjectInfo
     *            The subjectInfo for the resource.
     */
    public ResourceEntityProxy(final Factory factory,  final Resource resource, final SubjectInfo subjectInfo) {
    	if (resource.getModel() == null)
    	{
    		Model m = ModelFactory.createDefaultModel();
    		this.resource = m.createResource( resource.getURI());
    	} else {
    		this.resource = resource;
    	}
        this.subjectInfo = subjectInfo;   
        this.factory = factory;
    }

    /**
     * Invokes an method on the proxy.
     */
    @Override
    public Object intercept(final Object obj, final Method m, final Object[] args, final MethodProxy proxy)
            throws Throwable {
        // handle the cases where the method is not abstract
        if (!Modifier.isAbstract( m.getModifiers() )) {
            return interceptNonAbstract( obj, m, args, proxy );
        } else {
            return interceptAnnotated( obj, m, args, proxy );
        }
    }

    private Object interceptAnnotated(final Object obj, final Method m, final Object[] args, final MethodProxy proxy)
            throws Throwable {

        /* handle the resource wrapper method calls. */
        // if (ResourceEntityProxy.GET_RESOURCE.equals(m))
        if (m.getName().equals( "getResource" ) && (m.getParameterTypes().length == 0)
                && (m.getReturnType() == Resource.class)) {
            return resource;
        }      

        if (m.getName().equals( "getSubjectInfo" ) && (m.getParameterTypes().length == 0)
                && (m.getReturnType() == SubjectInfo.class)) {
            return subjectInfo;
        }

        /* handle the normal annotations */
        SubjectInfo workingInfo = subjectInfo;
        if (m.getDeclaringClass() != subjectInfo.getImplementedClass()) {
            // handle the case where T implements Resource and the method is
            // declared in the Resource interface.
            if (m.getDeclaringClass().isAssignableFrom( Resource.class )
                    && Resource.class.isAssignableFrom( subjectInfo.getImplementedClass() )) {
                return m.invoke( resource, args );
            } else {
                workingInfo = factory.getSubjectInfo( m.getDeclaringClass() );
            }
        }
        final PredicateInfo pi = workingInfo.getPredicateInfo( m );

        if (pi == null) {
            if (TypeChecker.canBeSetFrom( workingInfo.getImplementedClass(), subjectInfo.getImplementedClass() )
                    && TypeChecker.canBeSetFrom( workingInfo.getImplementedClass(), Resource.class )) {
                final Class<?>[] argTypes = new Class<?>[args.length];
                for (int i = 0; i < args.length; i++) {
                    argTypes[i] = args[i].getClass();
                }
                try {
                    final Method resourceMethod = Resource.class.getMethod( m.getName(), argTypes );
                    return resourceMethod.invoke( resource, args );
                } catch (final Exception e) {
                    // do nothing thow exception ouside of if.
                }
            }
            throw new InvokerException( String.format( "Null method (%s) called", m.getName() ) );
        }

        if (pi instanceof PredicateInfoImpl) {
            Object o = ((PredicateInfoImpl) pi).exec( factory, m, resource, args );
            subjectInfo.getPredicateInfo( m ).getPostExec();
            for (final Method peMethod : subjectInfo.getPredicateInfo( m ).getPostExec()) {
                switch (pi.getActionType()) {
                case GETTER:
                    o = peMethod.invoke( obj, o );
                    break;

                case EXISTENTIAL:
                case REMOVER:
                case SETTER:
                    peMethod.invoke( obj, args );
                }

            }
            return o;
        }

        throw new RuntimeException(
                String.format( "Internal predicateinfo class (%s) not (%s)", pi.getClass(), PredicateInfoImpl.class ) );
    }

    // non abstract annotated methods
    // override the implementation unless annotatation includes the
    // update=true value -- not implemented
    private Object interceptAnnotatedNonAbstract(final Object obj, final Method m, final Object[] args,
            final MethodProxy proxy, final Predicate p) throws Throwable {
        return interceptAnnotated( obj, m, args, proxy );
    }

    // handle the cases where the method is not abstract
    private Object interceptNonAbstract(final Object obj, final Method m, final Object[] args, final MethodProxy proxy)
            throws Throwable {
        // handle the special case methods
        if (m.getName().equals( "toString" ) && !m.isVarArgs() && (m.getParameterTypes().length == 0)) {
            return String.format( "%s[%s]", subjectInfo.getClass(), resource );
        }
        if (m.getName().equals( "hashCode" )) {
            return resource.hashCode();
        }
        if (m.getName().equals( "equals" )) {
            if (args[0] instanceof ResourceWrapper) {
                return resource.equals( ((ResourceWrapper) args[0]).getResource() );
            }
            if (args[0] instanceof Resource) {
                return resource.equals( args[0] );
            }
            return false;
        }

        
        return proxy.invokeSuper( obj, args );
    }
}
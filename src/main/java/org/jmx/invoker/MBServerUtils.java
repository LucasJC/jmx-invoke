package org.jmx.invoker;

import java.io.IOException;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * 
 * @author lcouchot
 *
 */
public class MBServerUtils 
{
	private static MBeanServerConnection connection;
    private static JMXConnector connector;

    /**
     * Method that connects to the jmx port of a jvm, invokes a method with specified parameters and then prints its response.
     * 
     * @param url jmxrmi url
     * @param method method label
     * @param mbClassName MBean class ObjectName
     * @param methodParams method params
     */
	public static void invokeMethod(String url, String method, String mbClassName, String[] methodParams) {
    	
		try{
			ObjectName objectName = new ObjectName(mbClassName);
	    	
			JMXServiceURL serviceURL = new JMXServiceURL(url);
		   
			connector = JMXConnectorFactory.connect(serviceURL);
	
			connection = connector.getMBeanServerConnection();
		   
			String ret = (String) connection.invoke(objectName, method, (Object[]) methodParams, null);
			
			System.out.println("Result for " + method + ": " + ret + ".\nURL: " + url + "\n");

			connector.close();
			
		} catch (IOException e) {
			printError("Error connecting to: " + url,e);
		} catch (MalformedObjectNameException e) {
			printError("Error building Service URL: " + url,e);
		} catch (InstanceNotFoundException e) {
			printError("Error invoking method. URL: " + url,e);
		} catch (MBeanException e) {
			printError("Error invoking method. URL: " + url,e);
		} catch (ReflectionException e) {
			printError("Error invoking method. URL: " + url,e);
		} catch (ClassCastException e) {
			printError("Error casting Service URL: " + url,e);
		} catch(RuntimeOperationsException e){
			printError("Error invoking method. URL: " + url,e);
		}
    }
	
	static void printError(String errorMessage, Exception exception){
		System.out.println(errorMessage + "\n" + exception.getCause());
	}
}

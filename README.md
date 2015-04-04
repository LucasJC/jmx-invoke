# jmx-invoker
Application that executes a method exposed by a Managed Bean remotely.

The packaged jar receives the following parameters:

**-urls 'file_name'**:
Text file with the urls list with a RMI url by line.

**-mbclass 'class_name'**:
Managed Bean JMX ObjectName.

**-method 'method_name'**:
Method name, as exposed by the Managed Bean Interface.

**[-params param1,param2,...]**:
Array of method params to pass for execution. (Optional)

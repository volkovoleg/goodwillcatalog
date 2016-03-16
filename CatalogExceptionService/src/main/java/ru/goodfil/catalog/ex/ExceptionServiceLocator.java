/**
 * ExceptionServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.goodfil.catalog.ex;

public class ExceptionServiceLocator extends org.apache.axis.client.Service implements ExceptionService {

    public ExceptionServiceLocator() {
    }


    public ExceptionServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ExceptionServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasichttpExcServiceEndpiont
    private java.lang.String BasichttpExcServiceEndpiont_address = "http://gw-internet.personal.2t.ru/ExceptionService/ExceptionService.svc";

    public java.lang.String getBasichttpExcServiceEndpiontAddress() {
        return BasichttpExcServiceEndpiont_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasichttpExcServiceEndpiontWSDDServiceName = "BasichttpExcServiceEndpiont";

    public java.lang.String getBasichttpExcServiceEndpiontWSDDServiceName() {
        return BasichttpExcServiceEndpiontWSDDServiceName;
    }

    public void setBasichttpExcServiceEndpiontWSDDServiceName(java.lang.String name) {
        BasichttpExcServiceEndpiontWSDDServiceName = name;
    }

    public IExceptionService getBasichttpExcServiceEndpiont() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasichttpExcServiceEndpiont_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasichttpExcServiceEndpiont(endpoint);
    }

    public IExceptionService getBasichttpExcServiceEndpiont(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            BasichttpExcServiceEndpiontStub _stub = new BasichttpExcServiceEndpiontStub(portAddress, this);
            _stub.setPortName(getBasichttpExcServiceEndpiontWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasichttpExcServiceEndpiontEndpointAddress(java.lang.String address) {
        BasichttpExcServiceEndpiont_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IExceptionService.class.isAssignableFrom(serviceEndpointInterface)) {
                BasichttpExcServiceEndpiontStub _stub = new BasichttpExcServiceEndpiontStub(new java.net.URL(BasichttpExcServiceEndpiont_address), this);
                _stub.setPortName(getBasichttpExcServiceEndpiontWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BasichttpExcServiceEndpiont".equals(inputPortName)) {
            return getBasichttpExcServiceEndpiont();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "ExceptionService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasichttpExcServiceEndpiont"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasichttpExcServiceEndpiont".equals(portName)) {
            setBasichttpExcServiceEndpiontEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

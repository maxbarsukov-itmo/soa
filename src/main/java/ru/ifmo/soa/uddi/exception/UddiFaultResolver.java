package ru.ifmo.soa.uddi.exception;

import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;
import org.springframework.ws.soap.SoapBody;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapMessage;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

/**
 * Converts UddiFaultException into a SOAP Fault with DispositionReport in <detail>.
 */
public class UddiFaultResolver implements EndpointExceptionResolver {

  @Override
  public boolean resolveException(MessageContext messageContext, Object endpoint, Exception ex) {
    if (ex instanceof UddiFaultException uddiEx) {
      SoapMessage response = (SoapMessage) messageContext.getResponse();
      SoapBody body = response.getSoapBody();
      SoapFault fault = body.addServerOrReceiverFault(ex.getMessage(), null);

      try {
        JAXBContext context = JAXBContext.newInstance(uddiEx.getDispositionReport().getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(uddiEx.getDispositionReport(), fault.addFaultDetail().getResult());
        return true;
      } catch (JAXBException e) {
        // Fallback to generic fault
        return false;
      }
    }
    return false;
  }
}

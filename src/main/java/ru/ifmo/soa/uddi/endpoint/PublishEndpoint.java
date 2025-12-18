package ru.ifmo.soa.uddi.endpoint;

import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.uddi.api_v3.*;
import ru.ifmo.soa.uddi.exception.UddiErrorCodes;
import ru.ifmo.soa.uddi.exception.UddiFaultException;
import ru.ifmo.soa.uddi.service.AuthService;
import ru.ifmo.soa.uddi.service.PublishService;

import javax.xml.namespace.QName;

@Endpoint
@RequiredArgsConstructor
public class PublishEndpoint {

  private static final String NAMESPACE_URI = "urn:uddi-org:api_v3";
  private final PublishService publishService;
  private final AuthService authService;

  private String requireValidPublisherId(String token) {
    if (token == null || token.isBlank()) {
      throw new UddiFaultException(
        UddiErrorCodes.E_AUTH_TOKEN_REQUIRED_ERRNO,
        UddiErrorCodes.E_AUTH_TOKEN_REQUIRED,
        "Auth token is required"
      );
    }
    return authService.getUserIdByToken(token);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_authToken")
  @ResponsePayload
  public JAXBElement<AuthToken> getAuthToken(@RequestPayload GetAuthToken request) {
    String token = publishService.getAuthToken(request);
    AuthToken response = new AuthToken();
    response.setAuthInfo(token);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "authToken"), AuthToken.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "discard_authToken")
  @ResponsePayload
  public JAXBElement<DispositionReport> discardAuthToken(@RequestPayload DiscardAuthToken request) {
    publishService.discardAuthToken(request);
    DispositionReport response = new DispositionReport();
    return new JAXBElement<>(new QName(NAMESPACE_URI, "dispositionReport"), DispositionReport.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "save_business")
  @ResponsePayload
  public JAXBElement<BusinessDetail> saveBusiness(@RequestPayload SaveBusiness request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    BusinessDetail response = publishService.saveBusiness(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "businessDetail"), BusinessDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "delete_business")
  @ResponsePayload
  public JAXBElement<DispositionReport> deleteBusiness(@RequestPayload DeleteBusiness request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    publishService.deleteBusiness(request, publisherId);
    DispositionReport response = new DispositionReport();
    return new JAXBElement<>(new QName(NAMESPACE_URI, "dispositionReport"), DispositionReport.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "save_service")
  @ResponsePayload
  public JAXBElement<ServiceDetail> saveService(@RequestPayload SaveService request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    ServiceDetail response = publishService.saveService(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "serviceDetail"), ServiceDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "delete_service")
  @ResponsePayload
  public JAXBElement<DispositionReport> deleteService(@RequestPayload DeleteService request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    publishService.deleteService(request, publisherId);
    DispositionReport response = new DispositionReport();
    return new JAXBElement<>(new QName(NAMESPACE_URI, "dispositionReport"), DispositionReport.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "save_binding")
  @ResponsePayload
  public JAXBElement<BindingDetail> saveBinding(@RequestPayload SaveBinding request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    BindingDetail response = publishService.saveBinding(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "bindingDetail"), BindingDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "delete_binding")
  @ResponsePayload
  public JAXBElement<DispositionReport> deleteBinding(@RequestPayload DeleteBinding request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    publishService.deleteBinding(request, publisherId);
    DispositionReport response = new DispositionReport();
    return new JAXBElement<>(new QName(NAMESPACE_URI, "dispositionReport"), DispositionReport.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "save_tModel")
  @ResponsePayload
  public JAXBElement<TModelDetail> saveTModel(@RequestPayload SaveTModel request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    TModelDetail response = publishService.saveTModel(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "tModelDetail"), TModelDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "delete_tModel")
  @ResponsePayload
  public JAXBElement<DispositionReport> deleteTModel(@RequestPayload DeleteTModel request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    publishService.deleteTModel(request, publisherId);
    DispositionReport response = new DispositionReport();
    return new JAXBElement<>(new QName(NAMESPACE_URI, "dispositionReport"), DispositionReport.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "add_publisherAssertions")
  @ResponsePayload
  public JAXBElement<PublisherAssertionsResponse> addPublisherAssertions(@RequestPayload AddPublisherAssertions request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    PublisherAssertionsResponse response = publishService.addPublisherAssertions(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "publisherAssertionsResponse"), PublisherAssertionsResponse.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "delete_publisherAssertions")
  @ResponsePayload
  public JAXBElement<PublisherAssertionsResponse> deletePublisherAssertions(@RequestPayload DeletePublisherAssertions request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    PublisherAssertionsResponse response = publishService.deletePublisherAssertions(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "publisherAssertionsResponse"), PublisherAssertionsResponse.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "set_publisherAssertions")
  @ResponsePayload
  public JAXBElement<PublisherAssertionsResponse> setPublisherAssertions(@RequestPayload SetPublisherAssertions request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    PublisherAssertionsResponse response = publishService.setPublisherAssertions(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "publisherAssertionsResponse"), PublisherAssertionsResponse.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_registeredInfo")
  @ResponsePayload
  public JAXBElement<RegisteredInfo> getRegisteredInfo(@RequestPayload GetRegisteredInfo request) {
    String publisherId = requireValidPublisherId(request.getAuthInfo());
    RegisteredInfo response = publishService.getRegisteredInfo(request, publisherId);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "registeredInfo"), RegisteredInfo.class, response);
  }
}

package ru.ifmo.soa.uddi.endpoint;

import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.uddi.api_v3.*;
import ru.ifmo.soa.uddi.service.InquiryService;

import javax.xml.namespace.QName;

@Endpoint
@RequiredArgsConstructor
public class InquiryEndpoint {

  private static final String NAMESPACE_URI = "urn:uddi-org:api_v3";

  private final InquiryService inquiryService;

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "find_business")
  @ResponsePayload
  public JAXBElement<BusinessList> findBusiness(@RequestPayload FindBusiness request) {
    BusinessList response = inquiryService.findBusiness(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "businessList"), BusinessList.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_businessDetail")
  @ResponsePayload
  public JAXBElement<BusinessDetail> getBusinessDetail(@RequestPayload GetBusinessDetail request) {
    BusinessDetail response = inquiryService.getBusinessDetail(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "businessDetail"), BusinessDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "find_relatedBusinesses")
  @ResponsePayload
  public JAXBElement<RelatedBusinessesList> findRelatedBusinesses(@RequestPayload FindRelatedBusinesses request) {
    RelatedBusinessesList response = inquiryService.findRelatedBusinesses(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "relatedBusinessesList"), RelatedBusinessesList.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "find_service")
  @ResponsePayload
  public JAXBElement<ServiceList> findService(@RequestPayload FindService request) {
    ServiceList response = inquiryService.findService(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "serviceList"), ServiceList.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_serviceDetail")
  @ResponsePayload
  public JAXBElement<ServiceDetail> getServiceDetail(@RequestPayload GetServiceDetail request) {
    ServiceDetail response = inquiryService.getServiceDetail(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "serviceDetail"), ServiceDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "find_binding")
  @ResponsePayload
  public JAXBElement<BindingDetail> findBinding(@RequestPayload FindBinding request) {
    BindingDetail response = inquiryService.findBinding(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "bindingDetail"), BindingDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_bindingDetail")
  @ResponsePayload
  public JAXBElement<BindingDetail> getBindingDetail(@RequestPayload GetBindingDetail request) {
    BindingDetail response = inquiryService.getBindingDetail(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "bindingDetail"), BindingDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "find_tModel")
  @ResponsePayload
  public JAXBElement<TModelList> findTModel(@RequestPayload FindTModel request) {
    TModelList response = inquiryService.findTModel(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "tModelList"), TModelList.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_tModelDetail")
  @ResponsePayload
  public JAXBElement<TModelDetail> getTModelDetail(@RequestPayload GetTModelDetail request) {
    TModelDetail response = inquiryService.getTModelDetail(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "tModelDetail"), TModelDetail.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_operationalInfo")
  @ResponsePayload
  public JAXBElement<OperationalInfos> getOperationalInfo(@RequestPayload GetOperationalInfo request) {
    OperationalInfos response = inquiryService.getOperationalInfo(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "operationalInfos"), OperationalInfos.class, response);
  }

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "get_assertionStatusReport")
  @ResponsePayload
  public JAXBElement<AssertionStatusReport> getAssertionStatusReport(@RequestPayload GetAssertionStatusReport request) {
    AssertionStatusReport response = inquiryService.getAssertionStatusReport(request);
    return new JAXBElement<>(new QName(NAMESPACE_URI, "assertionStatusReport"), AssertionStatusReport.class, response);
  }
}

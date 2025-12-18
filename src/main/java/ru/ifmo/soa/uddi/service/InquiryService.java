package ru.ifmo.soa.uddi.service;

import org.uddi.api_v3.*;

public interface InquiryService {
  BusinessList findBusiness(FindBusiness request);
  BusinessDetail getBusinessDetail(GetBusinessDetail request);
  RelatedBusinessesList findRelatedBusinesses(FindRelatedBusinesses request);

  ServiceList findService(FindService request);
  ServiceDetail getServiceDetail(GetServiceDetail request);

  BindingDetail findBinding(FindBinding request);
  BindingDetail getBindingDetail(GetBindingDetail request);

  TModelList findTModel(FindTModel request);
  TModelDetail getTModelDetail(GetTModelDetail request);

  OperationalInfos getOperationalInfo(GetOperationalInfo request);

  AssertionStatusReport getAssertionStatusReport(GetAssertionStatusReport request);
}

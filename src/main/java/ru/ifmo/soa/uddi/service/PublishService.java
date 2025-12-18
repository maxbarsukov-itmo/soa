package ru.ifmo.soa.uddi.service;

import org.uddi.api_v3.*;

public interface PublishService {
  String getAuthToken(GetAuthToken request);
  void discardAuthToken(DiscardAuthToken request);

  BusinessDetail saveBusiness(SaveBusiness request, String publisherId);
  void deleteBusiness(DeleteBusiness request, String publisherId);

  ServiceDetail saveService(SaveService request, String publisherId);
  void deleteService(DeleteService request, String publisherId);

  BindingDetail saveBinding(SaveBinding request, String publisherId);
  void deleteBinding(DeleteBinding request, String publisherId);

  TModelDetail saveTModel(SaveTModel request, String publisherId);
  void deleteTModel(DeleteTModel request, String publisherId);

  PublisherAssertionsResponse addPublisherAssertions(AddPublisherAssertions request, String publisherId);
  PublisherAssertionsResponse deletePublisherAssertions(DeletePublisherAssertions request, String publisherId);
  PublisherAssertionsResponse setPublisherAssertions(SetPublisherAssertions request, String publisherId);

  RegisteredInfo getRegisteredInfo(GetRegisteredInfo request, String publisherId);
}

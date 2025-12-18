package ru.ifmo.soa.uddi.service.impl;

import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uddi.api_v3.*;
import ru.ifmo.soa.uddi.exception.UddiErrorCodes;
import ru.ifmo.soa.uddi.exception.UddiFaultException;
import ru.ifmo.soa.uddi.model.entity.*;
import ru.ifmo.soa.uddi.repository.*;
import ru.ifmo.soa.uddi.service.AuthService;
import ru.ifmo.soa.uddi.service.PublishService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PublishServiceImpl implements PublishService {

  private final AuthService authService;
  private final BusinessEntityRepository businessRepo;
  private final BusinessServiceRepository serviceRepo;
  private final BindingTemplateRepository bindingRepo;
  private final TModelRepository tModelRepo;
  private final CategoryBagRepository categoryRepo;
  private final IdentifierBagRepository identifierRepo;
  private final ContactRepository contactRepo;
  private final DiscoveryUrlRepository discoveryRepo;
  private final TModelInstanceInfoRepository tModelInstRepo;
  private final OverviewDocRepository overviewDocRepo;
  private final PhoneRepository phoneRepo;
  private final EmailRepository emailRepo;

  @Override
  @Transactional
  public String getAuthToken(GetAuthToken request) {
    String userId = request.getUserID();
    if (userId == null || userId.isBlank()) {
      throw new UddiFaultException(
        UddiErrorCodes.E_UNKNOWN_USER_ERRNO,
        UddiErrorCodes.E_UNKNOWN_USER,
        "User ID must not be empty"
      );
    }
    return authService.createToken(userId);
  }

  @Override
  @Transactional
  public void discardAuthToken(DiscardAuthToken request) {
    String token = request.getAuthInfo();
    if (token == null || token.isBlank()) {
      throw new UddiFaultException(
        UddiErrorCodes.E_AUTH_TOKEN_REQUIRED_ERRNO,
        UddiErrorCodes.E_AUTH_TOKEN_REQUIRED,
        "Auth token is required"
      );
    }
    authService.invalidateToken(token);
  }

  @Override
  @Transactional
  public BusinessDetail saveBusiness(SaveBusiness request, String publisherId) {
    BusinessDetail result = new BusinessDetail();
    for (org.uddi.api_v3.BusinessEntity uddiBusiness : request.getBusinessEntity()) {
      String key = uddiBusiness.getBusinessKey();
      BusinessEntityEntity entity;
      boolean isNew = key == null || key.isBlank();

      if (isNew) {
        entity = new BusinessEntityEntity();
        entity.setOperator(publisherId);
      } else {
        entity = businessRepo.findByBusinessKey(key)
          .orElseThrow(() -> new UddiFaultException(
            UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
            UddiErrorCodes.E_INVALID_KEY_PASSED,
            "Business entity not found: " + key
          ));
        if (!publisherId.equals(entity.getOperator())) {
          throw new UddiFaultException(
            UddiErrorCodes.E_USER_MISMATCH_ERRNO,
            UddiErrorCodes.E_USER_MISMATCH,
            "Only operator can modify business entity"
          );
        }
      }

      entity.setName(getFirstValue(uddiBusiness.getName(), "Business name is required"));
      entity.setDescription(getFirstValue(uddiBusiness.getDescription(), null));

      businessRepo.save(entity);

      updateDiscoveryUrls(entity, uddiBusiness.getDiscoveryURLs());
      updateCategoryBags(EntityType.BUSINESS, entity.getBusinessKey(), uddiBusiness.getCategoryBag());
      updateIdentifierBags(EntityType.BUSINESS, entity.getBusinessKey(), uddiBusiness.getIdentifierBag());
      updateContacts(entity, uddiBusiness.getContacts().getContact());

      result.getBusinessEntity().add(toUddiBusiness(entity));
    }
    return result;
  }

  @Override
  @Transactional
  public void deleteBusiness(DeleteBusiness request, String publisherId) {
    for (String key : request.getBusinessKey()) {
      BusinessEntityEntity entity = businessRepo.findByBusinessKey(key)
        .orElseThrow(() -> new UddiFaultException(
          UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
          UddiErrorCodes.E_INVALID_KEY_PASSED,
          "Business not found: " + key
        ));
      if (!publisherId.equals(entity.getOperator())) {
        throw new UddiFaultException(
          UddiErrorCodes.E_USER_MISMATCH_ERRNO,
          UddiErrorCodes.E_USER_MISMATCH,
          "Only operator can delete business"
        );
      }
      businessRepo.delete(entity);
    }
  }

  @Override
  @Transactional
  public ServiceDetail saveService(SaveService request, String publisherId) {
    ServiceDetail result = new ServiceDetail();
    for (BusinessService uddiService : request.getBusinessService()) {
      String key = uddiService.getServiceKey();
      BusinessServiceEntity entity;
      boolean isNew = key == null || key.isBlank();

      if (isNew) {
        entity = new BusinessServiceEntity();
        String businessKey = uddiService.getBusinessKey();
        BusinessEntityEntity business = businessRepo.findByBusinessKey(businessKey)
          .orElseThrow(() -> new UddiFaultException(
            UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
            UddiErrorCodes.E_INVALID_KEY_PASSED,
            "Parent business not found: " + businessKey
          ));
        if (!publisherId.equals(business.getOperator())) {
          throw new UddiFaultException(
            UddiErrorCodes.E_USER_MISMATCH_ERRNO,
            UddiErrorCodes.E_USER_MISMATCH,
            "Only business operator can create services"
          );
        }
        entity.setBusinessEntity(business);
      } else {
        entity = serviceRepo.findByServiceKey(key)
          .orElseThrow(() -> new UddiFaultException(
            UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
            UddiErrorCodes.E_INVALID_KEY_PASSED,
            "Service not found: " + key
          ));
        BusinessEntityEntity business = entity.getBusinessEntity();
        if (!publisherId.equals(business.getOperator())) {
          throw new UddiFaultException(
            UddiErrorCodes.E_USER_MISMATCH_ERRNO,
            UddiErrorCodes.E_USER_MISMATCH,
            "Only business operator can modify service"
          );
        }
      }

      entity.setName(getFirstValue(uddiService.getName(), "Service name is required"));
      entity.setDescription(getFirstValue(uddiService.getDescription(), null));

      serviceRepo.save(entity);

      updateCategoryBags(EntityType.SERVICE, entity.getServiceKey(), uddiService.getCategoryBag());
      updateBindings(entity, uddiService.getBindingTemplates(), publisherId);

      result.getBusinessService().add(toUddiService(entity));
    }
    return result;
  }

  @Override
  @Transactional
  public void deleteService(DeleteService request, String publisherId) {
    for (String key : request.getServiceKey()) {
      BusinessServiceEntity service = serviceRepo.findByServiceKey(key)
        .orElseThrow(() -> new UddiFaultException(
          UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
          UddiErrorCodes.E_INVALID_KEY_PASSED,
          "Service not found: " + key
        ));
      BusinessEntityEntity business = service.getBusinessEntity();
      if (!publisherId.equals(business.getOperator())) {
        throw new UddiFaultException(
          UddiErrorCodes.E_USER_MISMATCH_ERRNO,
          UddiErrorCodes.E_USER_MISMATCH,
          "Only business operator can delete service"
        );
      }
      serviceRepo.delete(service);
    }
  }

  @Override
  @Transactional
  public BindingDetail saveBinding(SaveBinding request, String publisherId) {
    BindingDetail result = new BindingDetail();
    for (BindingTemplate uddiBinding : request.getBindingTemplate()) {
      String key = uddiBinding.getBindingKey();
      BindingTemplateEntity entity;
      boolean isNew = key == null || key.isBlank();

      if (isNew) {
        entity = new BindingTemplateEntity();
        String serviceKey = uddiBinding.getServiceKey();
        BusinessServiceEntity service = serviceRepo.findByServiceKey(serviceKey)
          .orElseThrow(() -> new UddiFaultException(
            UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
            UddiErrorCodes.E_INVALID_KEY_PASSED,
            "Parent service not found: " + serviceKey
          ));
        BusinessEntityEntity business = service.getBusinessEntity();
        if (!publisherId.equals(business.getOperator())) {
          throw new UddiFaultException(
            UddiErrorCodes.E_USER_MISMATCH_ERRNO,
            UddiErrorCodes.E_USER_MISMATCH,
            "Only business operator can create bindings"
          );
        }
        entity.setService(service);
      } else {
        entity = bindingRepo.findByBindingKey(key)
          .orElseThrow(() -> new UddiFaultException(
            UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
            UddiErrorCodes.E_INVALID_KEY_PASSED,
            "Binding not found: " + key
          ));
        BusinessEntityEntity business = entity.getService().getBusinessEntity();
        if (!publisherId.equals(business.getOperator())) {
          throw new UddiFaultException(
            UddiErrorCodes.E_USER_MISMATCH_ERRNO,
            UddiErrorCodes.E_USER_MISMATCH,
            "Only business operator can modify binding"
          );
        }
      }

      AccessPoint ap = uddiBinding.getAccessPoint();
      if (ap == null) {
        throw new UddiFaultException(
          UddiErrorCodes.E_INVALID_VALUE_ERRNO,
          UddiErrorCodes.E_INVALID_VALUE,
          "AccessPoint is required"
        );
      }
      entity.setAccessPoint(ap.getValue());
      entity.setHostingRedirector("redirect".equals(ap.getUseType()) ? ap.getValue() : null);
      entity.setDescription(getFirstValue(uddiBinding.getDescription(), null));

      bindingRepo.save(entity);

      updateTModelInstanceInfo(entity, uddiBinding.getTModelInstanceDetails());

      result.getBindingTemplate().add(toUddiBinding(entity));
    }
    return result;
  }

  @Override
  @Transactional
  public void deleteBinding(DeleteBinding request, String publisherId) {
    for (String key : request.getBindingKey()) {
      BindingTemplateEntity binding = bindingRepo.findByBindingKey(key)
        .orElseThrow(() -> new UddiFaultException(
          UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
          UddiErrorCodes.E_INVALID_KEY_PASSED,
          "Binding not found: " + key
        ));
      BusinessEntityEntity business = binding.getService().getBusinessEntity();
      if (!publisherId.equals(business.getOperator())) {
        throw new UddiFaultException(
          UddiErrorCodes.E_USER_MISMATCH_ERRNO,
          UddiErrorCodes.E_USER_MISMATCH,
          "Only business operator can delete binding"
        );
      }
      bindingRepo.delete(binding);
    }
  }

  @Override
  @Transactional
  public TModelDetail saveTModel(SaveTModel request, String publisherId) {
    TModelDetail result = new TModelDetail();
    for (org.uddi.api_v3.TModel uddiTModel : request.getTModel()) {
      String key = uddiTModel.getTModelKey();
      TModelEntity entity;
      boolean isNew = key == null || key.isBlank();

      if (isNew) {
        entity = new TModelEntity();
        entity.setOperator(publisherId);
      } else {
        entity = tModelRepo.findByTModelKey(key)
          .orElseThrow(() -> new UddiFaultException(
            UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
            UddiErrorCodes.E_INVALID_KEY_PASSED,
            "TModel not found: " + key
          ));
        if (!publisherId.equals(entity.getOperator())) {
          throw new UddiFaultException(
            UddiErrorCodes.E_USER_MISMATCH_ERRNO,
            UddiErrorCodes.E_USER_MISMATCH,
            "Only operator can modify TModel"
          );
        }
      }

      entity.setName(uddiTModel.getName().getValue());
      entity.setDescription(getFirstValue(uddiTModel.getDescription(), null));

      tModelRepo.save(entity);

      updateCategoryBags(EntityType.TMODEL, entity.getTModelKey(), uddiTModel.getCategoryBag());
      updateOverviewDocs(entity, uddiTModel.getOverviewDoc());

      result.getTModel().add(toUddiTModel(entity));
    }
    return result;
  }

  @Override
  @Transactional
  public void deleteTModel(DeleteTModel request, String publisherId) {
    for (String key : request.getTModelKey()) {
      TModelEntity tModel = tModelRepo.findByTModelKey(key)
        .orElseThrow(() -> new UddiFaultException(
          UddiErrorCodes.E_INVALID_KEY_PASSED_ERRNO,
          UddiErrorCodes.E_INVALID_KEY_PASSED,
          "TModel not found: " + key
        ));
      if (!publisherId.equals(tModel.getOperator())) {
        throw new UddiFaultException(
          UddiErrorCodes.E_USER_MISMATCH_ERRNO,
          UddiErrorCodes.E_USER_MISMATCH,
          "Only operator can delete TModel"
        );
      }
      tModelRepo.delete(tModel);
    }
  }

  @Override
  @Transactional
  public PublisherAssertionsResponse addPublisherAssertions(AddPublisherAssertions request, String publisherId) {
    PublisherAssertionsResponse response = new PublisherAssertionsResponse();
    PublisherAssertions assertions = new PublisherAssertions();
    for (PublisherAssertion pa : request.getPublisherAssertion()) {
      ensurePublisherOwnsBusiness(publisherId, pa.getFromKey());
      ensurePublisherOwnsBusiness(publisherId, pa.getToKey());
      assertions.getPublisherAssertion().add(pa);
    }
    response.getPublisherAssertion().addAll(assertions.getPublisherAssertion());
    return response;
  }

  @Override
  @Transactional
  public PublisherAssertionsResponse deletePublisherAssertions(DeletePublisherAssertions request, String publisherId) {
    PublisherAssertionsResponse response = new PublisherAssertionsResponse();
    PublisherAssertions assertions = new PublisherAssertions();
    for (PublisherAssertion pa : request.getPublisherAssertion()) {
      ensurePublisherOwnsBusiness(publisherId, pa.getFromKey());
      ensurePublisherOwnsBusiness(publisherId, pa.getToKey());
      assertions.getPublisherAssertion().add(pa);
    }
    response.getPublisherAssertion().addAll(assertions.getPublisherAssertion());
    return response;
  }

  @Override
  @Transactional
  public PublisherAssertionsResponse setPublisherAssertions(SetPublisherAssertions request, String publisherId) {
    PublisherAssertionsResponse response = new PublisherAssertionsResponse();
    PublisherAssertions assertions = new PublisherAssertions();
    for (PublisherAssertion pa : request.getPublisherAssertion()) {
      ensurePublisherOwnsBusiness(publisherId, pa.getFromKey());
      ensurePublisherOwnsBusiness(publisherId, pa.getToKey());
      assertions.getPublisherAssertion().add(pa);
    }
    response.getPublisherAssertion().addAll(assertions.getPublisherAssertion());
    return response;
  }

  private void ensurePublisherOwnsBusiness(String publisherId, String businessKey) {
    businessRepo.findByBusinessKey(businessKey)
      .filter(b -> publisherId.equals(b.getOperator()))
      .orElseThrow(() -> new UddiFaultException(
        UddiErrorCodes.E_USER_MISMATCH_ERRNO,
        UddiErrorCodes.E_USER_MISMATCH,
        "Publisher does not own business: " + businessKey
      ));
  }

  @Override
  @Transactional(readOnly = true)
  public RegisteredInfo getRegisteredInfo(GetRegisteredInfo request, String publisherId) {
    RegisteredInfo info = new RegisteredInfo();

    BusinessInfos businessInfos = new BusinessInfos();
    for (BusinessEntityEntity b : businessRepo.findByOperator(publisherId)) {
      BusinessInfo bi = new BusinessInfo();
      bi.setBusinessKey(b.getBusinessKey());

      Name name = new Name();
      name.setValue(b.getName());
      bi.getName().add(name);

      businessInfos.getBusinessInfo().add(bi);
    }
    info.setBusinessInfos(businessInfos);

    TModelInfos tModelInfos = new TModelInfos();
    List<TModelEntity> tModels = tModelRepo.findByOperator(publisherId);
    for (TModelEntity t : tModels) {
      TModelInfo ti = new TModelInfo();
      ti.setTModelKey(t.getTModelKey());
      Name name = new Name();
      name.setValue(t.getName());
      ti.setName(name);
      tModelInfos.getTModelInfo().add(ti);
    }
    info.setTModelInfos(tModelInfos);

    return info;
  }

  private String getFirstValue(List<? extends Object> list, String errorMessage) {
    if (list == null || list.isEmpty()) {
      if (errorMessage != null) {
        throw new UddiFaultException(
          UddiErrorCodes.E_INVALID_VALUE_ERRNO,
          UddiErrorCodes.E_INVALID_VALUE,
          errorMessage
        );
      }
      return null;
    }
    Object obj = list.get(0);
    if (obj instanceof Name name) return name.getValue();
    if (obj instanceof Description desc) return desc.getValue();
    return obj.toString();
  }

  private void updateDiscoveryUrls(BusinessEntityEntity business, DiscoveryURLs uddiUrls) {
    discoveryRepo.deleteAll(discoveryRepo.findByBusinessEntity_BusinessKey(business.getBusinessKey()));
    if (uddiUrls != null) {
      for (DiscoveryURL url : uddiUrls.getDiscoveryURL()) {
        DiscoveryUrlEntity entity = new DiscoveryUrlEntity();
        entity.setBusinessEntity(business);
        entity.setUseType(url.getUseType());
        entity.setUrl(url.getValue());
        discoveryRepo.save(entity);
      }
    }
  }

  private void updateContacts(BusinessEntityEntity business, List<Contact> uddiContacts) {
    contactRepo.deleteAll(contactRepo.findByBusinessEntity_BusinessKey(business.getBusinessKey()));
    if (uddiContacts != null) {
      for (Contact uddiContact : uddiContacts) {
        ContactEntity entity = new ContactEntity();
        entity.setBusinessEntity(business);
        entity.setUseType(uddiContact.getUseType());
        entity.setPersonName(getFirstValue(uddiContact.getPersonName(), null));
        contactRepo.save(entity);

        if (uddiContact.getPhone() != null) {
          for (Phone uddiPhone : uddiContact.getPhone()) {
            PhoneEntity phone = new PhoneEntity();
            phone.setContact(entity);
            phone.setUseType(uddiPhone.getUseType());
            phone.setPhoneNumber(uddiPhone.getValue());
            phoneRepo.save(phone);
          }
        }

        if (uddiContact.getEmail() != null) {
          for (Email uddiEmail : uddiContact.getEmail()) {
            EmailEntity email = new EmailEntity();
            email.setContact(entity);
            email.setUseType(uddiEmail.getUseType());
            email.setEmailAddress(uddiEmail.getValue());
            emailRepo.save(email);
          }
        }
      }
    }
  }

  private void updateCategoryBags(EntityType type, String entityKey, CategoryBag uddiBag) {
    categoryRepo.deleteAll(categoryRepo.findByEntityTypeAndEntityKey(type, entityKey));
    for (JAXBElement<?> element : uddiBag.getContent()) {
      if (element.getDeclaredType() == KeyedReference.class) {
        KeyedReference ref = (KeyedReference) element.getValue();
        CategoryBagEntity bag = new CategoryBagEntity();
        bag.setEntityType(type);
        bag.setEntityKey(entityKey);
        bag.setTModelKey(ref.getTModelKey());
        bag.setKeyName(ref.getKeyName());
        bag.setKeyValue(ref.getKeyValue());
        categoryRepo.save(bag);
      }
    }
  }

  private void updateIdentifierBags(EntityType type, String entityKey, IdentifierBag uddiBag) {
    identifierRepo.deleteAll(identifierRepo.findByEntityTypeAndEntityKey(type, entityKey));
    if (uddiBag != null) {
      for (KeyedReference ref : uddiBag.getKeyedReference()) {
        IdentifierBagEntity bag = new IdentifierBagEntity();
        bag.setEntityType(type);
        bag.setEntityKey(entityKey);
        bag.setTModelKey(ref.getTModelKey());
        bag.setKeyName(ref.getKeyName());
        bag.setKeyValue(ref.getKeyValue());
        identifierRepo.save(bag);
      }
    }
  }

  private void updateBindings(BusinessServiceEntity service, BindingTemplates uddiBindings, String publisherId) {
    bindingRepo.deleteAll(bindingRepo.findByService_ServiceKey(service.getServiceKey()));
    if (uddiBindings != null) {
      for (BindingTemplate uddiBinding : uddiBindings.getBindingTemplate()) {
        BindingTemplateEntity binding = new BindingTemplateEntity();
        binding.setService(service);
        AccessPoint ap = uddiBinding.getAccessPoint();
        binding.setAccessPoint(ap.getValue());
        binding.setHostingRedirector("redirect".equals(ap.getUseType()) ? ap.getValue() : null);
        binding.setDescription(getFirstValue(uddiBinding.getDescription(), null));
        bindingRepo.save(binding);

        updateTModelInstanceInfo(binding, uddiBinding.getTModelInstanceDetails());
      }
    }
  }

  private void updateTModelInstanceInfo(BindingTemplateEntity binding, TModelInstanceDetails uddiDetails) {
    tModelInstRepo.deleteAll(tModelInstRepo.findByBinding_BindingKey(binding.getBindingKey()));
    if (uddiDetails != null) {
      for (TModelInstanceInfo uddiInst : uddiDetails.getTModelInstanceInfo()) {
        TModelInstanceInfoEntity entity = new TModelInstanceInfoEntity();
        entity.setBinding(binding);
        entity.setTModelKey(uddiInst.getTModelKey());

        String instanceDetailsStr = null;
        InstanceDetails instanceDetails = uddiInst.getInstanceDetails();
        if (instanceDetails != null) {
          for (JAXBElement<?> elem : instanceDetails.getContent()) {
            if (elem.getDeclaredType() == String.class) {
              instanceDetailsStr = (String) elem.getValue();
              break;
            }
            if (elem.getDeclaredType() == Description.class) {
              if (instanceDetailsStr == null) {
                instanceDetailsStr = ((Description) elem.getValue()).getValue();
              }
            }
          }
        }

        entity.setInstanceDetails(instanceDetailsStr);
        tModelInstRepo.save(entity);
      }
    }
  }

  private void updateOverviewDocs(TModelEntity tModel, List<OverviewDoc> uddiDocs) {
    overviewDocRepo.deleteAll(overviewDocRepo.findByTModel_TModelKey(tModel.getTModelKey()));
    if (uddiDocs != null) {
      for (OverviewDoc uddiDoc : uddiDocs) {
        String description = null;
        String overviewUrl = null;

        for (JAXBElement<?> element : uddiDoc.getContent()) {
          if (element.getDeclaredType() == Description.class) {
            Description desc = (Description) element.getValue();
            if (description == null) {
              description = desc.getValue();
            }
          } else if (element.getDeclaredType() == OverviewURL.class) {
            OverviewURL url = (OverviewURL) element.getValue();
            if (overviewUrl == null) {
              overviewUrl = url.getValue();
            }
          }
        }

        OverviewDocEntity entity = new OverviewDocEntity();
        entity.setTModel(tModel);
        entity.setDescription(description);
        entity.setOverviewUrl(overviewUrl);
        overviewDocRepo.save(entity);
      }
    }
  }
  private org.uddi.api_v3.BusinessEntity toUddiBusiness(BusinessEntityEntity entity) {
    org.uddi.api_v3.BusinessEntity uddi = new org.uddi.api_v3.BusinessEntity();
    uddi.setBusinessKey(entity.getBusinessKey());
    Name name = new Name();
    name.setValue(entity.getName());
    uddi.getName().add(name);
    if (entity.getDescription() != null) {
      Description desc = new Description();
      desc.setValue(entity.getDescription());
      uddi.getDescription().add(desc);
    }
    return uddi;
  }

  private BusinessService toUddiService(BusinessServiceEntity entity) {
    BusinessService uddi = new BusinessService();
    uddi.setServiceKey(entity.getServiceKey());
    Name name = new Name();
    name.setValue(entity.getName());
    uddi.getName().add(name);
    if (entity.getDescription() != null) {
      Description desc = new Description();
      desc.setValue(entity.getDescription());
      uddi.getDescription().add(desc);
    }
    return uddi;
  }

  private BindingTemplate toUddiBinding(BindingTemplateEntity entity) {
    BindingTemplate uddi = new BindingTemplate();
    uddi.setBindingKey(entity.getBindingKey());
    AccessPoint ap = new AccessPoint();
    ap.setValue(entity.getAccessPoint());
    if (entity.getHostingRedirector() != null) {
      ap.setUseType("redirect");
    }
    uddi.setAccessPoint(ap);
    if (entity.getDescription() != null) {
      Description desc = new Description();
      desc.setValue(entity.getDescription());
      uddi.getDescription().add(desc);
    }
    return uddi;
  }

  private org.uddi.api_v3.TModel toUddiTModel(TModelEntity entity) {
    org.uddi.api_v3.TModel uddi = new org.uddi.api_v3.TModel();
    uddi.setTModelKey(entity.getTModelKey());
    Name name = new Name();
    name.setValue(entity.getName());
    uddi.setName(name);
    if (entity.getDescription() != null) {
      Description desc = new Description();
      desc.setValue(entity.getDescription());
      uddi.getDescription().add(desc);
    }
    return uddi;
  }
}

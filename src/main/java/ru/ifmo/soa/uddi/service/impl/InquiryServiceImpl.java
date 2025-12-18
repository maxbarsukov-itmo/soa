package ru.ifmo.soa.uddi.service.impl;

import lombok.RequiredArgsConstructor;
import jakarta.xml.bind.JAXBElement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uddi.api_v3.*;
import ru.ifmo.soa.uddi.model.entity.*;
import ru.ifmo.soa.uddi.repository.*;
import ru.ifmo.soa.uddi.service.InquiryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

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

  @Override
  @Transactional(readOnly = true)
  public BusinessList findBusiness(FindBusiness request) {
    List<BusinessEntityEntity> businesses = new ArrayList<>();

    if (request.getName() != null && !request.getName().isEmpty()) {
      String name = request.getName().get(0).getValue();
      businesses.addAll(businessRepo.findByNameLike(name));
    } else if (request.getCategoryBag() != null) {
      for (JAXBElement<?> el : request.getCategoryBag().getContent()) {
        if (el.getDeclaredType() == KeyedReference.class) {
          KeyedReference kr = (KeyedReference) el.getValue();
          businesses.addAll(businessRepo.findByCategory(kr.getTModelKey(), kr.getKeyValue()));
        }
      }
    } else if (request.getIdentifierBag() != null) {
      for (KeyedReference el : request.getIdentifierBag().getKeyedReference()) {
        businesses.addAll(businessRepo.findByIdentifier(el.getTModelKey(), el.getKeyValue()));
      }
    } else {
      businesses.addAll(businessRepo.findAll());
    }

    BusinessList result = new BusinessList();
    BusinessInfos infos = new BusinessInfos();
    for (BusinessEntityEntity b : businesses) {
      BusinessInfo info = new BusinessInfo();
      info.setBusinessKey(b.getBusinessKey());
      Name name = new Name();
      name.setValue(b.getName());
      info.getName().add(name);
      infos.getBusinessInfo().add(info);
    }
    result.setBusinessInfos(infos);
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public BusinessDetail getBusinessDetail(GetBusinessDetail request) {
    BusinessDetail detail = new BusinessDetail();
    for (String key : request.getBusinessKey()) {
      var opt = businessRepo.findByBusinessKey(key);
      opt.ifPresent(businessEntityEntity -> detail.getBusinessEntity().add(toUddiBusiness(businessEntityEntity)));
    }
    return detail;
  }

  @Override
  @Transactional(readOnly = true)
  public RelatedBusinessesList findRelatedBusinesses(FindRelatedBusinesses request) {
    // Not implemented in basic UDDI registry
    return new RelatedBusinessesList();
  }

  @Override
  @Transactional(readOnly = true)
  public ServiceList findService(FindService request) {
    List<BusinessServiceEntity> services = new ArrayList<>();

    if (request.getName() != null && !request.getName().isEmpty()) {
      String name = request.getName().get(0).getValue();
      services.addAll(serviceRepo.findByNameLike(name));
    } else if (request.getCategoryBag() != null) {
      for (JAXBElement<?> el : request.getCategoryBag().getContent()) {
        if (el.getDeclaredType() == KeyedReference.class) {
          KeyedReference kr = (KeyedReference) el.getValue();
          services.addAll(serviceRepo.findByCategory(kr.getTModelKey(), kr.getKeyValue()));
        }
      }
    } else if (!request.getBusinessKey().isEmpty()) {
      services.addAll(serviceRepo.findByBusinessEntity_BusinessKeyIn(List.of(request.getBusinessKey())));
    } else {
      services.addAll(serviceRepo.findAll());
    }

    ServiceList result = new ServiceList();
    ServiceInfos infos = new ServiceInfos();
    for (BusinessServiceEntity s : services) {
      ServiceInfo info = new ServiceInfo();
      info.setBusinessKey(s.getBusinessEntity().getBusinessKey());
      info.setServiceKey(s.getServiceKey());
      Name name = new Name();
      name.setValue(s.getName());
      info.getName().add(name);
      infos.getServiceInfo().add(info);
    }
    result.setServiceInfos(infos);
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public ServiceDetail getServiceDetail(GetServiceDetail request) {
    ServiceDetail detail = new ServiceDetail();
    for (String key : request.getServiceKey()) {
      var opt = serviceRepo.findByServiceKey(key);
      if (opt.isPresent()) {
        detail.getBusinessService().add(toUddiService(opt.get()));
      }
    }
    return detail;
  }

  @Override
  @Transactional(readOnly = true)
  public BindingDetail getBindingDetail(GetBindingDetail request) {
    BindingDetail detail = new BindingDetail();
    for (String key : request.getBindingKey()) {
      var opt = bindingRepo.findByBindingKey(key);
      if (opt.isPresent()) {
        detail.getBindingTemplate().add(toUddiBinding(opt.get()));
      }
    }
    return detail;
  }

  @Override
  @Transactional(readOnly = true)
  public BindingDetail findBinding(FindBinding request) {
    List<BindingTemplateEntity> bindings = new ArrayList<>();

    if (request.getServiceKey() != null && !request.getServiceKey().isEmpty()) {
      bindings.addAll(bindingRepo.findByService_ServiceKeyIn(Collections.singletonList(request.getServiceKey())));
    } else if (request.getTModelBag() != null) {
      for (String el : request.getTModelBag().getTModelKey()) {
          bindings.addAll(bindingRepo.findByTModelKey(el));
      }
    } else {
      bindings.addAll(bindingRepo.findAll());
    }

    BindingDetail result = new BindingDetail();
    for (BindingTemplateEntity b : bindings) {
      result.getBindingTemplate().add(toUddiBinding(b));
    }
    return result;
  }
  @Override
  @Transactional(readOnly = true)
  public TModelList findTModel(FindTModel request) {
    List<TModelEntity> models = new ArrayList<>();

    if (request.getName() != null && !request.getName().getValue().isEmpty()) {
      String name = request.getName().getValue();
      models.addAll(tModelRepo.findByNameLike(name));
    } else if (request.getCategoryBag() != null) {
      for (JAXBElement<?> el : request.getCategoryBag().getContent()) {
        if (el.getDeclaredType() == KeyedReference.class) {
          KeyedReference kr = (KeyedReference) el.getValue();
          models.addAll(tModelRepo.findByCategory(kr.getTModelKey(), kr.getKeyValue()));
        }
      }
    } else {
      models.addAll(tModelRepo.findAll());
    }

    TModelList result = new TModelList();
    TModelInfos infos = new TModelInfos();
    for (TModelEntity m : models) {
      TModelInfo info = new TModelInfo();
      info.setTModelKey(m.getTModelKey());
      Name name = new Name();
      name.setValue(m.getName());
      info.setName(name);
      infos.getTModelInfo().add(info);
    }
    result.setTModelInfos(infos);
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public TModelDetail getTModelDetail(GetTModelDetail request) {
    TModelDetail detail = new TModelDetail();
    for (String key : request.getTModelKey()) {
      var opt = tModelRepo.findByTModelKey(key);
      if (opt.isPresent()) {
        detail.getTModel().add(toUddiTModel(opt.get()));
      }
    }
    return detail;
  }

  @Override
  @Transactional(readOnly = true)
  public OperationalInfos getOperationalInfo(GetOperationalInfo request) {
    return new OperationalInfos();
  }

  @Override
  @Transactional(readOnly = true)
  public AssertionStatusReport getAssertionStatusReport(GetAssertionStatusReport request) {
    return new AssertionStatusReport();
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

    discoveryRepo.findByBusinessEntity_BusinessKey(entity.getBusinessKey()).forEach(d -> {
      DiscoveryURL url = new DiscoveryURL();
      url.setUseType(d.getUseType());
      url.setValue(d.getUrl());
      uddi.getDiscoveryURLs().getDiscoveryURL().add(url);
    });

    categoryRepo.findByEntityTypeAndEntityKey(EntityType.BUSINESS, entity.getBusinessKey()).forEach(cb -> {
      KeyedReference ref = new KeyedReference();
      ref.setTModelKey(cb.getTModelKey());
      ref.setKeyName(cb.getKeyName());
      ref.setKeyValue(cb.getKeyValue());
      uddi.getCategoryBag().getContent().add(
        new org.uddi.api_v3.ObjectFactory().createKeyedReference(ref)
      );
    });

    identifierRepo.findByEntityTypeAndEntityKey(EntityType.BUSINESS, entity.getBusinessKey()).forEach(ib -> {
      KeyedReference ref = new KeyedReference();
      ref.setTModelKey(ib.getTModelKey());
      ref.setKeyName(ib.getKeyName());
      ref.setKeyValue(ib.getKeyValue());
      uddi.getIdentifierBag().getKeyedReference().add(ref);
    });

    contactRepo.findByBusinessEntity_BusinessKey(entity.getBusinessKey()).forEach(c -> {
      Contact contact = new Contact();
      contact.setUseType(c.getUseType());
      if (c.getPersonName() != null) {
        PersonName pn = new PersonName();
        pn.setValue(c.getPersonName());
        contact.getPersonName().add(pn);
      }
      c.getPhones().forEach(p -> {
        Phone phone = new Phone();
        phone.setUseType(p.getUseType());
        phone.setValue(p.getPhoneNumber());
        contact.getPhone().add(phone);
      });
      c.getEmails().forEach(e -> {
        Email email = new Email();
        email.setUseType(e.getUseType());
        email.setValue(e.getEmailAddress());
        contact.getEmail().add(email);
      });
      uddi.getContacts().getContact().add(contact);
    });

    serviceRepo.findByBusinessEntity_BusinessKey(entity.getBusinessKey()).forEach(s -> {
      uddi.getBusinessServices().getBusinessService().add(toUddiService(s));
    });

    return uddi;
  }

  private BusinessService toUddiService(BusinessServiceEntity entity) {
    BusinessService uddi = new BusinessService();
    uddi.setServiceKey(entity.getServiceKey());
    uddi.setBusinessKey(entity.getBusinessEntity().getBusinessKey());

    Name name = new Name();
    name.setValue(entity.getName());
    uddi.getName().add(name);

    if (entity.getDescription() != null) {
      Description desc = new Description();
      desc.setValue(entity.getDescription());
      uddi.getDescription().add(desc);
    }

    categoryRepo.findByEntityTypeAndEntityKey(EntityType.SERVICE, entity.getServiceKey()).forEach(cb -> {
      KeyedReference ref = new KeyedReference();
      ref.setTModelKey(cb.getTModelKey());
      ref.setKeyName(cb.getKeyName());
      ref.setKeyValue(cb.getKeyValue());
      uddi.getCategoryBag().getContent().add(
        new org.uddi.api_v3.ObjectFactory().createKeyedReference(ref)
      );
    });

    bindingRepo.findByService_ServiceKey(entity.getServiceKey()).forEach(b -> {
      uddi.getBindingTemplates().getBindingTemplate().add(toUddiBinding(b));
    });

    return uddi;
  }

  private BindingTemplate toUddiBinding(BindingTemplateEntity entity) {
    BindingTemplate uddi = new BindingTemplate();
    uddi.setBindingKey(entity.getBindingKey());
    uddi.setServiceKey(entity.getService().getServiceKey());

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

    tModelInstRepo.findByBinding_BindingKey(entity.getBindingKey()).forEach(ti -> {
      TModelInstanceInfo info = new TModelInstanceInfo();
      info.setTModelKey(ti.getTModelKey());
      if (ti.getInstanceDetails() != null) {
        InstanceDetails details = new InstanceDetails();
        org.uddi.api_v3.ObjectFactory of = new org.uddi.api_v3.ObjectFactory();
        details.getContent().add(of.createInstanceParms(ti.getInstanceDetails()));
        info.setInstanceDetails(details);
      }
      uddi.getTModelInstanceDetails().getTModelInstanceInfo().add(info);
    });

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

    categoryRepo.findByEntityTypeAndEntityKey(EntityType.TMODEL, entity.getTModelKey()).forEach(cb -> {
      KeyedReference ref = new KeyedReference();
      ref.setTModelKey(cb.getTModelKey());
      ref.setKeyName(cb.getKeyName());
      ref.setKeyValue(cb.getKeyValue());
      uddi.getCategoryBag().getContent().add(
        new org.uddi.api_v3.ObjectFactory().createKeyedReference(ref)
      );
    });

    overviewDocRepo.findByTModel_TModelKey(entity.getTModelKey()).forEach(od -> {
      OverviewDoc doc = new OverviewDoc();
      org.uddi.api_v3.ObjectFactory of = new org.uddi.api_v3.ObjectFactory();
      if (od.getDescription() != null) {
        Description description = new Description();
        description.setValue(od.getDescription());
        doc.getContent().add(of.createDescription(description));
      }
      if (od.getOverviewUrl() != null) {
        OverviewURL url = new OverviewURL();
        url.setValue(od.getOverviewUrl());
        doc.getContent().add(of.createOverviewURL(url));
      }
      uddi.getOverviewDoc().add(doc);
    });

    return uddi;
  }
}

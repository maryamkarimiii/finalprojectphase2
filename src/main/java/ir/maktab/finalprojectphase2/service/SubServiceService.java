package ir.maktab.finalprojectphase2.service;


import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.data.model.SubService;

import java.util.List;
import java.util.Map;

public interface SubServiceService extends BaseService<SubService> {

    SubService findEnableSubServiceByName(String subServiceName);
    SubService findDisableSubServiceByName(String subServiceName);

    List<SubService> findAllByServiceName(String serviceName);

    Map<Service, List<SubService>> findAllEnableSubService();

    Map<Service, List<SubService>> findAllDisableSubService();

    List<Expert> findSubServiceExpertsBySubServiceName(String subServiceName);

    boolean isExist(String subServiceName);

    void updateSubServicePrice(String subServiceName, double newPrice);

    void updateSubServiceDescription(String subServiceName, String newDescription);


    void softDelete(SubService subService);

    void activeDisableSubService(String subServiceName);

}

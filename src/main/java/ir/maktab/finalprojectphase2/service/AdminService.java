package ir.maktab.finalprojectphase2.service;


import ir.maktab.finalprojectphase2.data.model.Admin;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.data.model.SubService;

import java.util.List;
import java.util.Map;

public interface AdminService {
    void save(Admin admin);

    boolean isExistByUsername(String username);

    void addExpertToSubService(String expertUsername, String subServiceName);

    void deleteExpertFromSubService(String expertUsername, String subServiceName);

    void addNewService(Service service);

    void addNewSubService(SubService subService);

    List<Service> seeALLEnableService();

    Map<Service, List<SubService>> seeALLEnableSubService();

    Map<Service, List<SubService>> seeALLDisableSubService();

    List<Service> seeAllDisableService();

    SubService findDisableSubServiceByName(String subServiceName);

    void activeDisableService(String serviceName);

    List<Expert> seeAllExpertWithNewExpertRegistrationStatus();

    void deleteExistenceService(Service service);

    void deleteExistenceSubService(SubService subService);

    Service findDisableServiceByName(String serviceName);

    void confirmExpert(String expertUsername);

    void activeDisableSubService(String subServiceName);

    void updateSubServicePrice(String subServiceName, double newPrice);

    void updateSubServiceDescription(String subServiceName, String newDescription);
}

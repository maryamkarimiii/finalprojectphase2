package ir.maktab.finalprojectphase2.service.impl;


import ir.maktab.finalprojectphase2.data.dao.AdminDao;
import ir.maktab.finalprojectphase2.data.enums.ExpertRegistrationStatus;
import ir.maktab.finalprojectphase2.data.enums.Role;
import ir.maktab.finalprojectphase2.data.model.Admin;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.data.model.SubService;
import ir.maktab.finalprojectphase2.exception.DuplicationException;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.AdminService;
import ir.maktab.finalprojectphase2.service.ExpertService;
import ir.maktab.finalprojectphase2.service.ServiceService;
import ir.maktab.finalprojectphase2.service.SubServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminDao adminDao;
    private final ServiceService serviceService;
    private final SubServiceService subServiceService;
    private final ExpertService expertService;

    @Override
    public void addNewService(Service service) {
        if (serviceService.isExist(service.getName()))
            throw new DuplicationException(String.format("the service (%s) is already exist", service.getName()));
        serviceService.save(service);
    }

    @Override
    public void addNewSubService(SubService subService) {
        if (subServiceService.isExist(subService.getName()))
            throw new DuplicationException(String.format("the subService (%s) is already exist", subService.getName()));
        subServiceService.save(subService);
    }

    @Override
    public List<Service> seeALLEnableService() {
        return serviceService.findAllEnableService();
    }

    @Override
    public List<Service> seeAllDisableService() {
        return serviceService.findAllDisableService();
    }

    @Override
    public Map<Service, List<SubService>> seeALLEnableSubService() {
        return subServiceService.findAllEnableSubService();
    }

    @Override
    public Map<Service, List<SubService>> seeALLDisableSubService() {
        return subServiceService.findAllDisableSubService();
    }

    @Override
    public Service findDisableServiceByName(String serviceName) {
        return serviceService.findDisableServiceByName(serviceName);
    }

    @Override
    public SubService findDisableSubServiceByName(String subServiceName) {
        return subServiceService.findDisableSubServiceByName(subServiceName);
    }

    @Override
    public List<Expert> seeAllExpertWithNewExpertRegistrationStatus() {
        return expertService.findAllWithNewExpertRegistrationStatus();
    }

    @Override
    public void deleteExistenceService(Service service) {
        if (!serviceService.isExist(service.getName()))
            throw new NotFoundException(String.format("the service (%s) dose not exist", service.getName()));
        serviceService.softDelete(service);
    }

    @Override
    public void deleteExistenceSubService(SubService subService) {
        if (!subServiceService.isExist(subService.getName()))
            throw new NotFoundException(String.format("the subService (%s) dose not exist", subService.getName()));
        subServiceService.softDelete(subService);
    }

    @Override
    public void confirmExpert(String expertUsername) {
        Expert expert = expertService.findActiveExpertByUsername(expertUsername);
        expert.setExpertRegistrationStatus(ExpertRegistrationStatus.CONFIRMED);
        expertService.update(expert);
    }

    @Override
    public void activeDisableSubService(String subServiceName) {
        subServiceService.activeDisableSubService(subServiceName);
    }

    @Override
    public void activeDisableService(String serviceName) {
        serviceService.activeDisableService(serviceName);
    }

    @Override
    public boolean isExistByUsername(String username) {
        return adminDao.existsByUsername(username);
    }

    @Override
    @Transactional
    public void addExpertToSubService(String expertUsername, String subServiceName) {
        if (!expertService.isExist(expertUsername))
            throw new NotFoundException("the expert dose not exist");
        if (!subServiceService.isExist(subServiceName))
            throw new NotFoundException("the subService dose not exist");
        Expert expert = expertService.findActiveExpertByUsername(expertUsername);
        SubService subService = subServiceService.findEnableSubServiceByName(subServiceName);
        subService.getExpertSet().add(expert);
        subServiceService.update(subService);
    }

    @Override
    @Transactional
    public void deleteExpertFromSubService(String expertUsername, String subServiceName) {
        if (!subServiceService.isExist(subServiceName))
            throw new NotFoundException("the subService dose not exist");
        SubService subService = subServiceService.findEnableSubServiceByName(subServiceName);
        boolean result = subService.getExpertSet().removeIf(expert -> expert.getUsername().equals(expertUsername));
        if (!result)
            throw new NotFoundException
                    (String.format("the expert with this username %s dose not exist in subService", expertUsername));

        subServiceService.update(subService);
    }

    @Override
    public void updateSubServicePrice(String subServiceName, double newPrice) {
        subServiceService.updateSubServicePrice(subServiceName, newPrice);
    }

    @Override
    public void updateSubServiceDescription(String subServiceName, String newDescription) {
        subServiceService.updateSubServiceDescription(subServiceName, newDescription);
    }

    public void save(Admin admin) {
        admin.setRole(Role.ADMIN);
        adminDao.save(admin);
    }
}

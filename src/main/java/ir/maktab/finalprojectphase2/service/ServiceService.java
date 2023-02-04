package ir.maktab.finalprojectphase2.service;


import ir.maktab.finalprojectphase2.data.model.Service;

import java.util.List;
public interface ServiceService extends BaseService<Service> {

    Service findEnableServiceByName(String serviceName);
    Service findDisableServiceByName(String serviceName);

    List<Service> findAllEnableService();

    List<Service> findAllDisableService();

    boolean isExist(String serviceName);

    void activeDisableService(String serviceName);

    void softDelete(Service service);
}

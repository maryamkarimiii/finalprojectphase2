package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.ServiceDao;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.ServiceService;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServiceService {
    private final ServiceDao serviceDao;

    @Override
    public void save(Service service) {
        serviceDao.save(service);
    }

    @Override
    public void update(Service service) {
        serviceDao.save(service);
    }


    @Override
    public Service findEnableServiceByName(String serviceName) {
        return serviceDao.findByNameAndDisable(serviceName, false)
                .orElseThrow(() -> new NotFoundException(String.format("the %s service dose not exist", serviceName)));
    }

    @Override
    public Service findDisableServiceByName(String serviceName) {
        return serviceDao.findByNameAndDisable(serviceName, true)
                .orElseThrow(() -> new NotFoundException(String.format("the %s service dose not exist", serviceName)));
    }

    @Override
    public List<Service> findAllEnableService() {
        return serviceDao.findAllByDisable(false).stream()
                .sorted(Comparator.comparing(Service::getName))
                .toList();
    }

    @Override
    public List<Service> findAllDisableService() {
        return serviceDao.findAllByDisable(true).stream()
                .sorted(Comparator.comparing(Service::getName))
                .toList();
    }


    @Override
    public boolean isExist(String serviceName) {
        return serviceDao.existsByName(serviceName);
    }

    @Override
    public void activeDisableService(String serviceName) {
        Service service = findDisableServiceByName(serviceName);
        service.setDisable(false);
        update(service);
    }

    @Override
    public void softDelete(Service service) {
        service.setDisable(true);
        serviceDao.save(service);
    }
}

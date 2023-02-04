package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.SubServiceDao;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Service;
import ir.maktab.finalprojectphase2.data.model.SubService;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.service.SubServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class SubServiceServiceImpl implements SubServiceService {
    private final SubServiceDao subServiceDao;
    private static final String ERROR_TEXT = "the %s subService dose not exist";


    @Override
    public void save(SubService subService) {
        subServiceDao.save(subService);
    }

    @Override
    public SubService findEnableSubServiceByName(String subServiceName) {
        return subServiceDao.findByNameAndDisable(subServiceName, false)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_TEXT, subServiceName)));
    }

    @Override
    public SubService findDisableSubServiceByName(String subServiceName) {
        return subServiceDao.findByNameAndDisable(subServiceName, true)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_TEXT, subServiceName)));
    }

    @Override
    public List<SubService> findAllByServiceName(String serviceName) {
        return subServiceDao.findAllByServiceNameAndDisableFalse(serviceName);
    }

    @Override
    public Map<Service, List<SubService>> findAllEnableSubService() {
        return subServiceDao.findAllByDisable(false).stream()
                .collect(Collectors.groupingBy(SubService::getService));
    }

    @Override
    public Map<Service, List<SubService>> findAllDisableSubService() {
        return subServiceDao.findAllByDisable(true).stream()
                .collect(Collectors.groupingBy(SubService::getService));
    }

    @Override
    public boolean isExist(String subServiceName) {
        return subServiceDao.existsByName(subServiceName);
    }

    @Override
    public void update(SubService subService) {
        subServiceDao.save(subService);
    }


    @Override
    public void updateSubServicePrice(String subServiceName, double newPrice) {
        SubService subService = findEnableSubServiceByName(subServiceName);
        subService.setBaseAmount(newPrice);
        subServiceDao.save(subService);
    }

    @Override
    public void updateSubServiceDescription(String subServiceName, String newDescription) {
        SubService subService = findEnableSubServiceByName(subServiceName);
        subService.setDescription(newDescription);
        subServiceDao.save(subService);
    }

    @Override
    public List<Expert> findSubServiceExpertsBySubServiceName(String subServiceName) {
        return findEnableSubServiceByName(subServiceName).getExpertSet().stream().toList();
    }

    @Override
    public void softDelete(SubService subService) {
        subService.setDisable(true);
        subServiceDao.save(subService);
    }

    @Override
    public void activeDisableSubService(String subServiceName) {
        SubService subService = findDisableSubServiceByName(subServiceName);
        subService.setDisable(false);
        update(subService);
    }

}

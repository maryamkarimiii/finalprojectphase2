package ir.maktab.finalprojectphase2.service;


import ir.maktab.finalprojectphase2.data.dto.ChangePasswordDTO;
import ir.maktab.finalprojectphase2.data.model.Expert;
import ir.maktab.finalprojectphase2.data.model.Offer;
import ir.maktab.finalprojectphase2.data.model.Order;

import java.io.IOException;
import java.util.List;

public interface ExpertService extends BaseService<Expert> {
    Expert login(String username, String password);

    boolean isExist(String username);

    Expert findActiveExpertByUsername(String username);

    Expert findDeActiveExpertByUsername(String username);

    void setExpertImage(Expert expert, String filePath) throws IOException;

    void getExpertImage(String username, String destinationFile) throws IOException;

    void changePassword(ChangePasswordDTO changePasswordDTO,String username);

    void calculateAndUpdateExpertScore(Expert expert);

    void softDelete(Expert expert);

    List<Expert> findAllWithNewExpertRegistrationStatus();

    List<Order> findExpertsRelatedOrders(String expertUsername);

    void createOffer(Offer offer);
}

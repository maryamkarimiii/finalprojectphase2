package ir.maktab.finalprojectphase2.service.impl;

import ir.maktab.finalprojectphase2.data.dao.ExpertDao;
import ir.maktab.finalprojectphase2.data.dto.ChangePasswordDTO;
import ir.maktab.finalprojectphase2.data.enums.ExpertRegistrationStatus;
import ir.maktab.finalprojectphase2.data.enums.Role;
import ir.maktab.finalprojectphase2.data.model.*;
import ir.maktab.finalprojectphase2.exception.EmptyCollectionException;
import ir.maktab.finalprojectphase2.exception.NotCorrectInputException;
import ir.maktab.finalprojectphase2.exception.NotFoundException;
import ir.maktab.finalprojectphase2.exception.ValidationException;
import ir.maktab.finalprojectphase2.service.CommentService;
import ir.maktab.finalprojectphase2.service.ExpertService;
import ir.maktab.finalprojectphase2.service.OfferService;
import ir.maktab.finalprojectphase2.service.OrderService;
import ir.maktab.finalprojectphase2.validation.ImageValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpertServiceImpl implements ExpertService{
    private final ExpertDao expertDao;
    private final OrderService orderService;
    private final OfferService offerService;
    private final CommentService commentService;

    @Override
    public void save(Expert expert) {
        expert.setUsername(expert.getEmail());
        expert.setRole(Role.EXPERT);
        expert.setExpertRegistrationStatus(ExpertRegistrationStatus.NEW);
        expert.setTotalScore(0.0);
        expert.setWallet(new Wallet(0.0));
        expertDao.save(expert);
    }

    @Override
    public boolean isExist(String username) {
        return expertDao.existsByUsername(username);
    }

    @Override
    public Expert login(String username, String password) {
        return expertDao.findByUsernameAndDisable(username, false)
                .filter(expert -> expert.getPassword().equals(password))
                .filter(expert -> expert.getExpertRegistrationStatus().equals(ExpertRegistrationStatus.CONFIRMED))
                .orElseThrow(() -> new NotCorrectInputException
                        (String.format("the %s username or %s password is incorrect,or you not confirm", username, password)));
    }

    @Override
    public Expert findActiveExpertByUsername(String username) {
        return expertDao.findByUsernameAndDisable(username, false)
                .orElseThrow(() -> new NotFoundException(String.format("the username %s dose not exist", username)));
    }

    @Override
    public Expert findDeActiveExpertByUsername(String username) {
        return expertDao.findByUsernameAndDisable(username, true)
                .orElseThrow(() -> new NotFoundException(String.format("the username %s dose not exist", username)));
    }

    @Override
    public List<Expert> findAllWithNewExpertRegistrationStatus() {
        return expertDao.findAllByExpertRegistrationStatus(ExpertRegistrationStatus.NEW);
    }

    @Override
    public List<Order> findExpertsRelatedOrders(String expertUsername) {
        Set<SubService> subServiceSet = findActiveExpertByUsername(expertUsername).getSubServiceSet();
        return orderService.findAllBySubServiceAndOrderStatus(subServiceSet.stream().toList());
    }

    @Override
    public void createOffer(Offer offer) {
        if (offerService.isExistByOfferId(offer.getOfferId()))
            throw new ValidationException
                    (String.format("you can put offer for this order %s just once", offer.getOfferId().getOrder()));
        offerService.save(offer);
    }

    @Override
    public void update(Expert expert) {
        expertDao.save(expert);
    }


    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, String username) {
        Expert expert = findActiveExpertByUsername(username);
        if (!expert.getPassword().equals(changePasswordDTO.getOldPassword()))
            throw new NotCorrectInputException("the password is not correct");
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))
            throw new NotCorrectInputException("the new password and confirm password are not same");
        expert.setPassword(changePasswordDTO.getNewPassword());
        expertDao.save(expert);
    }

    @Override
    public void calculateAndUpdateExpertScore(Expert expert) {
        Double averageOfExpertScore = commentService.averageOfExpertScore(expert);
        expert.setTotalScore(averageOfExpertScore);
        update(expert);
    }

    @Override
    public void softDelete(Expert expert) {
        expert.setDisable(true);
        expertDao.save(expert);
    }

    @Override
    public void setExpertImage(Expert expert, String filePath) throws IOException {
        ImageValidator.validateFileExistence(filePath);
        ImageValidator.validateFilePass(filePath);
        ImageValidator.validateImageSize(filePath);
        String imageToBase64 = encodeImageToBase64(filePath);
        expert.setImage(imageToBase64);
        update(expert);
    }

    @Override
    public void getExpertImage(String username, String destinationFile) throws IOException {
        Expert expert = findActiveExpertByUsername(username);
        String image = expert.getImage();
        decodeBase64ToImage(image, destinationFile);
    }

    private String encodeImageToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private void decodeBase64ToImage(String encodedImage, String destinationFile) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        FileUtils.writeByteArrayToFile(new File(destinationFile), decodedBytes);
    }
}

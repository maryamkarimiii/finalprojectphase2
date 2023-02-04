package ir.maktab.finalprojectphase2.data.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}

package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ChangePasswordController {

    @Autowired
    UserInfoController infoController;

    @Autowired
    UserService userService;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public String updatePassword() {

        if (!newPassword.equals(confirmPassword)){
            return "/ui/changepassword.jsf?faces-redirect=true&mismatch=true";
        }

        String username = infoController.getUsername();

        try {
            userService.updatePassword(username, oldPassword, newPassword);
        } catch (Exception e) {
            return "/ui/changepassword.jsf?faces-redirect=true&error=true";
        }
        return "/ui/profile.jsf?faces-redirect=true&updatedpassword=true";
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

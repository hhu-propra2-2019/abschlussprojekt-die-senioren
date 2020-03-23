package mops.gruppen1.applicationService;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private boolean isValid;
    @Getter
    private List<String> errorMessages;

    public ValidationResult() {
        isValid = true;
        errorMessages = new ArrayList<String>();
    }

    public boolean isValid() {
        return isValid;
    }

    public void addError(String error) {
        errorMessages.add(error);
        this.isValid = false;
    }
}

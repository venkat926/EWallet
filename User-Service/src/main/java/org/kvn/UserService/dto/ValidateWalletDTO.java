package org.kvn.UserService.dto;

public class ValidateWalletDTO {
    private boolean isValidWallet;
    private boolean doesWalletHasEnoughAmount;

    // constructor
    public ValidateWalletDTO(boolean isValidWallet, boolean doesWalletHasEnoughAmount) {
        this.isValidWallet = isValidWallet;
        this.doesWalletHasEnoughAmount = doesWalletHasEnoughAmount;
    }

    // Getters and Setters
    public boolean isDoesWalletHasEnoughAmount() {
        return doesWalletHasEnoughAmount;
    }

    public void setDoesWalletHasEnoughAmount(boolean doesWalletHasEnoughAmount) {
        this.doesWalletHasEnoughAmount = doesWalletHasEnoughAmount;
    }

    public boolean isValidWallet() {
        return isValidWallet;
    }

    public void setValidWallet(boolean validWallet) {
        isValidWallet = validWallet;
    }
}

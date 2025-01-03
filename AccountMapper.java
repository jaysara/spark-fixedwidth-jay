package com.test.filecompare;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class AccountMapper {

    public static DestAccount mapAccountToDestAccount(Account account) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        // Convert Account to JSON
        String json = mapper.writeValueAsString(account);

        // Convert JSON to DestAccount
        DestAccount destAccount = mapper.readValue(json, DestAccount.class);

        return destAccount;
    }

    public static void main(String[] args) throws JsonProcessingException {
        // Create a sample Account object
        Account account = new Account();
        account.setAccountId(123456);
        account.setEndDate("2023-06-30");
        account.setEcoaCde("ABC");

        // Map Account to DestAccount
        DestAccount destAccount = mapAccountToDestAccount(account);

        // Print the mapped DestAccount
        System.out.println("Mapped DestAccount:");
        System.out.println("Account ID: " + destAccount.getAccountId());
        System.out.println("End Date: " + destAccount.getEndDate());
        System.out.println("ECOA Code: " + destAccount.getEcoaCde());
    }
}

class Account {
    @JsonProperty("acct_id")
    private long accountId;
    @JsonProperty("end_dte")
    private String endDate;
    @JsonProperty("ecoa_cde")
    private String ecoaCde;

    // Getters and setters
    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getEcoaCde() { return ecoaCde; }
    public void setEcoaCde(String ecoaCde) { this.ecoaCde = ecoaCde; }
}

class DestAccount {
    private long accountId;
    private String endDate;
    private String ecoaCde;

    // Getters and setters
    public long getAccountId() { return accountId; }
    public void setAccountId(long accountId) { this.accountId = accountId; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getEcoaCde() { return ecoaCde; }
    public void setEcoaCde(String ecoaCde) { this.ecoaCde = ecoaCde; }
}

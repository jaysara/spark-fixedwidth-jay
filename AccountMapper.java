package com.test.cobol;

import java.lang.reflect.Field;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountMapper {

    public static void mapSrcToDestJson(SrcAccount src, DestAccount dest) {
        Class<?> srcClass = src.getClass();
        Class<?> destClass = dest.getClass();

        Field[] srcFields = srcClass.getDeclaredFields();

        for (Field srcField : srcFields) {
            srcField.setAccessible(true);
            try {
                // Get the @JsonProperty annotation value
                JsonProperty annotation = srcField.getAnnotation(JsonProperty.class);
                if (annotation != null) {
                    String destFieldName = annotation.value();

                    Field destField = destClass.getDeclaredField(destFieldName);
                    destField.setAccessible(true);

                    Object value = srcField.get(src);
                    destField.set(dest, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("Mapping failed for field: " + srcField.getName());
            }
        }
    }
    public static void mapSrcToDest(SrcAccount src, DestAccount dest) {
        Class<?> srcClass = src.getClass();
        Class<?> destClass = dest.getClass();

        Field[] srcFields = srcClass.getDeclaredFields();

        for (Field srcField : srcFields) {
            srcField.setAccessible(true);
            try {
                String srcFieldName = srcField.getName();
                String destFieldName = camelToSnake(srcFieldName);

                Field destField = destClass.getDeclaredField(destFieldName);
                destField.setAccessible(true);

                Object value = srcField.get(src);
                destField.set(dest, value);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                System.out.println("Mapping failed for field: " + srcField.getName());
            }
        }
    }

    private static String camelToSnake(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static void main(String[] args) {
        // Example usage
        SrcAccount src = new SrcAccount();
        src.setAccountId(12345);
        src.setEndDate("2025-01-01");
        src.setEcoaCde("EC01");

        DestAccount dest = new DestAccount();
        mapSrcToDestJson(src, dest);

        System.out.println(dest);
    }
}

// Source Account POJO
class SrcAccount {
    @JsonProperty("acct_id")
    private long accountId;

    @JsonProperty("end_dte")
    private String endDate;

    @JsonProperty("ecoa_cde")
    private String ecoaCde;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEcoaCde() {
        return ecoaCde;
    }

    public void setEcoaCde(String ecoaCde) {
        this.ecoaCde = ecoaCde;
    }
}

// Destination Account POJO
class DestAccount {
    private long acct_id;
    private String end_dte;
    private String ecoa_cde;

    public long getAcct_id() {
        return acct_id;
    }

    public void setAcct_id(long acct_id) {
        this.acct_id = acct_id;
    }

    public String getEnd_dte() {
        return end_dte;
    }

    public void setEnd_dte(String end_dte) {
        this.end_dte = end_dte;
    }

    public String getEcoa_cde() {
        return ecoa_cde;
    }

    public void setEcoa_cde(String ecoa_cde) {
        this.ecoa_cde = ecoa_cde;
    }

    @Override
    public String toString() {
        return "DestAccount{" +
                "acct_id=" + acct_id +
                ", end_dte='" + end_dte + '\'' +
                ", ecoa_cde='" + ecoa_cde + '\'' +
                '}';
    }
}


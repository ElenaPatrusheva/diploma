package ru.netology.diploma.db;

public interface DBManager {
    String getCreditResult();
    String getPaymentResult();
    void cleanDB();
}

package ru.netology.diploma.db;

import lombok.SneakyThrows;
import lombok.var;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

public class PostgresManager implements DBManager {

    @SneakyThrows
    @Override
    public String getCreditResult() {
        var runner = new QueryRunner();
        MapListHandler handler = new MapListHandler();
        try (var connection = getConnection()) {
            List<Map<String, Object>> query = runner.query(connection, "select status from credit_request_entity", handler);
            return (String) query.get(0).get("status");
        }
    }

    @SneakyThrows
    @Override
    public String getPaymentResult() {
        var runner = new QueryRunner();
        MapListHandler handler = new MapListHandler();
        try (var connection = getConnection()) {
            List<Map<String, Object>> query = runner.query(connection, "select status from payment_entity", handler);
            return (String) query.get(0).get("status");
        }
    }

    @SneakyThrows
    @Override
    public void cleanDB() {
        var runner = new QueryRunner();
        try (var connection = getConnection()) {
            runner.update(connection, "delete from credit_request_entity");
            runner.update(connection, "delete from payment_entity");
        }
    }

    @SneakyThrows
    private Connection getConnection() {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/diploma", "user", "password");
    }
}

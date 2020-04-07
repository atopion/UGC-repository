package com.atopion.UGC_repository.sql;

import java.util.LinkedList;

public class SQLResponseObject {

    private LinkedList<String> headers;
    private LinkedList<String> types;
    private LinkedList<LinkedList<String>> rows;

    public SQLResponseObject() {
        this.headers = new LinkedList<>();
        this.types = new LinkedList<>();
        this.rows = new LinkedList<>();
    }

    public LinkedList<String> getHeaders() {
        return headers;
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }

    public LinkedList<String> getTypes() {
        return types;
    }

    public void addType(String type) {
        this.types.add(type);
    }

    public LinkedList<LinkedList<String>> getRows() {
        return rows;
    }

    public void addRow(LinkedList<String> row) {
        this.rows.add(row);
    }
}

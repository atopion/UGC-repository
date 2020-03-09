package com.atopion.UGC_repository.sql;

import java.util.LinkedList;

public class SQLResponseObject {

    private LinkedList<String> headers;
    private LinkedList<LinkedList<String>> rows;

    public SQLResponseObject() {
        this.headers = new LinkedList<>();
        this.rows = new LinkedList<>();
    }

    public SQLResponseObject(LinkedList<String> headers, LinkedList<LinkedList<String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    public LinkedList<String> getHeaders() {
        return headers;
    }

    public void setHeaders(LinkedList<String> headers) {
        this.headers = headers;
    }

    public void addHeader(String header) {
        this.headers.add(header);
    }

    public LinkedList<LinkedList<String>> getRows() {
        return rows;
    }

    public void setRows(LinkedList<LinkedList<String>> rows) {
        this.rows = rows;
    }

    public void addRow(LinkedList<String> row) {
        this.rows.add(row);
    }
}

package com.atopion.UGC_repository.rest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JSONSerializer {

    public static String serialize(ResultSet set, String tableName) throws SQLException {
        StringBuilder result = new StringBuilder();
        ResultSetMetaData metaData = set.getMetaData();

        result.append("{\"table\":\"").append(tableName).append("\",\"data\":[");
        while(set.next()) {
            result.append('{');
            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                result.append('"').append(metaData.getColumnName(i)).append("\":");
                result.append('"').append(set.getString(i)).append('"');
                result.append(i == metaData.getColumnCount() ? "": ',');
            }
            result.append(set.isLast() ? '}' : "},");
        }
        result.append(']').append('}');

        return result.toString();
    }
}

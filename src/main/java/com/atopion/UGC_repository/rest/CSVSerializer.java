package com.atopion.UGC_repository.rest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CSVSerializer {

    public static String serialize(ResultSet set) throws SQLException {
        StringBuilder result = new StringBuilder();
        ResultSetMetaData metaData = set.getMetaData();

        for(int i = 1; i <= metaData.getColumnCount(); i++)
            result.append(mask(metaData.getColumnName(i))).append(i == metaData.getColumnCount() ? "" : ",");

        while(set.next()) {
            result.append("\n");
            for(int i = 1; i <= metaData.getColumnCount(); i++)
                result.append(mask(set.getString(i))).append(i == metaData.getColumnCount() ? "": ',');
        }

        return result.toString();
    }

    private static String mask(String str) {
        if(!str.contains("\"") && !str.contains(","))
            return str;
        else
            return "\"" + str.replace("\"", "\\\"") + "\"";
    }
}

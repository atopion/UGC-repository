package com.atopion.UGC_repository.rest;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class XMLSerializer {

    public static String serialize(ResultSet set, String tableName) throws SQLException {
        StringBuilder result = new StringBuilder();
        ResultSetMetaData metaData = set.getMetaData();

        result.append("<response><table>").append(tableName).append("</table><data>");
        while(set.next()) {
            result.append("<element>");
            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                result.append('<').append(metaData.getColumnName(i)).append(">");
                result.append(set.getString(i));
                result.append("</").append(metaData.getColumnName(i)).append(">");
            }
            result.append("</element>");
        }
        result.append("</data></response>");

        return result.toString();
    }
}

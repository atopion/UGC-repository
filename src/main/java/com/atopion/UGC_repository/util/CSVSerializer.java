package com.atopion.UGC_repository.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static String serialize(List<?> entities, List<String> headers) {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < headers.size(); i++)
            result.append(mask(headers.get(i))).append(i == headers.size() -1 ? "" : ",");

        for(Object entity : entities) {
            result.append("\n");
            Method[] methods = entity.getClass().getDeclaredMethods();
            for (String head : headers) {
                for (Method method : methods) {
                    if (method.getName().toLowerCase().startsWith("get") && method.getParameterCount() == 0 &&
                        method.getName().toLowerCase().replace("_", "").equals("get" + head.toLowerCase().replace("_", ""))) {
                        try {
                            if(method.getReturnType().equals(Date.class)) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                result.append(mask(format.format(method.invoke(entity)))).append(",");
                            } else {
                                result.append(mask(method.invoke(entity).toString())).append(",");
                            }
                        } catch (IllegalAccessException | InvocationTargetException ignored) {
                            System.out.println("CSVSerializer: Could not access method: " + method.getName());
                        }
                    }
                }
            }
            result.setLength(result.length() - 1);
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

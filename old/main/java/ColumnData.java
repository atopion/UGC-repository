package com.atopion.UGC_repository.util;

public class ColumnData {

    public static final int TYPE_VARCHAR = 1, TYPE_INT = 2, TYPE_DATETIME = 3;

    public String columnName = "";

    public boolean isNullable = false;

    public boolean isAutoIncrement = false;

    public int type = TYPE_VARCHAR;

    public ColumnData(String columnName, boolean isNullable, boolean isAutoIncrement, int type) {
        this.columnName = columnName;
        this.isNullable = isNullable;
        this.isAutoIncrement = isAutoIncrement;
        this.type = type;
    }

    public ColumnData() {}
}

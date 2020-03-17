package com.atopion.UGC_repository.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

import static java.util.Map.entry;

@Component
@EnableScheduling
public class TableData {

    private Logger logger = LoggerFactory.getLogger(TableData.class);

    private DataSource restDataSource;

    private HashMap<String, ArrayList<ColumnData>> tables;

    @Autowired
    public TableData(@Qualifier("restDataSource") DataSource restDataSource) {
        this.restDataSource = restDataSource;
        tables = findAllTables();

        if(tables == null) {
            tables = (HashMap<String, ArrayList<ColumnData>>) Map.ofEntries(
                    entry("applications", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("users", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("administrators", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("app_cuby_main", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("app_sammlungsportal_main", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("content_comments", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("content_liked_fields", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("content_likes", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("content_lists", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("content_lists_records", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0)))),
                    entry("content_annotations", new ArrayList<>(Arrays.asList(new ColumnData("", false, false, 0))))
            );
        }
    }

    public HashMap<String, ArrayList<ColumnData>> getTables() {
        return tables;
    }

    // Rebuild the table dataset every 15 minutes
    @Scheduled(fixedDelay = 900000L)
    public void rebuildTablesMap() {
        this.tables = findAllTables();
    }

    private HashMap<String, ArrayList<ColumnData>> findAllTables() {
        HashMap<String, ArrayList<ColumnData>> tables = new HashMap<>();
        try {
            DatabaseMetaData metaData = restDataSource.getConnection().getMetaData();
            ResultSet tableSet = metaData.getTables(null, null, null, new String[] {"TABLE"});
            while (tableSet.next()) {
                String tableName = tableSet.getString("TABLE_NAME");
                ResultSet columnSet = metaData.getColumns(null, null, tableName, null);
                ArrayList<ColumnData> columns = new ArrayList<>(columnSet.getMetaData().getColumnCount());
                while(columnSet.next()) {
                    ColumnData dat = new ColumnData();
                    dat.columnName = columnSet.getString("COLUMN_NAME");
                    dat.isNullable = columnSet.getString("IS_NULLABLE").equals("YES");
                    dat.type       = columnSet.getString("TYPE_NAME").equals("INT") ? ColumnData.TYPE_INT :
                                    (columnSet.getString("TYPE_NAME").equals("VARCHAR") ? ColumnData.TYPE_VARCHAR :
                                    (columnSet.getString("TYPE_NAME").equals("DATETIME") ? ColumnData.TYPE_DATETIME : -1));
                    dat.isAutoIncrement = columnSet.getString("IS_AUTOINCREMENT").equals("YES");
                    columns.add(dat);
                }
                tables.put(tableName, columns);
            }
            return tables;
        } catch (SQLException ex) {
            logger.error("Could not find the tables");
            return null;
        }
    }
}

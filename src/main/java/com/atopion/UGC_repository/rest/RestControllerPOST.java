package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.util.ColumnData;
import com.atopion.UGC_repository.util.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.swing.text.Document;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

@RestController
public class RestControllerPOST {

    private Logger logger = LoggerFactory.getLogger(RestControllerPOST.class);

    private DataSource restDataSource;

    private TableData tables;

    @Autowired
    public RestControllerPOST(@Qualifier("restDataSource") DataSource restDataSource, TableData tables) {
        this.restDataSource = restDataSource;
        this.tables = tables;
    }

    @PostMapping(path = "/rest-old/{table}", consumes = "application/json")
    public ResponseEntity<String> postNewEntryJSON(@PathVariable String table, @RequestBody Map<String, Object> data) {
        return new ResponseEntity<>("", postData(table, data));
    }

    @PostMapping(path = "/rest-old/{table}", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> postNewEntryXFORM(@PathVariable String table, @RequestParam Map<String, Object> data) {
        return new ResponseEntity<>("", postData(table, data));
    }


    private HttpStatus postData(String table, Map<String, Object> data) {

        if(!tables.getTables().containsKey(table))
            return HttpStatus.FORBIDDEN;

        ArrayList<ColumnData> tab = tables.getTables().get(table);

        StringBuilder query = new StringBuilder("INSERT INTO " + table + " (");
        StringBuilder vals  = new StringBuilder();

        LinkedList<String> cols = new LinkedList<>();

        for (ColumnData columnData : tab) {

            if (!columnData.isAutoIncrement) {
                String tmp = (String) data.getOrDefault(columnData.columnName, null);
                if(tmp != null || columnData.isNullable) {
                    cols.add(tmp);
                    query.append(columnData.columnName).append(", ");
                    vals.append("?, ");
                }
                else {
                    return HttpStatus.BAD_REQUEST;
                }
            }
        }
        query.setLength(query.length() -2);
        vals.setLength(vals.length() -2);
        query.append(") VALUES (").append(vals.toString()).append(");");

        try (Connection connection = restDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for(int i = 0; i < cols.size(); i++) {
                statement.setString((i+1), cols.get(i));
            }

            int numOfAffectedRows = statement.executeUpdate();
            logger.info("SQL Update executed. (Query: " + statement.toString() + ", affected rows: " + numOfAffectedRows + ")");

        } catch (SQLException ex) {
            logger.error("SQL Exception occurred: " + ex.getMessage());
            logger.info("Query was: " + query.toString());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}

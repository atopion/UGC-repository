package com.atopion.UGC_repository.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

@RestController
public class RestServiceController {

    private DataSource restDataSource;

    private final Logger logger = LoggerFactory.getLogger(RestServiceController.class);

    private LinkedList<String> tables;
    private ArrayList<String> formats;

    @Autowired
    public RestServiceController(@Qualifier("sqlDataSource") DataSource restDataSource) {
        this.restDataSource = restDataSource;
        tables = findAllTables();
        formats = new ArrayList<>(Arrays.asList("json", "xml", "csv"));
        if(tables == null) {
            tables = new LinkedList<>(Arrays.asList("applications", "users", "administrators", "app_cuby_main", "app_sammlungsportal_main", "content_comments", "content_liked_fields", "content_likes", "content_lists", "content_lists_records", "content_annotations"));
        }
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/rest/{table}",
            produces = { "application/json", "application/xml", "text/comma-separated-values" }
    )
    public String getTable(@PathVariable String table,
                           @RequestParam(name = "format", defaultValue = "json") String format) {

        if(!formats.contains(format))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Format: '" + format + "' not recognized. Known formats are: " + String.join(" ", formats));

        if(!tables.contains(table))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String query = "SELECT * FROM " + table + ";";

        try (Connection connection = restDataSource.getConnection();
             Statement sqlStatement = connection.createStatement();
             ResultSet set = sqlStatement.executeQuery(query)) {
            if(set != null) {
                switch (format) {
                    case "json":
                        return JSONSerializer.serialize(set, table);
                    case "xml":
                        return XMLSerializer.serialize(set, table);
                    case "csv":
                        return CSVSerializer.serialize(set);
                    default:
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (SQLException ex) {
            logger.error("SQL Exception occurred: " + ex.getMessage());
            logger.info("Query was: " + query);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private LinkedList<String> findAllTables() {
        LinkedList<String> tables = new LinkedList<>();
        try {
            DatabaseMetaData metaData = restDataSource.getConnection().getMetaData();
            ResultSet set = metaData.getTables(null, null, null, new String[] {"TABLE"});
            while (set.next()) {
                String tableName = set.getString("TABLE_NAME");
                tables.add(tableName);
            }
            return tables;
        } catch (SQLException ex) {
            logger.error("Could not find the tables");
            return null;
        }
    }
}

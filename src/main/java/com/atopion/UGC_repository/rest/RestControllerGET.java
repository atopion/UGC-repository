package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.util.TableData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@RestController
public class RestControllerGET {

    private DataSource restDataSource;

    private final Logger logger = LoggerFactory.getLogger(RestControllerGET.class);

    private ArrayList<String> formats;

    private TableData tables;

    @Autowired
    public RestControllerGET(@Qualifier("restDataSource") DataSource restDataSource, TableData tables) {
        this.tables = tables;
        this.restDataSource = restDataSource;
        formats = new ArrayList<>(Arrays.asList("json", "xml", "csv"));
    }

    @GetMapping(
            path = "/rest-old/{table}",
            produces = { "application/json", "application/xml", "text/plain" }
    )
    public ResponseEntity<String> getTable(@PathVariable String table,
                                   @RequestParam(name = "format", defaultValue = "json") String format) {

        if(!formats.contains(format))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Format '" + format + "' not recognized. Known formats are: " + String.join(" ", formats));

        if(!tables.getTables().containsKey(table))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String query = "SELECT * FROM " + table + ";";

        try (Connection connection = restDataSource.getConnection();
             Statement sqlStatement = connection.createStatement();
             ResultSet set = sqlStatement.executeQuery(query)) {
            return printResultSet(set, format, table);
        } catch (SQLException ex) {
            logger.error("SQL Exception occurred: " + ex.getMessage());
            logger.info("Query was: " + query);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(
            path = "/rest-old/{table}/{column}/{entry}",
            produces = {"text/plain", "application/json", "application/xml"}
    )
    private ResponseEntity<String> getEntryFromTable(@PathVariable String table, @PathVariable String entry, @PathVariable String column,
                                 @RequestParam(name = "format", defaultValue = "json") String format) {

        if(table.equals("") || column.equals("") || entry.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(!formats.contains(format))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Format '" + format + "' not recognized. Known formats are: " + String.join(" ", formats));

        if(!tables.getTables().containsKey(table))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        if(tables.getTables().get(table).stream().noneMatch(x -> x.columnName.equals(column)))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        String query = "SELECT * FROM " + table + " WHERE " + column + " = ?;";

        try (Connection connection = restDataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entry);

            try (ResultSet set = statement.executeQuery()) {
                return printResultSet(set, format, table);
            }
        } catch (SQLException ex) {
            logger.error("SQL Exception occurred: " + ex.getMessage());
            logger.info("Query was: " + query);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<String> printResultSet(ResultSet set, String format, String table) throws SQLException {
        if(set != null) {
            HttpHeaders header = new HttpHeaders();
            switch (format) {
                case "json":
                    header.setContentType(MediaType.APPLICATION_JSON);
                    return new ResponseEntity<>(JSONSerializer.serialize(set, table), header, HttpStatus.OK);
                case "xml":
                    header.setContentType(MediaType.APPLICATION_XML);
                    return new ResponseEntity<>(XMLSerializer.serialize(set, table), header, HttpStatus.OK);
                case "csv":
                    header.setContentType(MediaType.TEXT_PLAIN);
                    return new ResponseEntity<>(CSVSerializer.serialize(set), header, HttpStatus.OK);
                default:
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

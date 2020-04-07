package com.atopion.UGC_repository.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class SQLController {

    private DataSource sqlDataSource;

    private final Logger logger = LoggerFactory.getLogger(SQLController.class);

    @Autowired
    public SQLController(@Qualifier("sqlDataSource") DataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
    }


    @RequestMapping(method = GET, path = "/sql", produces = "text/html")
    public String sqlRequestHTML(@RequestParam(name = "query", defaultValue = "") String query, Model model) {

        SQLResponseObject queryResult = executeQuery(query);

        if(queryResult == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, query);
        }

        model.addAttribute("table_headers", queryResult.getHeaders());
        model.addAttribute("table_rows", queryResult.getRows());

        return "sqlResult";
    }

    @RequestMapping(method = GET, path = "/sql", produces = "application/json")
    public @ResponseBody String sqlRequestJSON(@RequestParam(name = "query", defaultValue = "") String query) {

        SQLResponseObject queryResult = executeQuery(query);

        if(queryResult == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, query);
        }

        return produceJSON(queryResult);
    }

    @RequestMapping(method = GET, path = "/sql", produces = "application/xml")
    public @ResponseBody String sqlRequestXML(@RequestParam(name = "query", defaultValue = "") String query) {

        SQLResponseObject queryResult = executeQuery(query);

        if(queryResult == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, query);
        }

        return produceXML(queryResult);
    }


    private SQLResponseObject executeQuery(String query) {

        if(query.equals(""))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Query cannot be empty.");

        // Securing the Query 1: Replacing the first word with SELECT (Limiting to SELECT Queries)
        query = "SELECT" + query.substring(query.indexOf(' ') == -1 ? query.length()-1 : query.indexOf(' '));

        // Securing the Query 2: Deleting everything after the first semicolon (Limiting to one query)
        query = query.substring(0, query.indexOf(';') == -1 ? query.length()-1 : query.indexOf(';'));

        SQLResponseObject result = new SQLResponseObject();


        try (Connection connection = sqlDataSource.getConnection();
             Statement sqlStatement = connection.createStatement();
             ResultSet set = sqlStatement.executeQuery(query)) {

            ResultSetMetaData metaData = set.getMetaData();

            for(int i = 1; i <= metaData.getColumnCount(); i++) {
                result.addHeader(metaData.getColumnName(i));
                result.addType(metaData.getColumnTypeName(i));
            }

            while(set.next()) {
                LinkedList<String> tmp = new LinkedList<>();
                for (int i = 1; i <= metaData.getColumnCount(); i++)
                    tmp.add(set.getString(i));
                result.addRow(tmp);
            }

        } catch (Exception ex) {
            logger.error("Exception occurred: " + ex.getMessage());
            logger.info("Query was: " + query);
            result = null;
        }

        return result;
    }

    private String produceJSON(SQLResponseObject object) {

        StringBuilder result = new StringBuilder();
        result.append('[');

        for(int j = 0; j < object.getRows().size(); j++) {
            result.append('{');
            for(int i = 0; i < object.getRows().get(j).size(); i++) {
                result.append('"').append(object.getHeaders().get(i))
                      .append("\":").append(object.getTypes().get(i).toLowerCase().contains("int") ? "" : "\"")
                      .append(object.getRows().get(j).get(i))
                      .append(object.getTypes().get(i).toLowerCase().contains("int") ? "" : "\"")
                      .append((i == object.getRows().get(j).size() -1 ? "" : ","));
            }
            result.append((j == object.getRows().size() -1 ? '}' : "},"));
        }
        result.append(']');

        return result.toString();
    }

    private String produceXML(SQLResponseObject object) {

        StringBuilder result = new StringBuilder();
        result.append("<response>\n");

        for(LinkedList<String> row : object.getRows()) {
            result.append("\t<element>\n");
            for(int i = 0; i < row.size(); i++) {
                result.append("\t\t<").append(object.getHeaders().get(i)).append('>')
                      .append(row.get(i))
                      .append("</").append(object.getHeaders().get(i)).append(">\n");
            }
            result.append("\t</element>\n");
        }
        result.append("</response>");

        return result.toString();
    }
}

package com.atopion.UGC_repository.sql;

import com.atopion.UGC_repository.DataSourceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.*;

@RestController
public class SQLController {

    @Qualifier("sqlDataSource")
    @Autowired
    private DataSource sqlDataSource;

    private final Logger logger = LoggerFactory.getLogger(SQLController.class);

    private final static String responseHTML_header = "<html><head></head><body>" +
            "<table BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=100%>";
    private final static String responseHTML_footer = "</table></body></html>";

    @RequestMapping(method = RequestMethod.GET, path = "/sql")
    public String sqlrequest(@RequestParam(name = "query", defaultValue = "") String request) {

        if(request.equals(""))
            return null;

        Connection connection = null;
        Statement sqlStatement = null;
        ResultSet set = null;
        StringBuilder result = new StringBuilder(responseHTML_header);

        try {
            connection = sqlDataSource.getConnection();
            sqlStatement = connection.createStatement();

            set = sqlStatement.executeQuery(request);
            ResultSetMetaData metaData = set.getMetaData();

            result.append("<tr>");
            for(int i = 1; i <= metaData.getColumnCount(); i++)
                result.append("<th>").append(metaData.getColumnName(i)).append("</th>");
            result.append("</tr>");

            while(set.next()) {
                result.append("<tr>");
                for(int i = 1; i <= metaData.getColumnCount(); i++)
                    result.append("<td><center>").append(set.getString(i)).append("</center></th>");
                result.append("</tr>");
            }

            result.append(responseHTML_footer);

        } catch (SQLException ex) {
            logger.error("SQLException: " + ex.getMessage());
        } finally {
            try {
                if (set != null)
                    set.close();
                if (sqlStatement != null)
                    sqlStatement.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result.toString();
    }
}

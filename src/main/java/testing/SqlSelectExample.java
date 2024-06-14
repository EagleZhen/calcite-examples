package testing;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.SqlParseException;

public class SqlSelectExample {
    public static void main(String[] args) throws SqlParseException {
        String sqlQuery = "SELECT column1, column2 FROM \"table\" WHERE column3 > 0 GROUP BY column2 ORDER BY column1 DESC LIMIT 10 OFFSET 5"; // replace with your SQL query

        SqlParser sqlParser = SqlParser.create(sqlQuery);
        SqlNode sqlNode = sqlParser.parseQuery();

        if (sqlNode instanceof SqlSelect) {
            SqlSelect sqlSelect = (SqlSelect) sqlNode;

            System.out.println("Select list: " + sqlSelect.getSelectList());
            System.out.println("From clause: " + sqlSelect.getFrom());
            System.out.println("Where clause: " + sqlSelect.getWhere());
            System.out.println("Group by clause: " + sqlSelect.getGroup());
            System.out.println("Having clause: " + sqlSelect.getHaving());
            System.out.println("Order by clause: " + sqlSelect.getOrderList());
            System.out.println("Fetch clause: " + sqlSelect.getFetch());
            System.out.println("Offset clause: " + sqlSelect.getOffset());
            System.out.println("Is distinct: " + sqlSelect.isDistinct());
        } else {
            System.out.println("The SQL query is not a valid SELECT statement.");
        }
    }
}

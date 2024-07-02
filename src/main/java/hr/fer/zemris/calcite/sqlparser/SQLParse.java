package hr.fer.zemris.calcite.sqlparser;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.util.SourceStringReader;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.validate.SqlConformanceEnum;

public class SQLParse {
    public static void main(String[] args) throws SqlParseException {
        SqlParser sqlParser = SqlParser.create(
                new SourceStringReader(args[0]), // Reads the SQL query from the first command-line argument
                SqlParser.config() // Configures the parser
                        .withParserFactory(SqlParserImpl.FACTORY)
                        .withQuoting(Quoting.DOUBLE_QUOTE) // Sets the quoting to double quotes, i.e. identifiers (such as table and column names) can be quoted using double quotes.
                        .withUnquotedCasing(Casing.TO_UPPER)
                        .withQuotedCasing(Casing.UNCHANGED)
                        .withConformance(SqlConformanceEnum.DEFAULT) // Calcite's default SQL behavior - IDK what this refers to exactly though
        );
        SqlNode sqlNode = sqlParser.parseQuery(); // This would generate an abstract syntax tree (AST) for the SQL query, could set a breakpoint here and use a debugger to check the values of the nodes in the AST
        System.out.println(sqlNode.toString());
    }
}

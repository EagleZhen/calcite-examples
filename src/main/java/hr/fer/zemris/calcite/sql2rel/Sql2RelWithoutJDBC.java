package hr.fer.zemris.calcite.sql2rel;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.adapter.java.ReflectiveSchema;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.util.SourceStringReader;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

// This SQL query works: 'SELECT * FROM multidb."medinfo"'

public class Sql2RelWithoutJDBC {
    public static void main(String[] args) throws Exception {
        // Establish a connection to the database
        Connection connection = DriverManager.getConnection("jdbc:calcite:");
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);

        // Add the custom schema
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        rootSchema.add("CUSTOM_SCHEMA", new ReflectiveSchema(new CustomSchema()));
        System.out.println(rootSchema.toString());

        // Create a configuration for the planner, including the schema,
        FrameworkConfig config = Frameworks.newConfigBuilder()
                .defaultSchema(rootSchema.getSubSchema("CUSTOM_SCHEMA"))
//                .parserConfig(SqlParser.configBuilder()  // to configure how calcite handles the casing
//                        .setQuoting(Quoting.DOUBLE_QUOTE)
//                        .setUnquotedCasing(Casing.TO_LOWER)
//                        .setQuotedCasing(Casing.UNCHANGED)
//                        .build())
                .build();

        // The behavior of the planner is defined by the configuration. Rules, schemes and everything else are defined in it.
        Planner planner = Frameworks.getPlanner(config);

        // Use a sample SQL query
        String sql = args[0];
        System.out.println("\nSQL query:\n" + sql);

        SqlNode sqlNode = planner.parse(new SourceStringReader(sql));
        System.out.println("\nParsed SQL query:\n" + sqlNode.toString());

        // Validate the SQL query, i.e. whether syntactically correct and semantically valid.
        sqlNode = planner.validate(sqlNode);
        System.out.println("\nValidated SQL query:\n" + sqlNode.toString());

        // Convert the SQL query to a relational expression
        RelRoot relRoot = planner.rel(sqlNode);
        System.out.println("\nRelational expression:\n" + relRoot.toString());

        // Print the relational expression with hierarchy
        System.out.println("\nRelational expression with hierarchy:\n" + RelOptUtil.toString(relRoot.rel));

        // Project the relational expression
        RelNode relNode = relRoot.project();
        System.out.println("\nProjected relational expression:\n" + RelOptUtil.toString(relNode));
    }
}

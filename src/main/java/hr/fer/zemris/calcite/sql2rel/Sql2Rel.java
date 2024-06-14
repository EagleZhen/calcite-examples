package hr.fer.zemris.calcite.sql2rel;

import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.apache.calcite.jdbc.CalciteConnection;

import org.apache.calcite.schema.SchemaPlus;

import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.RelNode;

import org.apache.calcite.util.SourceStringReader;

import java.sql.DriverManager;
import java.sql.Connection;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.calcite.adapter.jdbc.JdbcSchema;

import org.apache.calcite.plan.RelOptUtil;

// This SQL query works: 'SELECT * FROM multidb."medinfo"'

public class Sql2Rel {
    public static void main(String[] args) throws Exception {
        // Establish a connection to the database
        Connection connection = DriverManager.getConnection("jdbc:calcite:");
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);

        // Add a schema to the root schema
        SchemaPlus rootSchema = calciteConnection.getRootSchema();
        final DataSource ds = JdbcSchema.dataSource(
                "jdbc:postgresql://localhost:5432/multidb",
                "org.postgresql.Driver",
                "postgres",
                "");
        rootSchema.add("MULTIDB", JdbcSchema.create(rootSchema, "MULTIDB", ds, null, null));
        System.out.println(rootSchema.toString());

        // Create a configuration for the planner, including the schema,
        FrameworkConfig config = Frameworks.newConfigBuilder()
                .defaultSchema(rootSchema)
                .build();

        // The behavior of the planner is defined by the configuration. Rules, schemes and everything else are defined in it.
        Planner planner = Frameworks.getPlanner(config);

        // Input SQL query from the first command-line argument & parse it
        SqlNode sqlNode = planner.parse(new SourceStringReader(args[0]));
        System.out.println("Parsed SQL query: " + sqlNode.toString());

        // Validate the SQL query, i.e. whether syntactically correct and semantically valid.
        sqlNode = planner.validate(sqlNode);
        System.out.println("Validated SQL query: " + sqlNode.toString());

        // Convert the SQL query to a relational expression
        RelRoot relRoot = planner.rel(sqlNode);
        System.out.println("Relational expression: " + relRoot.toString());

        // Project the relational expression
        RelNode relNode = relRoot.project();
        System.out.println("Projected relational expression: " + RelOptUtil.toString(relNode));
    }
}

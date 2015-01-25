package cz.muni.pa165.surrealtravel;

import java.util.Objects;
import org.apache.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This test rule logs all exceptions thrown from the tests.
 * @author Roman Lacko [396157]
 */
public class ExceptionLoggingRule implements TestRule {

    private final Logger logger;

    private Statement mkstatement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } catch (Exception ex) {
                    logger.error("Unhandled exception caught: ", ex);
                    throw ex;
                }
            }
        };
    }

    public ExceptionLoggingRule(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public Statement apply(Statement stmnt, Description d) {
        return mkstatement(stmnt);
    }

}

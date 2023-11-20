package seng202.team7.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Runner class for cucumber tests (using junit 5)
 * features must be located in src/test/resources/features
 * step definitions must be located in src/test/java/seng202/team7/cucumber
 * publish is set to quiet in test/resources/junit-platform.properties
 * @author Kendra van Loon
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:build/reports/tests/cucumber/feature-report.html"},
        glue = "seng202.team7.cucumber.StepDefinitions",
        features = "src/test/resources/features",
        snippets = CucumberOptions.SnippetType.CAMELCASE)
public class RunCucumberTest {

}


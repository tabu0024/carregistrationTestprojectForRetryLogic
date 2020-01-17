package testRunner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "@targettt/rerun.txt", //Cucumber picks the failed scenarios from this file
        format = {"pretty", "html:target2/site/cucumber-pretty",
                "json:target2/cucumber.json"},
        glue = { "webDriver", "stepDefinition", "FailedScenarios" }
)
public class FailedScenarios {

}
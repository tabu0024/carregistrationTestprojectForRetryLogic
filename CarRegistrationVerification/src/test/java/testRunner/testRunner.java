package testRunner;

import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumber;


//@RunWith(Cucumber.class)
//@CucumberOptions(
//        features = {"featureFiles"},
//        glue = {"stepDefinitions"},
//        plugin ={"rerun:target/rerun.txt"}
//)


@RunWith(ExtendedCucumber.class)

@ExtendedCucumberOptions(
        jsonReport = "target/cucumber.json",
        retryCount =3,
        detailedReport = true,
        overviewReport = true,
        coverageReport = true,
        jsonUsageReport = "target/cucumber-usage.json",
        usageReport = true,
        toPDF=true,
        includeCoverageTags = {"@test1"},
        outputFolder = "target")

@CucumberOptions( strict = true,
        features = {"featureFiles"},
        glue = {"stepDefinitions"},
        plugin ={ "html:target/cucumber-html-report",
                "json:target/cucumber.json", "pretty:target/cucumber-pretty.txt",
                "usage:target/cucumber-usage.json", "junit:target/cucumber-results.xml" },
        tags = {"@test1"},
        monochrome = true
)
public class testRunner { }

package stepDefinitions;

import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;

public class MyStepdefs {

    private final By Coverstart = By.xpath("//*[@id=\"page-container\"]/div[4]/div[2]");
    private final By Coverend = By.xpath("//*[@id=\"page-container\"]/div[4]/div[3]");
    public String date;
    private WebDriver driver;
    private String header = "#page #header [title='Volkswagen Financial Services']";
    private String title = "Volkswagen Financial Services";
    private String url = "https://covercheck.vwfsinsuranceportal.co.uk";
    private String inputBox = "#vehicleReg";
    private String searchButton = ".track-searchTT";
    private String correctRegNumber = "OV12UYY";
    private String errorMessage = "#page-container > div.result";
    private String userMsg = ".dlg-dealersearch-control .error-required";

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    @Given("^I go to mysite$")
    public void openBrowser() {
        String browser = System.getProperty("BROWSER");
        String exePath;

        if (browser == null) {
            browser = System.getenv("BROWSER");
            if (browser == null) {
                browser = "chrome";
            }
        }
        switch (browser) {
            case "chrome":
                exePath = "selenium/chromedriverX";
               System.setProperty("webdriver.chrome.driver", exePath);
                driver = new ChromeDriver();
                break;
            case "firefox":
                 exePath = "selenium/geckodriver";
               System.setProperty("webdriver.gecko.driver", exePath);
                driver = new FirefoxDriver();
                break;
            default:
                driver = new ChromeDriver();
                exePath = "selenium/chromedriverX";
                System.setProperty("webdriver.chrome.driver", exePath);                break;
        }
            driver.manage().deleteAllCookies();
            driver.get(url);
            driver.manage().window().maximize();
    }

    @Given("^I am on Drive Away insurance Page$")
    public void iAmOnDriveAwayInsurancePage() throws Throwable {
        String checkTitle = driver.findElement(By.cssSelector(header)).toString();
        Assert.assertThat("Incorrect page", checkTitle, containsString(title));
    }

    @When("^I enter correct registration number$")
    public void iEnterCorrectRegistrationNumber() throws Throwable {
        Actions builder = new Actions(driver);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement input = driver.findElement(By.cssSelector(inputBox));
        Actions seriesOfAction = builder.moveToElement(input).click().sendKeys(input, correctRegNumber);
        seriesOfAction.perform();
    }

    @And("^I click on find vehicle button$")
    public void iClickOnFindVehicleButton() throws Throwable {
        driver.findElement(By.cssSelector(searchButton)).click();

    }

    @Then("^I verify Cover Start date$")
    public void iVerifyCoverStartDate() throws Throwable {
        driver.findElement(Coverstart).isDisplayed();
    }

    @And("^I verify Cover end date$")
    public void iVerifyCoverEndDate() throws Throwable {
        driver.findElement(Coverend).isDisplayed();
    }

    @And("^date format is correct$")
    public void dateFormatIsCorrect() throws Throwable {
        Assert.assertTrue("invalid date format", isValidDate(verifyDate(Coverstart)));
        Assert.assertTrue("invalid date format", isValidDate(verifyDate(Coverend)));
    }

    @And("^end date is less Then start date$")
    public void endDateIsLessThenstartDate() throws Throwable {
        DateFormat format = new SimpleDateFormat("dd MMM yyyy");

        String check[] = verifyEndDate(Coverstart, Coverend);
        String startDate = check[0];
        String endDate = check[1];

        Date sDate = format.parse(startDate);
        Date eDate = format.parse(endDate);

        Assert.assertTrue("end date cannot be less than startr date", eDate.after(sDate));
    }

    @And("^start date is not future date$")
    public void startDateIsNotFutureDate() throws Throwable {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date currentDate = new Date();

        String formattedDate = dateFormat.format(currentDate);
        Date curDate = dateFormat.parse(formattedDate);

        String check[] = verifyEndDate(Coverstart, Coverend);
        String startDate = check[0];

        Date sDate = dateFormat.parse(startDate);

        Assert.assertTrue("start date cannot be future date", sDate.before(curDate));
    }

    @And("^end date is past date$")
    public void endDateIsPastDate() throws Throwable {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);
        DateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Date curDate = format.parse(formattedDate);

        String check[] = verifyEndDate(Coverstart, Coverend);
        String endDate = check[1];

        Date eDate = format.parse(endDate);

        Assert.assertTrue("Registration has expired", eDate.before(curDate));
    }

    @When("^I enter incorrect \"([^\"]*)\"$")
    public void iEnterIncorrectRegisterationNumber(String regNumber) throws Throwable {
        Actions builder = new Actions(driver);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement input = driver.findElement(By.cssSelector(inputBox));
        Actions seriesOfAction = builder.moveToElement(input).click().sendKeys(input, regNumber);
        seriesOfAction.perform();
    }

    @Then("^I see error message$")
    public void iSeeErrorMessage() throws Throwable {
        String errMsg = driver.findElement(By.cssSelector(errorMessage)).getText();
        Assert.assertThat("invalid input", errMsg, containsString("Sorry record not found"));
    }

    @Then("^I see user message to enter registration number$")
    public void iSeeUserMessageToEnterRegistrationNumber() throws Throwable {
        String usrMsg = driver.findElement(By.cssSelector(userMsg)).getText();

        Assert.assertThat("invalid input", usrMsg, containsString("Please enter a valid car registration"));
    }

    String[] verifyEndDate(By startDate, By endDate) throws ParseException {
        String dates[] = new String[2];

        String start_Date = verifyDate(startDate);
        String end_Date = verifyDate(endDate);
        dates[0] = start_Date;
        dates[1] = end_Date;
        return dates;
    }

    public String verifyDate(By data) {
        String checkDate = driver.findElement(data).getText();
        String[] checkDate3 = checkDate.split(": ");
        date = checkDate3[1];
        return date;
    }

    @After
    public void iCloseTheBrowser() throws Throwable {
        driver.close();
    }
}

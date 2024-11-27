import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, monochrome = true, features = {"src/test/java/features"}, glue = {"definitions"}, tags = "@example")
public class TestRunner {
}

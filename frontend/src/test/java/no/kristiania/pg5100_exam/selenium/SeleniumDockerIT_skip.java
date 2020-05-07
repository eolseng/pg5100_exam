package no.kristiania.pg5100_exam.selenium;

import no.kristiania.pg5100_exam.Application;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.nio.file.Paths;

@ContextConfiguration(initializers = SeleniumDockerIT_skip.DockerInitializer.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SeleniumDockerIT_skip extends SeleniumTestBase {

    private static String PG5100_EXAM_HOST_ALIAS = "pg5100_exam-host";
    private static String PG_ALIAS = "postgresql-host";

    public static Network network = Network.newNetwork();

    public static GenericContainer postgres = new GenericContainer("postgres:10")
            .withExposedPorts(5432)
            .withNetwork(network)
            .withNetworkAliases(PG_ALIAS)
            .withEnv("POSTGRES_HOST_AUTH_METHOD", "trust")
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("POSTGRES")));

    public static class DockerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            // Get the host:port on local host to connect to Postgres inside Docker
            String host = postgres.getContainerIpAddress();
            int port = postgres.getMappedPort(5432);

            TestPropertyValues.of(
                    "spring.datasource.url=jdbc:postgresql://" + host + ":" + port + "/postgres",
                    "spring.datasource.username=postgres",
                    "spring.datasource.password",
                    "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    public static GenericContainer spring = new GenericContainer(
            new ImageFromDockerfile("pg5100_exam")
                    .withFileFromPath("target/pg5100_exam-exec.jar",
                            Paths.get("target/pg5100_exam-exec.jar"))
                    .withFileFromPath("Dockerfile", Paths.get("Dockerfile")))
            .withExposedPorts(8080)
            .withNetwork(network)
            .withNetworkAliases(PG5100_EXAM_HOST_ALIAS)
            .waitingFor(Wait.forHttp("/"))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("PG5100_EXAM")));

    public static BrowserWebDriverContainer browser = (BrowserWebDriverContainer) new BrowserWebDriverContainer()
            .withCapabilities(new ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .withNetwork(network);

    @BeforeAll
    public static void startDocker(){
        postgres.start();
        spring.start();
        browser.start();
    }

    @AfterAll
    public static void stopDocker(){
        browser.stop();
        spring.stop();
        postgres.stop();
    }

    @Override
    protected WebDriver getDriver() {
        return browser.getWebDriver();
    }

    @Override
    protected String getServerHost() {
        return PG5100_EXAM_HOST_ALIAS;
    }

    @Override
    protected int getServerPort() {
        return 8080;
    }
}

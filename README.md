# Logging Module
This logging module provides a simple, reusable logging mechanism for Spring Boot applications. It uses Logback for logging and integrates with Logstash for centralized log collection and analysis. It also supports logging method details and custom log entries via an aspect-oriented programming (AOP) approach.

## Features
Method Logging: Logs method name, arguments, execution time, and result.
Custom Log Entry: Defines a structured LogEntry record to capture log details in a standardized format.
Integration with Logstash: Supports sending logs to Logstash for centralized processing.
File-based Logging: Supports logging to files, which can be monitored by external tools like Logstash.

## Prerequisites
Java 8 or higher
Maven 3.x or higher
Spring Boot 2.x or higher
Logstash (optional, for sending logs to a central server)

## Installation
1. Add Dependency
To use the logging module in your Spring Boot project, add the following dependency to your pom.xml file. If the module is published to Maven Central, use:

        <dependency>
            <groupId>com.ipaam</groupId>
            <artifactId>logging</artifactId>
            <version>1.0.0</version>
        </dependency>
If you are using it locally, make sure to install the JAR to your local Maven repository first or use a system-level dependency.

2. Import Logback Configuration
Include the logback-spring.xml configuration file from the logging module into the src/main/resources directory of your project. This configuration ensures that logs are properly formatted and sent to Logstash (if configured).

        <configuration>
            <appender name="logstash" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
                <destination>localhost:5044</destination> <!-- Logstash input address -->
                <encoder class="net.logstash.logback.encoder.LoggingEventJsonEncoder"/>
            </appender>
            <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
                <encoder>
                    <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} - %msg%n</pattern>
                </encoder>
            </appender>
            <root level="INFO">
                <appender-ref ref="logstash" />
                <appender-ref ref="console" />
            </root>
        </configuration>

3. Configure Logstash (Optional)
If you want to send logs to Logstash for centralized logging, configure Logstash to read the log files and forward them to Elasticsearch or another destination.
Example of logstash.conf:

        input {
            file {
                path => "/path/to/logs/application.log"
                start_position => "beginning"
            }
        }
        
        output {
            elasticsearch {
                hosts => ["http://localhost:9200"]
                index => "your-logs"
            }
        }
4. Enable AOP in Your Application
Ensure that AOP is enabled in your Spring Boot application by adding the @EnableAspectJAutoProxy annotation to your main application class.

        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.context.annotation.EnableAspectJAutoProxy;
        
        @SpringBootApplication
        @EnableAspectJAutoProxy
        public class Application {
            public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
            }
        }
## Usage
1. Log Method Details
To log method execution details, simply annotate your methods with @LogMethod:


    import com.example.logging.LogMethod;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;
    
    @RestController
    public class DemoController {

    @LogMethod
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }
}
2. Log Custom Entries
You can create custom log entries by using the LogEntry record. It captures method name, arguments, execution time, result, and other details.


    import com.example.logging.LogEntry;
    import org.springframework.stereotype.Service;

    @Service
    public class MyService {

    public void someMethod() {
        // Create a custom log entry
        LogEntry logEntry = new LogEntry("someMethod", new Object[]{"arg1", "arg2"}, 100, "Success", "johndoe", "192.168.0.1");
        System.out.println(logEntry);
    }}


3. Configure Logback for File-based Logging
If you don't want to use Logstash, you can configure Logback to log directly to a file by adjusting the logback-spring.xml configuration.

        <appender name="file" class="ch.qos.logback.core.FileAppender">
            <file>/path/to/logs/application.log</file>
            <encoder>
                <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        
        <root level="INFO">
            <appender-ref ref="file" />
        </root>
4. View Logs in Logstash
Once Logstash is configured to read your application logs, you can view logs in real-time via tools like Kibana if you're forwarding them to Elasticsearch.

5. Run Logstash
If you're using Logstash to collect and process your logs, start Logstash with the following command:
logstash -f /path/to/logstash.conf
This will start Logstash, which will collect logs from the specified file and output them to the configured destination (e.g., Elasticsearch).

## Configuration
You can configure the following properties in the logback-spring.xml file:
Log Level: Set the logging level globally (DEBUG, INFO, WARN, ERROR).
Log Output: Configure where the logs are written (to a file, console, or Logstash).
Log Format: Customize the log output format (e.g., JSON for Logstash).

## Additional Notes
Performance: Using Logstash for centralized logging can be resource-intensive. Ensure your Logstash setup is properly scaled for your use case.

Security: If you're sending logs to an external service like Elasticsearch, ensure that the connection is secure (e.g., use HTTPS, authenticate users, etc.).

Log Retention: Configure proper log retention policies in Logstash or Elasticsearch to manage log storage effectively.


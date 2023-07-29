# Introduction
The multi-module-microservice-creator is a Java-based script that allows user to quickly create a multi-module Spring Boot project using CLI.

This script simplifies the setup process and provides a foundation for building Spring Boot applications with multiple modules.

# Prerequisites
Ensure that you have the following:

* Java 17 or higher installed
* Spring 3.1.0 or above

# Getting Started
1. Clone the script in your local machine.
2. Run the script 
3. Input the project name, package name, and the number of modules as requested. 
4. After following these steps, the project structure and pom.xml files will be generated in the current directory with the provided project name and package name, along with the specified number of modules.

# Generated Project Structure
1. The tool will create a new directory with the project name you provided.
2. Inside the project directory, there will be subdirectories for each module you specified.
3. Each module will have its own pom.xml file and a basic structure for a Spring Boot application.

# Build and Test the project
1. After generating the project, navigate to the project directory in the terminal or any IDE of your choice.
2. Build the Spring Boot application using Maven:

   `mvn clean install`
3. Run the application in the IDE or using the generated JAR file:

   `java -jar target/YourProjectName-1.0.0.jar`
4. Access the application in your web browser by visiting http://localhost:8080/health.
5. Feel free to update the {java.version} in the parent pom.xml in the properties:
   `<properties>
   <maven.compiler.source>{java.version}</maven.compiler.source>
   <maven.compiler.target>{java.version}</maven.compiler.target>
   </properties>`

# Customization:

* You can customize the generated files by modifying the generate*Content methods in the ExecutableProjectGenerator class.
* Adjust the generateMainPomXml and generateModulePomXml methods to add or remove dependencies as needed.
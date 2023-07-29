import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ExecutableProjectGenerator {

    public static void createProjectStructure(String projectDirectory, String projectName, String packageName, int moduleCount) {
        // Create the main project directory
        File projectDir = new File(projectDirectory, projectName);
        projectDir.mkdir();
        System.setProperty("user.dir", projectDir.getAbsolutePath());

        List<String> moduleNames = new ArrayList<>();

        String javaVersion = System.getProperty("java.version");
        Set<String> enteredModuleNames = new HashSet<>();

        // Generate the module pom.xml files and content
        for (int i = 1; i <= moduleCount; i++) {
            String moduleName =  getUniqueModuleName(enteredModuleNames, i);
            moduleNames.add(moduleName);

            File moduleDir = new File(projectDir, moduleName);
            moduleDir.mkdir();

            if (i == 1) {
                // First module (module1) contains MainApplication and HealthCheckController
                generateModulePomXml(projectDirectory, projectName, moduleName, packageName, i);
                String mainApplicationContent = generateMainApplicationContent(packageName, moduleName);
                try {
                    writeToFile(new File(moduleDir, "src/main/java/" + packageName.replace('.', '/') + "/" + moduleName), "MainApplication.java", mainApplicationContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String healthCheckControllerContent = generateHealthCheckControllerContent(packageName, moduleName);
                try {
                    writeToFile(new File(moduleDir, "src/main/java/" + packageName.replace('.', '/') + "/" + moduleName + "/controllers"), "HealthCheckController.java", healthCheckControllerContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Modules other than module1 contain SampleClass
                generateModulePomXml(projectDirectory, projectName, moduleName, packageName, i);
                String sampleClassContent = generateSampleClassContent(packageName, moduleName);
                try {
                    writeToFile(new File(moduleDir, "src/main/java/" + packageName.replace('.', '/') + "/" + moduleName), "SampleClass.java", sampleClassContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Generate the main pom.xml file
        generateMainPomXml(projectDir.getAbsolutePath(), projectName, packageName, moduleNames, javaVersion);
    }

    public static String getUniqueModuleName(Set<String> enteredModuleNames, int moduleNumber) {
        Scanner scanner = new Scanner(System.in);
        String moduleNameRegex = "^[a-zA-Z0-9_-]+$";

        while (true) {
            System.out.print("Enter the name of module " + moduleNumber + " (Module name should only contain letters, digits, underscores, and hyphens): ");
            String moduleName = scanner.nextLine();

            if (!moduleName.matches(moduleNameRegex)) {
                System.out.println("Module name should only contain letters, digits, underscores, and hyphens. Please try again.");
            } else if (enteredModuleNames.contains(moduleName)) {
                System.out.println("Module name already exists. Please enter a unique module name.");
            } else {
                return moduleName;
            }
        }
    }

    public static String generateHealthCheckControllerContent(String packageName, String moduleName) {
        StringBuilder controllerContent = new StringBuilder();
        controllerContent.append("package ").append(packageName).append(".").append(moduleName).append(".controllers;\n\n");
        controllerContent.append("import org.springframework.web.bind.annotation.GetMapping;\n");
        controllerContent.append("import org.springframework.web.bind.annotation.RestController;\n\n");
        controllerContent.append("@RestController\n");
        controllerContent.append("public class HealthCheckController {\n\n");
        controllerContent.append("    @GetMapping(\"/health\")\n");
        controllerContent.append("    public String healthCheck() {\n");
        controllerContent.append("        return \"Service is up and running!\";\n");
        controllerContent.append("    }\n");
        controllerContent.append("}\n");

        return controllerContent.toString();
    }

    public static String generateMainApplicationContent(String packageName, String moduleName) {
        StringBuilder mainAppContent = new StringBuilder();
        mainAppContent.append("package ").append(packageName).append(".").append(moduleName).append(";\n\n");
        mainAppContent.append("import org.springframework.boot.SpringApplication;\n");
        mainAppContent.append("import org.springframework.boot.autoconfigure.SpringBootApplication;\n\n");
        mainAppContent.append("@SpringBootApplication\n");
        mainAppContent.append("public class MainApplication {\n\n");
        mainAppContent.append("    public static void main(String[] args) {\n");
        mainAppContent.append("        SpringApplication.run(MainApplication.class, args);\n");
        mainAppContent.append("    }\n");
        mainAppContent.append("}\n");

        return mainAppContent.toString();
    }

    public static String generateSampleClassContent(String packageName, String moduleName) {
        StringBuilder sampleClassContent = new StringBuilder();
        sampleClassContent.append("package ").append(packageName).append(".").append(moduleName).append(";\n\n");
        sampleClassContent.append("public class SampleClass {\n\n");
        sampleClassContent.append("    public void helloWorld() {\n");
        sampleClassContent.append("        System.out.println(\"Hello, World! This is ").append(moduleName).append(" module.\");\n");
        sampleClassContent.append("    }\n");
        sampleClassContent.append("}\n");

        return sampleClassContent.toString();
    }

    public static void generateMainPomXml(String projectDirectory, String projectName, String packageName, List<String> moduleNames, String javaVersion) {
        String majorJavaVersion = javaVersion.split("\\.")[0];
        StringBuilder mainPomContent = new StringBuilder();
        mainPomContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        mainPomContent.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        mainPomContent.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        mainPomContent.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n");
        mainPomContent.append("         http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        mainPomContent.append("    <modelVersion>4.0.0</modelVersion>\n");
        mainPomContent.append("    <groupId>").append(packageName).append("</groupId>\n");
        mainPomContent.append("    <artifactId>").append(projectName).append("</artifactId>\n");
        mainPomContent.append("    <version>1.0.0</version>\n");
        mainPomContent.append("    <packaging>pom</packaging>\n");
        mainPomContent.append("    <properties>\n");
        mainPomContent.append("        <maven.compiler.source>").append(majorJavaVersion).append("</maven.compiler.source>\n");
        mainPomContent.append("        <maven.compiler.target>").append(majorJavaVersion).append("</maven.compiler.target>\n");
        mainPomContent.append("    </properties>\n");
        mainPomContent.append("    <dependencies>\n");
        mainPomContent.append("        <dependency>\n");
        mainPomContent.append("            <groupId>org.springframework.boot</groupId>\n");
        mainPomContent.append("            <artifactId>spring-boot-starter-web</artifactId>\n");
        mainPomContent.append("            <version>2.5.5</version>\n");
        mainPomContent.append("            <exclusions>\n");
        mainPomContent.append("                <exclusion>\n");
        mainPomContent.append("                    <groupId>org.springframework.boot</groupId>\n");
        mainPomContent.append("                    <artifactId>spring-boot-starter-logging</artifactId>\n");
        mainPomContent.append("                </exclusion>\n");
        mainPomContent.append("                <exclusion>\n");
        mainPomContent.append("                    <groupId>ch.qos.logback</groupId>\n");
        mainPomContent.append("                    <artifactId>logback-classic</artifactId>\n");
        mainPomContent.append("                </exclusion>\n");
        mainPomContent.append("            </exclusions>\n");
        mainPomContent.append("        </dependency>\n");
        mainPomContent.append("    </dependencies>\n");
        mainPomContent.append("    <modules>\n");

        for (String moduleName : moduleNames) {
            mainPomContent.append("        <module>").append(moduleName).append("</module>\n");
        }

        mainPomContent.append("    </modules>\n");
        mainPomContent.append("</project>");

        try {
            writeToFile(new File(projectDirectory), "pom.xml", mainPomContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateModulePomXml(String projectDirectory, String projectName, String moduleName, String packageName, int moduleNumber) {
        StringBuilder modulePomContent = new StringBuilder();
        modulePomContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        modulePomContent.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n");
        modulePomContent.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        modulePomContent.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0\n");
        modulePomContent.append("         http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n");
        modulePomContent.append("    <modelVersion>4.0.0</modelVersion>\n");
        modulePomContent.append("    <parent>\n");
        modulePomContent.append("        <groupId>").append(packageName).append("</groupId>\n");
        modulePomContent.append("        <artifactId>").append(projectName).append("</artifactId>\n");
        modulePomContent.append("        <version>1.0.0</version>\n");
        modulePomContent.append("    </parent>\n");
        modulePomContent.append("    <groupId>").append(packageName).append(".").append(moduleName).append("</groupId>\n");
        modulePomContent.append("    <artifactId>").append(moduleName).append("</artifactId>\n");
        modulePomContent.append("    <packaging>jar</packaging>\n");
        modulePomContent.append("    <properties>\n");
        modulePomContent.append("    </properties>\n");
        modulePomContent.append("</project>\n");

        try {
            writeToFile(new File(projectDirectory + "/" + projectName + "/" + moduleName), "pom.xml", modulePomContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (moduleNumber == 1) {
            String packagePath = packageName.replace('.', '/');
            String mainApplicationContent = generateMainApplicationContent(packageName, moduleName);
            try {
                writeToFile(new File(projectDirectory + "/" + projectName + "/" + moduleName, "src/main/java/" + packagePath + "/" + moduleName), "MainApplication.java", mainApplicationContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String sampleClassContent = generateSampleClassContent(packageName, moduleName);
            try {
                writeToFile(new File(projectDirectory + "/" + projectName + "/" + moduleName, "src/main/java/" + packageName.replace('.', '/') + "/" + moduleName), "SampleClass.java", sampleClassContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToFile(File directory, String fileName, String content) throws IOException {
        File file = new File(directory, fileName);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    public static void main(String[] args) {
        // Read the project directory path, project name, package name, and number of modules from user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path of the directory where you want to create the project: ");
        String projectDirectoryPath = scanner.nextLine();
        System.out.print("Enter the project name: ");
        String projectName = scanner.nextLine();
        System.out.print("Enter the package name: ");
        String packageName = scanner.nextLine();
        System.out.print("Enter the number of modules: ");
        int moduleCount = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        createProjectStructure(projectDirectoryPath, projectName, packageName, moduleCount);

        System.out.println("Executable project structure and pom.xml files generated successfully!");
    }
}

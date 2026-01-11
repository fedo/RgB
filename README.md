# RgB - TEI Document Viewer

A GWT-based web application for viewing and processing TEI (Text Encoding Initiative) XML documents. Originally created for the "Tecnologie Web 2010" course at University of Bologna, now modernized for current Java and GWT versions.

## Tech Stack

- **Java 17** (LTS)
- **GWT 2.11.0** (Google Web Toolkit)
- **Maven** for build management
- **Jetty 11** for development server
- **Saxon 12** for XSLT processing
- **Jakarta EE 6** Servlet API

## Prerequisites

- **Java 17+** - Download from [Adoptium](https://adoptium.net/) or use SDKMAN
- **Maven 3.8+** - Download from [Apache Maven](https://maven.apache.org/download.cgi)

### Verify Installation

```bash
java -version   # Should show 17 or higher
mvn -version    # Should show 3.8 or higher
```

## Quick Start

### 1. Compile the project

```bash
mvn clean compile
```

### 2. Run GWT compilation

```bash
mvn gwt:compile
```

### 3. Start the development server

```bash
mvn jetty:run
```

Then open http://localhost:8080/RgB/ in your browser.

## Development Mode

For faster development with GWT Super Dev Mode:

### Terminal 1 - Start the code server:
```bash
mvn gwt:codeserver
```

### Terminal 2 - Start the backend server:
```bash
mvn jetty:run
```

Then open http://localhost:8080/RgB/ and the GWT dev mode will allow live reloading of client-side changes.

## Build Commands

| Command | Description |
|---------|-------------|
| `mvn clean compile` | Compile Java sources |
| `mvn gwt:compile` | Compile GWT to JavaScript |
| `mvn package` | Build WAR file |
| `mvn jetty:run` | Run development server |
| `mvn gwt:codeserver` | Start GWT Super Dev Mode |
| `mvn clean package -Pprod` | Production build (optimized) |

## Project Structure

```
RgB/
├── pom.xml                    # Maven build configuration
├── src/
│   └── java/
│       └── it/unibo/cs/rgb/
│           ├── gwt/
│           │   ├── Main.gwt.xml      # GWT module descriptor
│           │   ├── RgB.java          # Utility class
│           │   └── client/
│           │       └── MainEntryPoint.java  # GWT entry point
│           ├── servlet/              # Backend servlets
│           └── tei/                  # TEI processing classes
├── web/
│   ├── welcomeGWT.html        # Host page
│   ├── rgb.css                # Application styles
│   ├── stylesheets/           # XSLT stylesheets
│   ├── collection6/           # TEI document collection
│   └── WEB-INF/
│       └── web.xml            # Servlet configuration
```

## Features

- **Document Viewer** - Browse and view TEI XML documents
- **Witness Comparison** - Compare different document witnesses
- **Stemma Codicum** - Generate document relationship trees
- **Concordance Extraction** - Search and extract text concordances
- **Differentiation** - Compare document versions with diff highlighting

## Configuration

### Changing the document collection

Edit `DocumentsList.java` to change the TEI folder:
```java
String teiFolder = "/collection6";  // Change to your collection
```

### Server port

The default port is 8080. To change it, modify `pom.xml`:
```xml
<httpConnector>
    <port>8080</port>  <!-- Change this -->
</httpConnector>
```

## Deployment

### Build a WAR file

```bash
mvn clean package -Pprod
```

The WAR file will be in `target/rgb-2.0.0.war`. Deploy to any Jakarta EE compatible server:
- Apache Tomcat 10+
- Eclipse Jetty 11+
- WildFly 27+

## Troubleshooting

### GWT compilation errors

If you see "Cannot find module", ensure the GWT module file path is correct:
```bash
mvn gwt:compile -Dgwt.module=it.unibo.cs.rgb.gwt.Main
```

### Java version issues

Ensure JAVA_HOME points to Java 17+:
```bash
export JAVA_HOME=/path/to/java17
mvn clean compile
```

### Port already in use

```bash
# Find and kill process on port 8080
lsof -i :8080
kill -9 <PID>
```

## Migration Notes

This project was modernized from:
- GWT 1.7 → GWT 2.11.0
- Java 6 → Java 17
- javax.servlet → jakarta.servlet
- Ant/NetBeans → Maven

## License

University project - educational purposes.

## Authors

- Original: fedo (2010)
- Modernization: 2026

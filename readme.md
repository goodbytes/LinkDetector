# LinkDetector

A Java utility that lets you detect where in plain text a hyperlink begins and ends.

Correctly identifying the start and end location of a link in text can be tricky, especially when those links either 
_include_ or are _surrounded by_ parenthesis, followed by a comma, and so on:
- `https://example.org/foo,` instead of `https://example.org/foo` (from a text like `... on https://example.org/foo, where ...`).
- `https://example.org/foo)` instead of `https://example.org/foo` (from a text like `... the webpage (https://example.org/foo) where ...` ).

These often lead to browsers being opened to invalid URLs, causing end-users to see 404 pages or other errors.

This project simplifies parsing the correct start and end of links in text, which helps avoid such issues.

## Usage

The distributable is available through the Maven central repository. You can then define this project to be a dependency of your project, like so:

```xml
<dependency>
    <groupId>nl.goodbytes.util</groupId>
    <artifactId>linkdetector</artifactId>
    <version>1.0.0</version> <!-- Please remember to check if this is the latest, as this example could be outdated. -->
</dependency>
```

To use the utility in your code, invoke the `parse` method of the `LinkDetector` class, as shown below. This will split
up the text in fragments (returned in a list). For each fragment, a start and end index is provided, and defines if it does or does
not represent a link.

```java
final String input = "Please find more information in the corresponding page on "
    + "Wikipedia (https://en.wikipedia.org/wiki/Ambiguity_(disambiguation)). Let me "
    + "know if you have questions!";

final List<Fragment> fragments = LinkDetector.parse(input);

for (final Fragment fragment : fragments)
{
    System.out.println("Fragment starting at index " + fragment.startIndex()
        + ", ending at index " + fragment.endIndex() + " (exclusive) "
        + (fragment.isLink() ? "is" : "is not") + " a link:");
    System.out.println("\t" + fragment);
    System.out.println();
}
```

The example code above generates the following output:
```
Fragment starting at index 0, ending at index 69 (exclusive) is not a link:
	Please find more information in the corresponding page on Wikipedia (

Fragment starting at index 69, ending at index 125 (exclusive) is a link:
	https://en.wikipedia.org/wiki/Ambiguity_(disambiguation)

Fragment starting at index 125, ending at index 162 (exclusive) is not a link:
	). Let me know if you have questions!
```

## Build / Compilation

This project should be compatible with any version of Java that is not _ancient_. It _should_ be compatible with 
Java 1.4, but to circumvent some issues with modern build tooling, its project descriptor defines 1.8.

The project can be built using standard [Maven](https://maven.apache.org/) invocations, like this:

```bash
mvn clean package
```

The project does not use any external dependencies (although for testing, the [JUnit](https://junit.org/) library is
added to the test scope of the build process).

## Attribution
This is but a simple Java wrapper around a regular expression that was provided by [Wiktor Kwapisiewicz](https://metacode.biz/@wiktor).

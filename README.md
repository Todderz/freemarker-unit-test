freemarker-unit-test
====================

JUnit Rule for easy Freemarker template unit tests

See SampleTest.java for an example.

Using a JUnit Rule, easily load and process a Freemarker template against a model, get the output as

1. text
2. an HtmlPage for use with HtmlUnit
3. an XML Source allowing use of Hamcrest XML matchers to make assertions against the output

It's a simple as:

Declaring the rule to load a named template:

@Rule
public FreemarkerTestRule template = new FreemarkerTestRule("src/test/java", "sample.ftl");


Then getting the html response for a given model, managed into xml:

Source theXml = template.xmlResponseFor(model);

Then making assertions against the xml:

assertThat(theXml, hasXPath("//td[@id='this-one']", containsString("Expected")));


Some further details at https://cleantestcode.wordpress.com/2014/06/01/unit-testing-freemarker-templates/

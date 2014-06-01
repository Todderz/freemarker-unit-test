package com.github.freemarkerunittest;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.DateTimeConstants.SUNDAY;
import static org.xmlmatchers.XmlMatchers.hasXPath;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SampleTest {

	private static final String CHECKED_RADIO_VALUE = "//input[@type='radio' and @checked='checked']/@value";

	private static final DateTimeFormatter yyyymmdd = DateTimeFormat.forPattern("yyyy/MM/dd");

	@Rule
	public FreemarkerTestRule template = new FreemarkerTestRule("src/test/java", "sample.ftl");

	private Map<String, Object> model;

	private DateTime sunday = new DateTime().withDayOfWeek(SUNDAY);
	private DateTime monday = sunday.plusDays(1);
	private DateTime tuesday = sunday.plusDays(2);

	@Before
	public void setUp() throws Exception {

		model = new HashMap<String, Object>();

		ExamDate first = unavailable(sunday.toDate());
		ExamDate second = availableWithSurcharge(monday.toDate());
		ExamDate third = availableNoSurcharge(tuesday.toDate());

		model.put("examDates", asList(first, second, third));
	}

	private ExamDate unavailable(Date date) {
		ExamDate cd1 = new ExamDate();
		cd1.setDeskAvailable(false);
		cd1.setExtraFee(true);
		cd1.setDate(date);
		return cd1;
	}

	private ExamDate availableWithSurcharge(Date date) {
		ExamDate cd2 = new ExamDate();
		cd2.setDeskAvailable(true);
		cd2.setExtraFee(true);
		cd2.setDate(date);
		return cd2;
	}

	private ExamDate availableNoSurcharge(Date date) {
		ExamDate cd3 = new ExamDate();
		cd3.setDeskAvailable(true);
		cd3.setExtraFee(false);
		cd3.setDate(date);
		return cd3;
	}
	

	@Test
	public void shouldGetString() throws Exception {
		System.out.println(template.stringResponseFor(model));
	}

	@Test
	public void shouldGetHtmlPage() throws Exception {
		System.out.println(template.htmlPageResponseFor(model).asText());
	}

	@Test
	public void shouldGetHtmlPageAsXml() throws Exception {
		System.out.println(template.htmlPageResponseFor(model).asXml());
	}

	@Test
	public void shouldGetTextPage() throws Exception {
		System.out.println(template.textPageResponseFor(model).getContent());
	}

	@Test
	public void shouldGetXHtmlPage() throws Exception {
		System.out.println(template.xhtmlPageResponseFor(model).asText());
	}

	@Test
	public void shouldGetXHtmlPageAsXml() throws Exception {
		System.out.println(template.xhtmlPageResponseFor(model).asXml());
	}

	@Test
	public void shouldGetStringIntoXmlWithHamcrest() throws Exception {
		Source theXml = template.xmlResponseFor(model);
		System.out.println(theXml.toString());
	}

	@Test
	public void shouldSelectTheFirstAvailableDate() throws Exception {

		Source theXml = template.xmlResponseFor(model);

		String mondayAsText = yyyymmdd.print(monday);

		assertThat(theXml, hasXPath(CHECKED_RADIO_VALUE, equalTo(mondayAsText)));
	}
}

package com.github.freemarkerunittest;

import static java.util.Arrays.asList;
import static org.xmlmatchers.transform.XmlConverters.the;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Locale;

import javax.xml.transform.Source;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.PageCreator;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class FreemarkerTestRule implements TestRule {

	private String templateName;
	private String templatePath;

	private Template template;

	private NameValuePair textContentHeader = new NameValuePair("Content-Type", "text/");
	private String anyUrl = "http://localhost";
	private WebWindow anyWindow = new WebClient().getCurrentWindow();
	private PageCreator pageCreator = new WebClient().getPageCreator();

	public FreemarkerTestRule(String templatePath, String templateName) {
		this.templatePath = templatePath;
		this.templateName = templateName;
	}

	public Statement apply(final Statement base, Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				loadTemplate();
				base.evaluate();
			}
		};
	}

	private void loadTemplate() throws Exception {

		Configuration cfg = configureTemplateLoader();

		template = cfg.getTemplate(templateName);

	}

	private Configuration configureTemplateLoader() throws Exception {

		Configuration cfg = new Configuration();
		//cfg.setClassForTemplateLoading(this.getClass(), "");
		
		cfg.setDirectoryForTemplateLoading(new File(templatePath));

		cfg.setIncompatibleImprovements(new Version(2, 3, 20));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.UK);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		return cfg;
	}


	public Writer writerResponseFor(Object dataModel) throws Exception {

		Writer out = new StringWriter();
		template.process(dataModel, out);

		return out;
	}

	public String stringResponseFor(Object dataModel) throws Exception {
		return writerResponseFor(dataModel).toString();
	}

	public HtmlPage htmlPageResponseFor(Object dataModel) throws Exception {
		return HTMLParser.parseHtml(webResponseFor(dataModel), anyWindow);
	}

	public XHtmlPage xhtmlPageResponseFor(Object dataModel) throws Exception {
		return HTMLParser.parseXHtml(webResponseFor(dataModel), anyWindow);
	}

	private StringWebResponse webResponseFor(Object dataModel) throws Exception {
		return new StringWebResponse(stringResponseFor(dataModel), new URL(anyUrl));
	}

	public TextPage textPageResponseFor(Object dataModel) throws Exception {

		WebResponseData responseData = new WebResponseData(
				stringResponseFor(dataModel).getBytes(), 200, "OK", asList(textContentHeader));

		WebResponse response = new WebResponse(responseData, new URL(anyUrl), HttpMethod.GET, 10);

		return (TextPage) pageCreator.createPage(response, anyWindow);
	}

	public Source xmlResponseFor(Object dataModel) throws Exception {
		return the(htmlPageResponseFor(dataModel).asXml());
	}
}

package de.sranko_informatik.si_xml_to_pdf_core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import freemarker.ext.dom.NodeModel;

public class FreemarkerGenerator {
	private Configuration cfg;
	private File templateDir = null;
	private String pathToPDF = null;
	private PdfRendererBuilder builder;
	
	/**
	 * @param pathToTemplate
	 */
	public FreemarkerGenerator() {
		Version ourVersion = Configuration.VERSION_2_3_27;
		cfg = new Configuration(ourVersion);
		cfg.setObjectWrapper(new DefaultObjectWrapper(ourVersion));
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setOutputEncoding("UTF-8");
		cfg.setLocale(Locale.GERMAN);
		//cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);		
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		this.builder = new PdfRendererBuilder();
	}
	
	/**
	 * @param pathToTemplateDir
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public FreemarkerGenerator(String pathToTemplateDir) throws IOException {
		this();
		System.out.println("Template-Dir: ".concat(pathToTemplateDir));
		setTemplateDir(pathToTemplateDir);
	}

	public void setTemplateDir(String pathToTemplateDir) {
		this.templateDir = new File(pathToTemplateDir);
	}
	
	public String getTemplateDirectory() {
		return this.templateDir.getAbsolutePath();
	}

	private String createBaseDocumentUri(String templateName) {
		File directory = new File(getTemplateDirectory());
        return directory.toPath().resolve(templateName).toUri().toString();
    }
	
    public byte[] generatePDF(Map modelData, String templateName, Locale locale) throws ParserConfigurationException, SAXException, IOException, TemplateException {

        String baseDocumentUri = createBaseDocumentUri(templateName);
        
        String html = generateHTML(modelData, templateName, locale);
        System.out.println(baseDocumentUri);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        builder.withHtmlContent(html, baseDocumentUri);
        builder.toStream(outputStream);
        builder.useFastMode();

        builder.run();
        
		return outputStream.toByteArray();
        
    }
    
    public String generateHTML(Map modelData, String templateName, Locale locale) throws ParserConfigurationException, SAXException, IOException, TemplateException {
		
		StringWriter stringWriter = new StringWriter();
		cfg.setDirectoryForTemplateLoading(templateDir);
		//cfg.getTemplate(templateName, locale, "UTF-8").process(modelData, stringWriter);
		cfg.getTemplate(templateName, "UTF-8").process(modelData, stringWriter);
		return stringWriter.toString();
	}
    
    private static String getFileExtension(File file) {
        String extension = "";
 
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf(".")+1);
            }
        } catch (Exception e) {
            extension = "";
        }
 
        return extension;
 
    }

}

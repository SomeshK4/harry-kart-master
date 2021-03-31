package se.atg.harrykart.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;

public abstract class BaseUtil<T> {
    private static final Logger logger;
    private final String contextPath;
    private final String xsdPath;
    private final JAXBContext jaxbContext;

    static {
        logger = LoggerFactory.getLogger((Class) BaseUtil.class);
    }

    protected BaseUtil(final String contextPath, final String xsdPath) {
        this.contextPath = contextPath;
        this.xsdPath = xsdPath;
        this.jaxbContext = this.createJAXBContext(contextPath);
    }

    private JAXBContext createJAXBContext(final String contextPath) {
        try {
            return JAXBContext.newInstance(this.contextPath);
        } catch (JAXBException e) {
            BaseUtil.logger.error(String.format("Failed to create JAXBContext for context path: '%s'", contextPath), (Throwable) e);
            return null;
        }
    }


    public T xmlToObject(final String xml, final boolean schemaValidation) throws Exception {
        return this.unmarshallObject(new ByteArrayInputStream(xml.getBytes()), schemaValidation);
    }

    private T unmarshallObject(final InputStream input, final boolean schemaValidation) throws JAXBException {
        final Unmarshaller unmarshaller = this.jaxbContext.createUnmarshaller();
        if (schemaValidation) {
            this.enableSchemaValidation(unmarshaller);
        }
        return this.unmarshallElement(input, unmarshaller);
    }

    private T unmarshallElement(final InputStream input, final Unmarshaller unmarshaller) throws JAXBException {
        final JAXBElement<T> jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(input);
        return jaxbElement.getValue();
    }

    private void enableSchemaValidation(final Unmarshaller unmarshaller) {
        final URL url = Thread.currentThread().getContextClassLoader().getResource(this.xsdPath);
        if (url != null) {
            this.loadSchema(unmarshaller, url);
        } else {
            BaseUtil.logger.error(String.format("Failed to find Schema: %s", this.xsdPath));
        }
    }

    private void loadSchema(final Unmarshaller unmarshaller, final URL url) {
        try {
            final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            final Schema schema = factory.newSchema(url);
            unmarshaller.setSchema(schema);
        } catch (SAXException e) {
            BaseUtil.logger.error(String.format("Failed to load schema: %s", this.xsdPath));
        }
    }


}

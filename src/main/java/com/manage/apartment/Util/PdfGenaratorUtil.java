package com.manage.apartment.Util;

import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

@Component
public class PdfGenaratorUtil implements ManageMyApartmentConstants {
    private static final Logger LOGGER = Logger.getLogger(PdfGenaratorUtil.class);
    private String className = this.getClass().getSimpleName();

    @Autowired
    private TemplateEngine templateEngine;

    public void createPdf(String templateName, Map map, String filename) throws IOException, DocumentException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.info(MessageFormat.format(LOGGER_ENTRY, className, methodName));
        Assert.notNull(templateName, "The templateName can not be null");
        Context ctx = new Context();
        if (map != null) {
            Iterator itMap = map.entrySet().iterator();
            while (itMap.hasNext()) {
                Map.Entry pair = (Map.Entry) itMap.next();
                ctx.setVariable(pair.getKey().toString(), pair.getValue());
            }
        }

        String processedHtml = templateEngine.process(templateName, ctx);

        OutputStream outputStream = new FileOutputStream(filename);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml);
        renderer.setScaleToFit(Boolean.TRUE);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        LOGGER.info(MessageFormat.format(LOGGER_EXIT, className, methodName));
    }
}

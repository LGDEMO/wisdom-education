package com.education.common.template;

import java.io.File;
import java.util.Map;

public abstract class BaseTemplate {

    protected String template;
    protected String outputDir;

    public BaseTemplate(String template, String outputDir) {
        this.template = template;
        this.outputDir = outputDir;
    }

    public void generateTemplate(Map data, String fileName) {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String target = outputDir + File.separator + fileName;
        File file = new File(target);
        this.writeToFile(data, file);
    }

    protected abstract void writeToFile(Map data, File outPutFile);
}

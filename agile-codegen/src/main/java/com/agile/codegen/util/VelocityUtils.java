package com.agile.codegen.util;

import cn.hutool.core.util.CharsetUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Template engine utility class.
 *
 * @author Huang Z.Y.
 */
@Service
public class VelocityUtils {

    /**
     * Velocity template rendering method.
     *
     * @param template Template.
     * @param map      Data model.
     * @return Rendered result.
     */
    public static String render(String template, Map<String, Object> map) {
        // Set up the Velocity resource loader
        Properties prop = new Properties();
        prop.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        VelocityContext context = new VelocityContext(map);
        // Function libraries, using lambda expressions to simplify code
        Optional.of(new MathTool()).ifPresent(mt -> context.put("math", mt));
        Optional.of(new DateTool()).ifPresent(dt -> context.put("dateTool", dt));
        Optional.of(new DictUtils()).ifPresent(dt -> context.put("dict", dt));
        Optional.of(new NamingCaseUtils()).ifPresent(nct -> context.put("str", nct));

        // Render the template, using lambda expressions to simplify code
        StringWriter sw = new StringWriter();
        Optional.ofNullable(Velocity.getTemplate(template, CharsetUtil.UTF_8)).ifPresent(tpl -> tpl.merge(context, sw));
        return sw.toString();
    }

    /**
     * Render text.
     *
     * @param str       Text to render.
     * @param dataModel Data model.
     * @return Rendered text.
     */
    public static String renderStr(String str, Map<String, Object> dataModel) {
        // Set up the Velocity resource loader
        Velocity.init();
        StringWriter stringWriter = new StringWriter();
        VelocityContext context = new VelocityContext(dataModel);
        // Function libraries
        context.put("math", new MathTool());
        context.put("dateTool", new DateTool());
        context.put("dict", new DictUtils());
        context.put("str", new NamingCaseUtils());
        Velocity.evaluate(context, stringWriter, "Agile Render", str);
        return stringWriter.toString();
    }

}

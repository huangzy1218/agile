package com.agile.plugin.excel.aop;

import com.agile.plugin.excel.annotation.RequestExcel;
import com.agile.plugin.excel.converter.LocalDateStringConverter;
import com.agile.plugin.excel.converter.LocalDateTimeStringConverter;
import com.agile.plugin.excel.handler.ListAnalysisEventListener;
import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

/**
 * RequestExcelArgumentResolver is a custom argument resolver  to resolve method parameters of type
 * {@link MultipartFile} annotated with {@link RequestExcel @RequestExcel}.
 *
 * @author Huang Z.Y.
 */
@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestExcel.class);
    }

    @Override
    @SneakyThrows(Exception.class)
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
        Class<?> parameterType = parameter.getParameterType();
        if (!parameterType.isAssignableFrom(List.class)) {
            // Annotation are on List and its subclasses
            throw new IllegalArgumentException(
                    "Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
        }

        // Handle custom readListener
        RequestExcel requestExcel = parameter.getParameterAnnotation(RequestExcel.class);
        assert requestExcel != null;
        Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
        // Instantiate readListenerClass
        ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);
        // Get the request file stream
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        assert request != null;
        InputStream inputStream;
        if (request instanceof MultipartRequest) {
            MultipartFile file = ((MultipartRequest) request).getFile(requestExcel.fileName());
            assert file != null;
            inputStream = file.getInputStream();
        } else {
            inputStream = request.getInputStream();
        }

        // Get target type
        Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();

        // Need to specify which class to read, and then read the first sheet file stream will automatically close
        EasyExcel.read(inputStream, excelModelClass, readListener)
                .registerConverter(LocalDateStringConverter.INSTANCE)
                .registerConverter(LocalDateTimeStringConverter.INSTANCE)
                .registerConverter(LocalTimeStringConverter.INSTANCE)
                .registerConverter(LongStringConverter.INSTANCE)
                .registerConverter(StringArrayConverter.INSTANCE)
                .ignoreEmptyRow(requestExcel.ignoreEmptyRow())
                .sheet()
                .headRowNumber(requestExcel.headRowNumber())
                .doRead();

        // 校验失败的数据处理 交给 BindResult
        WebDataBinder dataBinder = webDataBinderFactory.createBinder(webRequest, readListener.getErrors(), "excel");
        ModelMap model = modelAndViewContainer.getModel();
        model.put(BindingResult.MODEL_KEY_PREFIX + "excel", dataBinder.getBindingResult());

        return readListener.getList();
    }
}
    
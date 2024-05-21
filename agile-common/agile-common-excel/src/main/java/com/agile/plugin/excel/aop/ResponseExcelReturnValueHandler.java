package com.agile.plugin.excel.aop;

import com.agile.plugin.excel.annotation.ResponseExcel;
import com.agile.plugin.excel.handler.SheetWriteHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Process the return value of the method with the {@link ResponseExcel @ResponseExcel} annotation and write the
 * data to the Excel file.
 *
 * @author Huang Z.Y.
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final List<SheetWriteHandler> sheetWriteHandlerList;

    /**
     * Only the methods declared by {@link ResponseExcel @ResponseExcel} are handled.
     *
     * @param returnType Method signature
     * @return Whether to process
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return returnType.getMethodAnnotation(ResponseExcel.class) != null;
    }

    /**
     * Process logic.
     *
     * @param o                Return parameter
     * @param parameter        Method signature
     * @param mavContainer     Context container
     * @param nativeWebRequest Web request
     */
    @Override
    public void handleReturnValue(Object o, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest) throws Exception {
        // Get the HttpServletResponse object for writing Excel files into HTTP responses
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        ResponseExcel responseExcel = parameter.getMethodAnnotation(ResponseExcel.class);
        Assert.state(responseExcel != null, "No @ResponseExcel");
        // Mark that the request has been processed, avoiding subsequent view parsing and rendering by Spring
        mavContainer.setRequestHandled(true);

        // Iterate through the sheetWriteHandlerList to find the first
        // processor that supports the current return value type
        sheetWriteHandlerList.stream()
                .filter(handler -> handler.support(o))
                .findFirst()
                // If a suitable processor is found, call its export method for data export
                .ifPresent(handler -> handler.export(o, response, responseExcel));
    }

}
    
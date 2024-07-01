package com.agile.plugin.excel;

import com.agile.plugin.excel.controller.ExcelController;
import com.agile.plugin.excel.read.IndexData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(ExcelController.class)
@ContextConfiguration(classes = ExcelController.class)
public class RequestExcelTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private BindingResult bindingResult;
    @InjectMocks
    private ExcelController excelController;

    @Test
    public void testUpload() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Prepare mock data for uploading
        List<IndexData> dataList = new ArrayList<>();
        IndexData data1 = new IndexData();
        data1.setA("A");
        data1.setB("B");
        data1.setC("C");
        dataList.add(data1);
        IndexData data2 = new IndexData();
        data2.setA("a");
        data2.setB("b");
        data2.setC("c");
        dataList.add(data2);

        // Mock binding result to return an empty list (or mock error messages if needed)
        when(bindingResult.getTarget()).thenReturn(new ArrayList<>());

        // Convert dataList to Excel content
        MockMultipartFile file = getMockMultipartFile(dataList);

        // Perform the mock MVC request
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private MockMultipartFile getMockMultipartFile(List<IndexData> dataList) {
        StringBuilder content = new StringBuilder();
        content.append("A,B,C\n");
        for (IndexData data : dataList) {
            content.append(data.getA() + "," + data.getB() + "," + data.getC() + "\n");
        }

        // Prepare mock multipart file with Excel content
        MockMultipartFile file = new MockMultipartFile("file",
                "data.xlsx",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                content.toString().getBytes());
        return file;
    }

}

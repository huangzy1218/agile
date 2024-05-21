package com.agile.plugin.excel.head;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * Define and manage header information for Excel files.
 *
 * @author Huang Z.Y.
 */
@Data
public class HeadMeta {

    /**
     * Customize the header information.<br/>
     * The implementation class customizes the Excel header according to the class information of the data.
     */
    private List<List<String>> head;

    /**
     * Ignore the field name corresponding to the header.
     */
    private Set<String> ignoreHeadFields;

}
    
/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.agile.codegen.entity;

import lombok.Data;

/**
 * Stores code generator configuration information.
 *
 * @author Huang Z.Y.
 */
@Data
public class GenConfig {

    /**
     * Data source name.
     */
    private String dsName;

    /**
     * Package name.
     */
    private String packageName;

    /**
     * Author.
     */
    private String author;

    /**
     * Module name.
     */
    private String moduleName;

    /**
     * Table prefix.
     */
    private String tablePrefix;

    /**
     * Table name.
     */
    private String tableName;

    /**
     * Table remarks.
     */
    private String comments;

    /**
     * Code style 0 - avue 1 - element 2 - uview
     */
    private String style;

}

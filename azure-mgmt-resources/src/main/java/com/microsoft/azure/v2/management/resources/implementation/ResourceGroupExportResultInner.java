/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.v2.management.resources.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.azure.v2.management.resources.ResourceManagementErrorWithDetails;

/**
 * Resource group export result.
 */
public final class ResourceGroupExportResultInner {
    /**
     * The template content.
     */
    @JsonProperty(value = "template")
    private Object template;

    /**
     * The error.
     */
    @JsonProperty(value = "error")
    private ResourceManagementErrorWithDetails error;

    /**
     * Get the template value.
     *
     * @return the template value.
     */
    public Object template() {
        return this.template;
    }

    /**
     * Set the template value.
     *
     * @param template the template value to set.
     * @return the ResourceGroupExportResultInner object itself.
     */
    public ResourceGroupExportResultInner withTemplate(Object template) {
        this.template = template;
        return this;
    }

    /**
     * Get the error value.
     *
     * @return the error value.
     */
    public ResourceManagementErrorWithDetails error() {
        return this.error;
    }

    /**
     * Set the error value.
     *
     * @param error the error value to set.
     * @return the ResourceGroupExportResultInner object itself.
     */
    public ResourceGroupExportResultInner withError(ResourceManagementErrorWithDetails error) {
        this.error = error;
        return this;
    }
}

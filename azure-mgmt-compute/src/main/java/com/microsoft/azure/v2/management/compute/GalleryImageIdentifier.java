/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.v2.management.compute;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is the gallery image identifier.
 */
public final class GalleryImageIdentifier {
    /**
     * The gallery image publisher name.
     */
    @JsonProperty(value = "publisher", required = true)
    private String publisher;

    /**
     * The gallery image offer name.
     */
    @JsonProperty(value = "offer", required = true)
    private String offer;

    /**
     * The gallery image sku name.
     */
    @JsonProperty(value = "sku", required = true)
    private String sku;

    /**
     * Get the publisher value.
     *
     * @return the publisher value.
     */
    public String publisher() {
        return this.publisher;
    }

    /**
     * Set the publisher value.
     *
     * @param publisher the publisher value to set.
     * @return the GalleryImageIdentifier object itself.
     */
    public GalleryImageIdentifier withPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    /**
     * Get the offer value.
     *
     * @return the offer value.
     */
    public String offer() {
        return this.offer;
    }

    /**
     * Set the offer value.
     *
     * @param offer the offer value to set.
     * @return the GalleryImageIdentifier object itself.
     */
    public GalleryImageIdentifier withOffer(String offer) {
        this.offer = offer;
        return this;
    }

    /**
     * Get the sku value.
     *
     * @return the sku value.
     */
    public String sku() {
        return this.sku;
    }

    /**
     * Set the sku value.
     *
     * @param sku the sku value to set.
     * @return the GalleryImageIdentifier object itself.
     */
    public GalleryImageIdentifier withSku(String sku) {
        this.sku = sku;
        return this;
    }
}
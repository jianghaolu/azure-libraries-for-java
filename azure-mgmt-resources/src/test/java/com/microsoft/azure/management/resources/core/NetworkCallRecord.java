/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.core;

import com.microsoft.rest.v2.http.HttpMethod;

import java.util.Map;

public class NetworkCallRecord {
    public HttpMethod Method;
    public String Uri;

    public Map<String, String> Headers;
    public Map<String, String> Response;
}

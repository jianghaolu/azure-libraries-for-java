/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.fluentcore.utils;

import com.google.common.primitives.Ints;
import com.microsoft.azure.v2.AzureProxy;
import com.microsoft.azure.v2.CloudException;
import com.microsoft.azure.v2.Page;
import com.microsoft.azure.v2.PagedList;
import com.microsoft.azure.management.resources.fluentcore.model.Indexable;
import com.microsoft.azure.v2.management.resources.implementation.PageImpl;
import com.microsoft.azure.v2.management.resources.implementation.SubscriptionInner;
import com.microsoft.rest.v2.BodyResponse;
import com.microsoft.rest.v2.StreamResponse;
import com.microsoft.rest.v2.annotations.ExpectedResponses;
import com.microsoft.rest.v2.annotations.GET;
import com.microsoft.rest.v2.annotations.HeaderParam;
import com.microsoft.rest.v2.annotations.PathParam;
import com.microsoft.rest.v2.annotations.QueryParam;
import com.microsoft.rest.v2.annotations.UnexpectedResponseExceptionType;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.util.FlowableUtil;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.Observable;
import io.reactivex.exceptions.Exceptions;
import rx.functions.Func1;

import java.io.IOException;
import java.util.List;

/**
 * Defines a few utilities.
 */
public final class Utils {
    /**
     * Converts an object Boolean to a primitive boolean.
     *
     * @param value the <tt>Boolean</tt> value
     * @return <tt>false</tt> if the given Boolean value is null or false else <tt>true</tt>
     */
    public static boolean toPrimitiveBoolean(Boolean value) {
        if (value == null) {
            return false;
        }
        return value.booleanValue();
    }

    /**
     * Converts an object Integer to a primitive int.
     *
     * @param value the <tt>Integer</tt> value
     * @return <tt>0</tt> if the given Integer value is null else <tt>integer value</tt>
     */
    public static int toPrimitiveInt(Integer value) {
        if (value == null) {
            return 0;
        }
        return value.intValue();
    }

    /**
     * Converts an object Long to a primitive int.
     *
     * @param value the <tt>Long</tt> value
     * @return <tt>0</tt> if the given Long value is null else <tt>integer value</tt>
     */
    public static int toPrimitiveInt(Long value) {
        if (value == null) {
            return 0;
        }
        // throws IllegalArgumentException - if value is greater than Integer.MAX_VALUE
        // or less than Integer.MIN_VALUE
        return Ints.checkedCast(value);
    }

    /**
     * Converts an object Long to a primitive long.
     *
     * @param value the <tt>Long</tt> value
     * @return <tt>0</tt> if the given Long value is null else <tt>long value</tt>
     */
    public static long toPrimitiveLong(Long value) {
        if (value == null) {
            return 0;
        }
        return value;
    }

    /**
     * Creates an Odata filter string that can be used for filtering list results by tags.
     *
     * @param tagName the name of the tag. If not provided, all resources will be returned.
     * @param tagValue the value of the tag. If not provided, only tag name will be filtered.
     * @return the Odata filter to pass into list methods
     */
    public static String createOdataFilterForTags(String tagName, String tagValue) {
        if (tagName == null) {
            return null;
        } else if (tagValue == null) {
            return String.format("tagname eq '%s'", tagName);
        } else {
            return String.format("tagname eq '%s' and tagvalue eq '%s'", tagName, tagValue);
        }
    }

    /**
     * Gets an observable of type {@code U}, where U extends {@link Indexable}, that emits only the root
     * resource from a given observable of {@link Indexable}.
     *
     * @param stream the input observable of {@link Indexable}
     * @param <U> the specialized type of last item in the input stream
     * @return an observable that emits last item
     */
    @SuppressWarnings("unchecked")
    public static <U extends Indexable> Single<U> rootResource(Observable<Indexable> stream) {
        return stream.lastOrError().map(indexable -> (U) indexable);
    }

    /**
     * Download a file asynchronously.
     * @param url the URL pointing to the file
     * @return an Observable pointing to the content of the file
     */
    public static Single<byte[]> downloadFileAsync(String url) {
        FileService service = AzureProxy.create(FileService.class);
        Single<StreamResponse> response = service.download(url);
        return response.flatMap(r -> FlowableUtil.collectBytesInArray(r.body()));
    }

    /**
     * Converts the given list of a type to paged list of a different type.
     *
     * @param list the list to convert to paged list
     * @param mapper the mapper to map type in input list to output list
     * @param <OutT> the type of items in output paged list
     * @param <InT> the type of items in input paged list
     * @return the paged list
     */
    public static <OutT, InT> PagedList<OutT> toPagedList(List<InT> list, final Func1<InT, OutT> mapper) {
        PageImpl<InT> page = new PageImpl<>();
        page.setItems(list);
        page.setNextPageLink(null);
        PagedList<InT> pagedList = new PagedList<InT>(page) {
            @Override
            public Page<InT> nextPage(String nextPageLink) {
                return null;
            }
        };
        PagedListConverter<InT, OutT> converter = new PagedListConverter<InT, OutT>() {
            @Override
            public Observable<OutT> typeConvertAsync(InT inner) {
                return Observable.just(mapper.call(inner));
            }
        };
        return converter.convert(pagedList);
    }

    /**
     * Adds a value to the list if does not already exists.
     *
     * @param list the list
     * @param value value to add if not exists in the list
     */
    public static void addToListIfNotExists(List<String> list, String value) {
        boolean found = false;
        for (String item : list) {
            if (item.equalsIgnoreCase(value)) {
                found = true;
                break;
            }
        }
        if (!found) {
            list.add(value);
        }
    }

    /**
     * Removes a value from the list.
     *
     * @param list the list
     * @param value value to remove
     */
    public static void removeFromList(List<String> list, String value) {
        int foundIndex = -1;
        int i = 0;
        for (String id : list) {
            if (id.equalsIgnoreCase(value)) {
                foundIndex = i;
                break;
            }
            i++;
        }
        if (foundIndex != -1) {
            list.remove(foundIndex);
        }
    }

    /**
     * A Retrofit service used to download a file.
     */
    private interface FileService {
        @GET("{url}")
        @ExpectedResponses({200, 201, 202, 204})
        @UnexpectedResponseExceptionType(CloudException.class)
        Single<StreamResponse> download(@PathParam(value = "url", encoded = true) String url);
    }

    private Utils() {
    }
}

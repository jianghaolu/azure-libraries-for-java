/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.appservice.implementation;

import retrofit2.Retrofit;
import com.google.common.reflect.TypeToken;
import com.microsoft.azure.AzureServiceFuture;
import com.microsoft.azure.CloudException;
import com.microsoft.azure.ListOperationCallback;
import com.microsoft.azure.Page;
import com.microsoft.azure.PagedList;
import com.microsoft.rest.ServiceCallback;
import com.microsoft.rest.ServiceFuture;
import com.microsoft.rest.ServiceResponse;
import java.io.IOException;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import retrofit2.Response;
import rx.functions.Func1;
import rx.Observable;

/**
 * An instance of this class provides access to all the operations defined
 * in Providers.
 */
public class ProvidersInner {
    /** The Retrofit service to perform REST calls. */
    private ProvidersService service;
    /** The service client containing this operation class. */
    private WebSiteManagementClientImpl client;

    /**
     * Initializes an instance of ProvidersInner.
     *
     * @param retrofit the Retrofit instance built from a Retrofit Builder.
     * @param client the instance of the service client containing this operation class.
     */
    public ProvidersInner(Retrofit retrofit, WebSiteManagementClientImpl client) {
        this.service = retrofit.create(ProvidersService.class);
        this.client = client;
    }

    /**
     * The interface defining all the services for Providers to be
     * used by Retrofit to perform actually REST calls.
     */
    interface ProvidersService {
        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.appservice.Providers getAvailableStacks" })
        @GET("providers/Microsoft.Web/availableStacks")
        Observable<Response<ResponseBody>> getAvailableStacks(@Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.appservice.Providers listOperations" })
        @GET("providers/Microsoft.Web/operations")
        Observable<Response<ResponseBody>> listOperations(@Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.appservice.Providers getAvailableStacksOnPrem" })
        @GET("subscriptions/{subscriptionId}/providers/Microsoft.Web/availableStacks")
        Observable<Response<ResponseBody>> getAvailableStacksOnPrem(@Path("subscriptionId") String subscriptionId, @Query("api-version") String apiVersion, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

        @Headers({ "Content-Type: application/json; charset=utf-8", "x-ms-logging-context: com.microsoft.azure.management.appservice.Providers listOperationsNext" })
        @GET
        Observable<Response<ResponseBody>> listOperationsNext(@Url String nextUrl, @Header("accept-language") String acceptLanguage, @Header("User-Agent") String userAgent);

    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the Object object if successful.
     */
    public Object getAvailableStacks() {
        return getAvailableStacksWithServiceResponseAsync().toBlocking().single().body();
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Object> getAvailableStacksAsync(final ServiceCallback<Object> serviceCallback) {
        return ServiceFuture.fromResponse(getAvailableStacksWithServiceResponseAsync(), serviceCallback);
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the Object object
     */
    public Observable<Object> getAvailableStacksAsync() {
        return getAvailableStacksWithServiceResponseAsync().map(new Func1<ServiceResponse<Object>, Object>() {
            @Override
            public Object call(ServiceResponse<Object> response) {
                return response.body();
            }
        });
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the Object object
     */
    public Observable<ServiceResponse<Object>> getAvailableStacksWithServiceResponseAsync() {
        final String apiVersion = "2016-03-01";
        return service.getAvailableStacks(apiVersion, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Object>>>() {
                @Override
                public Observable<ServiceResponse<Object>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Object> clientResponse = getAvailableStacksDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Object> getAvailableStacksDelegate(Response<ResponseBody> response) throws CloudException, IOException {
        return this.client.restClient().responseBuilderFactory().<Object, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Object>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;CsmOperationDescriptionInner&gt; object if successful.
     */
    public PagedList<CsmOperationDescriptionInner> listOperations() {
        ServiceResponse<Page<CsmOperationDescriptionInner>> response = listOperationsSinglePageAsync().toBlocking().single();
        return new PagedList<CsmOperationDescriptionInner>(response.body()) {
            @Override
            public Page<CsmOperationDescriptionInner> nextPage(String nextPageLink) {
                return listOperationsNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<CsmOperationDescriptionInner>> listOperationsAsync(final ListOperationCallback<CsmOperationDescriptionInner> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listOperationsSinglePageAsync(),
            new Func1<String, Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>>>() {
                @Override
                public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> call(String nextPageLink) {
                    return listOperationsNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;CsmOperationDescriptionInner&gt; object
     */
    public Observable<Page<CsmOperationDescriptionInner>> listOperationsAsync() {
        return listOperationsWithServiceResponseAsync()
            .map(new Func1<ServiceResponse<Page<CsmOperationDescriptionInner>>, Page<CsmOperationDescriptionInner>>() {
                @Override
                public Page<CsmOperationDescriptionInner> call(ServiceResponse<Page<CsmOperationDescriptionInner>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;CsmOperationDescriptionInner&gt; object
     */
    public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> listOperationsWithServiceResponseAsync() {
        return listOperationsSinglePageAsync()
            .concatMap(new Func1<ServiceResponse<Page<CsmOperationDescriptionInner>>, Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>>>() {
                @Override
                public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> call(ServiceResponse<Page<CsmOperationDescriptionInner>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listOperationsNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;CsmOperationDescriptionInner&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> listOperationsSinglePageAsync() {
        final String apiVersion = "2016-03-01";
        return service.listOperations(apiVersion, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>>>() {
                @Override
                public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<CsmOperationDescriptionInner>> result = listOperationsDelegate(response);
                        return Observable.just(new ServiceResponse<Page<CsmOperationDescriptionInner>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<CsmOperationDescriptionInner>> listOperationsDelegate(Response<ResponseBody> response) throws CloudException, IOException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<CsmOperationDescriptionInner>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<CsmOperationDescriptionInner>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the Object object if successful.
     */
    public Object getAvailableStacksOnPrem() {
        return getAvailableStacksOnPremWithServiceResponseAsync().toBlocking().single().body();
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<Object> getAvailableStacksOnPremAsync(final ServiceCallback<Object> serviceCallback) {
        return ServiceFuture.fromResponse(getAvailableStacksOnPremWithServiceResponseAsync(), serviceCallback);
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the Object object
     */
    public Observable<Object> getAvailableStacksOnPremAsync() {
        return getAvailableStacksOnPremWithServiceResponseAsync().map(new Func1<ServiceResponse<Object>, Object>() {
            @Override
            public Object call(ServiceResponse<Object> response) {
                return response.body();
            }
        });
    }

    /**
     * Get available application frameworks and their versions.
     * Get available application frameworks and their versions.
     *
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the Object object
     */
    public Observable<ServiceResponse<Object>> getAvailableStacksOnPremWithServiceResponseAsync() {
        if (this.client.subscriptionId() == null) {
            throw new IllegalArgumentException("Parameter this.client.subscriptionId() is required and cannot be null.");
        }
        final String apiVersion = "2016-03-01";
        return service.getAvailableStacksOnPrem(this.client.subscriptionId(), apiVersion, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Object>>>() {
                @Override
                public Observable<ServiceResponse<Object>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<Object> clientResponse = getAvailableStacksOnPremDelegate(response);
                        return Observable.just(clientResponse);
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<Object> getAvailableStacksOnPremDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<Object, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<Object>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @throws CloudException thrown if the request is rejected by server
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent
     * @return the PagedList&lt;CsmOperationDescriptionInner&gt; object if successful.
     */
    public PagedList<CsmOperationDescriptionInner> listOperationsNext(final String nextPageLink) {
        ServiceResponse<Page<CsmOperationDescriptionInner>> response = listOperationsNextSinglePageAsync(nextPageLink).toBlocking().single();
        return new PagedList<CsmOperationDescriptionInner>(response.body()) {
            @Override
            public Page<CsmOperationDescriptionInner> nextPage(String nextPageLink) {
                return listOperationsNextSinglePageAsync(nextPageLink).toBlocking().single().body();
            }
        };
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @param serviceFuture the ServiceFuture object tracking the Retrofit calls
     * @param serviceCallback the async ServiceCallback to handle successful and failed responses.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the {@link ServiceFuture} object
     */
    public ServiceFuture<List<CsmOperationDescriptionInner>> listOperationsNextAsync(final String nextPageLink, final ServiceFuture<List<CsmOperationDescriptionInner>> serviceFuture, final ListOperationCallback<CsmOperationDescriptionInner> serviceCallback) {
        return AzureServiceFuture.fromPageResponse(
            listOperationsNextSinglePageAsync(nextPageLink),
            new Func1<String, Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>>>() {
                @Override
                public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> call(String nextPageLink) {
                    return listOperationsNextSinglePageAsync(nextPageLink);
                }
            },
            serviceCallback);
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;CsmOperationDescriptionInner&gt; object
     */
    public Observable<Page<CsmOperationDescriptionInner>> listOperationsNextAsync(final String nextPageLink) {
        return listOperationsNextWithServiceResponseAsync(nextPageLink)
            .map(new Func1<ServiceResponse<Page<CsmOperationDescriptionInner>>, Page<CsmOperationDescriptionInner>>() {
                @Override
                public Page<CsmOperationDescriptionInner> call(ServiceResponse<Page<CsmOperationDescriptionInner>> response) {
                    return response.body();
                }
            });
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
     * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the observable to the PagedList&lt;CsmOperationDescriptionInner&gt; object
     */
    public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> listOperationsNextWithServiceResponseAsync(final String nextPageLink) {
        return listOperationsNextSinglePageAsync(nextPageLink)
            .concatMap(new Func1<ServiceResponse<Page<CsmOperationDescriptionInner>>, Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>>>() {
                @Override
                public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> call(ServiceResponse<Page<CsmOperationDescriptionInner>> page) {
                    String nextPageLink = page.body().nextPageLink();
                    if (nextPageLink == null) {
                        return Observable.just(page);
                    }
                    return Observable.just(page).concatWith(listOperationsNextWithServiceResponseAsync(nextPageLink));
                }
            });
    }

    /**
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     * Gets all available operations for the Microsoft.Web resource provider. Also exposes resource metric definitions.
     *
    ServiceResponse<PageImpl<CsmOperationDescriptionInner>> * @param nextPageLink The NextLink from the previous successful call to List operation.
     * @throws IllegalArgumentException thrown if parameters fail the validation
     * @return the PagedList&lt;CsmOperationDescriptionInner&gt; object wrapped in {@link ServiceResponse} if successful.
     */
    public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> listOperationsNextSinglePageAsync(final String nextPageLink) {
        if (nextPageLink == null) {
            throw new IllegalArgumentException("Parameter nextPageLink is required and cannot be null.");
        }
        String nextUrl = String.format("%s", nextPageLink);
        return service.listOperationsNext(nextUrl, this.client.acceptLanguage(), this.client.userAgent())
            .flatMap(new Func1<Response<ResponseBody>, Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>>>() {
                @Override
                public Observable<ServiceResponse<Page<CsmOperationDescriptionInner>>> call(Response<ResponseBody> response) {
                    try {
                        ServiceResponse<PageImpl<CsmOperationDescriptionInner>> result = listOperationsNextDelegate(response);
                        return Observable.just(new ServiceResponse<Page<CsmOperationDescriptionInner>>(result.body(), result.response()));
                    } catch (Throwable t) {
                        return Observable.error(t);
                    }
                }
            });
    }

    private ServiceResponse<PageImpl<CsmOperationDescriptionInner>> listOperationsNextDelegate(Response<ResponseBody> response) throws CloudException, IOException, IllegalArgumentException {
        return this.client.restClient().responseBuilderFactory().<PageImpl<CsmOperationDescriptionInner>, CloudException>newInstance(this.client.serializerAdapter())
                .register(200, new TypeToken<PageImpl<CsmOperationDescriptionInner>>() { }.getType())
                .registerError(CloudException.class)
                .build(response);
    }

}

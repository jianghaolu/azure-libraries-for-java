/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.v2.management.resources.childresource;

import com.microsoft.azure.v2.management.resources.fluentcore.arm.models.implementation.ExternalChildResourceImpl;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

class PulletImpl extends ExternalChildResourceImpl<Pullet, Object, ChickenImpl, Object>
        implements Pullet {
    Integer age;
    private FailFlag failFlag = FailFlag.None;

    PulletImpl(String name, ChickenImpl parent) {
        super(name, name, parent, new Object());
    }

    public PulletImpl withAge(Integer age) {
        this.age = age;
        return this;
    }

    public PulletImpl withFailFlag(FailFlag failFlag) {
        this.failFlag = failFlag;
        return this;
    }

    public ChickenImpl attach() {
        return this.parent().withPullet(this);
    }

    @Override
    public Observable<Pullet> createResourceAsync() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        if (this.failFlag == FailFlag.OnCreate) {
            return Observable.error(new Exception("Creation of " + this.name() + " failed"));
        }

        Pullet self = this;
        return Observable
                .just(self)
                .observeOn(Schedulers.computation());
    }

    @Override
    public Observable<Pullet> updateResourceAsync() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        if (this.failFlag == FailFlag.OnUpdate) {
            return Observable.error(new Exception("Update of " + this.name() + " failed"));
        }

        Pullet self = this;
        return Observable
                .just(self)
                .observeOn(Schedulers.computation());
    }

    @Override
    public Completable deleteResourceAsync() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        if (this.failFlag == FailFlag.OnDelete) {
            return Completable.error(new Exception("Deletion of " + this.name() + " failed"));
        }

        return Completable.complete();
    }

    @Override
    protected Maybe<Object> getInnerAsync() {
        return Maybe.empty();
    }

    @Override
    public String id() {
        return null;
    }

    enum FailFlag {
        None,
        OnCreate,
        OnUpdate,
        OnDelete
    }
}
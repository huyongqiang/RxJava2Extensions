/*
 * Copyright 2016 David Karnok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.akarnokd.rxjava2.util;

import org.reactivestreams.*;

import io.reactivex.internal.subscriptions.*;

/**
 * A subscriber that is unbounded-in and can generate 0 or 1 resulting value. 
 * @param <T> the input value type
 * @param <R> the output value type
 */
public abstract class DeferredScalarSubscriber<T, R> extends DeferredScalarSubscription<R> 
implements Subscriber<T> {
    /** */
    private static final long serialVersionUID = 2984505488220891551L;

    protected Subscription s;
    
    protected boolean hasValue;
    
    public DeferredScalarSubscriber(Subscriber<? super R> actual) {
        super(actual);
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.s, s)) {
            this.s = s;
            
            actual.onSubscribe(this);
            
            s.request(Long.MAX_VALUE);
        }
    }
    
    @Override
    public void onError(Throwable t) {
        value = null;
        actual.onError(t);
    }
    
    @Override
    public void onComplete() {
        if (hasValue) {
            complete(value);
        } else {
            actual.onComplete();
        }
    }
}
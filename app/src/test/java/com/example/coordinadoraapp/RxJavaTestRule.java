package com.example.coordinadoraapp;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaTestRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> Schedulers.trampoline());

                RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
                RxJavaPlugins.setSingleSchedulerHandler(scheduler -> Schedulers.trampoline());

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                }
            }
        };
    }
}

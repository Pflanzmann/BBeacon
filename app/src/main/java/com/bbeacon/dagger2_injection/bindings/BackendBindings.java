package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.backend.Evaluator;
import com.bbeacon.backend.EvaluatorType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BackendBindings {

    @Binds
    public abstract EvaluatorType provideEvaluator(Evaluator evaluator);
}

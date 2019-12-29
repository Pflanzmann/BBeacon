package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.backend.AverageRanger;
import com.bbeacon.backend.Evaluator;
import com.bbeacon.backend.EvaluatorType;
import com.bbeacon.backend.RangerType;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BackendBindings {

    @Binds
    public abstract EvaluatorType bindEvaluator(Evaluator evaluator);

    @Binds
    public abstract RangerType bindAverageRanger(AverageRanger ranger);
}

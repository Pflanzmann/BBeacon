package com.bbeacon.dagger2_injection.bindings;

import com.bbeacon.backend.Calculator;
import com.bbeacon.backend.CalculatorType;
import com.bbeacon.backend.Evaluator;
import com.bbeacon.backend.EvaluatorType;
import com.bbeacon.backend.RangerType;
import com.bbeacon.backend.TxPowerRanger;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class BackendBindings {

    @Binds
    public abstract EvaluatorType bindEvaluator(Evaluator evaluator);

    @Binds
    public abstract RangerType bindAverageRanger(TxPowerRanger ranger);

    @Binds
    public abstract CalculatorType bindCalculator(Calculator calculatorExample);
}

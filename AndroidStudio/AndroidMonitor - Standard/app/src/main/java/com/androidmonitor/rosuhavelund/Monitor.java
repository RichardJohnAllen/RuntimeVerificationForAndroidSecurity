// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

import java.util.ArrayList;

public class Monitor
{
    private SyntaxTree syntaxTree;
    private Register now;
    private Register next;
    private String message;

    public Monitor(String formula, String message) {
        this.syntaxTree = new SyntaxTree(formula);
        ArrayList<SyntaxTree> subFormulae = syntaxTree.Flatten();

        this.next = new DefaultRegister(subFormulae);
        this.now = new Register(subFormulae);

        this.message = message;
    }

    public EvaluationResult Evaluate(Trace trace)
    {
        EvaluationResult result = new EvaluationResult(this.message);

        for (int i = trace.Length() - 1; i >= 0; i--)
        {
            TraceEvent event = trace.Event(i);

            boolean satisfiedSubProperty = this.now.Evaluate(event, this.next);
            this.next.Update(this.now);

            if (satisfiedSubProperty) {
                result.SatisfyingEvents().add(event);
            }
        }

        result.SetSatisfied(this.next.Property(0).getSatisfied());

        return result;
    }
}

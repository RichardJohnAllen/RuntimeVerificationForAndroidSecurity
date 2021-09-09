// Copyright Richard John Allen 29/4/2020

package com.androidmonitor.rosuhavelund;

public class Property
{
    private SyntaxTree node;
    private boolean satisfied;

    public Property(SyntaxTree node)
    {
        this(node, false);
    }

    public Property(SyntaxTree node, boolean satisfied)
    {
        this.node = node;
        this.satisfied = satisfied;
    }

    public String Formula()
    {
        return this.node.Formula();
    }

    public String SubFormula(int n)
    {
        return this.node.Subtrees().get(n).Formula();
    }

    public void setSatisfied(boolean valid)
    {
        this.satisfied = valid;
    }

    public boolean getSatisfied()
    {
        return this.satisfied;
    }
}

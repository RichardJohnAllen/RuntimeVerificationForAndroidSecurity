/*
===============================================================================

Rosu-Havelund algorithm.
An ad-hoc implemenation that interprets and evaluates LTL formulae
over a finite trace.

===============================================================================

Copyright (C) 2021  Richard John Allen

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

===============================================================================
*/

package RosuHavelund;

public class Property
{
    private SyntaxTree node;
    private boolean valid;

    public Property(SyntaxTree node)
    {
        this(node, false);
    }

    public Property(SyntaxTree node, boolean valid)
    {
        this.node = node;
        this.valid = valid;
    }

    public String Formula()
    {
        return this.node.Formula();
    }

    public String SubFormula(int n)
    {
        return this.node.Subtrees().get(n).Formula();
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    public boolean getValid()
    {
        return this.valid;
    }
}

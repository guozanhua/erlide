package org.erlide.ui.editors.erl.outline.filters;

import org.eclipse.jface.viewers.Viewer;
import org.erlide.core.erlang.IErlTypespec;

public class TypespecFilter extends ErlangViewerFilter {

    @Override
    public boolean select(final Viewer viewer, final Object parentElement,
            final Object element) {
        if (element instanceof IErlTypespec) {
            return false;
        }
        return true;
    }

}

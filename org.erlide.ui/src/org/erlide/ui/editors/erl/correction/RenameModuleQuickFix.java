package org.erlide.ui.editors.erl.correction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.erlide.engine.ErlangEngine;
import org.erlide.engine.model.IErlModel;
import org.erlide.engine.model.erlang.IErlModule;

public class RenameModuleQuickFix extends ErlangQuickFixRunnable {

    @Override
    public void run() throws Exception {
        final IMarker marker = getMarker();
        final List<String> margs = getQuickFix().getArgs();
        final String moduleName = margs.get(0);

        final IErlModel svc = ErlangEngine.getInstance().getModel();

        IErlModule module;
        module = svc.findModule(moduleName);
        if (module != null) {
            MessageDialog.openInformation(null, "Rename module quickfix",
                    "A module with name '" + moduleName + "' already exists.");
            return;
        }

        final IFile file = (IFile) marker.getResource();
        final IErlModule oldModule = svc.findModule(file);
        if (oldModule == null) {
            return;
        }

        String in = convertStreamToString(file.getContents());
        in = in.replaceAll("-module\\([^)]+\\)\\.", "-module(" + moduleName + ").");
        file.setContents(new ByteArrayInputStream(in.getBytes()), IResource.FORCE, null);
    }

    private static String convertStreamToString(final InputStream is) {
        final java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

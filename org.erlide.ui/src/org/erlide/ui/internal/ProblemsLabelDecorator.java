package org.erlide.ui.internal;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.erlide.core.erlang.IErlMember;
import org.erlide.core.erlang.ISourceRange;
import org.erlide.core.erlang.ISourceReference;
import org.erlide.ui.ErlideUIPluginImages;
import org.erlide.ui.views.outline.ErlangElementImageDescriptor;

public class ProblemsLabelDecorator implements ILightweightLabelDecorator {

	/**
	 * This is a special <code>LabelProviderChangedEvent</code> carrying
	 * additional information whether the event origins from a maker change.
	 * <p>
	 * <code>ProblemsLabelChangedEvent</code>s are only generated by <code>
	 * ProblemsLabelDecorator</code>s.
	 * </p>
	 */
	public static class ProblemsLabelChangedEvent extends
			LabelProviderChangedEvent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final boolean fMarkerChange;

		/**
		 * Note: This constructor is for internal use only. Clients should not
		 * call this constructor.
		 */
		public ProblemsLabelChangedEvent(final IBaseLabelProvider source,
				final IResource[] changedResource, final boolean isMarkerChange) {
			super(source, changedResource);
			fMarkerChange = isMarkerChange;
		}

		/**
		 * Returns whether this event origins from marker changes. If
		 * <code>false</code> an annotation model change is the origin. In
		 * this case viewers not displaying working copies can ignore these
		 * events.
		 * 
		 * @return if this event origins from a marker change.
		 */
		public boolean isMarkerChange() {
			return fMarkerChange;
		}

	}

	private static final int ERRORTICK_WARNING = ErlangElementImageDescriptor.WARNING;
	private static final int ERRORTICK_ERROR = ErlangElementImageDescriptor.ERROR;

	// private IProblemChangedListener fProblemChangedListener;
	// private Collection fListeners;
	// private ISourceRange fCachedRange;

	/*
	 * Creates decorator with a shared image registry.
	 * 
	 * @param registry The registry to use or <code>null</code> to use the
	 * erlide plugin's image registry.
	 */

	/**
	 * Note: This method is for internal use only. Clients should not call this
	 * method.
	 * 
	 * @param obj
	 *            the element to compute the flags for
	 * 
	 * @return the adornment flags
	 */
	protected int computeAdornmentFlags(final Object obj) {
		try {
			if (obj instanceof IResource) {
				return getErrorTicksFromMarkers((IResource) obj,
						IResource.DEPTH_INFINITE, null);
			} else if (obj instanceof IErlMember) {
				final IErlMember m = (IErlMember) obj;
				return getErrorTicksFromMarkers(m.getResource(),
						IResource.DEPTH_INFINITE, m);
			}
		} catch (final CoreException e) {
			if (e.getStatus().getCode() == IResourceStatus.MARKER_NOT_FOUND) {
				return 0;
			}
		}
		return 0;
	}

	public static int getErrorTicksFromMarkers(final IResource res,
			final int depth, final ISourceReference sourceElement)
			throws CoreException {
		if (res == null || !res.isAccessible()) {
			return 0;
		}
		int severity = 0;
		if (sourceElement == null) {
			severity = res.findMaxProblemSeverity(IMarker.PROBLEM, true, depth);
		} else {
			final IMarker[] markers = res.findMarkers(IMarker.PROBLEM, true,
					depth);
			if (markers != null && markers.length > 0) {
				for (int i = 0; i < markers.length
						&& severity != IMarker.SEVERITY_ERROR; i++) {
					final IMarker curr = markers[i];
					if (isMarkerInRange(curr, sourceElement)) {
						final int val = curr.getAttribute(IMarker.SEVERITY, -1);
						if (val == IMarker.SEVERITY_WARNING
								|| val == IMarker.SEVERITY_ERROR) {
							severity = val;
						}
					}
				}
			}
		}
		if (severity == IMarker.SEVERITY_ERROR) {
			return ERRORTICK_ERROR;
		} else if (severity == IMarker.SEVERITY_WARNING) {
			return ERRORTICK_WARNING;
		}
		return 0;
	}

	private static boolean isMarkerInRange(final IMarker marker,
			final ISourceReference sourceElement) throws CoreException {
		final int pos = marker.getAttribute(IMarker.CHAR_START, -1);
		if (pos != -1) {
			return isInside(pos, sourceElement);
		}
		final int line = marker.getAttribute(IMarker.LINE_NUMBER, -1);
		if (line != -1) {
			return isInsideLines(line, sourceElement);
		}
		return false;
	}

	// private boolean isInside(Position pos, ISourceReference sourceElement)
	// throws CoreException {
	// return pos != null && isInside(pos.getOffset(), sourceElement);
	// }

	private static boolean isInsideLines(final int line,
			final ISourceReference sourceElement) {
		return line >= sourceElement.getLineStart()
				&& line <= sourceElement.getLineEnd();
	}

	/**
	 * Tests if a position is inside the source range of an element.
	 * 
	 * @param pos
	 *            Position to be tested.
	 * @param sourceElement
	 *            Source element (must be a IErlElement)
	 * @return boolean Return <code>true</code> if position is located inside
	 *         the source element.
	 * @throws CoreException
	 *             Exception thrown if element range could not be accessed.
	 * 
	 */
	protected static boolean isInside(final int pos,
			final ISourceReference sourceElement) throws CoreException {
		// if (fCachedRange == null) {
		// fCachedRange= sourceElement.getSourceRange();
		// }
		// ISourceRange range= fCachedRange;
		final ISourceRange range = sourceElement.getSourceRange();
		if (range != null) {
			final int rangeOffset = range.getOffset();
			return rangeOffset <= pos && rangeOffset + range.getLength() > pos;
		}
		return false;
	}

	public void decorate(final Object element, final IDecoration decoration) {
		final int adornmentFlags = computeAdornmentFlags(element);
		if (adornmentFlags == ERRORTICK_ERROR) {
			decoration.addOverlay(ErlideUIPluginImages.DESC_OVR_ERROR);
		} else if (adornmentFlags == ERRORTICK_WARNING) {
			decoration.addOverlay(ErlideUIPluginImages.DESC_OVR_WARNING);
		}

	}

	public void addListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(final Object element, final String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(final ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}

/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>, Artjom Kochtchi
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package ilarkesto.gwt.client;

import ilarkesto.core.base.Str;
import ilarkesto.core.base.Utl;
import ilarkesto.core.logging.Log;

import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AViewEditWidget extends AWidget {

	private static Log log = Log.get(AViewEditWidget.class);
	private static AViewEditWidget currentEditor;
	private static ModeSwitchHandler globalModeSwitchHandler;

	private boolean viewMode = true;
	private ModeSwitchHandler modeSwitchHandler;

	private FocusPanel masterWrapper;
	private FocusPanel viewerWrapper;
	private FlowPanel editorWrapper;
	private SimplePanel errorWrapper;

	private boolean viewerInitialized;
	private boolean viewerInitializing;
	private boolean editorInitialized;
	private boolean editorInitializing;

	protected abstract void onViewerUpdate();

	protected abstract Widget onViewerInitialization();

	protected abstract void onEditorUpdate();

	protected abstract Widget onEditorInitialization();

	protected abstract void onEditorSubmit();

	@Override
	protected final Widget onInitialization() {
		masterWrapper = new FocusPanel();
		masterWrapper.setStyleName("AViewEditWidget");
		Gwt.addTooltipHtml(masterWrapper, getTooltip());
		return masterWrapper;
	}

	@Override
	protected void onUpdate() {
		if (isViewMode()) {
			updateViewer();
		} else {
			focusEditor();
			// updateEditor();
		}
	}

	protected void onEditorClose() {}

	protected void focusEditor() {}

	public void switchToEditMode() {
		if (isEditMode()) return;
		if (!isEditable()) return;
		ensureEditorInitialized();
		viewMode = false;
		if (currentEditor != null) {
			try {
				currentEditor.closeEditor();
			} catch (Throwable ex) {
				log.error(ex);
			}
		}
		currentEditor = this;
		updateEditor();
		focusEditor();
		if (modeSwitchHandler != null) modeSwitchHandler.onEditorActivated(this);
		if (globalModeSwitchHandler != null) globalModeSwitchHandler.onEditorActivated(this);
		onSwitchToEditModeCompleted();
	}

	protected void onSwitchToEditModeCompleted() {}

	public abstract boolean isEditable();

	public String getTooltip() {
		return null;
	}

	public void switchToViewMode() {
		if (isViewMode()) return;
		viewMode = true;
		onEditorClose();
		if (currentEditor == this) currentEditor = null;
		if (modeSwitchHandler != null) modeSwitchHandler.onViewerActivated(this);
		if (globalModeSwitchHandler != null) globalModeSwitchHandler.onViewerActivated(this);
		update();
	}

	public final boolean submitEditor() {
		return submitEditor(true);
	}

	public final boolean submitEditor(boolean switchToViewMode) {
		if (!isEditMode()) throw new RuntimeException("submitEditor() not allowed. Not in edit mode: " + toString());
		try {
			onEditorSubmit();
		} catch (Throwable ex) {
			ex.printStackTrace();
			setEditorError(Utl.getUserMessageStack(ex));
			return false;
		}
		setEditorError(null);
		if (switchToViewMode || !editorWrapper.isAttached()) {
			switchToViewMode();
			if (isAttached()) updateAutoUpdateWidget();
		}
		return true;
	}

	protected void updateAutoUpdateWidget() {
		Gwt.update(Gwt.getRootWidget());
	}

	protected final void cancelEditor() {
		if (!isEditMode()) throw new RuntimeException("cancelEditor() not allowed. Not in edit mode: " + toString());
		setEditorError(null);
		switchToViewMode();
	}

	protected void closeEditor() {
		cancelEditor();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		if (isEditMode()) closeEditor();
	}

	private void initializeViewer() {
		if (viewerInitialized) throw new RuntimeException("Viewer already initialized: " + toString());
		if (viewerInitializing) throw new RuntimeException("Viewer already initializing: " + toString());
		viewerInitializing = true;
		// GwtLogger.DEBUG("Initializing Viewer: " + toString());
		viewerWrapper = new FocusPanel();
		viewerWrapper.getElement().setId(getViewerId());
		viewerWrapper.setStyleName("AViewEditWidget-viewer");
		viewerWrapper.addClickHandler(new ViewerClickListener());
		viewerWrapper.setWidget(onViewerInitialization());
		viewerInitialized = true;
		viewerInitializing = false;
	}

	private void updateViewer() {
		if (viewerInitializing)
			throw new RuntimeException("Viewer initializing. Don't call update() within onViewerInitailization(): "
					+ toString());
		if (!viewerInitialized) initializeViewer();
		// GwtLogger.DEBUG("Updating viewer: " + toString());
		onViewerUpdate();
		if (isEditable()) {
			viewerWrapper.addStyleDependentName("editable");
		} else {
			viewerWrapper.removeStyleDependentName("editable");
		}
		masterWrapper.setWidget(viewerWrapper);
	}

	private void initializeEditor() {
		if (editorInitialized) throw new RuntimeException("Editor already initialized: " + toString());
		if (editorInitializing) throw new RuntimeException("Editor already initializing: " + toString());
		editorInitializing = true;
		// GwtLogger.DEBUG("Initializing Editor: " + toString());

		errorWrapper = new SimplePanel();

		editorWrapper = new FlowPanel();
		editorWrapper.setStyleName("AViewEditWidget-editor");
		editorWrapper.add(errorWrapper);
		Widget editor = onEditorInitialization();
		editor.getElement().setId(getEditroId());
		editorWrapper.add(editor);
		editorInitialized = true;
		editorInitializing = false;
	}

	protected void setEditorError(String text) {
		if (Str.isBlank(text)) {
			errorWrapper.clear();
		} else {
			errorWrapper.setWidget(Gwt.createDiv("AViewEditWidget-error", text));
		}
	}

	public void setModeSwitchHandler(ModeSwitchHandler modeSwitchHandler) {
		this.modeSwitchHandler = modeSwitchHandler;
	}

	protected final void ensureEditorInitialized() {
		if (editorInitializing)
			throw new RuntimeException("Editor initializing. Don't call update() within onEditorInitailization(): "
					+ toString());
		if (!editorInitialized) initializeEditor();
	}

	private void updateEditor() {
		initialize();
		masterWrapper.setWidget(editorWrapper);
		onEditorUpdate();
		getElement().scrollIntoView();
	}

	public final boolean isViewMode() {
		return viewMode;
	}

	public final boolean isEditMode() {
		return !viewMode;
	}

	@Override
	public String getId() {
		return Str.getSimpleName(getClass()).replace('$', '_');
	}

	protected String getViewerId() {
		return "viewer_" + getId();
	}

	protected String getEditroId() {
		return "editor_" + getId();
	}

	public static AViewEditWidget getCurrentEditor() {
		return currentEditor;
	}

	public static void setGlobalModeSwitchHandler(ModeSwitchHandler globalModeSwitchHandler) {
		AViewEditWidget.globalModeSwitchHandler = globalModeSwitchHandler;
	}

	private class ViewerClickListener implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (isEventSourceHref(event)) return; // no switch to edit mode when link clicked
			if (isEditable()) switchToEditMode();
			event.stopPropagation();
		}

		private boolean isEventSourceHref(ClickEvent event) {
			NativeEvent nativeEvent = event.getNativeEvent();
			if (nativeEvent != null) {
				EventTarget nativeEventTarget = nativeEvent.getEventTarget();
				if (nativeEventTarget != null) {
					String key = nativeEventTarget.toString();
					log.debug("Native event target:", key);

					// devMode
					if (key.startsWith("<div")) return false;
					if (key.startsWith("<a")) return true;

					if (key.startsWith("[object")) return false; // all browsers
					return true;
				}
			}
			return false;
		}
	}

	protected class SubmitEditorFocusListener implements FocusListener {

		public SubmitEditorFocusListener() {}

		@Override
		public void onFocus(Widget sender) {}

		@Override
		public void onLostFocus(Widget sender) {
			submitEditor();
		}

	}

	public class CancelKeyHandler implements KeyDownHandler {

		@Override
		public void onKeyDown(KeyDownEvent event) {
			int keyCode = event.getNativeKeyCode();
			if (keyCode == KeyCodes.KEY_ESCAPE) {
				cancelEditor();
			}
		}

	}

	public static interface ModeSwitchHandler {

		void onViewerActivated(AViewEditWidget widget);

		void onEditorActivated(AViewEditWidget widget);

	}

}

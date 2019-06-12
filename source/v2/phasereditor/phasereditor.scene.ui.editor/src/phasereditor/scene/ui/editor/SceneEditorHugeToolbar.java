// The MIT License (MIT)
//
// Copyright (c) 2015, 2019 Arian Fornaris
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions: The above copyright notice and this permission
// notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
package phasereditor.scene.ui.editor;

import static phasereditor.ui.IEditorSharedImages.IMG_ADD;

import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import phasereditor.inspect.core.InspectCore;
import phasereditor.scene.core.NameComputer;
import phasereditor.scene.core.TextModel;
import phasereditor.scene.core.TextualComponent;
import phasereditor.scene.core.VariableComponent;
import phasereditor.scene.ui.editor.messages.DropObjectsMessage;
import phasereditor.scene.ui.editor.messages.SelectObjectsMessage;
import phasereditor.scene.ui.editor.properties.CompilerSection;
import phasereditor.scene.ui.editor.properties.SceneEditorCommandAction;
import phasereditor.scene.ui.editor.properties.TransformSection;
import phasereditor.scene.ui.editor.properties.WebViewSection;
import phasereditor.scene.ui.editor.undo.WorldSnapshotOperation;
import phasereditor.ui.EditorSharedImages;
import phasereditor.ui.IEditorHugeToolbar;
import phasereditor.webrun.core.BatchMessage;

class SceneEditorHugeToolbar implements IEditorHugeToolbar {

	private SceneEditorCommandAction _positionToolAction;
	private SceneEditorCommandAction _scaleToolAction;
	private SceneEditorCommandAction _angleToolAction;
	private SceneEditor _editor;

	public SceneEditorHugeToolbar(SceneEditor editor) {
		_editor = editor;

		_positionToolAction = new SceneEditorCommandAction(_editor, SceneUIEditor.COMMAND_ID_POSITION_TOOL);
		_positionToolAction.setChecked(false);
		_positionToolAction.setEnabled(true);

		_scaleToolAction = new SceneEditorCommandAction(_editor, SceneUIEditor.COMMAND_ID_SCALE_TOOL);
		_scaleToolAction.setChecked(false);
		_scaleToolAction.setEnabled(true);

		_angleToolAction = new SceneEditorCommandAction(_editor, SceneUIEditor.COMMAND_ID_ANGLE_TOOL);
		_angleToolAction.setChecked(false);
		_angleToolAction.setEnabled(true);
	}

	@SuppressWarnings("unused")
	@Override
	public void createContent(Composite parent) {
		new ActionButton(parent, _positionToolAction);
		new ActionButton(parent, _scaleToolAction);
		new ActionButton(parent, _angleToolAction);

		sep(parent);

		{
			var btn = new Button(parent, SWT.PUSH);
			btn.setText("Add Object");
			btn.setImage(EditorSharedImages.getImage(IMG_ADD));
			btn.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
				var manager = new MenuManager();
				manager.add(new AddTextAction());
				manager.createContextMenu(btn).setVisible(true);
			}));
		}

		sep(parent);

		{
			var btn = CompilerSection.createCompileButton(parent, _editor);
			btn.setToolTipText(btn.getText());
			btn.setText("");
		}

		{
			var btn = CompilerSection.createGoToCodeButton(parent, _editor);
			btn.setToolTipText(btn.getText());
			btn.setText("");
		}

		{
			var btn = WebViewSection.createRefreshButton(parent, _editor);
			btn.setToolTipText(btn.getText());
			btn.setText("");
		}

		updateActions();
	}

	class AddTextAction extends Action {
		public AddTextAction() {
			super("Text");
			setToolTipText(InspectCore.getPhaserHelp().getMemberHelp("Phaser.GameObjects.Text"));
		}

		@Override
		public void run() {

			var displayList = _editor.getSceneModel().getDisplayList();
			var computer = new NameComputer(displayList);
			var name = computer.newName("text");

			var textModel = new TextModel();

			VariableComponent.set_variableName(textModel, name);
			TextualComponent.set_text(textModel, "Text...");

			var before = WorldSnapshotOperation.takeSnapshot(_editor);

			displayList.getChildren().add(textModel);

			var after = WorldSnapshotOperation.takeSnapshot(_editor);

			_editor.executeOperation(new WorldSnapshotOperation(before, after, "Create Text"));

			_editor.refreshOutline();

			_editor.setSelection(List.of(textModel));
			
			_editor.updatePropertyPagesContentWithSelection();

			_editor.setDirty(true);

			_editor.getBroker().sendAll(new BatchMessage(

					new DropObjectsMessage(List.of(textModel)),

					new SelectObjectsMessage(_editor)

			));
		}
	}

	public void updateActions() {
		_positionToolAction.setChecked(_editor.hasInteractiveTools(Set.of(TransformSection.POSITION_TOOL)));
		_scaleToolAction.setChecked(_editor.hasInteractiveTools(Set.of(TransformSection.SCALE_TOOL)));
		_angleToolAction.setChecked(_editor.hasInteractiveTools(Set.of(TransformSection.ANGLE_TOOL)));
	}

}
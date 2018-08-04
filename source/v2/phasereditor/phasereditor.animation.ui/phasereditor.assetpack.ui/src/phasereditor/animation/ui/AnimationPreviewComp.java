// The MIT License (MIT)
//
// Copyright (c) 2015, 2018 Arian Fornaris
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
package phasereditor.animation.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import phasereditor.assetpack.core.animations.AnimationModel;
import phasereditor.project.core.ProjectCore;
import phasereditor.ui.EditorSharedImages;
import phasereditor.ui.FrameGridCanvas;
import phasereditor.ui.FrameGridCanvas.IFrameProvider;
import phasereditor.ui.IEditorSharedImages;
import phasereditor.ui.IZoomable;
import phasereditor.ui.ImageCanvas_Zoom_1_1_Action;
import phasereditor.ui.ImageCanvas_Zoom_FitWindow_Action;

/**
 * @author arian
 *
 */
public class AnimationPreviewComp extends Composite {

	private Action _tilesAction;
	private Action _animationAction;
	FrameGridCanvas _gridCanvas;
	AnimationCanvas _animCanvas;
	private ImageCanvas_Zoom_1_1_Action _zoom_1_1_action;
	private ImageCanvas_Zoom_FitWindow_Action _zoom_fitWindow_action;
	protected AnimationModel _animModel;

	public AnimationPreviewComp(Composite parent, int style) {
		super(parent, style);

		setLayout(new StackLayout());

		_gridCanvas = new FrameGridCanvas(this, SWT.NONE);
		_animCanvas = new AnimationCanvas(this, SWT.NONE);

		afterCreateWidgets();
	}

	private void afterCreateWidgets() {

		// force the start the project builders
		ProjectCore.getBuildParticipants();

		moveTop(_gridCanvas);
	}

	public void setModel(AnimationModel animModel) {
		_animModel = animModel;

		if (getTopCanvas() == _animCanvas) {
			_animCanvas.setModel(animModel);
		}

		if (animModel == null) {
			_gridCanvas.loadFrameProvider(IFrameProvider.NULL);
			return;
		}

		for (var anim : _animModel.getFrames()) {
			var asset = anim.getFrameAsset();
			if (asset == null) {
				// just do not show this model, it has not resolved frames.
				_gridCanvas.loadFrameProvider(IFrameProvider.NULL);
				return;
			}
		}

		_gridCanvas.loadFrameProvider(new AnimationFrameProvider(animModel));
	}

	private void updateActionsState() {
		if (_zoom_1_1_action == null) {
			return;
		}

		StackLayout layout = (StackLayout) getLayout();
		Control control = layout.topControl;

		_tilesAction.setChecked(control == _gridCanvas);
		_animationAction.setChecked(control == _animCanvas);
	}

	void moveTop(Control control) {
		StackLayout layout = (StackLayout) getLayout();
		layout.topControl = control;
		layout();

		updateActionsState();

		control.setFocus();
	}

	public void createToolBar(IToolBarManager toolbar) {

		_tilesAction = new Action("Tiles", IAction.AS_CHECK_BOX) {
			{
				setImageDescriptor(EditorSharedImages.getImageDescriptor(IEditorSharedImages.IMG_APPLICATION_TILE));
			}

			@Override
			public void run() {
				moveTop(_gridCanvas);
				_animCanvas.stop();
			}
		};

		_animationAction = new Action("Tiles", IAction.AS_CHECK_BOX) {
			{
				setImageDescriptor(EditorSharedImages.getImageDescriptor(IEditorSharedImages.IMG_PLAY));
			}

			@Override
			public void run() {
				moveTop(_animCanvas);
				if (_animCanvas.getModel() == null) {
					_animCanvas.setModel(_animModel);
				} else {
					_animCanvas.play();
				}
			}
		};

		toolbar.add(_tilesAction);
		toolbar.add(_animationAction);

		toolbar.add(new Separator());

		_zoom_1_1_action = new ImageCanvas_Zoom_1_1_Action() {
			@Override
			public IZoomable getImageCanvas() {
				return getTopCanvas();
			}
		};
		_zoom_fitWindow_action = new ImageCanvas_Zoom_FitWindow_Action() {
			@Override
			public IZoomable getImageCanvas() {
				return getTopCanvas();
			}
		};
		toolbar.add(_zoom_1_1_action);
		toolbar.add(_zoom_fitWindow_action);

		updateActionsState();
	}

	IZoomable getTopCanvas() {
		Control top = ((StackLayout) getLayout()).topControl;
		if (top instanceof IZoomable) {
			return (IZoomable) top;
		}
		return null;
	}

}
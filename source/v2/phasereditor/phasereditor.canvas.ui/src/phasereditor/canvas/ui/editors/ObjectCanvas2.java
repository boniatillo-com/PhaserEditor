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
package phasereditor.canvas.ui.editors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import phasereditor.canvas.core.CanvasModel;
import phasereditor.canvas.core.EditorSettings;
import phasereditor.canvas.core.WorldModel;
import phasereditor.canvas.ui.editors.grid.CanvasPGrid;
import phasereditor.ui.BaseImageCanvas;
import phasereditor.ui.ZoomCanvas;

/**
 * @author arian
 *
 */
public class ObjectCanvas2 extends ZoomCanvas {

	private CanvasEditor _editor;
	private EditorSettings _settingsModel;
	private WorldModel _worldModel;
	private CanvasPGrid _pgrid;
	private TreeViewer _outline;
	private SceneRenderer _renderer;

	public ObjectCanvas2(Composite parent, int style) {
		super(parent, style);
		
		addPaintListener(this);
	}

	public void init(CanvasEditor editor, CanvasModel model, CanvasPGrid grid, TreeViewer outline) {
		_editor = editor;
		_settingsModel = model.getSettings();
		_worldModel = model.getWorld();
		_pgrid = grid;
		_outline = outline;
		
		_renderer = new SceneRenderer(this);
		
	}

	@Override
	protected void customPaintControl(PaintEvent e) {
		_renderer.renderWorld(e.gc, _worldModel);
	}

	@Override
	protected Point getImageSize() {
		return new Point(1, 1);
	}
	
}
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
package phasereditor.canvas.ui.editors.edithandlers;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import phasereditor.canvas.core.BaseSpriteModel;
import phasereditor.canvas.ui.editors.operations.ChangePropertyOperation;
import phasereditor.canvas.ui.editors.operations.CompositeOperation;
import phasereditor.canvas.ui.shapes.IObjectNode;
import phasereditor.canvas.ui.shapes.ISpriteNode;

/**
 * @author arian
 *
 */
@SuppressWarnings("boxing")
public class AnchorShortcutsPane extends ShortcutPane {

	private Label _xLabel;
	private Label _yLabel;

	public AnchorShortcutsPane(IObjectNode object) {
		super(object);

		double[] values = { 0, 0.5, 1 };

		_xLabel = createValueLabel();
		_yLabel = createValueLabel();

		add(_xLabel, 0, 0, 3, 1);
		add(_yLabel, 0, 1, 3, 1);

		int row = 3;
		for (double y : values) {
			int col = 0;
			for (double x : values) {
				Btn btn = new Btn(x, y);
				setRowIndex(btn, row);
				setColumnIndex(btn, col++);
				getChildren().add(btn);
			}
			row++;
		}
	}

	@Override
	public void updateHandler() {

		BaseSpriteModel model = (BaseSpriteModel) _model;

		_xLabel.setText("x = " + Double.toString(model.getAnchorX()));
		_yLabel.setText("y = " + Double.toString(model.getAnchorY()));

		for (Object node : getChildren()) {
			if (node instanceof Btn) {
				((Btn) node).updateHandler();
			}
		}

		super.updateHandler();
	}

	class Btn extends ShortcutButton {

		private double _anchorX;
		private double _anchorY;
		private Rectangle _rect2;

		public Btn(double anchorX, double anchorY) {
			_anchorX = anchorX;
			_anchorY = anchorY;

			double size = 30;

			setSize(size, size);

			Rectangle rect1 = new Rectangle(size, size);
			rect1.setFill(Color.TRANSPARENT);
			rect1.setStrokeWidth(0);

			_rect2 = new Rectangle(size / 4, size / 4);
			_rect2.setFill(Color.ALICEBLUE);
			_rect2.setStroke(Color.BLACK);
			_rect2.setStrokeWidth(1);
			_rect2.setEffect(new DropShadow(1, Color.WHITE));

			double x = 0;
			double y = 0;

			switch ((int) (_anchorX * 10)) {
			case 5:
				x = size / 2 - _rect2.getWidth() / 2;
				break;
			case 10:
				x = size - _rect2.getWidth();
				break;
			default:
				break;
			}

			switch ((int) (_anchorY * 10)) {
			case 5:
				y = size / 2 - _rect2.getHeight() / 2;
				break;
			case 10:
				y = size - _rect2.getHeight();
				break;
			default:
				break;
			}

			_rect2.relocate(x, y);

			setGraphic(new Group(rect1, _rect2));

		}

		public void updateHandler() {
			BaseSpriteModel model = (BaseSpriteModel) _model;
			
			_rect2.setFill(
					model.getAnchorX() == _anchorX && model.getAnchorY() == _anchorY ? Color.RED : Color.ALICEBLUE);
		}

		@Override
		protected void doAction() {
			CompositeOperation operations = new CompositeOperation();
			ISpriteNode sprite = (ISpriteNode) _object;
			BaseSpriteModel model = sprite.getModel();

			double anchorDX = _anchorX - model.getAnchorX();
			double dx = anchorDX * _control.getTextureWidth();

			double anchorDY = _anchorY - model.getAnchorY();
			double dy = anchorDY * _control.getTextureHeight();

			Point2D p = new Point2D(dx, dy);
			for (Transform t : _node.getTransforms()) {
				p = t.deltaTransform(p);
			}

			double x = model.getX() + p.getX();
			double y = model.getY() + p.getY();

			String id = model.getId();

			operations.add(new ChangePropertyOperation<Number>(id, "x", Double.valueOf(x)));
			operations.add(new ChangePropertyOperation<Number>(id, "y", Double.valueOf(y)));
			operations.add(new ChangePropertyOperation<Number>(id, "anchor.x", Double.valueOf(_anchorX)));
			operations.add(new ChangePropertyOperation<Number>(id, "anchor.y", Double.valueOf(_anchorY)));

			_canvas.getUpdateBehavior().executeOperations(operations);
		}
	}

	@Override
	public IObjectNode getObject() {
		return _object;
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
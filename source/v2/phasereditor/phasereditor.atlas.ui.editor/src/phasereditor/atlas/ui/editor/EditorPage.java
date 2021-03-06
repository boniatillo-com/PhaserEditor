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
package phasereditor.atlas.ui.editor;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.graphics.Image;

public class EditorPage extends ArrayList<TexturePackerEditorFrame> implements IAdaptable {
	private static final long serialVersionUID = 1L;
	private int _index;
	private TexturePackerEditorModel _model;
	private Image _image;
	private IFile _imageFile;

	public EditorPage(TexturePackerEditorModel model, int index) {
		super();
		_model = model;
		_index = index;
	}

	public int getIndex() {
		return _index;
	}

	public void setIndex(int index) {
		_index = index;
	}

	public String getName() {
		return _model.getAtlasImageName(_index);
	}

	public void sortByIndexes() {
		sort((a, b) -> {
			int i1 = a.getIndex();
			int i2 = b.getIndex();
			return Integer.compare(i1 == -1 ? Integer.MAX_VALUE : i1, i2 == -1 ? Integer.MAX_VALUE : i2);
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	public void setImage(Image img) {
		_image = img;
	}

	public Image getImage() {
		return _image;
	}

	public void setImageFile(IFile imageFile) {
		_imageFile = imageFile;
	}

	public IFile getImageFile() {
		return _imageFile;
	}

	public void dispose() {
		if (_image != null) {
			_image.dispose();
		}
	}
}
// The MIT License (MIT)
//
// Copyright (c) 2015 Arian Fornaris
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
package phasereditor.assetpack.ui.preview;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import phasereditor.audio.ui.Html5AudioPlayer;
import phasereditor.ui.info.BaseInformationControl;

public class AudioFileInformationControl extends BaseInformationControl {

	public AudioFileInformationControl(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createContent2(Composite parentComp) {
		// return new GdxAutoMusicControl(parentComp, SWT.NONE);
		return new Html5AudioPlayer(parentComp, SWT.NONE);
	}

	@Override
	protected void updateContent(Control control, Object model) {
		// ((GdxAutoMusicControl) control).load((IFile) model);
		((Html5AudioPlayer)control).load((IFile) model, true);
	}

	@Override
	protected void handleHidden(Control control) {
//		GdxAutoMusicControl musicControl = (GdxAutoMusicControl) control;
//		musicControl.stop();
//		musicControl.disposeMusic();
		((Html5AudioPlayer)control).load(null);
	}
}

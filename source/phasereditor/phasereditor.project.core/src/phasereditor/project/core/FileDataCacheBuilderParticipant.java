// The MIT License (MIT)
//
// Copyright (c) 2015, 2017 Arian Fornaris
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
package phasereditor.project.core;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

/**
 * @author arian
 *
 */
public abstract class FileDataCacheBuilderParticipant<TData> implements IProjectBuildParticipant {

	public abstract FileDataCache<TData> getFileDataCache();

	@Override
	public void startupOnInitialize(IProject project, Map<String, Object> env) {
		fullBuild(project, env);
	}

	@Override
	public void fullBuild(IProject project, Map<String, Object> env) {
		try {
			getFileDataCache().buildProject(project);
		} catch (CoreException e) {
			ProjectCore.logError(e);
		}
	}

	@Override
	public void clean(IProject project, Map<String, Object> env) {
		getFileDataCache().clean(project);
	}
	
	@Override
	public void projectDeleted(IProject project, Map<String, Object> env) {
		getFileDataCache().clean(project);
	}

	@Override
	public void build(IProject project, IResourceDelta delta, Map<String, Object> env) {
		try {
			getFileDataCache().buildDelta(project, delta);
		} catch (CoreException e) {
			ProjectCore.logError(e);
		}
	}
}

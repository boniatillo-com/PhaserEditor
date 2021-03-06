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
package phasereditor.webrun.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

/**
 * @author arian
 *
 */
public class PluginResourcesHandler extends ResourceHandler {

	private String _plugin;
	private String _pluginFolder;

	public PluginResourcesHandler(String plugin, String pluginFolder) {
		super();
		_plugin = plugin;
		_pluginFolder = pluginFolder;
	}

	@Override
	public Resource getResource(String path) {
		try {
			var spec = "platform:/plugin/" + _plugin + "/" + _pluginFolder + path;
			return Resource.newResource(new URL(spec));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return super.getResource(path);
	}
}

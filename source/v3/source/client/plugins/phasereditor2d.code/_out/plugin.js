var phasereditor2d;
(function (phasereditor2d) {
    var code;
    (function (code) {
        class CodePlugin extends colibri.ui.ide.Plugin {
            constructor() {
                super("phasereditor2d.core");
            }
            static getInstance() {
                if (!this._instance) {
                    this._instance = new CodePlugin();
                }
                return this._instance;
            }
            registerExtensions(reg) {
                reg.addExtension(colibri.ui.ide.EditorExtension.POINT_ID, new colibri.ui.ide.EditorExtension("phasereditor2d.core.ui.editors", [
                    new code.ui.editors.MonacoEditorFactory("javascript", phasereditor2d.webContentTypes.core.CONTENT_TYPE_JAVASCRIPT),
                    new code.ui.editors.MonacoEditorFactory("typescript", phasereditor2d.webContentTypes.core.CONTENT_TYPE_SCRIPT),
                    new code.ui.editors.MonacoEditorFactory("html", phasereditor2d.webContentTypes.core.CONTENT_TYPE_HTML),
                    new code.ui.editors.MonacoEditorFactory("css", phasereditor2d.webContentTypes.core.CONTENT_TYPE_CSS),
                    new code.ui.editors.MonacoEditorFactory("json", phasereditor2d.webContentTypes.core.CONTENT_TYPE_JSON),
                    new code.ui.editors.MonacoEditorFactory("xml", phasereditor2d.webContentTypes.core.CONTENT_TYPE_XML),
                    new code.ui.editors.MonacoEditorFactory("text", phasereditor2d.webContentTypes.core.CONTENT_TYPE_TEXT),
                ]));
            }
        }
        code.CodePlugin = CodePlugin;
        colibri.ui.ide.Workbench.getWorkbench().addPlugin(CodePlugin.getInstance());
    })(code = phasereditor2d.code || (phasereditor2d.code = {}));
})(phasereditor2d || (phasereditor2d = {}));
var phasereditor2d;
(function (phasereditor2d) {
    var code;
    (function (code) {
        var ui;
        (function (ui) {
            var editors;
            (function (editors) {
                var io = colibri.core.io;
                class MonacoEditorFactory extends colibri.ui.ide.EditorFactory {
                    constructor(language, contentType) {
                        super("phasereditor2d.core.ui.editors.MonacoEditorFactory#" + language);
                        this._language = language;
                        this._contentType = contentType;
                    }
                    acceptInput(input) {
                        if (input instanceof io.FilePath) {
                            const contentType = colibri.ui.ide.Workbench.getWorkbench().getContentTypeRegistry().getCachedContentType(input);
                            return this._contentType === contentType;
                        }
                        return false;
                    }
                    createEditor() {
                        return new MonacoEditor(this._language);
                    }
                }
                editors.MonacoEditorFactory = MonacoEditorFactory;
                class MonacoEditor extends colibri.ui.ide.FileEditor {
                    constructor(language) {
                        super("phasereditor2d.core.ui.editors.JavaScriptEditor");
                        this._language = language;
                    }
                    createPart() {
                        this._monacoEditor = monaco.editor.create(this.getElement(), {
                            language: this._language
                        });
                        this.updateContent();
                    }
                    setInput(file) {
                        super.setInput(file);
                        this.updateContent();
                    }
                    async updateContent() {
                        const file = this.getInput();
                        if (!file) {
                            return;
                        }
                        const content = await colibri.ui.ide.FileUtils.preloadAndGetFileString(file);
                        this._monacoEditor.setValue(content);
                    }
                    layout() {
                        super.layout();
                        this._monacoEditor.layout();
                    }
                    onEditorInputContentChanged() {
                    }
                }
                editors.MonacoEditor = MonacoEditor;
            })(editors = ui.editors || (ui.editors = {}));
        })(ui = code.ui || (code.ui = {}));
    })(code = phasereditor2d.code || (phasereditor2d.code = {}));
})(phasereditor2d || (phasereditor2d = {}));
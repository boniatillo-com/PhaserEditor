var phasereditor2d;
(function (phasereditor2d) {
    var ide;
    (function (ide_1) {
        var ide = colibri.ui.ide;
        ide_1.ICON_NEW_FILE = "file-new";
        class IDEPlugin extends ide.Plugin {
            constructor() {
                super("phasereditor2d.ide");
            }
            static getInstance() {
                return this._instance;
            }
            registerExtensions(reg) {
                // icons loader
                // icons loader
                reg.addExtension(ide.IconLoaderExtension.POINT_ID, ide.IconLoaderExtension.withPluginFiles(this, [
                    ide_1.ICON_NEW_FILE
                ]));
            }
            createWindow(windows) {
                windows.push(new ide_1.ui.windows.DesignWindow());
            }
            openNewWizard() {
            }
        }
        IDEPlugin._instance = new IDEPlugin();
        ide_1.IDEPlugin = IDEPlugin;
        ide.Workbench.getWorkbench().addPlugin(IDEPlugin.getInstance());
        ide_1.VER = "3.0.0";
        function main() {
            console.log(`%c %c Phaser Editor 2D %c v${ide_1.VER} %c %c https://phasereditor2d.com `, "background-color:red", "background-color:#3f3f3f;color:whitesmoke", "background-color:orange;color:black", "background-color:red", "background-color:silver");
            ide.Workbench.getWorkbench().launch();
        }
        window.addEventListener("load", main);
    })(ide = phasereditor2d.ide || (phasereditor2d.ide = {}));
})(phasereditor2d || (phasereditor2d = {}));
var phasereditor2d;
(function (phasereditor2d) {
    var ide;
    (function (ide) {
        var ui;
        (function (ui) {
            var actions;
            (function (actions) {
                var controls = colibri.ui.controls;
                class OpenNewWizardAction extends controls.Action {
                    constructor() {
                        super({
                            text: "New",
                            icon: ide.IDEPlugin.getInstance().getIcon(ide.ICON_NEW_FILE)
                        });
                    }
                    run() {
                        const viewer = new controls.viewers.TreeViewer();
                        viewer.setLabelProvider(new WizardLabelProvider());
                        viewer.setContentProvider(new controls.viewers.ArrayTreeContentProvider());
                        viewer.setCellRendererProvider(new WizardCellRendererProvider());
                        const extensions = colibri.ui.ide.Workbench.getWorkbench()
                            .getExtensionRegistry()
                            .getExtensions(phasereditor2d.ide.ui.wizards.NewWizardExtension.POINT);
                        viewer.setInput(extensions);
                        const dlg = new controls.dialogs.ViewerDialog(viewer);
                        dlg.create();
                        dlg.setTitle("New");
                        {
                            const selectCallback = () => {
                                dlg.close();
                                this.openFileDialog(viewer.getSelectionFirstElement());
                            };
                            const btn = dlg.addButton("Select", () => selectCallback());
                            btn.disabled = true;
                            viewer.addEventListener(controls.EVENT_SELECTION_CHANGED, e => {
                                btn.disabled = viewer.getSelection().length !== 1;
                            });
                            viewer.addEventListener(controls.viewers.EVENT_OPEN_ITEM, e => selectCallback());
                        }
                        dlg.addButton("Cancel", () => dlg.close());
                    }
                    openFileDialog(extension) {
                        const dlg = new ui.wizards.FileLocationDialog();
                        dlg.create();
                        dlg.setTitle(`New ${extension.getWizardName()}`);
                        dlg.setInitialFileName(`${extension.getInitialFileName()}.${extension.getFileExtension()}`);
                    }
                }
                actions.OpenNewWizardAction = OpenNewWizardAction;
                class WizardLabelProvider {
                    getLabel(obj) {
                        return obj.getWizardName();
                    }
                }
                class WizardCellRendererProvider {
                    getCellRenderer(element) {
                        const ext = element;
                        return new controls.viewers.IconImageCellRenderer(ext.getIcon());
                    }
                    preload(element) {
                        return controls.Controls.resolveNothingLoaded();
                    }
                }
            })(actions = ui.actions || (ui.actions = {}));
        })(ui = ide.ui || (ide.ui = {}));
    })(ide = phasereditor2d.ide || (phasereditor2d.ide = {}));
})(phasereditor2d || (phasereditor2d = {}));
var phasereditor2d;
(function (phasereditor2d) {
    var ide;
    (function (ide_2) {
        var ui;
        (function (ui) {
            var windows;
            (function (windows) {
                var controls = colibri.ui.controls;
                var ide = colibri.ui.ide;
                class DesignWindow extends ide.WorkbenchWindow {
                    constructor() {
                        super();
                        this._outlineView = new phasereditor2d.outline.ui.views.OutlineView();
                        this._filesView = new phasereditor2d.files.ui.views.FilesView();
                        this._inspectorView = new phasereditor2d.inspector.ui.views.InspectorView();
                        this._blocksView = new phasereditor2d.blocks.ui.views.BlocksView();
                        this._editorArea = new ide.EditorArea();
                        this._split_Files_Blocks = new controls.SplitPanel(this.createViewFolder(this._filesView), this.createViewFolder(this._blocksView));
                        this._split_Editor_FilesBlocks = new controls.SplitPanel(this._editorArea, this._split_Files_Blocks, false);
                        this._split_Outline_EditorFilesBlocks = new controls.SplitPanel(this.createViewFolder(this._outlineView), this._split_Editor_FilesBlocks);
                        this._split_OutlineEditorFilesBlocks_Inspector = new controls.SplitPanel(this._split_Outline_EditorFilesBlocks, this.createViewFolder(this._inspectorView));
                        this.getClientArea().add(this._split_OutlineEditorFilesBlocks_Inspector);
                        this.initToolbar();
                        this.initialLayout();
                    }
                    initToolbar() {
                        const toolbar = this.getToolbar();
                        const leftArea = toolbar.getLeftArea();
                        const manager = new controls.ToolbarManager(leftArea);
                        manager.add(new ui.actions.OpenNewWizardAction());
                    }
                    getEditorArea() {
                        return this._editorArea;
                    }
                    initialLayout() {
                        //const b = { x: 0, y: 0, width: window.innerWidth, height: window.innerHeight };
                        this._split_Files_Blocks.setSplitFactor(0.2);
                        this._split_Editor_FilesBlocks.setSplitFactor(0.6);
                        this._split_Outline_EditorFilesBlocks.setSplitFactor(0.15);
                        this._split_OutlineEditorFilesBlocks_Inspector.setSplitFactor(0.8);
                        //this.setBounds(b);
                        this.layout();
                    }
                }
                windows.DesignWindow = DesignWindow;
            })(windows = ui.windows || (ui.windows = {}));
        })(ui = ide_2.ui || (ide_2.ui = {}));
    })(ide = phasereditor2d.ide || (phasereditor2d.ide = {}));
})(phasereditor2d || (phasereditor2d = {}));
var phasereditor2d;
(function (phasereditor2d) {
    var ide;
    (function (ide) {
        var ui;
        (function (ui) {
            var wizards;
            (function (wizards) {
                var controls = colibri.ui.controls;
                var viewers = colibri.ui.controls.viewers;
                var dialogs = colibri.ui.controls.dialogs;
                class FileLocationDialog extends dialogs.Dialog {
                    constructor() {
                        super("FileLocationDialog");
                    }
                    createDialogArea() {
                        const clientArea = document.createElement("div");
                        clientArea.classList.add("DialogClientArea");
                        clientArea.style.display = "grid";
                        clientArea.style.gridTemplateRows = "1fr auto";
                        clientArea.style.gridTemplateRows = "1fr";
                        clientArea.style.gridRowGap = "5px";
                        clientArea.appendChild(this.createCenterArea());
                        clientArea.appendChild(this.createBottomArea());
                        this.getElement().appendChild(clientArea);
                    }
                    createBottomArea() {
                        const bottomArea = document.createElement("div");
                        bottomArea.classList.add("DialogSection");
                        bottomArea.style.display = "grid";
                        bottomArea.style.gridTemplateColumns = "auto 1fr";
                        bottomArea.style.gridTemplateRows = "auto";
                        bottomArea.style.columnGap = "10px";
                        bottomArea.style.rowGap = "10px";
                        bottomArea.style.alignItems = "center";
                        {
                            const label = document.createElement("label");
                            label.innerText = "Location";
                            bottomArea.appendChild(label);
                            const text = document.createElement("input");
                            text.type = "text";
                            text.readOnly = true;
                            bottomArea.appendChild(text);
                            this._filteredViewer.getViewer().addEventListener(controls.EVENT_SELECTION_CHANGED, e => {
                                const file = this._filteredViewer.getViewer().getSelectionFirstElement();
                                text.value = file === null ? "" : `${file.getFullName()}/`;
                            });
                        }
                        {
                            const label = document.createElement("label");
                            label.innerText = "Name";
                            bottomArea.appendChild(label);
                            const text = document.createElement("input");
                            text.type = "text";
                            bottomArea.appendChild(text);
                            setTimeout(() => text.focus(), 10);
                            text.addEventListener("keyup", e => this.validate());
                            this._fileNameText = text;
                        }
                        return bottomArea;
                    }
                    validate() {
                        const folder = this._filteredViewer.getViewer().getSelectionFirstElement();
                        let valid = folder !== null;
                        if (valid) {
                            const name = this._fileNameText.value;
                            if (name.indexOf("/") >= 0 || name.trim() === "") {
                                valid = false;
                            }
                            else {
                                const file = folder.getFile(name);
                                if (file) {
                                    valid = false;
                                }
                            }
                        }
                        this._createBtn.disabled = !valid;
                    }
                    setInitialFileName(filename) {
                        this._fileNameText.value = filename;
                    }
                    create() {
                        super.create();
                        this._createBtn = this.addButton("Create", () => { });
                        this.addButton("Cancel", () => this.close());
                        this.validate();
                    }
                    createCenterArea() {
                        const centerArea = document.createElement("div");
                        this.createFilteredViewer();
                        centerArea.appendChild(this._filteredViewer.getElement());
                        return centerArea;
                    }
                    createFilteredViewer() {
                        const viewer = new viewers.TreeViewer();
                        viewer.setLabelProvider(new phasereditor2d.files.ui.viewers.FileLabelProvider());
                        viewer.setContentProvider(new phasereditor2d.files.ui.viewers.FileTreeContentProvider(true));
                        viewer.setCellRendererProvider(new phasereditor2d.files.ui.viewers.FileCellRendererProvider());
                        viewer.setInput(colibri.ui.ide.Workbench.getWorkbench().getProjectRoot());
                        viewer.addEventListener(controls.EVENT_SELECTION_CHANGED, e => {
                            this.validate();
                        });
                        this._filteredViewer = new viewers.FilteredViewerInElement(viewer);
                        addEventListener(controls.EVENT_CONTROL_LAYOUT, (e) => {
                            this._filteredViewer.resizeTo();
                        });
                    }
                }
                wizards.FileLocationDialog = FileLocationDialog;
            })(wizards = ui.wizards || (ui.wizards = {}));
        })(ui = ide.ui || (ide.ui = {}));
    })(ide = phasereditor2d.ide || (phasereditor2d.ide = {}));
})(phasereditor2d || (phasereditor2d = {}));
var phasereditor2d;
(function (phasereditor2d) {
    var ide;
    (function (ide) {
        var ui;
        (function (ui) {
            var wizards;
            (function (wizards) {
                class NewWizardExtension extends colibri.core.extensions.Extension {
                    constructor(config) {
                        super(config.id);
                        this._wizardName = config.wizardName;
                        this._icon = config.icon;
                        this._initialFileName = config.initialFileName;
                        this._fileExtension = config.fileExtension;
                    }
                    getInitialFileName() {
                        return this._initialFileName;
                    }
                    getFileExtension() {
                        return this._fileExtension;
                    }
                    getWizardName() {
                        return this._wizardName;
                    }
                    getIcon() {
                        return this._icon;
                    }
                }
                NewWizardExtension.POINT = "phasereditor2d.ide.ui.wizards.new";
                wizards.NewWizardExtension = NewWizardExtension;
            })(wizards = ui.wizards || (ui.wizards = {}));
        })(ui = ide.ui || (ide.ui = {}));
    })(ide = phasereditor2d.ide || (phasereditor2d.ide = {}));
})(phasereditor2d || (phasereditor2d = {}));

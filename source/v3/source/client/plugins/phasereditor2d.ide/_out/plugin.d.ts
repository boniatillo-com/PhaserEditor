declare namespace phasereditor2d.ide {
    import ide = colibri.ui.ide;
    const ICON_NEW_FILE = "file-new";
    class IDEPlugin extends ide.Plugin {
        private static _instance;
        static getInstance(): IDEPlugin;
        private constructor();
        registerExtensions(reg: colibri.core.extensions.ExtensionRegistry): void;
        createWindow(windows: ide.WorkbenchWindow[]): void;
        openNewWizard(): void;
    }
    const VER = "3.0.0";
}
declare namespace phasereditor2d.ide.ui.actions {
    import controls = colibri.ui.controls;
    class OpenNewWizardAction extends controls.Action {
        constructor();
        run(): void;
        private openFileDialog;
    }
}
declare namespace phasereditor2d.ide.ui.windows {
    import ide = colibri.ui.ide;
    class DesignWindow extends ide.WorkbenchWindow {
        private _outlineView;
        private _filesView;
        private _inspectorView;
        private _blocksView;
        private _editorArea;
        private _split_Files_Blocks;
        private _split_Editor_FilesBlocks;
        private _split_Outline_EditorFilesBlocks;
        private _split_OutlineEditorFilesBlocks_Inspector;
        constructor();
        private initToolbar;
        getEditorArea(): ide.EditorArea;
        private initialLayout;
    }
}
declare namespace phasereditor2d.ide.ui.wizards {
    import dialogs = colibri.ui.controls.dialogs;
    class FileLocationDialog extends dialogs.Dialog {
        private _filteredViewer;
        private _fileNameText;
        private _createBtn;
        constructor();
        protected createDialogArea(): void;
        private createBottomArea;
        private validate;
        setInitialFileName(filename: string): void;
        create(): void;
        private createCenterArea;
        private createFilteredViewer;
    }
}
declare namespace phasereditor2d.ide.ui.wizards {
    import controls = colibri.ui.controls;
    class NewWizardExtension extends colibri.core.extensions.Extension {
        static POINT: string;
        private _wizardName;
        private _icon;
        private _initialFileName;
        private _fileExtension;
        constructor(config: {
            id: string;
            wizardName: string;
            icon: controls.IImage;
            initialFileName: string;
            fileExtension: string;
        });
        getInitialFileName(): string;
        getFileExtension(): string;
        getWizardName(): string;
        getIcon(): controls.IImage;
    }
}
//# sourceMappingURL=plugin.d.ts.map
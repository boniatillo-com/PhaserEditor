/// <reference path="../../EditorViewerProvider.ts" />
/// <reference path="./outline/SceneEditorOutlineProvider.ts" />

namespace phasereditor2d.ui.ide.editors.scene {

    import io = core.io;

    class SceneEditorFactory extends EditorFactory {

        constructor() {
            super("phasereditor2d.SceneEditorFactory");
        }

        acceptInput(input: any): boolean {

            if (input instanceof io.FilePath) {
                const contentType = Workbench.getWorkbench().getContentTypeRegistry().getCachedContentType(input);
                return contentType === CONTENT_TYPE_SCENE;
            }

            return false;
        }

        createEditor(): EditorPart {
            return new SceneEditor();
        }


    }

    export class SceneEditor extends FileEditor {

        private _blocksProvider: blocks.SceneEditorBlocksProvider;
        private _outlineProvider: outline.SceneEditorOutlineProvider;
        private _propertyProvider: properties.SceneEditorSectionProvider;
        private _game: Phaser.Game;
        private _overlayLayer: OverlayLayer;
        private _gameCanvas: HTMLCanvasElement;
        private _gameScene: GameScene;
        private _sceneMaker: SceneMaker;
        private _dropManager: DropManager;
        private _cameraManager: CameraManager;
        private _selectionManager: SelectionManager;
        private _actionManager: ActionManager;
        private _gameBooted: boolean;
        private _sceneRead: boolean;

        static getFactory(): EditorFactory {
            return new SceneEditorFactory();
        }

        constructor() {
            super("phasereditor2d.SceneEditor");

            this._blocksProvider = new blocks.SceneEditorBlocksProvider();
            this._outlineProvider = new outline.SceneEditorOutlineProvider(this);
            this._propertyProvider = new properties.SceneEditorSectionProvider();

        }

        async save() {

            const writer = new json.SceneWriter(this.getGameScene());

            const data = writer.toJSON();

            const content = JSON.stringify(data, null, 4);

            const ok = await FileUtils.setFileString(this.getInput(), content);

            if (ok) {
                this.setDirty(false);
            }
        }

        protected createPart() {

            this.setLayoutChildren(false);

            this._gameCanvas = document.createElement("canvas");
            this._gameCanvas.style.position = "absolute";
            this.getElement().appendChild(this._gameCanvas);

            this._overlayLayer = new OverlayLayer(this);
            this.getElement().appendChild(this._overlayLayer.getCanvas());


            // create game scene

            this._gameScene = new GameScene(this);

            this._game = new Phaser.Game({
                type: Phaser.WEBGL,
                canvas: this._gameCanvas,
                scale: {
                    mode: Phaser.Scale.NONE
                },
                render: {
                    pixelArt: true,
                    transparent: true
                },
                audio: {
                    noAudio: true
                },
                scene: this._gameScene,
            });

            this._sceneRead = false;

            this._gameBooted = false;

            (<any>this._game.config).postBoot = () => {
                this.onGameBoot();
            };

            // init managers and factories

            this._sceneMaker = new SceneMaker(this);
            this._dropManager = new DropManager(this);
            this._cameraManager = new CameraManager(this);
            this._selectionManager = new SelectionManager(this);
            this._actionManager = new ActionManager(this);

        }

        async setInput(file: core.io.FilePath) {
            super.setInput(file);

            if (this._gameBooted) {
                await this.readScene();
            }
        }

        private async readScene() {

            this._sceneRead = true;

            try {

                const file = this.getInput();

                const content = await FileUtils.getFileString(file);

                const data = JSON.parse(content);

                const parser = new json.SceneParser(this.getGameScene());

                await parser.createSceneCache_async(data);

                await parser.createScene(data);

            } catch (e) {
                alert(e.message);
                throw e;
            }
        }

        getActionManager() {
            return this._actionManager;
        }

        getSelectionManager() {
            return this._selectionManager;
        }

        getOverlayLayer() {
            return this._overlayLayer;
        }

        getGameCanvas() {
            return this._gameCanvas;
        }

        getGameScene() {
            return this._gameScene;
        }

        getGame() {
            return this._game;
        }

        getSceneMaker() {
            return this._sceneMaker;
        }

        layout() {

            super.layout();

            if (!this._gameBooted) {
                return;
            }

            this._overlayLayer.resizeTo();

            const parent = this.getElement();
            const w = parent.clientWidth;
            const h = parent.clientHeight;

            this._game.scale.resize(w, h);
            this._gameScene.scale.resize(w, h);
            this._gameScene.getCamera().setSize(w, h);

            this.repaint();
        }

        getPropertyProvider() {
            return this._propertyProvider;
        }

        getEditorViewerProvider(key: string) {

            switch (key) {
                case views.blocks.BlocksView.EDITOR_VIEWER_PROVIDER_KEY:
                    return this._blocksProvider;
                case views.outline.OutlineView.EDITOR_VIEWER_PROVIDER_KEY:
                    return this._outlineProvider;
                default:
                    break;
            }

            return null;
        }

        getOutlineProvider() {
            return this._outlineProvider;
        }

        refreshOutline() {
            this._outlineProvider.repaint();
        }

        private async onGameBoot() {
            this._gameBooted = true;

            if (!this._sceneRead) {
                await this.readScene();
            }

            this.layout();

            this.refreshOutline();

            // for some reason, we should do this after a time, or the game is not stopped well.
            setTimeout(() => this._game.loop.stop(), 500);
        }

        repaint(): void {

            if (!this._gameBooted) {
                return;
            }

            this._game.loop.tick();

            this._overlayLayer.render();
        }
    }

}
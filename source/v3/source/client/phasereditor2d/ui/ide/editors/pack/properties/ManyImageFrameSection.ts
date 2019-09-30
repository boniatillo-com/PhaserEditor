namespace phasereditor2d.ui.ide.editors.pack.properties {

    export class ManyImageFrameSection extends controls.properties.PropertySection<any> {
        constructor(page: controls.properties.PropertyPage) {
            super(page, "phasereditor2d.ui.ide.editors.pack.properties.ManyImageFrameSection", "Images", true);
        }

        protected createForm(parent: HTMLDivElement) {
            parent.classList.add("ManyImagePreviewFormArea");

            const viewer = new controls.viewers.TreeViewer("PreviewBackground");
            viewer.setContentProvider(new controls.viewers.ArrayTreeContentProvider());
            viewer.setTreeRenderer(new controls.viewers.GridTreeViewerRenderer(viewer, true));
            viewer.setLabelProvider(new viewers.AssetPackLabelProvider());
            viewer.setCellRendererProvider(new viewers.AssetPackCellRendererProvider());

            const filteredViewer = new ide.properties.FilteredViewerInPropertySection(this.getPage(), viewer);
            parent.appendChild(filteredViewer.getElement());

            this.addUpdater(async () => {
                const frames = await this.getImageFrames();
                viewer.setInput(frames);
                filteredViewer.resizeTo();
            });
        }

        private async getImageFrames() {
            const packItems = this.getSelection().filter(e => e instanceof AssetPackItem);
            await AssetPackUtils.preloadAssetPackItems(packItems);

            const frames = this.getSelection().flatMap(obj => {

                if (obj instanceof AssetPackItem) {
                    return AssetPackUtils.getImageFrames(obj);
                }

                return [(<ImageFrame>obj)]
            });

            return frames;
        }

        canEdit(obj: any): boolean {
            return obj instanceof ImageFrame || obj instanceof AssetPackItem && AssetPackUtils.isImageFrameContainer(obj);
        }

        canEditNumber(n: number): boolean {
            return n > 1;
        }

    }

}
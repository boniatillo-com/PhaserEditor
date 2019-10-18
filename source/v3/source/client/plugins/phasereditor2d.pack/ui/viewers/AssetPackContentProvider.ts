namespace phasereditor2d.pack.ui.viewers {

    import controls = colibri.ui.controls;

    export abstract class AssetPackContentProvider implements controls.viewers.ITreeContentProvider {

        abstract getRoots(input: any): any[];

        getChildren(parent: any): any[] {
            
            if (parent instanceof core.AssetPack) {
                return parent.getItems();
            }


            if (parent instanceof core.AssetPackItem) {
                
                if (parent.getType() === core.IMAGE_TYPE) {
                    return [];
                }

                if (core.AssetPackUtils.isImageFrameContainer(parent)) {
                    return core.AssetPackUtils.getImageFrames(parent);
                }
            }

            return [];
        }
    }
}
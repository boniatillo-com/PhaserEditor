namespace phasereditor2d.ui.ide.editors.pack {

    export class AssetPackLabelProvider implements controls.viewers.ILabelProvider {

        getLabel(obj: any): string {
            if (obj instanceof AssetPack) {
                return obj.getFile().getName();
            }

            if (obj instanceof AssetPackItem) {
                return obj.getKey();
            }

            if (obj instanceof ImageFrame) {
                return obj.getName();
            }

            return "";
        }

    }

}
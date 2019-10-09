namespace phasereditor2d.ui.ide.editors.pack.viewers {

    export class AssetPackLabelProvider implements controls.viewers.ILabelProvider {

        getLabel(obj: any): string {
            if (obj instanceof AssetPack) {
                return obj.getFile().getName();
            }

            if (obj instanceof AssetPackItem) {
                return obj.getKey();
            }

            if (obj instanceof controls.ImageFrame) {
                return obj.getName();
            }

            if (typeof (obj) === "string") {
                return obj;
            }

            return "";
        }

    }

}
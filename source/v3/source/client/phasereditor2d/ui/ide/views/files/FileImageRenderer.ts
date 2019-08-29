/// <reference path="../../../../../phasereditor2d.ui.controls/Controls.ts"/>
/// <reference path="../../../../../phasereditor2d.ui.controls/viewers/Viewer.ts"/>
/// <reference path="../../Part.ts"/>
/// <reference path="../../ViewPart.ts"/>

namespace phasereditor2d.ui.files {

    import io = phasereditor2d.core.io;
    import viewers = phasereditor2d.ui.controls.viewers;

    export class FileImageRenderer extends viewers.ImageCellRenderer {

        constructor(center : boolean) {
            super(center);
        }

        getLabel(obj: io.FilePath): string {
            return obj.getName();
        }

        getImage(obj: io.FilePath): controls.IImage {
            return controls.Controls.getImage("files/" + obj.getFullName());
        }
    }
}
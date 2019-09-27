namespace phasereditor2d.ui.ide.editors.pack {
    export type FrameDataType = {
        "filename": string,
        "trimmed": boolean,
        "rotated": boolean,
        "frame": {
            "x": number,
            "y": number,
            "w": number,
            "h": number
        },
        "spriteSourceSize": {
            "x": number,
            "y": number,
            "w": number,
            "h": number
        },
        "sourceSize": {
            "w": number,
            "h": number
        }
    }
}
namespace phasereditor2d.ui.ide.editors.pack {

    const IMAGE_FRAME_CONTAINER_TYPES = new Set([
        IMAGE_TYPE,
        MULTI_ATLAS_TYPE,
        ATLAS_TYPE,
        UNITY_ATLAS_TYPE,
        ATLAS_XML_TYPE,
        SPRITESHEET_TYPE
    ]);

    const ATLAS_TYPES = new Set([
        MULTI_ATLAS_TYPE,
        ATLAS_TYPE,
        UNITY_ATLAS_TYPE,
        ATLAS_XML_TYPE,
    ]);

    export class AssetPackUtils {

        static isAtlasPackItem(packItem : AssetPackItem) {
            return ATLAS_TYPES.has(packItem.getType());
        }

        static isImageFrameContainer(packItem: AssetPackItem) {
            return IMAGE_FRAME_CONTAINER_TYPES.has(packItem.getType());
        }

        static getImageFrames(packItem: AssetPackItem) {
            const parser = this.getImageFrameParser(packItem);
            if (parser) {
                return parser.parse();
            }
            return [];
        }

        static getImageFrameParser(packItem: AssetPackItem) {
            switch (packItem.getType()) {
                case IMAGE_TYPE:
                    return new pack.parsers.ImageParser(packItem);
                case ATLAS_TYPE:
                    return new pack.parsers.AtlasParser(packItem);
                case ATLAS_XML_TYPE:
                    return new pack.parsers.AtlasXMLParser(packItem);
                case UNITY_ATLAS_TYPE:
                    return new pack.parsers.UnityAtlasParser(packItem);
                case MULTI_ATLAS_TYPE:
                    return new pack.parsers.MultiAtlasParser(packItem);
                case SPRITESHEET_TYPE:
                    return new pack.parsers.SpriteSheetParser(packItem);
                default:
                    break;
            }
            return null;
        }

        static async preloadAssetPackItems(packItems: AssetPackItem[]) {
            for (const item of packItems) {
                if (this.isImageFrameContainer(item)) {
                    const parser = this.getImageFrameParser(item);
                    await parser.preload();
                }
            }
        }

        static async getAllPacks() {
            const files = await FileUtils.getFilesWithContentType(CONTENT_TYPE_ASSET_PACK);

            const packs: AssetPack[] = [];

            for (const file of files) {
                const pack = await AssetPack.createFromFile(file);
                packs.push(pack);
            }

            return packs;
        }

        

        static getFileFromPackUrl(url: string): core.io.FilePath {
            return FileUtils.getFileFromPath(url);
        }

        static getFileStringFromPackUrl(url: string): string {
            const file = FileUtils.getFileFromPath(url);
            const str = Workbench.getWorkbench().getFileStorage().getFileStringFromCache(file);
            return str;
        }

        static getFileJSONFromPackUrl(url: string): any {
            const str = this.getFileStringFromPackUrl(url);
            return JSON.parse(str);
        }
        static getFileXMLFromPackUrl(url: string): Document {
            const str = this.getFileStringFromPackUrl(url);
            const parser = new DOMParser();
            return parser.parseFromString(str, "text/xml");
        }

        static getImageFromPackUrl(url: string): controls.IImage {
            const file = this.getFileFromPackUrl(url);
            if (file) {
                return Workbench.getWorkbench().getFileImage(file);
            }
            return null;
        }

    }
}
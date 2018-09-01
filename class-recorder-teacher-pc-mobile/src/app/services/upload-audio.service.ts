import { Injectable } from "@angular/core";
import { ServerConnectionService } from "./server-connection.service";
import { TokenService } from "./token.service";
import { FileTransfer, FileTransferObject, FileUploadOptions, FileUploadResult } from "@ionic-native/file-transfer";


@Injectable()
export class UploadAudioService {

    constructor(private serverConnectionService: ServerConnectionService, 
        private tokenService: TokenService, 
        private transfer: FileTransfer) { 
    }

    uploadAudio(audioDir: string, containerFormat: string, videoName: string): Promise<FileUploadResult> {
        let baseUrl = this.serverConnectionService.getBaseUrl();
        const fileTransfer: FileTransferObject = this.transfer.create();
        let options: FileUploadOptions = {
            fileKey: 'file',
            headers: {
                'Authorization': 'Basic ' + this.tokenService.token
            }
        }
        return fileTransfer.upload(audioDir, `${baseUrl}/api/uploadFile/${containerFormat}/${videoName}`, options)
    }

}